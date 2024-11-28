package com.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.entidades.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
