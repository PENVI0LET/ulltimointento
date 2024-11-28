package com.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.entidades.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findBynombreUsuarioAndPassword(String nombreUsuario, String password);
}
