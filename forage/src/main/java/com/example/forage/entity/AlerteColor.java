package com.example.forage.entity;

public enum AlerteColor {
    ALERTE_1_1(1, 2, "#d9ff00", "Demande créée → Devis Étude créé (niveau 1)"),
    ALERTE_1_2(1, 2, "#dc0e0e", "Demande créée → Devis Étude créé (niveau 2)"),

    ALERTE_2_1(2, 3, "#d9ff00", "Devis Étude créé → Devis Étude accepté (niveau 1)"),
    ALERTE_2_2(2, 3, "#dc0e0e", "Devis Étude créé → Devis Étude accepté (niveau 2)"),

    ALERTE_3_1(3, 4, "#d9ff00", "Devis Étude accepté → Devis Forage créé (niveau 1)"),
    ALERTE_3_2(3, 4, "#dc0e0e", "Devis Étude accepté → Devis Forage créé (niveau 2)"),

    ALERTE_4_1(4, 5, "#d9ff00", "Devis Forage créé → Devis Forage accepté (niveau 1)"),
    ALERTE_4_2(4, 5, "#dc0e0e", "Devis Forage créé → Devis Forage accepté (niveau 2)"),

    ALERTE_5_1(5, 6, "#d9ff00", "Devis Forage accepté → Forage commencée (niveau 1)"),
    ALERTE_5_2(5, 6, "#dc0e0e", "Devis Forage accepté → Forage commencée (niveau 2)"),

    ALERTE_6_1(6, 7, "#d9ff00", "Forage commencée → Demande terminée (niveau 1)"),
    ALERTE_6_2(6, 7, "#dc0e0e", "Forage commencée → Demande terminée (niveau 2)");

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

    public static AlerteColor findByTransitionAndNiveau(Integer id1, Integer id2, Integer niveau) {
        if (id1 == null || id2 == null || niveau == null)
            return null;
        for (AlerteColor a : values()) {
            if (a.idStatus1.equals(id1) && a.idStatus2.equals(id2)) {
                // niveau 1 → nom contient "niveau 1", niveau 2 → "niveau 2"
                if (a.description.contains("niveau " + niveau)) {
                    return a;
                }
            }
        }
        return null;
    }

    public static AlerteColor findByTransition(Integer id1, Integer id2) {
        for (AlerteColor a : values()) {
            if (a.idStatus1.equals(id1) && a.idStatus2.equals(id2)) {
                return a;
            }
        }
        return null;
    }

}
