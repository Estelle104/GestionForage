package com.example.forage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "status_devis")
public class StatusDevis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libele", nullable = false, unique = true)
    private String libele;

    public StatusDevis() {
    }

    public StatusDevis(Long id, String libele) {
        this.id = id;
        this.libele = libele;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibele() {
        return libele;
    }

    public void setLibele(String libele) {
        this.libele = libele;
    }

    @Override
    public String toString() {
        return "StatusDevis [id=" + id + ", libele=" + libele + "]";
    }
}
