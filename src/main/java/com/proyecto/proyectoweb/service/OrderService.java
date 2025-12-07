package com.proyecto.proyectoweb.service;

import com.proyecto.proyectoweb.model.Order;
import com.proyecto.proyectoweb.model.UserD;
import com.proyecto.proyectoweb.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
    
    public List<Order> getUserOrders(UserD user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    public Order getOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            return order.get();
        } else {
            throw new RuntimeException("Orden no encontrada con id: " + id);
        }
    }
    
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}