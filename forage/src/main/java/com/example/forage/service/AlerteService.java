package com.example.forage.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forage.dto.AlerteDTO;
import com.example.forage.dto.StatusDemandeWithAlerteDTO;
import com.example.forage.dto.StatusIntervalDTO;
import com.example.forage.entity.Demande;
import com.example.forage.entity.AlerteColor;
import com.example.forage.entity.Parametre;
import com.example.forage.entity.Status;
import com.example.forage.entity.StatusDemande;
import com.example.forage.repository.ParametreRepository;
import com.example.forage.repository.StatusDemandeRepository;

import java.util.Date;

@Service
public class AlerteService {

    @Autowired
    private StatusDemandeRepository statusDemandeRepository;

    @Autowired
    private ParametreRepository parametreRepository;

    public List<StatusDemandeWithAlerteDTO> getStatusDemandeWithAlertes(Demande demande) {
        try {
            System.out.println("[AlerteService] Récupération des statuts pour demande ID: " + demande.getId());

            List<StatusDemande> statusDemandes = statusDemandeRepository.findByDemande(demande);

            System.out.println("[AlerteService] Statuts bruts trouvés: "
                    + (statusDemandes != null ? statusDemandes.size() : 0));

            if (statusDemandes == null || statusDemandes.isEmpty()) {
                System.out.println("[AlerteService] Aucun statut trouvé, retour d'une liste vide");
                return new ArrayList<>();
            }

            System.out.println("[AlerteService] Tri des statuts par date...");
            Collections.sort(statusDemandes, (s1, s2) -> {
                if (s1.getDateStatus() == null)
                    return 1;
                if (s2.getDateStatus() == null)
                    return -1;
                return s1.getDateStatus().compareTo(s2.getDateStatus());
            });

            List<StatusDemandeWithAlerteDTO> result = new ArrayList<>();

            for (int i = 0; i < statusDemandes.size(); i++) {
                try {
                    StatusDemande sd = statusDemandes.get(i);
                    String statusLibele = sd.getStatus() != null ? sd.getStatus().getLibele() : "UNKNOWN";
                    System.out.println("[AlerteService]   Statut " + (i + 1) + ": " + statusLibele);

                    StatusDemandeWithAlerteDTO dto = new StatusDemandeWithAlerteDTO(
                            sd.getId(),
                            statusLibele,
                            sd.getDateStatus(),
                            sd.getObservation(),
                            sd.getDureeTravail());

                    dto.setStatusId(sd.getStatus() != null ? sd.getStatus().getId() : null);

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
                    System.err.println(
                            "[AlerteService] ERREUR lors du traitement du statut " + i + ": " + e.getMessage());
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

    private List<AlerteDTO> calculerAlertes(Status status1, Status status2, Double dureeTravail) {
        List<AlerteDTO> alertes = new ArrayList<>();

        // Récupérer TOUS les paramètres pour cette transition (alerte 1 et alerte 2)
        List<Parametre> parametres = parametreRepository.findByIdStatus1AndIdStatus2(
                status1.getId(), status2.getId());

        if (parametres == null || parametres.isEmpty()) {
            return alertes;
        }

        Double dureeActuelleMinutes = dureeTravail != null ? dureeTravail : 0.0;

        for (Parametre parametre : parametres) {
            Double debutMinute = parametre.getDebutMinute();
            Double finMinute = parametre.getFinMinute();
            Integer niveauAlerte = parametre.getAlerte(); // 1 ou 2

            boolean isAlerte = dureeActuelleMinutes >= debutMinute
                    && dureeActuelleMinutes <= finMinute;

            // Couleur selon le niveau d'alerte
            String couleur;
            String description;
            if (niveauAlerte == 1) {
                couleur = "#FFA500"; // orange pour alerte 1
                description = "Alerte niveau 1";
            } else {
                couleur = "#FF0000"; // rouge pour alerte 2
                description = "Alerte niveau 2";
            }

            // Surcharge avec AlerteColor si défini
            AlerteColor alerteColor = AlerteColor.getAlerteByStatuses(status1.getId(), status2.getId());
            if (alerteColor != null) {
                couleur = alerteColor.getHexColor();
                description = alerteColor.getDescription();
            }

            AlerteDTO alerte = new AlerteDTO();
            alerte.setIdStatus1(status1.getId());
            alerte.setIdStatus2(status2.getId());
            alerte.setStatusLabel(status1.getLibele() + " → " + status2.getLibele());
            alerte.setDebutMinute(debutMinute);
            alerte.setFinMinute(finMinute);
            alerte.setDureeActuelle(dureeActuelleMinutes);
            alerte.setIsAlerte(isAlerte);
            alerte.setCouleur(couleur);
            alerte.setDescription(description);
            alerte.setNiveauAlerte(niveauAlerte); // ajoutez ce champ dans AlerteDTO

            alertes.add(alerte);
        }

        return alertes;
    }

    public List<StatusIntervalDTO> buildIntervals(List<StatusDemandeWithAlerteDTO> statuses) {

        List<StatusIntervalDTO> intervals = new ArrayList<>();

        if (statuses == null || statuses.size() < 2)
            return intervals;

        statuses.sort(Comparator.comparing(StatusDemandeWithAlerteDTO::getDateStatus));

        for (int i = 0; i < statuses.size() - 1; i++) {

            var s1 = statuses.get(i);
            var s2 = statuses.get(i + 1);

            Integer fromId = s1.getStatusId();
            Integer toId = s2.getStatusId();

            long duree = s2.getDureeTravail() != null
                    ? s2.getDureeTravail().longValue()
                    : 0L;

            String color = "#999999";
            boolean hasAlerte = false;
            Integer niveauAlerte = null;

            if (s2.getAlertes() != null && !s2.getAlertes().isEmpty()) {

                AlerteDTO alerteActive = s2.getAlertes().stream()
                        .filter(a -> Boolean.TRUE.equals(a.getIsAlerte()))
                        .findFirst()
                        .orElse(null);

                if (alerteActive != null) {
                    hasAlerte = true;
                    niveauAlerte = alerteActive.getNiveauAlerte();

                    AlerteColor config = AlerteColor.findByTransitionAndNiveau(
                            fromId, toId, niveauAlerte);

                    color = (config != null)
                            ? config.getHexColor()
                            : alerteActive.getCouleur(); 

                } else {
                    AlerteColor config = AlerteColor.findByTransitionAndNiveau(fromId, toId, 1);
                    if (config != null) {
                        color = config.getHexColor();
                    }
                }
            }

            StatusIntervalDTO dto = new StatusIntervalDTO();
            dto.setFromStatus(s1.getStatusLibele());
            dto.setToStatus(s2.getStatusLibele());
            dto.setDurationMinutes(duree);
            dto.setColor(color);
            dto.setHasAlerte(hasAlerte);
            dto.setNiveauAlerte(niveauAlerte);

            intervals.add(dto);
        }

        return intervals;
    }
}