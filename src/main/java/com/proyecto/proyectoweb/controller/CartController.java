package com.proyecto.proyectoweb.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.proyecto.proyectoweb.model.Cart;
import com.proyecto.proyectoweb.model.Order;
import com.proyecto.proyectoweb.model.User;
import com.proyecto.proyectoweb.service.CartService;
import com.proyecto.proyectoweb.service.OrderService;
import com.proyecto.proyectoweb.service.ProductService;
import com.proyecto.proyectoweb.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;

    public CartController(CartService cartService, OrderService orderService, 
                         ProductService productService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
    }

    // Obtener usuario autenticado
    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getName().equals("anonymousUser")) {
            return userService.findByEmail(auth.getName()).orElse(null);
        }
        return null;
    }

    private Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId, 
                          @RequestParam(defaultValue = "1") int quantity,
                          HttpSession session) {
        
        Long userId = getCurrentUserId();
        if (userId == null) {
            return "redirect:/login";
        }
        cartService.addToCart(userId, productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return "redirect:/login";
        }
        Cart cart = cartService.getCartWithItems(userId);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping("/remove")
    public String removeFromCart(@RequestParam Long productId, HttpSession session) {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return "redirect:/login";
        }
        cartService.removeFromCart(userId, productId);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long productId, 
                               @RequestParam int quantity, 
                               HttpSession session) {
        Long userId = getCurrentUserId();
        cartService.updateQuantity(userId, productId, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model) {
        Long userId = getCurrentUserId();
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
        
        Long userId = getCurrentUserId();
        
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