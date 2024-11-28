package com.proyecto.entidades;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos") // Nombre de la tabla en plural
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autogenerado
    private Long id;

    @Column(nullable = false, unique = true, length = 100) // El nombre debe ser único y no nulo
    private String nombre;

    @Column(nullable = false) // El precio debe ser obligatorio
    private Double precio;

    @ManyToOne // Muchos productos pertenecen a una categoría
    @JoinColumn(name = "categoria_id", nullable = false) // Llave foránea
    private Categoria categoria;

    @Column(nullable = false) // Estado del producto: disponible o faltante
    private boolean faltante = false;

    // Constructor por defecto
    public Producto() {}

    // Constructor parametrizado
    public Producto(String nombre, Double precio, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public boolean isFaltante() { // Correcto para boolean
        return faltante;
    }

    public void setFaltante(boolean faltante) {
        this.faltante = faltante;
    }

    /**
     * Calcula el subtotal basado en una cantidad.
     *
     * @param cantidad Cantidad del producto
     * @return Subtotal como BigDecimal
     */
    public BigDecimal calcularSubtotal(int cantidad) {
        return BigDecimal.valueOf(precio).multiply(BigDecimal.valueOf(cantidad));
    }
}
