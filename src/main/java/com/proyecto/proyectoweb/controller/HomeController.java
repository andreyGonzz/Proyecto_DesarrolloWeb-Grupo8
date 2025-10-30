package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.model.Product;
import com.proyecto.proyectoweb.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/index")
    public String index(Model model) {
        List<Product> all = productService.findAll();

        // Popular: primeros 4 si hay
        List<Product> popular = new ArrayList<>();
        for (int i = 0; i < Math.min(4, all.size()); i++) popular.add(all.get(i));

        // Nuevos: por categoría 'Nuevos' si existe, sino intentar tomar últimos añadidos
        List<Product> nuevos = productService.findByCategory("Nuevos");
        if (nuevos == null || nuevos.isEmpty()) {
            nuevos = new ArrayList<>();
            for (int i = Math.max(0, all.size() - 4); i < all.size(); i++) nuevos.add(all.get(i));
        }

        // Suplementos y Vitaminas
        List<Product> suplementos = productService.findByCategory("Suplementos");
        List<Product> vitaminas = productService.findByCategory("Vitaminas");

        // Otros productos (categoria 'Productos')
        List<Product> otros = productService.findByCategory("Productos");

        model.addAttribute("popularProducts", popular);
        model.addAttribute("newProducts", nuevos);
        model.addAttribute("suplementos", suplementos);
        model.addAttribute("vitaminas", vitaminas);
        model.addAttribute("otrosProducts", otros);

        return "index";
    }
}
