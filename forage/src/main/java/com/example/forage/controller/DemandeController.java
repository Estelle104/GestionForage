package com.example.forage.controller;

import com.example.forage.entity.Demande;
import com.example.forage.service.DemandeService;
import com.example.forage.repository.ClientRepository;
import com.example.forage.repository.CommuneRepository;
import com.example.forage.repository.StatusRepository;

import java.sql.Timestamp;

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
    public String save(@ModelAttribute Demande demande,
                       @RequestParam Long clientId,
                       @RequestParam Long communeId,
                       @RequestParam String lieuForage,
                       @RequestParam(required = false) Long statusId,
                        @RequestParam(required = false) String dateDemande) {
                    
        // parser le string en timestamp 
        Timestamp dateT = Timestamp.valueOf(dateDemande);

        service.creerDemande(demande.getReference(), clientId, communeId, statusId, lieuForage, dateT );

        return "redirect:/demandes";
    }
}