package com.proyecto.controladores;

import com.proyecto.entidades.Cliente;
import com.proyecto.entidades.DetallePedido;
import com.proyecto.entidades.Pedido;
import com.proyecto.entidades.Producto;
import com.proyecto.repositorios.ClienteRepository;
import com.proyecto.repositorios.DetallesPedidoRepository;
import com.proyecto.repositorios.PedidoRepository;
import com.proyecto.repositorios.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequestMapping("/trabajador")
public class PedidoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallesPedidoRepository detallePedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping("/crear-pedido")
    public String mostrarFormularioPedido(Model model) {
        List<Producto> productos = productoRepository.findAll();
        model.addAttribute("productos", productos);
        return "trabajador/crear-pedido";
    }

    @GetMapping("/clientes/buscar")
    @ResponseBody
    public Cliente buscarCliente(@RequestParam("id") Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @PostMapping("/pedidos/guardar")
    public String guardarPedido(
            @RequestParam("idCliente") Long idCliente,
            @RequestParam("nombreCliente") String nombreCliente,
            @RequestParam("telefonoCliente") String telefonoCliente,
            @RequestParam("direccionCliente") String direccionCliente,
            @RequestParam("productoId") List<Long> productoIds,
            @RequestParam("cantidad") List<Integer> cantidades,
            Model model) {

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElse(new Cliente(nombreCliente, telefonoCliente, direccionCliente));
        cliente.setId(idCliente);
        clienteRepository.save(cliente);

        Pedido pedido = new Pedido(cliente, new Date());
        pedidoRepository.save(pedido);

        BigDecimal totalPedido = BigDecimal.ZERO;

        for (int i = 0; i < productoIds.size(); i++) {
            Long productoId = productoIds.get(i);
            Integer cantidad = cantidades.get(i);

            if (cantidad != null && cantidad > 0) {
                Optional<Producto> productoOpt = productoRepository.findById(productoId);
                if (productoOpt.isPresent()) {
                    Producto producto = productoOpt.get();
                    DetallePedido detallePedido = new DetallePedido(pedido, producto, cantidad);
                    detallePedidoRepository.save(detallePedido);

                    totalPedido = totalPedido.add(producto.calcularSubtotal(cantidad));
                }
            }
        }

        pedido.setTotal(totalPedido);
        pedidoRepository.save(pedido);

        model.addAttribute("totalPedido", totalPedido);
        return "redirect:/trabajador/crear-pedido?success=true&total=" + totalPedido;
    }

    @GetMapping("/listar-pedidos")
    public String listarPedidos(Model model) {
        List<Pedido> pedidos = pedidoRepository.findAll();

        pedidos.forEach(pedido -> {
            if (pedido.getTotal() != null) {
                // Normalizar el formato numérico para evitar errores
                try {
                    NumberFormat format = NumberFormat.getInstance(Locale.US);
                    Number number = format.parse(pedido.getTotal().toString());
                    pedido.setTotal(BigDecimal.valueOf(number.doubleValue()));
                } catch (ParseException e) {
                    pedido.setTotal(BigDecimal.ZERO);
                }
            } else {
                pedido.setTotal(BigDecimal.ZERO);
            }
        });

        model.addAttribute("pedidos", pedidos);
        return "trabajador/listar-pedidos";
    }

    @GetMapping("/pedido/detalle/{id}")
    public String detallesPedido(@PathVariable("id") Long id, Model model) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        List<DetallePedido> detallesPedido = detallePedidoRepository.findByPedidoId(id);

        detallesPedido.forEach(detalle -> {
            detalle.setSubtotal(detalle.getSubtotal() != null ? detalle.getSubtotal() : BigDecimal.ZERO);
        });

        pedido.setTotal(pedido.getTotal() != null ? pedido.getTotal() : BigDecimal.ZERO);

        model.addAttribute("pedido", pedido);
        model.addAttribute("detalles", detallesPedido);

        return "trabajador/detalles-pedido";
    }
}