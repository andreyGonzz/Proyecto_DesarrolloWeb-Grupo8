package com.proyecto.proyectoweb.service;

import com.proyecto.proyectoweb.domain.Blog;
import com.proyecto.proyectoweb.domain.User;
import com.proyecto.proyectoweb.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BlogService {
    
    private final BlogRepository blogRepository;
    
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }
    
    // Obtener todos los blogs publicados
    public List<Blog> findAllPublished() {
        return blogRepository.findByPublishedTrueOrderByCreatedAtDesc();
    }
    
    // Obtener todos los blogs (para admin)
    public List<Blog> findAll() {
        return blogRepository.findAllByOrderByCreatedAtDesc();
    }
    
    // Obtener blog por ID
    public Optional<Blog> findById(Long id) {
        return blogRepository.findById(id);
    }
    
    // Obtener blog por ID y incrementar contador de vistas
    @Transactional
    public Optional<Blog> findByIdAndIncrementViews(Long id) {
        Optional<Blog> blogOpt = blogRepository.findById(id);
        if (blogOpt.isPresent()) {
            Blog blog = blogOpt.get();
            blog.incrementViewCount();
            blogRepository.save(blog);
        }
        return blogOpt;
    }
    
    // Obtener blogs por categoría
    public List<Blog> findByCategory(String category) {
        return blogRepository.findByCategoryAndPublishedTrueOrderByCreatedAtDesc(category);
    }
    
    // Obtener blogs por autor
    public List<Blog> findByAuthor(User author) {
        return blogRepository.findByAuthorOrderByCreatedAtDesc(author);
    }
    
    // Buscar blogs por título
    public List<Blog> searchByTitle(String title) {
        return blogRepository.findByTitleContainingIgnoreCaseAndPublishedTrueOrderByCreatedAtDesc(title);
    }
    
    // Obtener blogs populares
    public List<Blog> findPopularBlogs() {
        return blogRepository.findTop5ByPublishedTrueOrderByViewCountDescCreatedAtDesc();
    }
    
    // Obtener blogs recientes
    public List<Blog> findRecentBlogs() {
        return blogRepository.findTop3ByPublishedTrueOrderByCreatedAtDesc();
    }
    
    // Obtener blogs relacionados
    public List<Blog> findRelatedBlogs(String category, Long currentBlogId) {
        return blogRepository.findRelatedBlogs(category, currentBlogId);
    }
    
    // Guardar blog
    @Transactional
    public Blog save(Blog blog) {
        try {
            return blogRepository.save(blog);
        } catch (Exception e) {
            log.error("Error al guardar blog: {}", e.getMessage());
            throw new RuntimeException("Error al guardar el blog", e);
        }
    }
    
    // Publicar blog
    @Transactional
    public void publishBlog(Long id) {
        Optional<Blog> blogOpt = blogRepository.findById(id);
        if (blogOpt.isPresent()) {
            Blog blog = blogOpt.get();
            blog.publish();
            blogRepository.save(blog);
        } else {
            throw new RuntimeException("Blog no encontrado con id: " + id);
        }
    }
    
    // Despublicar blog
    @Transactional
    public void unpublishBlog(Long id) {
        Optional<Blog> blogOpt = blogRepository.findById(id);
        if (blogOpt.isPresent()) {
            Blog blog = blogOpt.get();
            blog.unpublish();
            blogRepository.save(blog);
        } else {
            throw new RuntimeException("Blog no encontrado con id: " + id);
        }
    }
    
    // Eliminar blog
    @Transactional
    public void deleteBlog(Long id) {
        if (blogRepository.existsById(id)) {
            blogRepository.deleteById(id);
        } else {
            throw new RuntimeException("Blog no encontrado con id: " + id);
        }
    }
    
    // Contar blogs por categoría
    public Long countByCategory(String category) {
        return blogRepository.countByCategoryAndPublished(category);
    }
}