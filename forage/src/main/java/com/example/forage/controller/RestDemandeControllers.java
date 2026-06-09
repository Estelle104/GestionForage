package com.example.forage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.forage.dto.StatusDemandeWithAlerteDTO;
import com.example.forage.dto.StatusIntervalDTO;
import com.example.forage.entity.Demande;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.service.AlerteService;

@RestController
@RequestMapping("/apis")
public class RestDemandeControllers {

    private final DemandeRepository demandeRepository;
    private final AlerteService alerteService;

    public RestDemandeControllers(DemandeRepository demandeRepository,
                                 AlerteService alerteService) {
        this.demandeRepository = demandeRepository;
        this.alerteService = alerteService;
    }

    @GetMapping("/demandes")
    public ResponseEntity<List<Map<String, Object>>> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String alerte,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String reference,
            @RequestParam(defaultValue = "asc") String sort
    ) {

        Sort.Direction direction =
                "desc".equalsIgnoreCase(sort) ? Sort.Direction.DESC : Sort.Direction.ASC;

        List<Demande> demandes =
                demandeRepository.findAll(Sort.by(direction, "dateDemande"));

        List<Map<String, Object>> result = new ArrayList<>();

        for (Demande d : demandes) {

            // STATUS + ALERTES (SERVICE)
            List<StatusDemandeWithAlerteDTO> statuses =
                    alerteService.getStatusDemandeWithAlertes(d);

            // INTERVALLES (SERVICE)
            List<StatusIntervalDTO> intervals =
                    alerteService.buildIntervals(statuses);

            // HAS ALERTE GLOBAL
            boolean hasAlerte = statuses.stream()
                    .filter(s -> s.getAlertes() != null)
                    .flatMap(s -> s.getAlertes().stream())
                    .anyMatch(a -> Boolean.TRUE.equals(a.getIsAlerte()));

            // FILTRE STATUS
            if (status != null && !status.isBlank()) {

                String input = status.trim().toLowerCase();

                boolean matched;

                try {
                    Integer id = Integer.parseInt(input);

                    matched = statuses.stream()
                            .anyMatch(s -> id.equals(s.getStatusId()));

                } catch (Exception e) {

                    matched = statuses.stream()
                            .anyMatch(s -> s.getStatusLibele() != null &&
                                    s.getStatusLibele().trim().equalsIgnoreCase(input));
                }

                if (!matched) continue;
            }

            // FILTRE ALERTE
            if (alerte != null && !alerte.isBlank()) {

                boolean wantAlerte = Boolean.parseBoolean(alerte);

                if (wantAlerte != hasAlerte) continue;
            }

            // FILTRE COLOR (depuis alertes status)
            if (color != null && !color.isBlank()) {

                String wantedColor = color.replace("#", "").trim().toLowerCase();

                boolean matchedColor = statuses.stream()
                        .filter(s -> s.getAlertes() != null)
                        .flatMap(s -> s.getAlertes().stream())
                        .map(a -> a.getCouleur())
                        .filter(c -> c != null)
                        .map(c -> c.replace("#", "").toLowerCase())
                        .anyMatch(c -> c.equals(wantedColor));

                if (!matchedColor) continue;
            }

            // FILTRE REFERENCE
            if (reference != null && !reference.isBlank()) {

                String ref = reference.trim().toLowerCase();

                if (d.getReference() == null ||
                        !d.getReference().toLowerCase().contains(ref)) {
                    continue;
                }
            }

            // RESPONSE MAP
            Map<String, Object> map = new HashMap<>();

            map.put("id", d.getId());
            map.put("reference", d.getReference());
            map.put("client", d.getClient() != null ? d.getClient().getNom() : null);
            map.put("commune", d.getCommune() != null ? d.getCommune().getNom() : null);
            map.put("dateDemande", d.getDateDemande());
            map.put("currentStatus", d.getStatus() != null ? d.getStatus().getLibele() : null);

            map.put("hasAlerte", hasAlerte);

            // DATA MÉTIER
            map.put("statuses", statuses);
            map.put("intervals", intervals);

            result.add(map);
        }

        return ResponseEntity.ok(result);
    }
}