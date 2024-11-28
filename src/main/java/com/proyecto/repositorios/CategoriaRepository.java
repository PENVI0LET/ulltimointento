package com.proyecto.repositorios;

import com.proyecto.entidades.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    // Ejemplo de método personalizado para buscar categorías por nombre
    Categoria findByNombre(String nombre);
}
