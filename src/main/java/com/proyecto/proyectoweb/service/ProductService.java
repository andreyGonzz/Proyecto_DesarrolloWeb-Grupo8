package com.proyecto.proyectoweb.service;

import com.proyecto.proyectoweb.model.Product;
import com.proyecto.proyectoweb.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Product save(Product p) {
        return productRepository.save(p);
    }

    public List<Product> findAllOrderByCreatedAtAsc() {
        return productRepository.findAllByOrderByCreatedAtAsc();
    }
}
