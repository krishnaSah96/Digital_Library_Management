package com.booksphere.repository;

import com.booksphere.model.Book;
import java.util.List;
import java.util.Optional;

/**
 * Interface: BookDAO
 * 
 * Demonstrates:
 * 1. Abstraction (Pure Interface defining contract for CRUD operations)
 * 2. Functional Requirements support: Add, Search, Delete, Update records
 */
public interface BookDAO {
    // Functional requirement: User can add records
    Book save(Book book);

    // Functional requirement: User can search records
    Optional<Book> findById(int id);
    Optional<Book> findByIsbn(String isbn);
    List<Book> findAll();
    List<Book> search(String query);
    List<Book> findByCategory(String category);

    // Functional requirement: User can update records
    boolean update(Book book);

    // Functional requirement: User can delete records
    boolean deleteById(int id);

    int count();
}
