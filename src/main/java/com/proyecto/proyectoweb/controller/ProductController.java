package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.Product;
import com.proyecto.proyectoweb.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> list(@RequestParam(required = false) String category) {
        List<Product> result = (category == null || category.isBlank())
                ? productService.findAll()
                : productService.findByCategory(category);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product p) {
        Product saved = productService.save(p);
        return ResponseEntity.ok(saved);
    }
}
