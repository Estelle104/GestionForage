package com.example.forage.controller;

import java.sql.Timestamp;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.forage.entity.Demande;
import com.example.forage.entity.Status;
import com.example.forage.entity.StatusDemande;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.repository.StatusRepository;
import com.example.forage.service.StatusDemandeService;

@Controller
@RequestMapping("/status-demandes")
public class StatusDemandeController {

    private final StatusDemandeService statusDemandeService;
    private final DemandeRepository demandeRepository;
    private final StatusRepository statusRepository;

    public StatusDemandeController(StatusDemandeService statusDemandeService,
            DemandeRepository demandeRepository,
            StatusRepository statusRepository) {
        this.statusDemandeService = statusDemandeService;
        this.demandeRepository = demandeRepository;
        this.statusRepository = statusRepository;
    }

    @GetMapping
    public String list(Model model) {
        java.util.List<StatusDemande> list = statusDemandeService.findAll();
        
        list.sort((a, b) -> {
            int c = a.getDemande().getId().compareTo(b.getDemande().getId());
            if (c == 0) {
                return a.getDateStatus().compareTo(b.getDateStatus());
            }
            return c;
        });

        java.util.Map<Integer, Long> lastDateMap = new java.util.HashMap<>();
        java.util.Map<Integer, Long> durationMap = new java.util.HashMap<>();
        long maxDuration = 1;

        for (StatusDemande sd : list) {
            Integer demandeId = sd.getDemande().getId();
            long current = sd.getDateStatus().getTime();
            if (lastDateMap.containsKey(demandeId)) {
                long prev = lastDateMap.get(demandeId);
                long diff = current - prev;
                durationMap.put(sd.getId(), diff);
                if (diff > maxDuration) {
                    maxDuration = diff;
                }
            } else {
                durationMap.put(sd.getId(), 0L);
            }
            lastDateMap.put(demandeId, current);
        }

        java.util.Map<Integer, String> dureeMap = new java.util.HashMap<>();
        java.util.Map<Integer, String> colorMap = new java.util.HashMap<>();

        for (StatusDemande sd : list) {
            long diff = durationMap.get(sd.getId());
            if (diff == 0) {
                dureeMap.put(sd.getId(), "-");
                colorMap.put(sd.getId(), "transparent");
            } else {
                long diffHours = diff / (60 * 60 * 1000);
                long diffDays = diffHours / 24;
                long remHours = diffHours % 24;
                String dStr = "";
                if (diffDays > 0) dStr += diffDays + "j ";
                dStr += remHours + "h";
                dureeMap.put(sd.getId(), dStr);
                
                double intensity = (double) diff / maxDuration;
                intensity = 0.1 + (0.9 * intensity); // from 0.1 to 1.0
                colorMap.put(sd.getId(), "rgba(255, 0, 0, " + String.format(java.util.Locale.US, "%.2f", intensity) + ")");
            }
        }

        model.addAttribute("statusDemandes", list);
        model.addAttribute("dureeMap", dureeMap);
        model.addAttribute("colorMap", colorMap);
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
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        int dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK);
        if (dayOfWeek == java.util.Calendar.SATURDAY || dayOfWeek == java.util.Calendar.SUNDAY) {
            throw new IllegalArgumentException("La date doit être un jour ouvrable (lundi à vendredi).");
        }

        int hourOfDay = cal.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = cal.get(java.util.Calendar.MINUTE);
        if (hourOfDay < 8 || hourOfDay > 16 || (hourOfDay == 16 && minute > 0)) {
            throw new IllegalArgumentException("L'heure d'insertion doit être comprise entre 8h et 16h.");
        }
            
        sd.setDateStatus(ts);
        sd.setObservation(observation);
        statusDemandeService.save(sd);

        // Mettre à jour le status de la demande
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
}
