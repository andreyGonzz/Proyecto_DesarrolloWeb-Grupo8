package com.proyecto.proyectoweb.repository;

import com.proyecto.proyectoweb.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findAllByOrderByCreatedAtAsc();
    List<Product> findByNameContainingIgnoreCase(String name);

    
}
