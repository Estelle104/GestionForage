package com.example.forage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "parametre")
public class Parametre {

    @Id
    @Column(name = "id_status1")
    private Integer idStatus1;

    @Column(name = "id_status2")
    private Integer idStatus2;

    @Column(name = "duree_minute", nullable = false)
    private Double dureeMinute;

    @Column(name = "alerte")
    private Integer alerte;

    public Parametre() {
    }

    public Parametre(Integer idStatus1, Integer idStatus2, Double dureeMinute, Integer alerte) {
        this.idStatus1 = idStatus1;
        this.idStatus2 = idStatus2;
        this.dureeMinute = dureeMinute;
        this.alerte = alerte;
    }

    public Integer getIdStatus1() {
        return idStatus1;
    }

    public void setIdStatus1(Integer idStatus1) {
        this.idStatus1 = idStatus1;
    }

    public Integer getIdStatus2() {
        return idStatus2;
    }

    public void setIdStatus2(Integer idStatus2) {
        this.idStatus2 = idStatus2;
    }

    public Double getDureeMinute() {
        return dureeMinute;
    }

    public void setDureeMinute(Double dureeMinute) {
        this.dureeMinute = dureeMinute;
    }

    public Integer getAlerte() {
        return alerte;
    }

    public void setAlerte(Integer alerte) {
        this.alerte = alerte;
    }

    @Override
    public String toString() {
        return "Parametre [idStatus1=" + idStatus1 + ", idStatus2=" + idStatus2 + ", dureeMinute=" + dureeMinute
                + ", alerte=" + alerte + "]";
    }
}
