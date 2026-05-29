package com.example.forage.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "devis")
public class Devis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_demande")
    private Demande demande;

    @Column(name = "created_at")
    private Timestamp dateDevis;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_type")
    private Type type;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @OneToMany(mappedBy = "devis", fetch = FetchType.LAZY)
    private List<DetailsDevis> details = new ArrayList<>();

    @Transient
    private Double montantDevis = 0.0;

    public Devis() {
    }

    @PrePersist
    public void onCreate() {
        if (dateDevis == null) {
            dateDevis = new Timestamp(System.currentTimeMillis());
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

    public Double getMontantDevis() {
        return montantDevis != null ? montantDevis : 0.0;
    }

    public void setMontantDevis(Double montantDevis) {
        this.montantDevis = montantDevis;
    }

    public Timestamp getDateDevis() {
        return dateDevis;
    }

    public void setDateDevis(Timestamp dateDevis) {
        this.dateDevis = dateDevis;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public List<DetailsDevis> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsDevis> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "Devis [id=" + id + ", demande=" + demande + ", type=" + type + ", montantDevis=" + montantDevis
                + ", dateDevis=" + dateDevis + ", observation=" + observation + "]";
    }
}