package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.UserD;
import com.proyecto.proyectoweb.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String nombre = body.get("nombre");
        String apellidos = body.get("apellidos");
        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("username and password required");
        }
        try {
            UserD u = userService.register(email, password, nombre, apellidos);
            u.setPassword(null);
            return ResponseEntity.ok(u);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
