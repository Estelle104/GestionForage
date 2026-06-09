package com.example.forage.entity;

public enum AlerteColor {
    // 7 Alertes pour les 7 transitions de statuts
    ALERTE_1(1, 2, "#FFE69C", "Demande créée → Devis Étude créé"),
    ALERTE_2(2, 3, "#FFB3B3", "Devis Étude créé → Devis Étude accepté"),
    ALERTE_3(3, 4, "#FFDA6A", "Devis Étude accepté → Devis Étude refusé"),
    ALERTE_4(4, 5, "#FF6666", "Devis Étude refusé → Devis Forage créé"),
    ALERTE_5(5, 6, "#FFC107", "Devis Forage créé → Devis Forage accepté"),
    ALERTE_6(6, 7, "#DC3545", "Devis Forage accepté → Devis Forage refusé"),
    ALERTE_7(7, 8, "#7A0019", "Devis Forage refusé → Demande terminée");


    private final Integer idStatus1;
    private final Integer idStatus2;
    private final String hexColor;
    private final String description;

    AlerteColor(Integer idStatus1, Integer idStatus2, String hexColor, String description) {
        this.idStatus1 = idStatus1;
        this.idStatus2 = idStatus2;
        this.hexColor = hexColor;
        this.description = description;
    }

    public Integer getIdStatus1() {
        return idStatus1;
    }

    public Integer getIdStatus2() {
        return idStatus2;
    }

    public String getHexColor() {
        return hexColor;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Récupère la couleur d'alerte pour une transition de statuts donnée
     */
    public static AlerteColor getAlerteByStatuses(Integer status1Id, Integer status2Id) {
        if (status1Id == null || status2Id == null) {
            return null;
        }
        for (AlerteColor alerte : AlerteColor.values()) {
            if (alerte.idStatus1.equals(status1Id) && alerte.idStatus2.equals(status2Id)) {
                return alerte;
            }
        }
        return null;
    }
}
