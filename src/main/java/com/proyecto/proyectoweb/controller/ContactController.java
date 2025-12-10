package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class ContactController {
    
    private final EmailService emailService;
    
    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

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
        
        try {
            String adminEmail = "saludconamoor@gmail.com";
            emailService.sendContactEmail(adminEmail, name, email, subject, message);
            emailService.sendConfirmationToUser(email, name);
            
            log.info("Email de contacto enviado exitosamente. De: {} ({})", name, email);
            
            model.addAttribute("success", true);
            model.addAttribute("message", "¡Mensaje enviado! Te contactaremos pronto.");
            
        } catch (Exception e) {
            log.error("Error enviando email de contacto", e);
            
            model.addAttribute("success", false);
            model.addAttribute("message", "Error al enviar el mensaje. Intenta nuevamente.");
        }
        
        model.addAttribute("activePage", "contact");
        return "contact";
    }
}