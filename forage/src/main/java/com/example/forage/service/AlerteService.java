package com.example.forage.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forage.dto.AlerteDTO;
import com.example.forage.dto.StatusDemandeWithAlerteDTO;
import com.example.forage.entity.Demande;
import com.example.forage.entity.AlerteColor;
import com.example.forage.entity.Parametre;
import com.example.forage.entity.Status;
import com.example.forage.entity.StatusDemande;
import com.example.forage.repository.ParametreRepository;
import com.example.forage.repository.StatusDemandeRepository;

@Service
public class AlerteService {

    @Autowired
    private StatusDemandeRepository statusDemandeRepository;

    @Autowired
    private ParametreRepository parametreRepository;

    /**
     * Récupère tous les statuts d'une demande avec les alertes associées
     * Triés par ordre chronologique
     */
    public List<StatusDemandeWithAlerteDTO> getStatusDemandeWithAlertes(Demande demande) {
        try {
            System.out.println("[AlerteService] Récupération des statuts pour demande ID: " + demande.getId());
            
            // Récupérer tous les status_demande de cette demande
            List<StatusDemande> statusDemandes = statusDemandeRepository.findByDemande(demande);
            
            System.out.println("[AlerteService] Statuts bruts trouvés: " + (statusDemandes != null ? statusDemandes.size() : 0));
            
            if (statusDemandes == null || statusDemandes.isEmpty()) {
                System.out.println("[AlerteService] Aucun statut trouvé, retour d'une liste vide");
                return new ArrayList<>();
            }
            
            System.out.println("[AlerteService] Tri des statuts par date...");
            // Trier par date_status
            Collections.sort(statusDemandes, (s1, s2) -> {
                if (s1.getDateStatus() == null) return 1;
                if (s2.getDateStatus() == null) return -1;
                return s1.getDateStatus().compareTo(s2.getDateStatus());
            });

            List<StatusDemandeWithAlerteDTO> result = new ArrayList<>();

            for (int i = 0; i < statusDemandes.size(); i++) {
                try {
                    StatusDemande sd = statusDemandes.get(i);
                    String statusLibele = sd.getStatus() != null ? sd.getStatus().getLibele() : "UNKNOWN";
                    System.out.println("[AlerteService]   Statut " + (i+1) + ": " + statusLibele);
                    
                    StatusDemandeWithAlerteDTO dto = new StatusDemandeWithAlerteDTO(
                        sd.getId(),
                        statusLibele,
                        sd.getDateStatus(),
                        sd.getObservation(),
                        sd.getDureeTravail()
                    );

                    // Calculer les alertes pour ce statut (entre le statut précédent et celui-ci)
                    List<AlerteDTO> alertes = new ArrayList<>();
                    
                    if (i > 0) {
                        StatusDemande previousStatus = statusDemandes.get(i - 1);
                        if (previousStatus.getStatus() != null && sd.getStatus() != null) {
                            alertes = calculerAlertes(previousStatus.getStatus(), sd.getStatus(), sd.getDureeTravail());
                        }
                    }

                    dto.setAlertes(alertes);
                    result.add(dto);
                } catch (Exception e) {
                    System.err.println("[AlerteService] ERREUR lors du traitement du statut " + i + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("[AlerteService] Résultat final: " + result.size() + " DTO créés");
            return result;
        } catch (Exception e) {
            System.err.println("[AlerteService] ERREUR GLOBALE dans getStatusDemandeWithAlertes: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Calcule les alertes entre deux statuts
     * L'alerte s'active si duree_travail > duree_minute
     */
    private List<AlerteDTO> calculerAlertes(Status status1, Status status2, Double dureeTravail) {
        List<AlerteDTO> alertes = new ArrayList<>();

        // Récupérer le paramètre pour ces deux statuts
        var parametreOptional = parametreRepository.findByIdStatus1AndIdStatus2(
            status1.getId(), status2.getId()
        );

        if (parametreOptional.isEmpty()) {
            return alertes; // Pas de paramètre configuré
        }
        
        Parametre parametre = parametreOptional.get();

        // Vérifier si l'alerte est active: duree_travail > duree_minute
        Double dureeActuelleMinutes = dureeTravail != null ? dureeTravail : 0.0;
        Double dureeConfigMinutes = parametre.getDureeMinute();
        
        boolean isAlerte = dureeActuelleMinutes > dureeConfigMinutes;

        // Récupérer la couleur fixe pour cette alerte
        AlerteColor alerteColor = AlerteColor.getAlerteByStatuses(status1.getId(), status2.getId());
        String couleur = alerteColor != null ? alerteColor.getHexColor() : "#999999";
        String description = alerteColor != null ? alerteColor.getDescription() : "Alerte";

        AlerteDTO alerte = new AlerteDTO();
        alerte.setIdStatus1(status1.getId());
        alerte.setIdStatus2(status2.getId());
        alerte.setStatusLabel(status1.getLibele() + " → " + status2.getLibele());
        alerte.setDureeMinute(dureeConfigMinutes);
        alerte.setDureeActuelle(dureeActuelleMinutes);
        alerte.setIsAlerte(isAlerte);
        alerte.setCouleur(couleur);
        alerte.setDescription(description);

        alertes.add(alerte);
        return alertes;
    }
}
