package com.proyecto.proyectoweb.service;

import com.proyecto.proyectoweb.model.*;
import com.proyecto.proyectoweb.repository.CartRepository;
import com.proyecto.proyectoweb.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;

    public CartService(CartRepository cartRepository, OrderRepository orderRepository, 
                      ProductService productService, UserService userService) {
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public Cart getOrCreateCart(User user) {
        Optional<Cart> cartOpt = cartRepository.findByUser(user);
        if (cartOpt.isPresent()) {
            return cartOpt.get();
        } else {
            Cart newCart = new Cart(user);
            return cartRepository.save(newCart);
        }
    }
    
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("Carrito no encontrado para el usuario: " + userId));
    }
    
    public Cart addToCart(Long userId, Long productId, Integer quantity) {
        User user = userService.getUserById(userId);
        Product product = productService.getProductById(productId);
        Cart cart = getOrCreateCart(user);
        
        cart.addItem(product, quantity);
        return cartRepository.save(cart);
    }
    
    public Cart removeFromCart(Long userId, Long productId) {
        Cart cart = getCartByUserId(userId);
        cart.removeItem(productId);
        return cartRepository.save(cart);
    }
    
    public Cart updateQuantity(Long userId, Long productId, Integer quantity) {
        Cart cart = getCartByUserId(userId);
        cart.updateQuantity(productId, quantity);
        return cartRepository.save(cart);
    }
    
    @Transactional(readOnly = true)
    public Cart getCartWithItems(Long userId) {
        Cart cart = getCartByUserId(userId);
        // Forzar carga de items
        cart.getItems().forEach(item -> item.getProduct().getName());
        return cart;
    }
    
    public Order createOrderFromCart(Long userId, String shippingAddress, 
                                    String paymentMethod, String shippingMethod) {
        Cart cart = getCartWithItems(userId);
        
        if (cart.isEmpty()) {
            throw new RuntimeException("No se puede crear orden: carrito vacío");
        }
        
        // Calcular costo de envío
        BigDecimal shippingCost = calculateShippingCost(cart.getTotal(), shippingMethod);
        BigDecimal finalTotal = cart.getTotal().add(shippingCost);
        
        // Crear orden
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotalAmount(finalTotal);
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setShippingMethod(shippingMethod);
        
        // Convertir CartItems a OrderItems
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(
                cartItem.getProduct(),
                cartItem.getQuantity(),
                cartItem.getProduct().getPrice()
            );
            order.addItem(orderItem);
        }
        
        // Guardar orden
        Order savedOrder = orderRepository.save(order);
        
        // Limpiar carrito
        cart.clear();
        cartRepository.save(cart);
        
        return savedOrder;
    }
    
    private BigDecimal calculateShippingCost(BigDecimal cartTotal, String shippingMethod) {
        BigDecimal freeShippingThreshold = new BigDecimal("50000.00");
        
        if (cartTotal.compareTo(freeShippingThreshold) >= 0 && "standard".equals(shippingMethod)) {
            return BigDecimal.ZERO;
        }
        
        switch (shippingMethod) {
            case "standard": return new BigDecimal("5000.00");
            case "express": return new BigDecimal("3500.00");
            case "nextDay": return new BigDecimal("7000.00");
            default: return new BigDecimal("5000.00");
        }
    }
}
