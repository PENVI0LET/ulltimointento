package com.proyecto.controladores;

import com.proyecto.entidades.Categoria;
import com.proyecto.entidades.Inventario;
import com.proyecto.entidades.Producto;
import com.proyecto.repositorios.CategoriaRepository;
import com.proyecto.repositorios.InventarioRepository;
import com.proyecto.repositorios.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/trabajador") // Ahora apunta al módulo "trabajador"
public class TrabajadorController {
    
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository; // Inyección del repositorio de Categoría
    
    @Autowired
    private InventarioRepository inventarioRepository;

    // Ruta principal para el dashboard
    @GetMapping("/index")
    public String mostrarIndex() {
        return "trabajador/index"; // Redirige al dashboard principal
    }

    // Ver todos los productos o filtrarlos por categoría
    @GetMapping("/productos")
    public String listarProductos(@RequestParam(required = false) Long categoriaId, Model model) {
        // Obtener todas las categorías para mostrar en el filtro
        List<Categoria> categorias = categoriaRepository.findAll(); // Usando la instancia inyectada
        model.addAttribute("categorias", categorias);

        // Filtrar productos por categoría si se seleccionó una categoría, de lo contrario mostrar todos
        List<Producto> productos;
        if (categoriaId != null && categoriaId > 0) {
            productos = productoRepository.findByCategoriaId(categoriaId);
        } else {
            productos = productoRepository.findAll();
        }

        model.addAttribute("productos", productos);
        return "trabajador/productos"; // Vista para mostrar los productos
    }

    // Marcar producto como faltante
    @PostMapping("/productos/marcar-faltante/{id}")
    public String marcarFaltante(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setFaltante(true); // Cambiar el estado de "faltante"
            productoRepository.save(producto);
        }
        return "redirect:/trabajador/productos"; // Redirige a la lista de productos
    }
    
 // Ver el inventario
    @GetMapping("/inventario")
    public String verInventario(Model model) {
        List<Inventario> inventario = inventarioRepository.findAll(); // Obtener todos los elementos del inventario
        model.addAttribute("inventario", inventario);
        return "trabajador/inventario"; // Vista para mostrar el inventario
    }

    // Marcar un elemento del inventario como faltante
    @PostMapping("/inventario/marcar-faltante/{id}")
    public String marcarFaltanteInventario(@PathVariable Long id) {
        Optional<Inventario> inventarioOpt = inventarioRepository.findById(id);
        if (inventarioOpt.isPresent()) {
            Inventario item = inventarioOpt.get();
            item.setFaltante(true); // Marcar como faltante
            inventarioRepository.save(item);
        }
        return "redirect:/trabajador/inventario"; // Redirige al inventario
    }
}
