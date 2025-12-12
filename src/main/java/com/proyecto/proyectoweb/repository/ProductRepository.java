package com.proyecto.proyectoweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.proyectoweb.domain.Product;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findAllByOrderByCreatedAtAsc();
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByIdBetween(Long primero, Long segundo);
    
}
