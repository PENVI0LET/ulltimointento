package com.proyecto.controladores;

import com.proyecto.entidades.Categoria;
import com.proyecto.entidades.Inventario;
import com.proyecto.entidades.Producto;
import com.proyecto.repositorios.CategoriaRepository;
import com.proyecto.repositorios.DetallesPedidoRepository;
import com.proyecto.repositorios.InventarioRepository;
import com.proyecto.repositorios.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/gestor")
public class GestorController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Ruta para el dashboard
    @GetMapping("/dashboard")
    public String mostrarDashboard() {
        return "gestor/dashboard";  // Asegúrate de tener una vista llamada dashboard.html o dashboard.jsp
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
        return "gestor/productos"; // Vista para mostrar los productos
    }

    // Formulario para crear un nuevo producto
    @GetMapping("/productos/nuevo")
    public String mostrarFormularioProducto(Model model) {
        model.addAttribute("producto", new Producto());
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
        return "gestor/formularioProducto";
    }

    // Guardar producto
    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto) {
        productoRepository.save(producto);
        return "redirect:/gestor/productos";
    }

    @GetMapping("/productos/editar/{id}")
    public String mostrarFormularioEditarProducto(@PathVariable("id") Long id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(null);
        if (producto == null) {
            model.addAttribute("error", "Producto no encontrado.");
            return "redirect:/gestor/productos";
        }
        model.addAttribute("producto", producto);
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
        return "gestor/formularioProducto";
    }

    @PostMapping("/productos/editar")
    public String actualizarProducto(@ModelAttribute("producto") Producto producto) {
        productoRepository.save(producto);
        return "redirect:/gestor/productos";
    }



    //ELIMINAR PRODUCTO
    @Autowired
    private DetallesPedidoRepository detallesPedidoRepository; // Inyectar el repositorio
    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        long relaciones = detallesPedidoRepository.countByProductoId(id);

        if (relaciones > 0) {
            // Producto tiene relaciones, no se puede eliminar
            redirectAttributes.addFlashAttribute("error",
                    "POR NORMATIVA DE SUPERINTENDENCIA DE INDUSTRIA Y COMERCIO NO SE PUEDE ELIMINAR UN PRODUCTO QUE TENGA UNA COMPRA - CONTACTA CON EL ADMINISTRADOR.");
            return "redirect:/gestor/productos";
        }

        // Producto no tiene relaciones, se puede eliminar
        productoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente.");
        return "redirect:/gestor/productos";
    }

    // Listar categorías
    @GetMapping("/categorias")
    public String listarCategorias(Model model) {
        List<Categoria> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
        return "gestor/categorias";
    }

    // Formulario para crear una nueva categoría
    @GetMapping("/categorias/nueva")
    public String mostrarFormularioCategoria(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "gestor/formularioCategoria";
    }

    // Guardar categoría
    @PostMapping("/categorias/guardar")
    public String guardarCategoria(@ModelAttribute("categoria") Categoria categoria) {
        categoriaRepository.save(categoria);
        return "redirect:/gestor/categorias";
    }

    // Editar categoría
    @GetMapping("/categorias/editar/{id}")
    public String mostrarFormularioEditarCategoria(@PathVariable("id") Long id, Model model) {
        Categoria categoria = categoriaRepository.findById(id).orElse(null);
        model.addAttribute("categoria", categoria);
        return "gestor/formularioCategoria";
    }

 // Eliminar categoría
    @GetMapping("/categorias/eliminar/{id}")
    public String eliminarCategoria(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            // Verificar si la categoría está asociada a productos u otras entidades
            long relaciones = productoRepository.countByCategoriaId(id);

            if (relaciones > 0) {
                // Si hay relaciones, no permitimos eliminar
                redirectAttributes.addFlashAttribute("error",
                        "POR NORMATIVA DE LA SUPERINTENDENCIA DE INDUSTRIA Y COMERCIO NO PUEDES ELIMINAR UNA CATEGORÍA SI ESTÁ ASOCIADA A UNA COMPRA - CONTACTA CON EL ADMINISTRADOR.");
                return "redirect:/gestor/categorias";
            }

            // Si no hay relaciones, procedemos a eliminar
            categoriaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Categoría eliminada exitosamente.");
        } catch (Exception e) {
            // Manejar cualquier error inesperado
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al intentar eliminar la categoría.");
        }
        return "redirect:/gestor/categorias";
    }

    
    @Autowired
    private InventarioRepository inventarioRepository;

    // Listar inventarios
    @GetMapping("/inventario")
    public String listarInventarios(Model model) {
        List<Inventario> inventarios = inventarioRepository.findAll();
        model.addAttribute("inventarios", inventarios);
        return "gestor/inventario";
    }

    // Formulario para agregar nuevo inventario
    @GetMapping("/inventario/nuevo")
    public String nuevoInventario(Model model) {
        model.addAttribute("inventario", new Inventario());
        return "gestor/formularioInventario";
    }

    // Guardar un nuevo inventario
    @PostMapping("/inventario/guardar")
    public String guardarInventario(@ModelAttribute("inventario") Inventario inventario) {
        inventario.setFaltante(false); // Por defecto, todos los nuevos inventarios estarán disponibles.
        inventarioRepository.save(inventario);
        return "redirect:/gestor/inventario";
    }

    // Editar inventario
    @GetMapping("/inventario/editar/{id}")
    public String editarInventario(@PathVariable("id") Long id, Model model) {
        Inventario inventario = inventarioRepository.findById(id).orElse(null);
        if (inventario == null) {
            model.addAttribute("error", "Inventario no encontrado.");
            return "redirect:/gestor/inventario";
        }
        model.addAttribute("inventario", inventario);
        return "gestor/editarInventario";
    }

    // Actualizar inventario
    @PostMapping("/inventario/actualizar")
    public String actualizarInventario(@ModelAttribute("inventario") Inventario inventario) {
        inventarioRepository.save(inventario);
        return "redirect:/gestor/inventario";
    }

    // Cambiar estado entre faltante y disponible
    @PostMapping("/inventario/toggle/{id}")
    public String cambiarEstadoInventario(@PathVariable Long id) {
        Inventario inventario = inventarioRepository.findById(id).orElse(null);
        if (inventario != null) {
            inventario.setFaltante(!inventario.getFaltante()); // Alterna el estado.
            inventarioRepository.save(inventario);
        }
        return "redirect:/gestor/inventario";
    }

    // Eliminar inventario
    @GetMapping("/inventario/eliminar/{id}")
    public String eliminarInventario(@PathVariable Long id) {
        inventarioRepository.deleteById(id);
        return "redirect:/gestor/inventario";
    }

    
    
}
