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
        List<StatusDemande> list = statusDemandeService.findAll();
        
        list.sort((a, b) -> {
            int c = a.getDemande().getId().compareTo(b.getDemande().getId());
            if (c == 0) {
                return a.getDateStatus().compareTo(b.getDateStatus());
            }
            return c;
        });

        Map<Integer, Long> lastDate = new HashMap<>();
        Map<Integer, Long> duree = new HashMap<>();
        long maxDuree = 1;

        for (StatusDemande sd : list) {
            Integer demandeId = sd.getDemande().getId();
            long current = sd.getDateStatus().getTime();
            
            if (lastDate.containsKey(demandeId)) {
                long prev = lastDate.get(demandeId);
                long diff = current - prev;
                duree.put(sd.getId(), diff);
                if (diff > maxDuree) {
                    maxDuree = diff;
                }
            } else {
                duree.put(sd.getId(), 0L);
            }
            lastDate.put(demandeId, current);
        }

        Map<Integer, String> dureeMap = new HashMap<>();
        Map<Integer, String> colorMap = new HashMap<>();

        for (StatusDemande sd : list) {
            long diff = duree.get(sd.getId());
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
                
                double intensity = (double) diff / maxDuree;
                intensity = 0.1 + (0.9 * intensity); // from 0.1 to 1.0
                colorMap.put(sd.getId(), "rgba(255, 0, 0, " + String.format(Locale.US, "%.2f", intensity) + ")");
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
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(ts.getTime());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            throw new IllegalArgumentException("La date doit être un jour ouvrable (lundi à vendredi).");
        }

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
        model.addAttribute("statuses", statusRepository.findAll());
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
