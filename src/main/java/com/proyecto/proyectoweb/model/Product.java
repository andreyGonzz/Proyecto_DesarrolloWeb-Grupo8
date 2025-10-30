package com.proyecto.proyectoweb.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
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
    private String category; // Suplementos, Vitaminas, Productos, Nuevos

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column
    private String imageUrl;
}
