package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.User;
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

    // Root should show the login page
    @GetMapping({"/", "/login"})
    public String loginForm(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) model.addAttribute("error", error);
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username,
                              @RequestParam String password,
                              Model model) {
        Optional<User> maybe = userService.findByUsername(username);
        if (maybe.isPresent()) {
            User u = maybe.get();
            if (u.getPassword() != null && u.getPassword().equals(password)) {
                // success -> redirect to index
                return "redirect:/index";
            }
        }
        model.addAttribute("error", "Usuario o contraseña incorrectos");
        return "login";
    }
}
