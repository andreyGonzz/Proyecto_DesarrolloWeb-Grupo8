-- Script SQL para crear la tabla de blogs
-- Basado en el script de reviews proporcionado

CREATE TABLE blogs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    content TEXT,
    summary VARCHAR(500),
    image_url VARCHAR(500),
    author_id BIGINT NOT NULL,
    category VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    published BOOLEAN DEFAULT FALSE,
    view_count BIGINT DEFAULT 0,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_category (category),
    INDEX idx_published (published),
    INDEX idx_created_at (created_at),
    INDEX idx_author_id (author_id),
    INDEX idx_view_count (view_count)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Datos de ejemplo para la tabla blogs (opcional)
INSERT INTO blogs (title, content, summary, image_url, author_id, category, published, view_count) VALUES
('Los Beneficios de la Vitamina D', 
 'La vitamina D es esencial para la salud ósea y el sistema inmunológico. En este artículo exploramos sus múltiples beneficios...', 
 'Descubre por qué la vitamina D es crucial para tu salud y cómo obtenerla de forma natural.', 
 'vitamin-d.jpg', 
 1, 
 'Vitaminas', 
 TRUE, 
 150),

('Guía Completa de Suplementos Naturales', 
 'Los suplementos naturales pueden ser una excelente adición a tu rutina de bienestar. Te explicamos cuáles elegir...', 
 'Una guía completa sobre los mejores suplementos naturales para mejorar tu salud.', 
 'natural-supplements.jpg', 
 1, 
 'Suplementos', 
 TRUE, 
 89),

('Alimentación Saludable: Tips Para Comenzar', 
 'Cambiar tus hábitos alimenticios puede parecer abrumador, pero con estos consejos prácticos será más fácil...', 
 'Consejos prácticos para comenzar una alimentación más saludable desde hoy.', 
 'healthy-eating.jpg', 
 1, 
 'Nutrición', 
 TRUE, 
 234);