package com.example.forage.entity;

import java.sql.Timestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "demande")
public class Demande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String reference;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_client")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_commune")
    private Commune commune;

    @Column(name = "lieu_forage", nullable = false)
    private String lieuForage;

    @Column(name = "date_demande", nullable = false)
    private Timestamp dateDemande;

    @Column(name = "status_demande")
    private String statusDemande;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status", nullable = false)
    private Status status;


    public Demande() {
    }

    @PrePersist
    public void onCreate() {
        if (dateDemande == null) {
            dateDemande = new Timestamp(System.currentTimeMillis());
        }
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Commune getCommune() {
        return commune;
    }

    public void setCommune(Commune commune) {
        this.commune = commune;
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

    public String getStatusDemande() {
        return statusDemande;
    }

    public void setStatusDemande(String statusDemande) {
        this.statusDemande = statusDemande;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}