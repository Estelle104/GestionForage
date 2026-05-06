package com.example.forage.entity;

import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "demande")
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;

    private String nomDemandeur;

    private String lieuForage;

    private Timestamp dateDemande;


    public Demande() {
    }

    public Long getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNomDemandeur() {
        return nomDemandeur;
    }

    public void setNomDemandeur(String nomDemandeur) {
        this.nomDemandeur = nomDemandeur;
    }

    public String getLieuForage() {
        return lieuForage;
    }

    public void setLieuForage(String lieuForage) {
        this.lieuForage = lieuForage;
    }

    public Timestamp getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Timestamp dateDemande) {
        this.dateDemande = dateDemande;
    }
}