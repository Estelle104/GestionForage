package com.example.forage.entity;

public enum StatusType {
    DEMANDE_CREEE(1, "DEMANDE_CREEE"),
    DEVIS_ETUDE_CREE(2, "DEVIS_ETUDE_CREE"),
    DEVIS_ETUDE_ACCEPTE(3, "DEVIS_ETUDE_ACCEPTE"),
    DEVIS_ETUDE_REFUSE(4, "DEVIS_ETUDE_REFUSE"),
    DEVIS_FORAGE_CREE(5, "DEVIS_FORAGE_CREE"),
    DEVIS_FORAGE_ACCEPTE(6, "DEVIS_FORAGE_ACCEPTE"),
    DEVIS_FORAGE_REFUSE(7, "DEVIS_FORAGE_REFUSE"),
    DEMANDE_TERMINEE(8, "DEMANDE_TERMINEE");

    private final Integer id;
    private final String libele;

    StatusType(Integer id, String libele) {
        this.id = id;
        this.libele = libele;
    }

    public Integer getId() {
        return id;
    }

    public String getLibele() {
        return libele;
    }

    public static StatusType getById(Integer id) {
        if (id == null) {
            return null;
        }
        for (StatusType status : StatusType.values()) {
            if (status.id.equals(id)) {
                return status;
            }
        }
        return null;
    }

    public static StatusType getByLibele(String libele) {
        if (libele == null) {
            return null;
        }
        for (StatusType status : StatusType.values()) {
            if (status.libele.equals(libele)) {
                return status;
            }
        }
        return null;
    }
}
