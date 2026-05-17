package com.example.forage.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.example.forage.dto.DevisCreationDTO;
import com.example.forage.entity.Demande;
import com.example.forage.entity.Devis;
import com.example.forage.service.DevisService;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.repository.TypeRepository;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/devis")
public class DevisController {

    private final DevisService devisService;
    private final DemandeRepository demandeRepo;
    private final TypeRepository typeRepo;

    public DevisController(DevisService devisService, DemandeRepository demandeRepo,
            TypeRepository typeRepo) {
        this.devisService = devisService;
        this.demandeRepo = demandeRepo;
        this.typeRepo = typeRepo;
    }

    // Liste de tous les devis
    @GetMapping
    public String list(Model model) {
        model.addAttribute("devisList", devisService.findAll());
        return "devis/list";
    }

    // Formulaire création devis
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("types", typeRepo.findAll());
        return "devis/form";
    }


    // Afficher les détails d'un devis
    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {
        Devis devis = devisService.findById(id);
        model.addAttribute("devis", devis);
        model.addAttribute("detailsList", devisService.getDetailsParDevis(id));
        return "devis/details";
    }


    // Export PDF
    @GetMapping("/{id}/pdf")
    public void exportPdf(@PathVariable Long id, HttpServletResponse response) {
        try {
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=devis_" + id + ".pdf");

            OutputStream out = response.getOutputStream();
            devisService.genererPdf(id, out);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException("Erreur export PDF", e);
        }
    }

   
    @GetMapping("/ajax/demande/{reference}")
    @ResponseBody
    public ResponseEntity<?> getDemandeInfo(@PathVariable String reference) {
        try {
            Demande demande = demandeRepo.findAll().stream()
                    .filter(d -> d.getReference().equals(reference))
                    .findFirst()
                    .orElse(null);

            if (demande == null) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Demande non trouvée avec la référence: " + reference);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("client", demande.getClient() != null ? demande.getClient().getNom() : "N/A");
            response.put("lieuForage", demande.getLieuForage());
            response.put("dateDemande", demande.getDateDemande());
            response.put("commune", demande.getCommune() != null ? demande.getCommune().getNom() : "N/A");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur lors de la récupération de la demande: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

  
    @PostMapping("/creer")
    @ResponseBody
    public ResponseEntity<?> creerDevis(@RequestBody DevisCreationDTO devisDTO) {
        try {
            if (devisDTO == null) {
                return ResponseEntity.badRequest().body(
                        Map.of("success", false, "error", "Payload vide"));
            }

            Devis devisCreated = devisService.creerDevisAvecDetails(devisDTO);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("devisId", devisCreated.getId());
            response.put("message", "Devis créé avec succès. ID: " + devisCreated.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Validation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (RuntimeException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur métier: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Erreur serveur: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
}
