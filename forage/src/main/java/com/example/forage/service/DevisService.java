package com.example.forage.service;

import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.List;

import com.example.forage.dto.DevisCreationDTO;
import com.example.forage.dto.DetailDevisDTO;
import com.example.forage.entity.Demande;
import com.example.forage.entity.DetailsDevis;
import com.example.forage.entity.Devis;
import com.example.forage.entity.Status;
import com.example.forage.entity.StatusDemande;
import com.example.forage.entity.Type;
import com.example.forage.entity.TypeDevisStatus;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.repository.DetailsDevisRepository;
import com.example.forage.repository.DevisRepository;
import com.example.forage.repository.StatusDemandeRepository;
import com.example.forage.repository.StatusRepository;
import com.example.forage.repository.TypeRepository;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public class DevisService {

    private final DevisRepository devisRepo;
    private final DetailsDevisRepository detailsRepo;
    private final DemandeRepository demandeRepo;
    private final TypeRepository typeRepo;
    private final StatusRepository statusRepo;
    private final StatusDemandeRepository statusDemandeRepo;

    public DevisService(DevisRepository devisRepo, DetailsDevisRepository detailsRepo,
            DemandeRepository demandeRepo, TypeRepository typeRepo, StatusRepository statusRepo,
            StatusDemandeRepository statusDemandeRepo) {
        this.devisRepo = devisRepo;
        this.detailsRepo = detailsRepo;
        this.demandeRepo = demandeRepo;
        this.typeRepo = typeRepo;
        this.statusRepo = statusRepo;
        this.statusDemandeRepo = statusDemandeRepo;
    }

    public List<Devis> findAll() {
        List<Devis> devisList = devisRepo.findAll();
        for (Devis devis : devisList) {
            devis.setMontantDevis(calculerTotalParDevisId(devis.getId()));
        }
        return devisList;
    }

    public Devis findById(Long id) {
        Devis devis = devisRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Devis introuvable avec l'id: " + id));
        devis.setMontantDevis(calculerTotalParDevisId(id));
        return devis;
    }

    @Transactional
    public Devis creerDevisAvecDetails(DevisCreationDTO devisDTO) {
        if (devisDTO == null) {
            throw new IllegalArgumentException("Le DTO du devis ne peut pas être null");
        }
        if (devisDTO.getDemandeReference() == null || devisDTO.getDemandeReference().isEmpty()) {
            throw new IllegalArgumentException("La référence de la demande est obligatoire");
        }
        if (devisDTO.getTypeId() == null) {
            throw new IllegalArgumentException("Le type de devis est obligatoire");
        }
        if (devisDTO.getDetails() == null || devisDTO.getDetails().isEmpty()) {
            throw new IllegalArgumentException("Au moins un détail de devis est obligatoire");
        }

        Demande demande = demandeRepo.findAll().stream()
                .filter(d -> d.getReference().equals(devisDTO.getDemandeReference()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Demande introuvable avec la référence: " + devisDTO.getDemandeReference()));

        Type type = typeRepo.findById(devisDTO.getTypeId())
                .orElseThrow(() -> new RuntimeException("Type introuvable avec l'id: " + devisDTO.getTypeId()));

        Devis devis = new Devis();
        devis.setDemande(demande);
        devis.setType(type);
        devis.setObservation(devisDTO.getObservation());
        devis.setMontantDevis(0.0);
        devis = devisRepo.save(devis);

        for (DetailDevisDTO detailDTO : devisDTO.getDetails()) {
            if (detailDTO.getDesignation() == null || detailDTO.getDesignation().isEmpty()) {
                throw new IllegalArgumentException("La désignation du détail ne peut pas être vide");
            }
            if (detailDTO.getQuantite() == null || detailDTO.getQuantite() <= 0) {
                throw new IllegalArgumentException("La quantité doit être > 0");
            }
            if (detailDTO.getPrixUnitaire() == null || detailDTO.getPrixUnitaire() <= 0) {
                throw new IllegalArgumentException("Le prix unitaire doit être > 0");
            }

            DetailsDevis detail = new DetailsDevis();
            detail.setDevis(devis);
            detail.setDesignation(detailDTO.getDesignation());
            detail.setQuantite(detailDTO.getQuantite());
            detail.setUnite(detailDTO.getUnite() != null ? detailDTO.getUnite() : "");
            detail.setPrixUnitaire(detailDTO.getPrixUnitaire());

            Double montantParLigne = detailDTO.getQuantite() * detailDTO.getPrixUnitaire();
            detail.setMontantParLigne(montantParLigne);

            detailsRepo.save(detail);
        }

        recalculerMontantDevis(devis);

        Long statusId = TypeDevisStatus.getStatusIdByTypeId(devisDTO.getTypeId());
        if (statusId == null) {
            throw new RuntimeException(
                    "Aucun status par défaut n'existe pour le type de devis avec l'id: " + devisDTO.getTypeId());
        }

        Status status = statusRepo.findById(statusId)
                .orElseThrow(() -> new RuntimeException(
                        "Status introuvable avec l'id: " + statusId + ". Veuillez créer les status par défaut pour les types de devis."));

        StatusDemande statusDemande = new StatusDemande();
        statusDemande.setDemande(demande);
        statusDemande.setStatus(status);
        statusDemande.setDateStatus(new Timestamp(System.currentTimeMillis()));
        statusDemandeRepo.save(statusDemande);

        return devis;
    }

    public List<DetailsDevis> getDetailsParDevis(Long devisId) {
        return detailsRepo.findByDevisId(devisId);
    }

    private void recalculerMontantDevis(Devis devis) {
        double total = calculerTotalParDevisId(devis.getId());
        devis.setMontantDevis(total);
        devisRepo.save(devis);
    }

    private double calculerTotalParDevisId(Long devisId) {
        List<DetailsDevis> details = detailsRepo.findByDevisId(devisId);
        double total = 0.0;
        for (DetailsDevis d : details) {
            if (d.getMontantParLigne() != null) {
                total += d.getMontantParLigne();
            }
        }
        return total;
    }

    public void genererPdf(Long devisId, OutputStream out) {
        Devis devis = findById(devisId);
        List<DetailsDevis> details = getDetailsParDevis(devisId);

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Polices
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(44, 62, 80));
            Font headerFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, new BaseColor(52, 73, 94));

            // Titre
            Paragraph titre = new Paragraph("DEVIS N° " + devis.getId(), titleFont);
            titre.setAlignment(Element.ALIGN_CENTER);
            titre.setSpacingAfter(20);
            document.add(titre);

            // Informations du devis
            Paragraph infoSection = new Paragraph("Informations du devis", subtitleFont);
            infoSection.setSpacingAfter(10);
            document.add(infoSection);

            document.add(new Paragraph("Reference demande: " + devis.getDemande().getReference(), normalFont));
            document.add(new Paragraph("Client: " + devis.getDemande().getClient().getNom(), normalFont));
            document.add(new Paragraph("Date: " + devis.getDateDevis(), normalFont));
            document.add(new Paragraph("Type: " + (devis.getType() != null ? devis.getType().getLibele() : "N/A"), normalFont));
            document.add(new Paragraph(" "));

            // Tableau des détails
            Paragraph detailSection = new Paragraph("Details du devis", subtitleFont);
            detailSection.setSpacingAfter(10);
            document.add(detailSection);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 1.5f, 1.5f, 2f, 2f});

            // En-tête du tableau
            BaseColor headerBg = new BaseColor(44, 62, 80);
            String[] headers = {"Designation", "Quantite", "Unite", "Prix Unitaire", "Montant"};
            for (String h : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
                cell.setBackgroundColor(headerBg);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                table.addCell(cell);
            }

            // Lignes
            boolean alternate = false;
            BaseColor altBg = new BaseColor(236, 240, 241);
            for (DetailsDevis d : details) {
                BaseColor bg = alternate ? altBg : BaseColor.WHITE;

                PdfPCell c1 = new PdfPCell(new Phrase(d.getDesignation(), normalFont));
                c1.setBackgroundColor(bg);
                c1.setPadding(6);
                table.addCell(c1);

                PdfPCell c2 = new PdfPCell(new Phrase(String.valueOf(d.getQuantite()), normalFont));
                c2.setBackgroundColor(bg);
                c2.setHorizontalAlignment(Element.ALIGN_CENTER);
                c2.setPadding(6);
                table.addCell(c2);

                PdfPCell c3 = new PdfPCell(new Phrase(d.getUnite(), normalFont));
                c3.setBackgroundColor(bg);
                c3.setHorizontalAlignment(Element.ALIGN_CENTER);
                c3.setPadding(6);
                table.addCell(c3);

                PdfPCell c4 = new PdfPCell(new Phrase(String.format("%.2f", d.getPrixUnitaire()), normalFont));
                c4.setBackgroundColor(bg);
                c4.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c4.setPadding(6);
                table.addCell(c4);

                PdfPCell c5 = new PdfPCell(new Phrase(String.format("%.2f", d.getMontantParLigne()), normalFont));
                c5.setBackgroundColor(bg);
                c5.setHorizontalAlignment(Element.ALIGN_RIGHT);
                c5.setPadding(6);
                table.addCell(c5);

                alternate = !alternate;
            }

            document.add(table);

            // Total
            Paragraph totalParagraph = new Paragraph();
            totalParagraph.setSpacingBefore(15);
            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            totalParagraph.add(new Phrase("MONTANT TOTAL: " + String.format("%.2f", devis.getMontantDevis()) + " Ar", boldFont));
            document.add(totalParagraph);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la generation du PDF", e);
        }
    }
}
