package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.Order;
import com.proyecto.proyectoweb.model.User;
import com.proyecto.proyectoweb.service.OrderService;
import com.proyecto.proyectoweb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/orders")
    public String getCurrentOrders(Model model) {
        User user = getDemoUser();
        List<Order> orders = orderService.getUserOrders(user);
        
        model.addAttribute("orders", orders);
        model.addAttribute("activePage", "orders");
        return "order-status";
    }

    @GetMapping("/order-history")
    public String getOrderHistory(Model model) {
        User user = getDemoUser();
        List<Order> orders = orderService.getUserOrders(user);
        
        model.addAttribute("orders", orders);
        model.addAttribute("activePage", "history");
        return "order-history";
    }

    @GetMapping("/orders/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {
        User user = getDemoUser();
        Order order = orderService.getOrderById(id);
        
        if (order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
            return "redirect:/orders?error=notfound";
        }
        
        model.addAttribute("order", order);
        return "order-details";
    }

    @GetMapping("/invoices")
    public String getInvoices(Model model) {
        User user = getDemoUser();
        List<Order> orders = orderService.getUserOrders(user);
    
        BigDecimal totalSpent = orders.stream()
            .map(Order::getTotalAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    
        int totalOrders = orders.size();
        BigDecimal averageOrder = totalOrders > 0 ? 
            totalSpent.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP) : 
            BigDecimal.ZERO;
    
        model.addAttribute("orders", orders);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("averageOrder", averageOrder);
        model.addAttribute("activePage", "invoices");
        return "invoices";
    }

    private User getDemoUser() {
        return userService.findByEmail("demo")
                .orElseGet(() -> {
                    List<User> users = userService.findAllUsers();
                    if (!users.isEmpty()) {
                        return users.get(0);
                    }
                    User demoUser = new User();
                    demoUser.setId(1L);
                    demoUser.setEmail("demo@gmail.com");
                    return demoUser;
                });
    }
    
    
    
}