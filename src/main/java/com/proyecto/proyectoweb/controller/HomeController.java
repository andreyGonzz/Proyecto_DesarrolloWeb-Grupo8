package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.domain.Product;
import com.proyecto.proyectoweb.domain.Blog;
import com.proyecto.proyectoweb.service.ProductService;
import com.proyecto.proyectoweb.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final BlogService blogService;

    public HomeController(ProductService productService, BlogService blogService) {
        this.productService = productService;
        this.blogService = blogService;
    }

    @GetMapping({"/", "/index", "/home"})
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

        // Obtener blogs recientes de la base de datos
        List<Blog> recentBlogs = blogService.findRecentBlogs();
        if (recentBlogs == null) {
            recentBlogs = new ArrayList<>();
        }

        model.addAttribute("popularProducts", popular);
        model.addAttribute("newProducts", nuevos);
        model.addAttribute("suplementos", suplementos);
        model.addAttribute("vitaminas", vitaminas);
        model.addAttribute("otrosProducts", otros);
        model.addAttribute("recentBlogs", recentBlogs);

        return "index";
    }
}