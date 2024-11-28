package com.proyecto.controladores;

import com.proyecto.entidades.Rol;
import com.proyecto.entidades.Usuario;
import com.proyecto.repositorios.RolRepository;
import com.proyecto.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;
    
    // Método para mostrar la página principal de administración con los usuarios
    @GetMapping
    public String mostrarAdminPage(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll(); // Obtener todos los usuarios
        model.addAttribute("usuarios", usuarios); // Pasar los usuarios al modelo
        return "admin/admin"; // La vista de admin que mostrará los usuarios
    }

    // Método para mostrar el formulario de creación de usuario
    @GetMapping("/usuarios/nuevo")
    public String mostrarFormularioCrearUsuario(Model model) {
        List<Rol> roles = rolRepository.findAll(); // Obtener todos los roles
        model.addAttribute("roles", roles); // Pasar los roles al modelo
        model.addAttribute("usuario", new Usuario()); // Crear un nuevo objeto Usuario
        return "admin/crear-usuario"; // Vista para crear un usuario
    }

    @PostMapping("/guardarUsuario")
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioRepository.save(usuario);  // No necesitas establecer 'id', se genera automáticamente
        return "redirect:/admin";
    }


    // Método para mostrar el formulario de edición de usuario
    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEditarUsuario(@PathVariable Long id, Model model) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            List<Rol> roles = rolRepository.findAll(); // Asegúrate de tener el repositorio de roles
            model.addAttribute("usuario", usuarioOpt.get());
            model.addAttribute("roles", roles);
            return "admin/editar-usuario"; // Vista para editar un usuario
        } else {
            return "redirect:/admin";
        }
    }

    // Método para actualizar un usuario
    @PostMapping("/usuarios/actualizar/{id}")
    public String actualizarUsuario(@PathVariable Long id, @ModelAttribute("usuario") Usuario usuarioActualizado) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setNombreUsuario(usuarioActualizado.getNombreUsuario());
            usuario.setPassword(usuarioActualizado.getPassword());
            usuario.setRol(usuarioActualizado.getRol()); // Actualizar el rol
            usuarioRepository.save(usuario);
        }
        return "redirect:/admin"; // Redirigir a la página principal de administración
    }

    // Método para eliminar un usuario
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id); // Eliminar el usuario
        return "redirect:/admin"; // Redirigir a la página principal de administración
    }
}

