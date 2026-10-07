-- =====================================================================
-- BookSphere Digital Library Management and Book Discovery System
-- SQL Schema Definition (DDL)
-- =====================================================================

CREATE DATABASE IF NOT EXISTS booksphere_db;
USE booksphere_db;

-- 1. Categories Table
CREATE TABLE IF NOT EXISTS categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Books Table (Supports Physical Books and E-Books)
CREATE TABLE IF NOT EXISTS books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    category_id INT,
    publisher VARCHAR(150),
    publication_year INT,
    pages INT,
    rating DOUBLE DEFAULT 0.0,
    available_copies INT DEFAULT 1,
    total_copies INT DEFAULT 1,
    is_ebook BOOLEAN DEFAULT FALSE,
    file_size_mb DOUBLE DEFAULT 0.0,
    download_format VARCHAR(20) DEFAULT 'NONE',
    summary TEXT,
    cover_image_url VARCHAR(500),
    tags VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE SET NULL
);

-- 3. Users Table (Base User / Member / Librarian)
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    full_name VARCHAR(150) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(20),
    role ENUM('MEMBER', 'LIBRARIAN', 'ADMIN') DEFAULT 'MEMBER',
    max_borrow_limit INT DEFAULT 5,
    status ENUM('ACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Borrow Records Table (Loan Management & Fine System)
CREATE TABLE IF NOT EXISTS borrow_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT NOT NULL,
    user_id INT NOT NULL,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE NULL,
    fine_amount DECIMAL(8, 2) DEFAULT 0.00,
    status ENUM('BORROWED', 'RETURNED', 'OVERDUE') DEFAULT 'BORROWED',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 5. Discovery Logs / Reading History
CREATE TABLE IF NOT EXISTS discovery_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    search_keyword VARCHAR(255),
    filter_category VARCHAR(100),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);
