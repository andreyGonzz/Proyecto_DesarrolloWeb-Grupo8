package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.Order;
import com.proyecto.proyectoweb.model.User;
import com.proyecto.proyectoweb.service.OrderService;
import com.proyecto.proyectoweb.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    // Método para obtener el usuario actual autenticado
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return userService.findByEmail(auth.getName()).orElse(null);
        }
        return null;
    }

    @GetMapping("/orders")
    public String getCurrentOrders(Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        List<Order> orders = orderService.getUserOrders(user);
        
        model.addAttribute("orders", orders);
        model.addAttribute("activePage", "orders");
        model.addAttribute("usuarioActual", user);
        return "order-history"; // Usamos la misma vista para ambos
    }

    @GetMapping("/order-history")
    public String getOrderHistory(Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        List<Order> orders = orderService.getUserOrders(user);
        
        model.addAttribute("orders", orders);
        model.addAttribute("activePage", "history");
        model.addAttribute("usuarioActual", user);
        return "order-history";
    }

    @GetMapping("/orders/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {
        User user = getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            Order order = orderService.getOrderById(id);
            
            // Verificar que la orden pertenece al usuario actual
            if (order.getUser() == null || !order.getUser().getId().equals(user.getId())) {
                return "redirect:/orders?error=notfound";
            }
            
            model.addAttribute("order", order);
            model.addAttribute("usuarioActual", user);
            return "order-details";
        } catch (Exception e) {
            return "redirect:/orders?error=notfound";
        }
    }

   @GetMapping("/invoices")
public String getInvoices(Model model) {
    User user = getCurrentUser();
    if (user == null) {
        return "redirect:/login";
    }
    
    List<Order> orders = orderService.getUserOrders(user);
    
    if (orders == null) {
        orders = new ArrayList<>();
    }
    
    BigDecimal totalSpent = BigDecimal.ZERO;
    for (Order order : orders) {
        if (order.getTotalAmount() != null) {
            totalSpent = totalSpent.add(order.getTotalAmount());
        }
    }
    
    int totalOrders = orders.size();
    BigDecimal averageOrder = totalOrders > 0 ? 
        totalSpent.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP) : 
        BigDecimal.ZERO;
    
    model.addAttribute("orders", orders);
    model.addAttribute("totalSpent", totalSpent);
    model.addAttribute("totalOrders", totalOrders);
    model.addAttribute("averageOrder", averageOrder);
    model.addAttribute("activePage", "invoices");
    model.addAttribute("usuarioActual", user);
    return "invoices";
}
}