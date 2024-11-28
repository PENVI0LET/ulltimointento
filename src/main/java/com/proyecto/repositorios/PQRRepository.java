package com.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.entidades.PQR;

public interface PQRRepository extends JpaRepository<PQR, Long> {
}
