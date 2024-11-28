package com.proyecto.controladores;

import com.proyecto.entidades.Categoria;
import com.proyecto.entidades.DetallePedido;
import com.proyecto.entidades.Inventario;
import com.proyecto.entidades.Pedido;
import com.proyecto.entidades.Producto;
import com.proyecto.entidades.Usuario;
import com.proyecto.repositorios.CategoriaRepository;
import com.proyecto.repositorios.DetallesPedidoRepository;
import com.proyecto.repositorios.InventarioRepository;
import com.proyecto.repositorios.PedidoRepository;
import com.proyecto.repositorios.ProductoRepository;
import com.proyecto.repositorios.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/gerente") // Endpoint para el módulo del gerente
public class GerenteController {
    
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;
    

    @Autowired
    private InventarioRepository inventarioRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private DetallesPedidoRepository detallePedidoRepository;

    // Panel principal del gerente
    @GetMapping("/index")
    public String mostrarIndex() {
        return "gerente/index"; // Redirige a la vista del panel principal
    }
    
 // Listar productos o filtrarlos por categoría
    @GetMapping("/productos")
    public String listarProductos(@RequestParam(required = false) Long categoriaId, Model model) {
        // Cargar todas las categorías para el filtro
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);

        // Filtrar productos si hay una categoría seleccionada
        List<Producto> productos;
        if (categoriaId != null && categoriaId > 0) {
            productos = productoRepository.findByCategoriaId(categoriaId);
        } else {
            productos = productoRepository.findAll();
        }

        model.addAttribute("productos", productos);
        return "gerente/productos"; // Vista de gestión de productos para el gerente
    }

    // Marcar producto como faltante
    @PostMapping("/productos/marcar-faltante/{id}")
    public String marcarFaltante(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setFaltante(true); // Cambiar estado a faltante
            productoRepository.save(producto);
        }
        return "redirect:/gerente/productos"; // Redirigir a la lista de productos
    }

    // Marcar producto como disponible
    @PostMapping("/productos/marcar-disponible/{id}")
    public String marcarDisponible(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setFaltante(false); // Cambiar estado a disponible
            productoRepository.save(producto);
        }
        return "redirect:/gerente/productos"; // Redirigir a la lista de productos
    }
    
    
    // Listar el inventario
    @GetMapping("/inventario")
    public String listarInventario(Model model) {
        List<Inventario> inventario = inventarioRepository.findAll();
        model.addAttribute("inventario", inventario);
        return "gerente/inventario";
    }

    // Mostrar formulario de edición para un elemento del inventario
    @GetMapping("/inventario/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Optional<Inventario> inventarioOpt = inventarioRepository.findById(id);
        if (inventarioOpt.isPresent()) {
            model.addAttribute("item", inventarioOpt.get());
            return "gerente/editar_inventario";
        } else {
            return "redirect:/gerente/inventario"; // Redirigir si el ID no existe
        }
    }

    // Guardar los cambios de edición del inventario
    @PostMapping("/inventario/editar/{id}")
    public String guardarEdicion(@PathVariable Long id,
                                 @RequestParam String item,
                                 @RequestParam Integer cantidad,
                                 @RequestParam Boolean faltante) {
        Optional<Inventario> inventarioOpt = inventarioRepository.findById(id);
        if (inventarioOpt.isPresent()) {
            Inventario inventario = inventarioOpt.get();
            inventario.setItem(item);
            inventario.setCantidad(cantidad);
            inventario.setFaltante(faltante);
            inventarioRepository.save(inventario);
        }
        return "redirect:/gerente/inventario";
    }

    // Eliminar un elemento del inventario
    @PostMapping("/inventario/eliminar/{id}")
    public String eliminarInventario(@PathVariable Long id) {
        inventarioRepository.deleteById(id);
        return "redirect:/gerente/inventario";
    }
    
 // Listar trabajadores
    @GetMapping("/trabajadores")
    public String listarTrabajadores(Model model) {
        List<Usuario> trabajadores = usuarioRepository.findAll()
                .stream()
                .filter(usuario -> usuario.getRol().getId() == 4) // Filtrar por rol de trabajador (id = 4)
                .toList();
        model.addAttribute("trabajadores", trabajadores);
        return "gerente/trabajadores"; // Vista para listar trabajadores
    }

    // Mostrar formulario de edición de un trabajador
    @GetMapping("/trabajadores/editar/{id}")
    public String mostrarFormularioEdicionTrabajador(@PathVariable Long id, Model model) {
        Optional<Usuario> trabajadorOpt = usuarioRepository.findById(id);
        if (trabajadorOpt.isPresent() && trabajadorOpt.get().getRol().getId() == 4) {
            model.addAttribute("trabajador", trabajadorOpt.get());
            return "gerente/editar_trabajador"; // Vista para editar trabajador
        }
        return "redirect:/gerente/trabajadores";
    }

    // Guardar cambios al editar un trabajador
    @PostMapping("/trabajadores/editar/{id}")
    public String guardarEdicionTrabajador(
            @PathVariable Long id,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String email) {
        Optional<Usuario> trabajadorOpt = usuarioRepository.findById(id);
        if (trabajadorOpt.isPresent() && trabajadorOpt.get().getRol().getId() == 4) {
            Usuario trabajador = trabajadorOpt.get();
            trabajador.setNombre(nombre);
            trabajador.setApellido(apellido);
            trabajador.setEmail(email);
            usuarioRepository.save(trabajador);
        }
        return "redirect:/gerente/trabajadores";
    }

    // Eliminar un trabajador
    @PostMapping("/trabajadores/eliminar/{id}")
    public String eliminarTrabajador(@PathVariable Long id) {
        Optional<Usuario> trabajadorOpt = usuarioRepository.findById(id);
        if (trabajadorOpt.isPresent() && trabajadorOpt.get().getRol().getId() == 4) {
            usuarioRepository.delete(trabajadorOpt.get());
        }
        return "redirect:/gerente/trabajadores";
    }
    
    @GetMapping("/pedidos")
    public String listarPedidos(Model model) {
        List<Pedido> pedidos = pedidoRepository.findAll();
        BigDecimal totalPedidos = pedidos.stream()
                                         .map(Pedido::getTotal)
                                         .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("totalPedidos", totalPedidos);
        return "gerente/pedidos"; // Vista de gestión de pedidos
    }
    
    @PostMapping("/pedidos/eliminar/{id}")
    public String eliminarPedido(@PathVariable Long id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            pedidoRepository.delete(pedidoOpt.get());
        }
        return "redirect:/gerente/pedidos";
    }
    
    @GetMapping("/pedidos/detalle/{id}")
    public String mostrarDetallePedido(@PathVariable Long id, Model model) {
        // Buscar el pedido por su ID
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado")); // Lanza una excepción si no se encuentra el pedido

        // Obtener los detalles del pedido
        List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(id);

        // Asegurar que el total del pedido y los subtotales no sean nulos
        pedido.setTotal(pedido.getTotal() != null ? pedido.getTotal() : BigDecimal.ZERO);
        
        detalles.forEach(detalle -> {
            detalle.setSubtotal(detalle.getSubtotal() != null ? detalle.getSubtotal() : BigDecimal.ZERO);
        });

        // Pasar los datos al modelo
        model.addAttribute("pedido", pedido);
        model.addAttribute("detalle", detalles);

        // Retornar la vista para mostrar los detalles del pedido
        return "gerente/detalle_pedido";
    }

}