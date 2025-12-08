package com.proyecto.proyectoweb.service;

import com.proyecto.proyectoweb.model.Cart;
import com.proyecto.proyectoweb.model.User;
import com.proyecto.proyectoweb.repository.CartRepository;
import com.proyecto.proyectoweb.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


import com.proyecto.proyectoweb.model.Cart;
import com.proyecto.proyectoweb.repository.CartRepository;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
<<<<<<< HEAD
                      CartRepository cartRepository) { 
=======
                      CartRepository cartRepository) {
>>>>>>> 72c05f2ca1aefdf78c0ce4430258a39a09cd3204
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
        
<<<<<<< HEAD
=======
        // Crear carrito directamente con el repositorio
>>>>>>> 72c05f2ca1aefdf78c0ce4430258a39a09cd3204
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
<<<<<<< HEAD

}
=======
}
>>>>>>> 72c05f2ca1aefdf78c0ce4430258a39a09cd3204
