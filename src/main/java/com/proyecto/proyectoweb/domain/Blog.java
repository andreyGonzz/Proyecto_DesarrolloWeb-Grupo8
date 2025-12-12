package com.proyecto.proyectoweb.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(length = 500)
    private String summary;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;
    
    @Column(nullable = false)
    private String category;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "published")
    private Boolean published = false;
    
    @Column(name = "view_count")
    private Long viewCount = 0L;
    
    // Constructor personalizado
    public Blog(String title, String content, String summary, String imageUrl, User author, String category) {
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.imageUrl = imageUrl;
        this.author = author;
        this.category = category;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.published = false;
        this.viewCount = 0L;
    }
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
        if (published == null) {
            published = false;
        }
        if (viewCount == null) {
            viewCount = 0L;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Métodos de utilidad
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    public void publish() {
        this.published = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    public void unpublish() {
        this.published = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    @Transient
    public String getFormattedCreatedAt() {
        return createdAt != null ? createdAt.toLocalDate().toString() : "";
    }
    
    @Transient
    public String getAuthorName() {
        return author != null ? author.getNombre() + " " + author.getApellidos() : "Autor Desconocido";
    }
}