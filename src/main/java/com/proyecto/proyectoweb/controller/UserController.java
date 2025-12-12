package com.proyecto.proyectoweb.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.proyecto.proyectoweb.domain.User;
import com.proyecto.proyectoweb.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("usuarioActual")
    public User usuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return userService.findByEmail(auth.getName()).orElse(null);
        }
        return null;
    }

    @GetMapping("/usuario/editar")
    public String mostrarFormularioEdicion(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User usuario = userService.findByEmail(email).orElse(null);
        model.addAttribute("usuario", usuario);
        return "editar-usuario";
    }

    @PostMapping("/usuario/editar")
    public String actualizarUsuario(@RequestParam String nombre,
                                    @RequestParam String apellidos,
                                    @RequestParam String email,
                                    Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String actualEmail = auth.getName();
        User usuario = userService.findByEmail(actualEmail).orElse(null);
        if (usuario != null) {
            usuario.setNombre(nombre);
            usuario.setApellidos(apellidos);
            usuario.setEmail(email);
            userService.save(usuario);
            model.addAttribute("mensaje", "Información actualizada correctamente.");
        }
        model.addAttribute("usuario", usuario);
        return "editar-usuario";
    }
}
