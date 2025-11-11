-- Script de criação do banco de dados Conecta Bairro

CREATE DATABASE IF NOT EXISTS conecta_bairro;
USE conecta_bairro;

-- Tabela de usuários
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    user_type VARCHAR(50) NOT NULL,
    profile_image VARCHAR(255),
    bio TEXT,
    rating DOUBLE DEFAULT 0.0,
    total_reviews INT DEFAULT 0,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_user_type (user_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de serviços
CREATE TABLE services (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DOUBLE NOT NULL,
    location VARCHAR(100) NOT NULL,
    image_url VARCHAR(255),
    rating DOUBLE DEFAULT 0.0,
    total_reviews INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    provider_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (provider_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_category (category),
    INDEX idx_location (location),
    INDEX idx_provider_id (provider_id),
    FULLTEXT INDEX ft_search (title, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de requisições de serviço
CREATE TABLE service_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(50) NOT NULL,
    scheduled_date TIMESTAMP NULL,
    notes TEXT,
    total_price DOUBLE,
    customer_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE,
    INDEX idx_status (status),
    INDEX idx_customer_id (customer_id),
    INDEX idx_service_id (service_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Tabela de avaliações
CREATE TABLE reviews (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    rating INT NOT NULL,
    comment TEXT NOT NULL,
    reviewer_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    request_id BIGINT UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (reviewer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (service_id) REFERENCES services(id) ON DELETE CASCADE,
    FOREIGN KEY (request_id) REFERENCES service_requests(id) ON DELETE CASCADE,
    INDEX idx_rating (rating),
    INDEX idx_reviewer_id (reviewer_id),
    INDEX idx_service_id (service_id),
    INDEX idx_request_id (request_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Criar usuários de teste
INSERT INTO users (name, email, password, phone, user_type, bio, is_verified) VALUES
('Maria Silva', 'maria@example.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DlH.PKZbv5H8KnzzVgXXbVxzy3/C3m', '(93) 99123-1234', 'SERVICE_PROVIDER', 'Profissional em limpeza residencial', TRUE),
('João Santos', 'joao@example.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DlH.PKZbv5H8KnzzVgXXbVxzy3/C3m', '(93) 99123-5678', 'CUSTOMER', 'Cliente frequente', FALSE),
('Ana Costa', 'ana@example.com', '$2a$10$slYQmyNdGzin7olVN3p5Be7DlH.PKZbv5H8KnzzVgXXbVxzy3/C3m', '(93) 99123-9012', 'SERVICE_PROVIDER', 'Encanador profissional', TRUE);

-- Criar serviços de teste
INSERT INTO services (title, description, category, price, location, provider_id, is_active) VALUES
('Limpeza Residencial', 'Serviço completo de limpeza para residências', 'limpeza', 120.00, 'Centro', 1, TRUE),
('Manutenção Hidráulica', 'Conserto e manutenção de sistemas hidráulicos', 'hidraulica', 150.00, 'Vila Nova', 3, TRUE),
('Pintura de Interiores', 'Pintura profissional para interiores', 'pintura', 200.00, 'Jardins', 1, TRUE);
