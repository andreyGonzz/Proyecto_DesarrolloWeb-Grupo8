package com.proyecto.proyectoweb.service;

import com.proyecto.proyectoweb.domain.Blog;
import com.proyecto.proyectoweb.domain.Review;
import com.proyecto.proyectoweb.domain.User;
import com.proyecto.proyectoweb.repository.ReviewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    
    public List<Review> getReviewsByBlog(Blog blog) {
        return reviewRepository.findByBlogAndIsApprovedTrueOrderByCreatedAtDesc(blog);
    }
    
    public List<Review> getAllReviewsByBlog(Blog blog) {
        return reviewRepository.findByBlogOrderByCreatedAtDesc(blog);
    }
    
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUserOrderByCreatedAtDesc(user);
    }
    
    @Transactional
    public Review saveReview(Review review) {
        try {
            return reviewRepository.save(review);
        } catch (Exception e) {
            log.error("Error al guardar review: {}", e.getMessage());
            throw new RuntimeException("Error al guardar la reseña", e);
        }
    }
    
    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }
    
    @Transactional
    public void approveReview(Long id) {
        Optional<Review> reviewOpt = reviewRepository.findById(id);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            review.setIsApproved(true);
            reviewRepository.save(review);
        }
    }
    
    @Transactional
    public void rejectReview(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
        }
    }
    
    @Transactional
    public void deleteReview(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
        }
    }
    
    public Long countReviewsByBlog(Blog blog) {
        return reviewRepository.countByBlogAndIsApprovedTrue(blog);
    }
    
    public Double calculateAverageRating(Blog blog) {
        Double avg = reviewRepository.calculateAverageRating(blog);
        return avg != null ? avg : 0.0;
    }
    
    public List<Review> getPendingReviews() {
        return reviewRepository.findByIsApprovedFalseOrderByCreatedAtDesc();
    }
}