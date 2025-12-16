package com.proyecto.proyectoweb.repository;

import com.proyecto.proyectoweb.domain.Blog;
import com.proyecto.proyectoweb.domain.Review;
import com.proyecto.proyectoweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByBlogAndIsApprovedTrueOrderByCreatedAtDesc(Blog blog);
    
    List<Review> findByUserOrderByCreatedAtDesc(User user);
    
    Long countByBlogAndIsApprovedTrue(Blog blog);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.blog = :blog AND r.isApproved = true")
    Double calculateAverageRating(@Param("blog") Blog blog);
    
    List<Review> findByIsApprovedFalseOrderByCreatedAtDesc();
    
    List<Review> findByBlogOrderByCreatedAtDesc(Blog blog);
}