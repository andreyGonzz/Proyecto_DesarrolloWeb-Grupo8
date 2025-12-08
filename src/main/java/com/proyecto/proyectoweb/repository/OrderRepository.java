package com.proyecto.proyectoweb.repository;

import com.proyecto.proyectoweb.model.Order;
import com.proyecto.proyectoweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findByStatus(String status);
}
