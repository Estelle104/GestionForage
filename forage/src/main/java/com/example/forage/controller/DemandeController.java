package com.example.forage.controller;

import com.example.forage.entity.Demande;
import com.example.forage.service.DemandeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/demandes")
public class DemandeController {

    private final DemandeService service;

    public DemandeController(DemandeService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("demandes", service.findAll());
        return "demande/list"; 
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("demande", new Demande());
        return "demande/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Demande demande,
                       @RequestParam(required = false) Long statusId) {

        service.creerDemande(demande, statusId);

        return "redirect:/demandes";
    }
}