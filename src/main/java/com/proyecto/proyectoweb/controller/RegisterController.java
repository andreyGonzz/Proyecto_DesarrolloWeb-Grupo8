package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.domain.User;
import com.proyecto.proyectoweb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showForm() {
        return "register";
    }

    @PostMapping("/register")
    public String submitForm(@RequestParam String email,
                             @RequestParam String nombre,
                             @RequestParam String apellidos,
                             @RequestParam String password,
                             @RequestParam(required = false) String confirm,
                             Model model) {
        // Basic validation
        if (email == null || email.trim().isEmpty()) {
            model.addAttribute("error", "El email es obligatorio");
            return "register";
        }
        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "La contraseña es obligatoria");
            return "register";
        }
        if (confirm != null && !password.equals(confirm)) {
            model.addAttribute("error", "Las contraseñas no coinciden");
            return "register";
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            model.addAttribute("error", "El nombre es obligatorio");
            return "register";
        }

        if (apellidos == null || apellidos.trim().isEmpty()) {
            model.addAttribute("error", "Los apellidos son obligatorio");
            return "register";
        }

        try {
            User u = userService.register(email, password, nombre, apellidos);
            model.addAttribute("success", "Usuario registrado correctamente: " + u.getNombre());
            return "login"; // after successful register redirect to login page (template)
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}
