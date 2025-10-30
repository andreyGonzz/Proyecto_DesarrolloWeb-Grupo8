package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.Product;
import com.proyecto.proyectoweb.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProductsPageController {

    private final ProductService productService;

    public ProductsPageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/productos")
    public String productos(Model model) {
        List<Product> all = productService.findAll();
        model.addAttribute("allProducts", all);
        return "productos";
    }
}
