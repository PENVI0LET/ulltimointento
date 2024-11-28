package com.proyecto.repositorios;

import com.proyecto.entidades.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Ejemplo de método para buscar pedidos por cliente
    List<Pedido> findByClienteId(Long clienteId);

    // Ejemplo de método para buscar pedidos realizados en una fecha específica
    List<Pedido> findByFecha(LocalDateTime fecha);
}
