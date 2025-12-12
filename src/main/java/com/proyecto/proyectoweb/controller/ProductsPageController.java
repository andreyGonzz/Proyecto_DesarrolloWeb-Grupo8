package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.domain.Product;
import com.proyecto.proyectoweb.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/productos/vitaminas")
    public String vitaminas(Model model) {
        List<Product> vitaminas = productService.findByCategory("Vitaminas");

        model.addAttribute("Vitaminas", vitaminas);
        return "vitaminas";
    }

    @GetMapping("/productos/populares")
    public String populares(Model model) {
        List<Product> populares = productService.findAll();
        model.addAttribute("Populares", populares);
        return "populares";
    }

    @GetMapping("/productos/suplementos")
    public String suplementos(Model model) {
        List<Product> suplementos = productService.findByCategory("Suplementos");
        model.addAttribute("Suplementos", suplementos);
        return "suplementos";
    }

    @GetMapping("/productos/nuevos")
    public String nuevos(Model model) {
        List<Product> nuevos = productService.findAllOrderByCreatedAtAsc();
        model.addAttribute("nuevos", nuevos);
        return "nuevos";
    }

    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam("q") String q, Model model) {
        List<Product> productos = productService.findByNameContainingIgnoreCase(q);
        model.addAttribute("productos", productos);
        model.addAttribute("busqueda", q);
        return "busquedaNombre";
    }

    @GetMapping("/producto/{id}")
    public String productoDetalle(@PathVariable Long id, Model model) {
        try {
            Product producto = productService.getProductById(id);
            model.addAttribute("producto", producto);
            return "producto-detalle";
        } catch (RuntimeException e) {
            return "redirect:/productos";
        }
    }
    
}
