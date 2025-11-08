package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.Order;
import com.proyecto.proyectoweb.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderConfirmationController {

    private final OrderService orderService;

    public OrderConfirmationController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/order-confirmation")
    public String orderConfirmation(@RequestParam Long orderId, Model model) {
        try {
            Order order = orderService.getOrderById(orderId);
            model.addAttribute("order", order);
            return "order-confirmation";
        } catch (Exception e) {
            return "redirect:/cart?error=order_not_found";
        }
    }
}
