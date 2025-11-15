package com.proyecto.proyectoweb.service;

import com.proyecto.proyectoweb.model.User;
import com.proyecto.proyectoweb.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User register(String email, String rawPassword, String nombre, String apellidos) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
        User u = new User();
        u.setEmail(email);
        u.setNombre(nombre);
        u.setApellidos(apellidos);
        u.setPassword(rawPassword);
        u.setRoles("ROLE_USER");
        return userRepository.save(u);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}
