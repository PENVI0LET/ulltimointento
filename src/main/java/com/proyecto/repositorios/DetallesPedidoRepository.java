package com.proyecto.repositorios;

import com.proyecto.entidades.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetallesPedidoRepository extends JpaRepository<DetallePedido, Long> {

    // Método personalizado para encontrar detalles por ID de pedido
    List<DetallePedido> findByPedidoId(Long pedidoId);
    long countByProductoId(Long productoId);

}
