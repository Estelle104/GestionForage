package com.example.forage.dto;

public class AlerteDTO {
    private Integer idStatus1;
    private Integer idStatus2;
    private String statusLabel;
    private Double dureeMinute;
    private Double dureeActuelle;
    private Boolean isAlerte;
    private String couleur;
    private String description;

    public AlerteDTO() {
    }

    public AlerteDTO(Integer idStatus1, Integer idStatus2, String statusLabel,
                     Double dureeMinute, Double dureeActuelle, Boolean isAlerte,
                     String couleur, String description) {
        this.idStatus1 = idStatus1;
        this.idStatus2 = idStatus2;
        this.statusLabel = statusLabel;
        this.dureeMinute = dureeMinute;
        this.dureeActuelle = dureeActuelle;
        this.isAlerte = isAlerte;
        this.couleur = couleur;
        this.description = description;
    }

    public Integer getIdStatus1() {
        return idStatus1;
    }

    public void setIdStatus1(Integer idStatus1) {
        this.idStatus1 = idStatus1;
    }

    public Integer getIdStatus2() {
        return idStatus2;
    }

    public void setIdStatus2(Integer idStatus2) {
        this.idStatus2 = idStatus2;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Double getDureeMinute() {
        return dureeMinute;
    }

    public void setDureeMinute(Double dureeMinute) {
        this.dureeMinute = dureeMinute;
    }

    public Double getDureeActuelle() {
        return dureeActuelle;
    }

    public void setDureeActuelle(Double dureeActuelle) {
        this.dureeActuelle = dureeActuelle;
    }

    public Boolean getIsAlerte() {
        return isAlerte;
    }

    public void setIsAlerte(Boolean isAlerte) {
        this.isAlerte = isAlerte;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
