package com.example.forage.entity;


public enum TypeDevisStatus {
    FORAGE(1, 6, "DEVIS_FORAGE_CREE"),
    ETUDE(2, 7, "DEVIS_ETUDE_CREE");

    private final Integer typeId;
    private final Integer statusId;
    private final String statusLibele;

    TypeDevisStatus(Integer typeId, Integer statusId, String statusLibele) {
        this.typeId = typeId;
        this.statusId = statusId;
        this.statusLibele = statusLibele;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getStatusLibele() {
        return statusLibele;
    }

    
    public static Integer getStatusIdByTypeId(Integer typeId) {
        if (typeId == null) {
            return null;
        }
        for (TypeDevisStatus mapping : TypeDevisStatus.values()) {
            if (mapping.typeId.equals(typeId)) {
                return mapping.statusId;
            }
        }
        return null;
    }

    
    public static String getStatusLibeleByTypeId(Integer typeId) {
        if (typeId == null) {
            return null;
        }
        for (TypeDevisStatus mapping : TypeDevisStatus.values()) {
            if (mapping.typeId.equals(typeId)) {
                return mapping.statusLibele;
            }
        }
        return null;
    }

   
    public static TypeDevisStatus getByTypeId(Integer typeId) {
        if (typeId == null) {
            return null;
        }
        for (TypeDevisStatus mapping : TypeDevisStatus.values()) {
            if (mapping.typeId.equals(typeId)) {
                return mapping;
            }
        }
        return null;
    }
}
