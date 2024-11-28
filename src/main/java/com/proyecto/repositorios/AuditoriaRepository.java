package com.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.entidades.Auditoria;

public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
}
