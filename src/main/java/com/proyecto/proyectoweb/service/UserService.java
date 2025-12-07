package com.proyecto.proyectoweb.service;

import com.proyecto.proyectoweb.model.UserD;
import com.proyecto.proyectoweb.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<UserD> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserD register(String email, String rawPassword, String nombre, String apellidos) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
        UserD u = new UserD();
        u.setEmail(email);
        u.setNombre(nombre);
        u.setApellidos(apellidos);
        u.setPassword(passwordEncoder.encode(rawPassword)); // Guardar con BCrypt
        u.setRoles("ROLE_USER");
        return userRepository.save(u);
    }

    public UserD getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public List<UserD> findAllUsers() {
        return userRepository.findAll();
    }

    public UserD save(UserD user) {
        return userRepository.save(user);
    }

}
