package com.proyecto.proyectoweb.service;

<<<<<<< HEAD
import com.proyecto.proyectoweb.model.UserD;
import com.proyecto.proyectoweb.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
=======

>>>>>>> 50721d4b3ddea2484c2747238609cd247931922c
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
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRoles("ROLE_USER");
        User savedUser = userRepository.save(u);
        
        Cart newCart = new Cart(savedUser);
        cartRepository.save(newCart);
        
        return savedUser;
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
