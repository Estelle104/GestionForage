package com.example.forage.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "parametre")
public class Parametre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_status1", nullable = false)
    private Integer idStatus1;

    @Column(name = "id_status2", nullable = false)
    private Integer idStatus2;

    @Column(name = "debut_minute", nullable = false)
    private Double debutMinute;

    @Column(name = "fin_minute", nullable = false)
    private Double finMinute;

    @Column(name = "alerte")
    private Integer alerte;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getDebutMinute() {
        return debutMinute;
    }

    public void setDebutMinute(Double debutMinute) {
        this.debutMinute = debutMinute;
    }

    public Double getFinMinute() {
        return finMinute;
    }

    public void setFinMinute(Double finMinute) {
        this.finMinute = finMinute;
    }

    public Integer getAlerte() {
        return alerte;
    }

    public void setAlerte(Integer alerte) {
        this.alerte = alerte;
    }

    // constructeurs, getters, setters...
}