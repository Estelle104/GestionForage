package com.example.forage.dto;

import java.sql.Timestamp;
import java.util.List;

public class StatusDemandeWithAlerteDTO {
    private Integer id;
    private String statusLibele;
    private Timestamp dateStatus;
    private String observation;
    private Double dureeTravail;
    private List<AlerteDTO> alertes;
    private Integer statusId;

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public StatusDemandeWithAlerteDTO() {
    }

    public StatusDemandeWithAlerteDTO(Integer id, String statusLibele, Timestamp dateStatus,
            String observation, Double dureeTravail) {
        this.id = id;
        this.statusLibele = statusLibele;
        this.dateStatus = dateStatus;
        this.observation = observation;
        this.dureeTravail = dureeTravail;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatusLibele() {
        return statusLibele;
    }

    public void setStatusLibele(String statusLibele) {
        this.statusLibele = statusLibele;
    }

    public Timestamp getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(Timestamp dateStatus) {
        this.dateStatus = dateStatus;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Double getDureeTravail() {
        return dureeTravail;
    }

    public void setDureeTravail(Double dureeTravail) {
        this.dureeTravail = dureeTravail;
    }

    public List<AlerteDTO> getAlertes() {
        return alertes;
    }

    public void setAlertes(List<AlerteDTO> alertes) {
        this.alertes = alertes;
    }
}
