package com.example.forage.controller;

import com.example.forage.service.DemandeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final DemandeService demandeService;

    public HomeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("demandes", demandeService.findAll());
        return "demande/list";
    }
}

