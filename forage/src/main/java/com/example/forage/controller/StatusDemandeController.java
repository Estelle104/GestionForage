package com.example.forage.controller;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


import com.example.forage.dto.StatusDemandeWithAlerteDTO;
import com.example.forage.entity.Demande;
import com.example.forage.entity.Status;
import com.example.forage.entity.StatusDemande;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.repository.StatusRepository;
import com.example.forage.service.StatusDemandeService;
import com.example.forage.service.AlerteService;

@Controller
@RequestMapping("/status-demandes")
public class StatusDemandeController {

    private final StatusDemandeService statusDemandeService;
    private final DemandeRepository demandeRepository;
    private final StatusRepository statusRepository;
    private final AlerteService alerteService;

    public StatusDemandeController(StatusDemandeService statusDemandeService,
            DemandeRepository demandeRepository,
            StatusRepository statusRepository,
            AlerteService alerteService) {
        this.statusDemandeService = statusDemandeService;
        this.demandeRepository = demandeRepository;
        this.statusRepository = statusRepository;
        this.alerteService = alerteService;
    }

    @GetMapping
    public String list(Model model) {
        List<Demande> demandes = demandeRepository.findAll();
        Map<Integer, List<StatusDemandeWithAlerteDTO>> statusParDemande = new HashMap<>();


        // Pour chaque demande, récupérer les statuts avec les alertes
        for (Demande demande : demandes) {
            try {
                List<StatusDemandeWithAlerteDTO> statusesWithAlertes = alerteService.getStatusDemandeWithAlertes(demande);
                
                
                if (!statusesWithAlertes.isEmpty()) {
                    statusParDemande.put(demande.getId(), statusesWithAlertes);
                }
            } catch (Exception e) {
                System.err.println("ERREUR pour demande " + demande.getId() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        // System.out.println("Total demandes avec statuts: " + statusParDemande.size());
        // System.out.println("=== FIN DEBUG ===\n");

        model.addAttribute("demandes", demandes);
        model.addAttribute("statusParDemande", statusParDemande);
        return "status_demande/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("demandes", demandeRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        return "status_demande/form";
    }

    @PostMapping("/create")
    public String create(@RequestParam @NonNull Integer demandeId,
            @RequestParam @NonNull Integer statusId,
            @RequestParam String dateStatus,
            @RequestParam String observation) {
        Demande demande = demandeRepository.findById(demandeId).orElseThrow();
        Status status = statusRepository.findById(statusId).orElseThrow();

        StatusDemande sd = new StatusDemande();
        sd.setDemande(demande);
        sd.setStatus(status);
        
        Timestamp ts;
        try {
            if (dateStatus != null && !dateStatus.isEmpty()) {
                ts = Timestamp.valueOf(dateStatus.replace("T", " ") + (dateStatus.length() == 16 ? ":00" : ""));
            } else {
                ts = new Timestamp(System.currentTimeMillis());
            }
        } catch (Exception e) {
            ts = new Timestamp(System.currentTimeMillis());
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        int jw = cal.get(Calendar.DAY_OF_WEEK);
        // if (jw == Calendar.SATURDAY || jw == Calendar.SUNDAY) {
        //     throw new IllegalArgumentException("La date doit être un jour ouvrable (lundi à vendredi).");
        // }

        int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        if (hourOfDay < 8 || hourOfDay > 16 || (hourOfDay == 16 && minute > 0)) {
            throw new IllegalArgumentException("L'heure d'insertion doit être comprise entre 8h et 16h.");
        }
            
        sd.setDateStatus(ts);
        sd.setObservation(observation);
        statusDemandeService.save(sd);

        demande.setStatus(status);
        demandeRepository.save(demande);

        return "redirect:/status-demandes";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        StatusDemande sd = statusDemandeService.findById(id);
        model.addAttribute("statusDemande", sd);
        model.addAttribute("demandes", demandeRepository.findAll());
        
        // Seul les statuts déjà eu par la demande doivent être affichés
        java.util.List<StatusDemande> history = statusDemandeService.findByDemande(sd.getDemande());
        java.util.List<Status> listStatuses = history.stream()
                .map(StatusDemande::getStatus)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
        
        model.addAttribute("statuses", listStatuses);
        return "status_demande/form";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id,
            @RequestParam @NonNull Integer demandeId,
            @RequestParam @NonNull Integer statusId,
            @RequestParam String dateStatus,
            @RequestParam String observation) {
        StatusDemande sd = statusDemandeService.findById(id);
        Demande demande = demandeRepository.findById(demandeId).orElseThrow();
        Status status = statusRepository.findById(statusId).orElseThrow();

        // Valider que le statut choisi fait partie de l'historique des statuts de la demande
        java.util.List<StatusDemande> history = statusDemandeService.findByDemande(demande);
        boolean existsInHistory = history.stream()
                .anyMatch(h -> h.getStatus().getId().equals(statusId));
        if (!existsInHistory) {
            throw new IllegalArgumentException("Le statut choisi n'a jamais été associé à cette demande.");
        }

        sd.setDemande(demande);
        sd.setStatus(status);
        try {
            if (dateStatus != null && !dateStatus.isEmpty()) {
                sd.setDateStatus(
                        Timestamp.valueOf(dateStatus.replace("T", " ") + (dateStatus.length() == 16 ? ":00" : "")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        sd.setObservation(observation);
        statusDemandeService.save(sd);

        // Mettre à jour le status de la demande
        demande.setStatus(status);
        demandeRepository.save(demande);

        return "redirect:/status-demandes";
    }

    @GetMapping("/ajax/demande/{demandeId}/statuses")
    @ResponseBody
    public ResponseEntity<?> getStatusesForDemande(@PathVariable Integer demandeId) {
        try {
            Demande demande = demandeRepository.findById(demandeId).orElse(null);
            if (demande == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Demande non trouvée"));
            }
            List<StatusDemande> history = statusDemandeService.findByDemande(demande);
            Map<Integer, String> statusMap = new java.util.LinkedHashMap<>();
            for (StatusDemande sd : history) {
                statusMap.put(sd.getStatus().getId(), sd.getStatus().getLibele());
            }
            List<Map<String, Object>> statuses = new java.util.ArrayList<>();
            for (Map.Entry<Integer, String> entry : statusMap.entrySet()) {
                statuses.add(Map.of("id", entry.getKey(), "libele", entry.getValue()));
            }
            return ResponseEntity.ok(statuses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/ajax/demande/{demandeId}/status/{statusId}")
    @ResponseBody
    public ResponseEntity<?> getStatusDetails(@PathVariable Integer demandeId, @PathVariable Integer statusId) {
        try {
            Demande demande = demandeRepository.findById(demandeId).orElse(null);
            if (demande == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Demande non trouvée"));
            }
            List<StatusDemande> history = statusDemandeService.findByDemande(demande);
            StatusDemande match = history.stream()
                .filter(sd -> sd.getStatus().getId().equals(statusId))
                .findFirst()
                .orElse(null);
            if (match == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Statut non trouvé dans l'historique"));
            }
            
            String dateStr = "";
            if (match.getDateStatus() != null) {
                dateStr = match.getDateStatus().toLocalDateTime().toString().substring(0, 16);
            }
            
            return ResponseEntity.ok(Map.of(
                "statusDemandeId", match.getId(),
                "dateStatus", dateStr,
                "observation", match.getObservation() != null ? match.getObservation() : ""
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));
        }
    }
}
