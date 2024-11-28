package com.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proyecto.entidades.Producto;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoria_Nombre(String nombreCategoria);
    List<Producto> findByFaltanteTrue(); // Nuevo método
    List<Producto> findByCategoriaId(Long categoriaId);
    @Query("SELECT p FROM Producto p WHERE p.faltante = false")
    List<Producto> findProductosDisponibles();
    long countByCategoriaId(Long categoriaId);

    


    // Filtrar productos por disponibilidad (faltante)
    public List<Producto> findByFaltante(boolean faltante);

    // Filtrar productos por categoría y disponibilidad
    List<Producto> findByCategoriaIdAndFaltante(Long categoriaId, Boolean faltante);

}
