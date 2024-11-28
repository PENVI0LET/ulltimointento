package com.proyecto.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "inventario")
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String item;

    @Column(nullable = false)
    private Integer cantidad;

    @Column
    private Boolean faltante; // Campo opcional, true para "faltante", false para "disponible"

    public Inventario() {}

    public Inventario(String item, Integer cantidad, Boolean faltante) {
        this.item = item;
        this.cantidad = cantidad;
        this.faltante = faltante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getFaltante() {
        return faltante;
    }

    public void setFaltante(Boolean faltante) {
        this.faltante = faltante;
    }
}
