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

    public User createDemoUserIfNotExists() {
        return userRepository.findByUsername("demo")
                .orElseGet(() -> {
                    User demoUser = new User();
                    demoUser.setUsername("demo");
                    demoUser.setPassword("demo123");
                    demoUser.setRoles("ROLE_USER");
                    return userRepository.save(demoUser);
                });
    }
}
