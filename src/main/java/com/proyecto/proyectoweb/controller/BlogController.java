package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.domain.Blog;
import com.proyecto.proyectoweb.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/blog")
@Slf4j
public class BlogController {
    
    private final BlogService blogService;
    
    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }
    
    // Ver detalle de un blog
    @GetMapping("/{id}")
    public String viewBlog(@PathVariable Long id, Model model) {
        Optional<Blog> blogOpt = blogService.findByIdAndIncrementViews(id);
        
        if (blogOpt.isEmpty()) {
            return "redirect:/";
        }
        
        Blog blog = blogOpt.get();
        model.addAttribute("blog", blog);
        
        return "blog-detail";
    }
}