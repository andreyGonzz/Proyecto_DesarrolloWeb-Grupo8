package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.Product;
import com.proyecto.proyectoweb.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/{primero}/{segundo}")
    public ResponseEntity<List<Product>> id(@PathVariable Long primero, @PathVariable Long segundo) {
        List<Product> id = productService.findByIdBetween(primero, segundo);
        return ResponseEntity.ok(id);
    }

}
