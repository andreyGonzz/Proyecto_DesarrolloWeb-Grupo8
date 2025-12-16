package com.proyecto.proyectoweb.controller;

import com.proyecto.proyectoweb.domain.Blog;
import com.proyecto.proyectoweb.domain.Review;
import com.proyecto.proyectoweb.domain.User;
import com.proyecto.proyectoweb.service.BlogService;
import com.proyecto.proyectoweb.service.ReviewService;
import com.proyecto.proyectoweb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/blog")
@Slf4j
public class BlogController {
    
    private final BlogService blogService;
    private final ReviewService reviewService;
    private final UserService userService;
    
    public BlogController(BlogService blogService, ReviewService reviewService, UserService userService) {
        this.blogService = blogService;
        this.reviewService = reviewService;
        this.userService = userService;
    }
    
    @GetMapping("/{id}")
    public String viewBlog(@PathVariable Long id, Model model, Authentication authentication) {
        Optional<Blog> blogOpt = blogService.findByIdAndIncrementViews(id);
        
        if (blogOpt.isEmpty()) {
            return "redirect:/";
        }
        
        Blog blog = blogOpt.get();
        model.addAttribute("blog", blog);
        
        List<Review> reviews = reviewService.getReviewsByBlog(blog);
        model.addAttribute("reviews", reviews);
        
        Double averageRating = reviewService.calculateAverageRating(blog);
        model.addAttribute("averageRating", averageRating);
        
        Long reviewCount = reviewService.countReviewsByBlog(blog);
        model.addAttribute("reviewCount", reviewCount);
        
        model.addAttribute("newReview", new Review());
        
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            Optional<User> userOpt = userService.findByEmail(email);
            userOpt.ifPresent(user -> model.addAttribute("currentUser", user));
        }
        
        return "blog-detail";
    }
    
    @PostMapping("/{id}/review")
    public String addReview(@PathVariable Long id,
                           @ModelAttribute Review review,
                           @RequestParam Integer rating,
                           Authentication authentication) {
        
        if (authentication == null) {
            return "redirect:/login";
        }
        
        Optional<Blog> blogOpt = blogService.findById(id);
        if (blogOpt.isEmpty()) {
            return "redirect:/";
        }
        
        String email = authentication.getName();
        Optional<User> userOpt = userService.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        Review newReview = new Review();
        newReview.setContent(review.getContent());
        newReview.setRating(rating);
        newReview.setBlog(blogOpt.get());
        newReview.setUser(userOpt.get());
        newReview.setIsApproved(true);
        
        reviewService.saveReview(newReview);
        
        return "redirect:/blog/" + id + "#reviews";
    }
    
    @PostMapping("/review/{id}/approve")
    public String approveReview(@PathVariable Long id) {
        reviewService.approveReview(id);
        return "redirect:/admin/reviews";
    }
    
    @PostMapping("/review/{id}/reject")
    public String rejectReview(@PathVariable Long id) {
        reviewService.rejectReview(id);
        return "redirect:/admin/reviews";
    }
}