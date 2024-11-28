package com.proyecto.controladores;

import com.proyecto.entidades.PQR;
import com.proyecto.repositorios.PQRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/pqr")
public class PQRController {

    @Autowired
    private PQRRepository pqrRepositorio;

    // Mostrar todos los PQRs
    @GetMapping("/lista")
    public String listarPQRs(Model model) {
        model.addAttribute("pqrList", pqrRepositorio.findAll());
        return "index/listaPQR"; // Ruta correcta para la vista
    }

    // Mostrar formulario de creación de PQR
    @GetMapping("/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("pqr", new PQR());
        return "index/crearPQR"; // Ruta a tu formulario
    }

    // Procesar el formulario de PQR y mostrar el mensaje de éxito
    @PostMapping("/crear")
    public String guardarPQR(PQR pqr, Model model) {
        // Guardar el PQR en la base de datos
        pqrRepositorio.save(pqr);

        // Agregar mensaje de éxito
        model.addAttribute("mensaje", "PQR enviada - Nos pondremos en contacto con usted pronto.");
        model.addAttribute("pqr", new PQR()); // Resetear el formulario

        // Volver a la página del formulario
        return "index/crearPQR"; // Ruta al formulario
    }


    // Mostrar detalles de un PQR específico
    @GetMapping("/ver/{id}")
    public String verPQR(@PathVariable Long id, Model model) {
        PQR pqr = pqrRepositorio.findById(id).orElse(null);
        model.addAttribute("pqr", pqr);
        return "index/verPQR"; // Ruta correcta para la vista
    }

    // Eliminar un PQR
    @GetMapping("/eliminar/{id}")
    public String eliminarPQR(@PathVariable Long id) {
        pqrRepositorio.deleteById(id);
        return "redirect:/pqr/lista"; // Redirige a la lista de PQRs
    }
}
