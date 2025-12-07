package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.UserD;
import com.proyecto.proyectoweb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    
    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null)
            model.addAttribute("error", error);
        return "login";
    }

    // El manejo del POST /login ahora lo realiza Spring Security (formLogin). Este método
    // personalizado se elimina para evitar conflicto y doble autenticación.
}

