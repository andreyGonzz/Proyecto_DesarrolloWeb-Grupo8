package com.proyecto.proyectoweb.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    // Constructor
    public Product(String name, String category, BigDecimal price, String imageUrl) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.createdAt = java.time.LocalDateTime.now();
    }

    // MÉTODO ESENCIAL QUE FALTA
    @Transient
    public String getFormattedPrice() {
        if (price != null) {
            return String.format("₡%,.2f", price);
        }
        return "₡0.00";
    }
}