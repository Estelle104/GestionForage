package com.example.forage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.forage.dto.AlerteDTO;
import com.example.forage.dto.StatusDemandeWithAlerteDTO;
import com.example.forage.entity.Demande;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.service.AlerteService;

@RestController
@RequestMapping("/api")
public class RestDemandeController {

    private final DemandeRepository demandeRepository;
    private final AlerteService alerteService;

    public RestDemandeController(DemandeRepository demandeRepository,
            AlerteService alerteService) {
        this.demandeRepository = demandeRepository;
        this.alerteService = alerteService;
    }

    @GetMapping("/demandes")
    public ResponseEntity<?> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String alerte,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String reference,
            @RequestParam(required = false, defaultValue = "asc") String sort) {

        Sort.Direction dir = "desc".equalsIgnoreCase(sort)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        List<Demande> demandes = demandeRepository.findAll(Sort.by(dir, "dateDemande"));

        List<Map<String, Object>> out = new ArrayList<>();

        for (Demande d : demandes) {

            List<StatusDemandeWithAlerteDTO> statuses = alerteService.getStatusDemandeWithAlertes(d);

            // HAS ALERTE GLOBAL
            boolean hasAlerte = statuses.stream()
                    .filter(s -> s.getAlertes() != null)
                    .flatMap(s -> s.getAlertes().stream())
                    .anyMatch(a -> Boolean.TRUE.equals(a.getIsAlerte()));

            // FILTRE REFERENCE DEMANDE
            if (reference != null && !reference.isBlank()) {
                String refInput = reference.trim().toLowerCase();

                if (d.getReference() == null ||
                        !d.getReference().toLowerCase().contains(refInput)) {
                    continue;
                }
            }

            // FILTRE STATUS (HISTORIQUE)
            if (status != null && !status.isBlank()) {

                String input = status.trim().toLowerCase();

                boolean matched;

                try {
                    Integer wantedId = Integer.parseInt(input);

                    matched = statuses.stream()
                            .anyMatch(s -> s.getStatusId() != null &&
                                    s.getStatusId().equals(wantedId));

                } catch (Exception e) {

                    matched = statuses.stream()
                            .anyMatch(s -> s.getStatusLibele() != null &&
                                    s.getStatusLibele()
                                            .trim()
                                            .toLowerCase()
                                            .equals(input));
                }

                if (!matched)
                    continue;
            }

            // FILTRE ALERTE ACTIVE (FIX)
            if (alerte != null && !alerte.isBlank()) {

                boolean wantAlerte;

                if (alerte.equalsIgnoreCase("true")) {
                    wantAlerte = true;
                } else if (alerte.equalsIgnoreCase("false")) {
                    wantAlerte = false;
                } else {
                    continue; // valeur invalide
                }

                if (hasAlerte != wantAlerte) {
                    continue;
                }
            }

            // FILTRE COULEUR ALERTE
            if (color != null && !color.isBlank()) {

                String wantedColor = color
                        .replace("#", "")
                        .trim()
                        .toLowerCase();

                boolean matchedColor = statuses.stream()
                        .filter(s -> s.getAlertes() != null)
                        .flatMap(s -> s.getAlertes().stream())
                        .map(AlerteDTO::getCouleur)
                        .filter(c -> c != null)
                        .map(c -> c.replace("#", "").toLowerCase())
                        .anyMatch(c -> c.equals(wantedColor));

                if (!matchedColor)
                    continue;
            }

            // RESPONSE
            Map<String, Object> m = new HashMap<>();
            m.put("id", d.getId());
            m.put("reference", d.getReference());
            m.put("client", d.getClient() != null ? d.getClient().getNom() : null);
            m.put("commune", d.getCommune() != null ? d.getCommune().getNom() : null);
            m.put("dateDemande", d.getDateDemande());
            m.put("currentStatus", d.getStatus() != null ? d.getStatus().getLibele() : null);
            m.put("hasAlerte", hasAlerte);
            m.put("statuses", statuses);

            out.add(m);
        }

        return ResponseEntity.ok(out);
    }
}