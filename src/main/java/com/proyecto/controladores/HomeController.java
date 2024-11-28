package com.proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        // Esto devolverá el archivo index.html ubicado en templates/index/
        return "index/index"; // La carpeta templates/ es implícita y no se necesita especificar
    }
    
    @GetMapping("/menu")
    public String menu() {
        return "index/menu"; // Asegúrate de que el archivo menu.html esté en templates/index/
    }

 // Método que maneja la ruta de la página "Nosotros"
    @GetMapping("/nosotros")
    public String nosotros() {
        // Retorna el nombre del archivo "nosotros.html" ubicado en /src/main/resources/templates/index
        return "index/nosotros";
    }
    
    @GetMapping("/login")
    public String contact() {
        return "login"; // Asegúrate de que el archivo contact.html esté en templates/index/
    }
}
