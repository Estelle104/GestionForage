package com.example.forage.entity;

import java.sql.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "status_demande")
public class StatusDemande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_demande")
    private Demande demande;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_status")
    private Status status;

    private Timestamp date_status;

    private String observation;

    private Double duree_travail;

    public StatusDemande(Demande demande, Status status, Timestamp date_status, String observation) {
        this.demande = demande;
        this.status = status;
        this.date_status = date_status;
        this.observation = observation;
    }

    public StatusDemande(Integer id, Demande demande, Status status, Timestamp date_status, String observation) {
        this.id = id;
        this.demande = demande;
        this.status = status;
        this.date_status = date_status;
        this.observation = observation;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public StatusDemande() {
    }

    @PrePersist
    public void onCreate() {
        if (date_status == null) {
            date_status = new Timestamp(System.currentTimeMillis());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getDateStatus() {
        return date_status;
    }

    public void setDateStatus(Timestamp dateStatus) {
        this.date_status = dateStatus;
    }

    public Double getDureeTravail() {
        return duree_travail;
    }

    public void setDureeTravail(Double dureeTravail) {
        this.duree_travail = dureeTravail;
    }
}
