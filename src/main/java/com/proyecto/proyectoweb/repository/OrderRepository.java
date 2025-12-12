package com.proyecto.proyectoweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.proyectoweb.domain.Order;
import com.proyecto.proyectoweb.domain.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findByStatus(String status);
}
