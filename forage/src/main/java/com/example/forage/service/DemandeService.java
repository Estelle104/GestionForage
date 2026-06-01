package com.example.forage.service;

import com.example.forage.entity.Client;
import com.example.forage.entity.Commune;
import com.example.forage.entity.Demande;
import com.example.forage.entity.Status;
import com.example.forage.entity.StatusDemande;
import com.example.forage.repository.ClientRepository;
import com.example.forage.repository.CommuneRepository;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.repository.StatusDemandeRepository;
import com.example.forage.repository.StatusRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class DemandeService {

    private final DemandeRepository repo;
    private final ClientRepository clientRepo;
    private final CommuneRepository communeRepo;
    private final StatusRepository statusRepo;
    private final StatusDemandeRepository statusDemandeRepo;

    public DemandeService(DemandeRepository repo, ClientRepository clientRepo, CommuneRepository communeRepo,
            StatusRepository statusRepo,
            StatusDemandeRepository statusDemandeRepo) {
        this.repo = repo;
        this.clientRepo = clientRepo;
        this.communeRepo = communeRepo;
        this.statusRepo = statusRepo;
        this.statusDemandeRepo = statusDemandeRepo;
    }

    public void save(Demande d) {
        repo.save(d);
    }

    public List<Demande> findAll() {
        return repo.findAll();
    }

    @Transactional
    public void creerDemande(String reference, Integer clientId, Integer communeId, Integer idStatus, String lieuForage, Timestamp dateDemande) {

        if (reference == null || reference.isBlank()) {
            throw new RuntimeException("La reference est obligatoire");
        }

        for (Demande demande : repo.findAll()) {
            if (demande.getReference() != null && demande.getReference().equals(reference)) {
                throw new RuntimeException("Cette reference existe deja. La reference doit etre unique");
            }
        }

        if (clientId == null || communeId == null || lieuForage == null || lieuForage.isBlank()) {
            throw new RuntimeException("Client, commune et lieu de forage sont obligatoires");
        }

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Commune commune = communeRepo.findById(communeId)
                .orElseThrow(() -> new RuntimeException("Commune not found"));

        if (idStatus == null) {
            idStatus = 1;
        }

        Status status = statusRepo.findById(idStatus).orElseThrow(() -> new RuntimeException("Status not found"));
        
        Demande d = new Demande();
        d.setReference(reference);
        d.setClient(client);
        d.setCommune(commune);
        d.setStatus(status);
        d.setLieuForage(lieuForage);

        if (dateDemande == null) {
            d.setDateDemande(new Timestamp(System.currentTimeMillis()));
        } else {
            d.setDateDemande(dateDemande);
        }

        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeInMillis(d.getDateDemande().getTime());
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        if (dayOfWeek == java.util.Calendar.SATURDAY || dayOfWeek == java.util.Calendar.SUNDAY) {
            throw new IllegalArgumentException("La date doit être un jour ouvrable (lundi à vendredi).");
        }

        int hourOfDay = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = cal.get(java.util.Calendar.MINUTE);
        if (hourOfDay < 8 || hourOfDay > 16 || (hourOfDay == 16 && minute > 0)) {
            throw new IllegalArgumentException("L'heure d'insertion doit être comprise entre 8h et 16h.");
        }

        System.out.println("[DemandeService] save reference=" + reference
                + " clientId=" + clientId
                + " communeId=" + communeId
                + " statusId=" + idStatus
                + " lieuForage=" + lieuForage);

        repo.save(d);

        StatusDemande sm = new StatusDemande();
        sm.setDemande(d);
        sm.setStatus(status);
        sm.setDateStatus(d.getDateDemande());

        statusDemandeRepo.save(sm);
    }

}