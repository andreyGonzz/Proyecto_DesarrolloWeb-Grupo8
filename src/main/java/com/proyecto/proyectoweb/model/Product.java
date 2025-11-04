package com.proyecto.proyectoweb.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.*;

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

    @Column
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
