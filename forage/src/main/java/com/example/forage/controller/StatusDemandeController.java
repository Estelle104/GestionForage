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
        model.addAttribute("statusDemandes", statusDemandeService.findAll());
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
