package com.proyecto.proyectoweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.proyectoweb.domain.Cart;
import com.proyecto.proyectoweb.domain.User;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
    Optional<Cart> findByUserId(Long userId);
    
    @Query("SELECT COUNT(ci) FROM Cart c JOIN c.items ci WHERE c.user.id = :userId")
    Long countItemsInCart(@Param("userId") Long userId);
}
