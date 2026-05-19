package com.example.forage.controller;

import com.example.forage.entity.Demande;
import com.example.forage.service.DemandeService;
import com.example.forage.repository.ClientRepository;
import com.example.forage.repository.CommuneRepository;
import com.example.forage.repository.StatusRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/demandes")
public class DemandeController {

    private final DemandeService service;
    private final ClientRepository clientRepository;
    private final CommuneRepository communeRepository;
    private final StatusRepository statusRepository;

    public DemandeController(DemandeService service, ClientRepository clientRepository,
            CommuneRepository communeRepository, StatusRepository statusRepository) {
        this.service = service;
        this.clientRepository = clientRepository;
        this.communeRepository = communeRepository;
        this.statusRepository = statusRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("demandes", service.findAll());
        return "demande/list"; 
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("demande", new Demande());
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("communes", communeRepository.findAll());
        model.addAttribute("statuses", statusRepository.findAll());
        return "demande/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String reference,
                       @RequestParam Long clientId,
                       @RequestParam Long communeId,
                       @RequestParam String lieuForage,
                       @RequestParam(required = false) String statusId,
                        @RequestParam(required = false) String dateDemande) {

        Timestamp dateT = null;
        if (dateDemande != null && !dateDemande.isBlank()) {
            try {
                dateT = Timestamp.valueOf(LocalDateTime.parse(dateDemande));
            } catch (DateTimeParseException | IllegalArgumentException e) {
                throw new IllegalArgumentException("Format de date invalide: " + dateDemande, e);
            }
        }

        Long parsedStatusId = (statusId == null || statusId.isBlank()) ? null : Long.valueOf(statusId);

        service.creerDemande(reference, clientId, communeId, parsedStatusId, lieuForage, dateT);

        return "redirect:/demandes";
    }
}