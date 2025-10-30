package com.proyecto.proyectoweb.service;

import com.proyecto.proyectoweb.model.User;
import com.proyecto.proyectoweb.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User register(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Usuario ya existe");
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(rawPassword);
        u.setRoles("ROLE_USER");
        return userRepository.save(u);
    }
}
