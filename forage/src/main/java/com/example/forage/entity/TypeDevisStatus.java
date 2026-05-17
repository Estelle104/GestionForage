package com.example.forage.entity;


public enum TypeDevisStatus {
    FORAGE(1L, 6L, "DEVIS_FORAGE_CREE"),
    ETUDE(2L, 7L, "DEVIS_ETUDE_CREE");

    private final Long typeId;
    private final Long statusId;
    private final String statusLibele;

    TypeDevisStatus(Long typeId, Long statusId, String statusLibele) {
        this.typeId = typeId;
        this.statusId = statusId;
        this.statusLibele = statusLibele;
    }

    public Long getTypeId() {
        return typeId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public String getStatusLibele() {
        return statusLibele;
    }

    
    public static Long getStatusIdByTypeId(Long typeId) {
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

    
    public static String getStatusLibeleByTypeId(Long typeId) {
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

   
    public static TypeDevisStatus getByTypeId(Long typeId) {
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
