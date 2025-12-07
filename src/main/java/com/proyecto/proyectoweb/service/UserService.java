package com.proyecto.proyectoweb.service;


import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyecto.proyectoweb.model.Cart;
import com.proyecto.proyectoweb.model.User;
import com.proyecto.proyectoweb.repository.CartRepository;
import com.proyecto.proyectoweb.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      CartRepository cartRepository) { 
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
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
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRoles("ROLE_USER");
        User savedUser = userRepository.save(u);
        
        Cart newCart = new Cart(savedUser);
        cartRepository.save(newCart);
        
        return savedUser;
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
