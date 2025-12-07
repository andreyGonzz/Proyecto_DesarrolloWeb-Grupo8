package com.proyecto.proyectoweb.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Getter
@Setter
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserD user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

    @Column(precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    private LocalDateTime lastUpdated;

    public Cart() {
        this.lastUpdated = LocalDateTime.now();
    }

    public Cart(UserD user) {
        this.user = user;
        this.lastUpdated = LocalDateTime.now();
    }

    public void calculateTotal() {
        this.total = items.stream()
            .map(CartItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.lastUpdated = LocalDateTime.now();
    }

    public void addItem(Product product, int quantity) {
        CartItem existingItem = items.stream()
            .filter(item -> item.getProduct().getId().equals(product.getId()))
            .findFirst()
            .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem(this, product, quantity);
            items.add(newItem);
        }
        calculateTotal();
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
        calculateTotal();
    }

    public void updateQuantity(Long productId, int quantity) {
        if (quantity <= 0) {
            removeItem(productId);
            return;
        }

        items.stream()
            .filter(item -> item.getProduct().getId().equals(productId))
            .findFirst()
            .ifPresent(item -> {
                item.setQuantity(quantity);
                calculateTotal();
            });
    }

    public void clear() {
        items.clear();
        total = BigDecimal.ZERO;
        lastUpdated = LocalDateTime.now();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public String getFormattedTotal() {
        return String.format("₡%,.2f", total);
    }

    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }
}