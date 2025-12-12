package com.proyecto.proyectoweb.repository;

import com.proyecto.proyectoweb.domain.Blog;
import com.proyecto.proyectoweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    
    // Buscar blogs publicados ordenados por fecha de creación descendente
    List<Blog> findByPublishedTrueOrderByCreatedAtDesc();
    
    // Buscar blogs por categoría
    List<Blog> findByCategoryAndPublishedTrueOrderByCreatedAtDesc(String category);
    
    // Buscar blogs por autor
    List<Blog> findByAuthorOrderByCreatedAtDesc(User author);
    
    // Buscar blogs por título que contenga un texto (para búsquedas)
    List<Blog> findByTitleContainingIgnoreCaseAndPublishedTrueOrderByCreatedAtDesc(String title);
    
    // Obtener los blogs más populares (por visualizaciones)
    List<Blog> findTop5ByPublishedTrueOrderByViewCountDescCreatedAtDesc();
    
    // Obtener blogs recientes (últimos N blogs)
    List<Blog> findTop3ByPublishedTrueOrderByCreatedAtDesc();
    
    // Buscar todos los blogs (incluyendo no publicados) para admin
    List<Blog> findAllByOrderByCreatedAtDesc();
    
    // Contar blogs por categoría
    @Query("SELECT COUNT(b) FROM Blog b WHERE b.category = :category AND b.published = true")
    Long countByCategoryAndPublished(@Param("category") String category);
    
    // Buscar blogs relacionados por categoría (excluyendo el blog actual)
    @Query("SELECT b FROM Blog b WHERE b.category = :category AND b.id != :currentId AND b.published = true ORDER BY b.createdAt DESC")
    List<Blog> findRelatedBlogs(@Param("category") String category, @Param("currentId") Long currentId);
}