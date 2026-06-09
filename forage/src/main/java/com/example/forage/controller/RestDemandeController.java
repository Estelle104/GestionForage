package com.example.forage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.forage.dto.StatusDemandeWithAlerteDTO;
import com.example.forage.entity.Demande;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.service.AlerteService;

@RestController
@RequestMapping("/api")
public class RestDemandeController {

    private final DemandeRepository demandeRepository;
    private final AlerteService alerteService;

    public RestDemandeController(DemandeRepository demandeRepository, AlerteService alerteService) {
        this.demandeRepository = demandeRepository;
        this.alerteService = alerteService;
    }

    /**
     * Liste les demandes avec leurs statuts (chronologique).
     * Filtrage par `status` (id ou libellé) et `alerte` (true/false).
     */
    @GetMapping("/demandes")
    public ResponseEntity<?> list(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String alerte,
            @RequestParam(required = false, defaultValue = "asc") String sort
    ) {
        Sort.Direction dir = "desc".equalsIgnoreCase(sort) ? Sort.Direction.DESC : Sort.Direction.ASC;
        List<Demande> demandes = demandeRepository.findAll(Sort.by(dir, "dateDemande"));

        List<Map<String, Object>> out = new ArrayList<>();

        for (Demande d : demandes) {
            List<StatusDemandeWithAlerteDTO> statuses = alerteService.getStatusDemandeWithAlertes(d);
            boolean hasAlerte = statuses.stream()
                    .flatMap(s -> s.getAlertes() != null ? s.getAlertes().stream() : List.<Object>of().stream())
                    .anyMatch(a -> {
                        try {
                            return ((com.example.forage.dto.AlerteDTO) a).getIsAlerte();
                        } catch (Exception e) {
                            return false;
                        }
                    });

            // appliquer filtre status si fourni (compare id ou libellé)
            if (status != null && !status.isBlank()) {
                boolean matched = false;
                try {
                    Integer id = Integer.valueOf(status);
                    if (d.getStatus() != null && d.getStatus().getId().equals(id)) matched = true;
                } catch (Exception e) {
                    // non integer → comparer libellé
                    if (d.getStatus() != null && d.getStatus().getLibele() != null
                            && d.getStatus().getLibele().equalsIgnoreCase(status)) matched = true;
                }
                if (!matched) continue;
            }

            if (alerte != null && !alerte.isBlank()) {
                boolean want = Boolean.parseBoolean(alerte);
                if (want != hasAlerte) continue;
            }

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

        return ResponseEntity.ok(out.stream().collect(Collectors.toList()));
    }
}
