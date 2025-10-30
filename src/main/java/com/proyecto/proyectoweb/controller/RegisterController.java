package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.User;
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
    public String submitForm(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam(required = false) String confirm,
                             Model model) {
        // Basic validation
        if (username == null || username.trim().isEmpty()) {
            model.addAttribute("error", "El nombre de usuario es obligatorio");
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

        try {
            User u = userService.register(username.trim(), password);
            model.addAttribute("success", "Usuario registrado correctamente: " + u.getUsername());
            return "login"; // after successful register redirect to login page (template)
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}
