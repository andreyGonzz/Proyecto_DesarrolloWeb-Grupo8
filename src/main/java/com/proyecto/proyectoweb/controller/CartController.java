package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.*;
import com.proyecto.proyectoweb.service.CartService;
import com.proyecto.proyectoweb.service.OrderService;
import com.proyecto.proyectoweb.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;
    private final ProductService productService;

    public CartController(CartService cartService, OrderService orderService, ProductService productService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.productService = productService;
    }

    // Helper para obtener usuario actual (simulado)
    private Long getCurrentUserId(HttpSession session) {
        // Por ahora usamos un usuario fijo - luego integrarás con tu login
        // Puedes cambiar este ID por el de cualquier usuario en tu BD
        return 1L; // ID del usuario 'maria'
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, 
                          @RequestParam(defaultValue = "1") int quantity,
                          HttpSession session) {
        
        Long userId = getCurrentUserId(session);
        cartService.addToCart(userId, productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Long userId = getCurrentUserId(session);
        Cart cart = cartService.getCartWithItems(userId);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId, HttpSession session) {
        Long userId = getCurrentUserId(session);
        cartService.removeFromCart(userId, productId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId, 
                               @RequestParam int quantity, 
                               HttpSession session) {
        Long userId = getCurrentUserId(session);
        cartService.updateQuantity(userId, productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Long userId = getCurrentUserId(session);
        Cart cart = cartService.getCartWithItems(userId);
        
        if (cart.isEmpty()) {
            return "redirect:/cart";
        }
        
        model.addAttribute("cart", cart);
        return "checkout";
    }

    @PostMapping("/process-order")
    public String processOrder(@RequestParam String fullName,
                             @RequestParam String address,
                             @RequestParam String city,
                             @RequestParam String zipCode,
                             @RequestParam String paymentMethod,
                             @RequestParam String shippingMethod,
                             HttpSession session,
                             Model model) {
        
        Long userId = getCurrentUserId(session);
        
        try {
            String shippingAddress = String.format("%s, %s, %s %s", fullName, address, city, zipCode);
            
            Order order = cartService.createOrderFromCart(userId, shippingAddress, paymentMethod, shippingMethod);
            return "redirect:/order-confirmation?orderId=" + order.getId();
            
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar la orden: " + e.getMessage());
            Cart cart = cartService.getCartWithItems(userId);
            model.addAttribute("cart", cart);
            return "checkout";
        }
    }
}
