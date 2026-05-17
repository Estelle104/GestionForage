package com.example.forage.dto;

/**
 * DTO pour un détail de devis
 * Utilisé pour recevoir les données de détails (depuis le formulaire en JSON)
 */
public class DetailDevisDTO {
    private String designation;
    private Double quantite;
    private String unite;
    private Double prixUnitaire;

    public DetailDevisDTO() {
    }

    public DetailDevisDTO(String designation, Double quantite, String unite, Double prixUnitaire) {
        this.designation = designation;
        this.quantite = quantite;
        this.unite = unite;
        this.prixUnitaire = prixUnitaire;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    /**
     * Calcule le montant par ligne: quantité * prix unitaire
     */
    public Double calculerMontantParLigne() {
        if (quantite != null && prixUnitaire != null) {
            return quantite * prixUnitaire;
        }
        return 0.0;
    }

    @Override
    public String toString() {
        return "DetailDevisDTO [designation=" + designation + ", quantite=" + quantite + ", unite=" + unite
                + ", prixUnitaire=" + prixUnitaire + "]";
    }
}
