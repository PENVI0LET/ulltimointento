package com.proyecto.controladores;

import com.proyecto.entidades.Usuario;
import com.proyecto.repositorios.UsuarioRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/auth")
@SessionAttributes("usuario")  // Para mantener el usuario en la sesión
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Método para manejar el login y redirigir a la vista adecuada
    @PostMapping("/login")
    public String login(@RequestParam String nombreUsuario, @RequestParam String password, Model model) {
        // Verificamos las credenciales
        Optional<Usuario> usuarioOpt = usuarioRepository.findBynombreUsuarioAndPassword(nombreUsuario, password);

        if (!usuarioOpt.isPresent()) {
            model.addAttribute("error", "Credenciales incorrectas");
            return "login"; // Retorna a la vista de login si las credenciales son incorrectas
        }

        // Si las credenciales son correctas, obtener el usuario y almacenarlo en la sesión
        Usuario usuario = usuarioOpt.get();
        String rol = usuario.getRol().getNombre();
        model.addAttribute("usuario", usuario);  // Guardar el usuario en el modelo (y en sesión por @SessionAttributes)

        // Redirigir según el rol
        switch (rol) {
            case "ADMIN":
                return "redirect:/admin"; // Redirige a la vista de admin
            case "GESTOR":
                return "redirect:/gestor/dashboard"; // Redirige al dashboard del gestor
            case "GERENTE":
                return "redirect:/gerente/index"; // Redirige al dashboard del gerente
            case "TRABAJADOR":
                return "redirect:/trabajador/index"; // Redirige al dashboard del trabajador
            case "cliente":
                return "clienteDashboard"; // Redirige al dashboard del cliente
            default:
                return "login"; // Si no hay un rol definido, redirige al login
        }
    }

    // Método para manejar el cierre de sesión
    @GetMapping("/logout")
    public String logout(SessionStatus status) {
        status.setComplete();  // Elimina el usuario de la sesión
        return "redirect:/"; // Redirige a la página de inicio (puedes poner el nombre de la página de login si prefieres)
    }

    // Método para verificar si el usuario está autenticado antes de acceder a cualquier página
    @ModelAttribute("usuario")
    public Usuario verificarSesion(Model model) {
        if (model.containsAttribute("usuario")) {
            return (Usuario) model.getAttribute("usuario");
        }
        return null; // Si no hay un usuario en la sesión, no retorna nada
    }
}
