package com.proyecto.proyectoweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("activePage", "contact");
        return "contact";
    }

    @PostMapping("/contact")
    public String handleContactForm(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message,
            Model model) {
        
        // Aquí iría la lógica para enviar el correo
        // Por ahora solo mostramos mensaje de éxito
        
        model.addAttribute("success", true);
        model.addAttribute("message", "Tu mensaje ha sido enviado. Te contactaremos pronto.");
        return "contact";
    }
}