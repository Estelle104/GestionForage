package com.example.forage.entity;

import java.sql.Timestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "status_demande")
public class StatusDemande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Demande demande;

    @ManyToOne
    private Status status;

    private Timestamp dateStatus;

    public StatusDemande() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public void setStatus(Status status2) {
        this.status = status2;
    }

    public Timestamp getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(Timestamp dateStatus) {
        this.dateStatus = dateStatus;
    }



    
}
