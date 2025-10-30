package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.Product;
import com.proyecto.proyectoweb.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Handle multipart form for product creation with image upload.
     * Fields: name, category, price, file (image)
     * Saves file to uploads/images/ and stores only the filename in product.imageUrl
     */
    @PostMapping("/products")
    public ResponseEntity<?> createProductWithImage(@RequestParam String name,
                                                    @RequestParam String category,
                                                    @RequestParam BigDecimal price,
                                                    @RequestParam(required = false) MultipartFile file) throws IOException {

        String filename = null;
        if (file != null && !file.isEmpty()) {
            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.contains(".")) {
                ext = original.substring(original.lastIndexOf('.'));
            }
            filename = UUID.randomUUID().toString() + ext;
            Path uploadDir = Path.of("uploads", "images");
            Files.createDirectories(uploadDir);
            Path target = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        }

        Product p = new Product();
        p.setName(name);
        p.setCategory(category);
        p.setPrice(price);
        p.setImageUrl(filename); // store only filename

        Product saved = productService.save(p);
        return ResponseEntity.ok(saved);
    }
}
