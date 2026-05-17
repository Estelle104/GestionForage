package com.example.forage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "details_devis")
public class DetailsDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_devis")
    private Devis devis;

    @Column(name = "quantite")
    private Double quantite;

    @Column(name = "unite")
    private String unite;

    @Column(name = "prix_unitaire")
    private Double prixUnitaire;

    @Column(name = "designation")
    private String designation;

    @Column(name = "montant_par_ligne")
    private Double montantParLigne;

    public DetailsDevis() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Devis getDevis() {
        return devis;
    }

    public void setDevis(Devis devis) {
        this.devis = devis;
    }

    public Double getQuantite() {
        return quantite;
    }

    public void setQuantite(Double quantite) {
        this.quantite = quantite;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public Double getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(Double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getMontantParLigne() {
        return montantParLigne;
    }

    public void setMontantParLigne(Double montantParLigne) {
        this.montantParLigne = montantParLigne;
    }

    @Override
    public String toString() {
        return "DetailsDevis [id=" + id + ", devisId=" + (devis != null ? devis.getId() : null)
                + ", quantite=" + quantite + ", unite=" + unite + ", prixUnitaire=" + prixUnitaire
                + ", designation=" + designation + ", montantParLigne=" + montantParLigne + "]";
    }
}