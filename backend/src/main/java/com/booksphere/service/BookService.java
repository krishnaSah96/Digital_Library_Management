package com.booksphere.service;

import com.booksphere.exception.BookNotFoundException;
import com.booksphere.exception.InvalidBookDataException;
import com.booksphere.model.Book;
import com.booksphere.model.EBook;
import com.booksphere.repository.BookDAO;
import com.booksphere.util.FileManager;
import com.booksphere.util.Validation;

import java.util.*;

/**
 * Service Class: BookService
 * 
 * Demonstrates:
 * 1. Business Logic Layer
 * 2. Functional Requirements: Add, Search, Delete, Update records
 * 3. Exception Handling (Throws custom and standard exceptions)
 * 4. Validation & Secure Input verification
 */
public class BookService {
    private final BookDAO bookDAO;

    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    /**
     * Functional Requirement 1: User can add records
     */
    public Book addBook(Book book) {
        validateBookData(book);

        // Sanitize string inputs for security
        book.setTitle(Validation.sanitizeInput(book.getTitle()));
        book.setAuthor(Validation.sanitizeInput(book.getAuthor()));
        book.setCategory(Validation.sanitizeInput(book.getCategory()));
        book.setPublisher(Validation.sanitizeInput(book.getPublisher()));
        if (book.getTags() != null) {
            book.setTags(Validation.sanitizeInput(book.getTags()));
        }

        Book saved = bookDAO.save(book);
        FileManager.appendLog("ACTION", "New record added: [#" + saved.getId() + "] " + saved.getTitle() + " by " + saved.getAuthor());
        return saved;
    }

    /**
     * Functional Requirement 2: User can search records
     */
    public List<Book> searchBooks(String query) {
        String cleanQuery = Validation.sanitizeInput(query);
        return bookDAO.search(cleanQuery);
    }

    // Polymorphism: Method Overloading for search by query AND category
    public List<Book> searchBooks(String query, String category) {
        List<Book> initial = searchBooks(query);
        if (category == null || category.trim().isEmpty() || "All".equalsIgnoreCase(category)) {
            return initial;
        }
        List<Book> filtered = new ArrayList<>();
        for (Book b : initial) {
            if (b.getCategory() != null && b.getCategory().equalsIgnoreCase(category.trim())) {
                filtered.add(b);
            }
        }
        return filtered;
    }

    public List<Book> getAllBooks() {
        return bookDAO.findAll();
    }

    public Book getBookById(int id) throws BookNotFoundException {
        return bookDAO.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    /**
     * Functional Requirement 3: User can update records
     */
    public Book updateBook(int id, Book updateData) throws BookNotFoundException {
        Book existing = getBookById(id);
        validateBookData(updateData);

        existing.setTitle(Validation.sanitizeInput(updateData.getTitle()));
        existing.setAuthor(Validation.sanitizeInput(updateData.getAuthor()));
        existing.setCategory(Validation.sanitizeInput(updateData.getCategory()));
        existing.setPublicationYear(updateData.getPublicationYear());
        existing.setAvailableCopies(updateData.getAvailableCopies());
        existing.setTotalCopies(Math.max(updateData.getTotalCopies(), updateData.getAvailableCopies()));
        existing.setIsbn(updateData.getIsbn());
        existing.setPages(updateData.getPages());
        existing.setPublisher(Validation.sanitizeInput(updateData.getPublisher()));
        existing.setRating(updateData.getRating());
        existing.setSummary(updateData.getSummary());
        if (updateData.getCoverImageUrl() != null && !updateData.getCoverImageUrl().isEmpty()) {
            existing.setCoverImageUrl(updateData.getCoverImageUrl());
        }
        if (updateData.getTags() != null) {
            existing.setTags(Validation.sanitizeInput(updateData.getTags()));
        }

        if (existing instanceof EBook && updateData instanceof EBook) {
            ((EBook) existing).setFileSizeMb(((EBook) updateData).getFileSizeMb());
            ((EBook) existing).setDownloadFormat(((EBook) updateData).getDownloadFormat());
        }

        bookDAO.update(existing);
        FileManager.appendLog("ACTION", "Record updated: [#" + existing.getId() + "] " + existing.getTitle());
        return existing;
    }

    /**
     * Functional Requirement 4: User can delete records
     */
    public boolean deleteBook(int id) throws BookNotFoundException {
        Book existing = getBookById(id);
        boolean success = bookDAO.deleteById(id);
        if (success) {
            FileManager.appendLog("ACTION", "Record deleted: [#" + id + "] " + existing.getTitle());
        }
        return success;
    }

    /**
     * Summary Analytics for Dashboard
     */
    public Map<String, Object> getLibraryStats() {
        List<Book> all = bookDAO.findAll();
        int totalBooks = all.size();
        int totalCopies = 0;
        int availableCopies = 0;
        int ebooksCount = 0;
        Set<String> categories = new HashSet<>();

        for (Book b : all) {
            totalCopies += b.getTotalCopies();
            availableCopies += b.getAvailableCopies();
            if (b.getCategory() != null) categories.add(b.getCategory());
            if (b instanceof EBook) ebooksCount++;
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUniqueTitles", totalBooks);
        stats.put("totalCopies", totalCopies);
        stats.put("availableCopies", availableCopies);
        stats.put("borrowedCopies", Math.max(0, totalCopies - availableCopies));
        stats.put("ebooksCount", ebooksCount);
        stats.put("physicalCount", totalBooks - ebooksCount);
        stats.put("categoryCount", categories.size());
        stats.put("categories", new ArrayList<>(categories));
        return stats;
    }

    private void validateBookData(Book book) {
        if (book == null) {
            throw new InvalidBookDataException("Book payload cannot be null.");
        }
        if (!Validation.isValidString(book.getTitle(), 1, 200)) {
            throw new InvalidBookDataException("Title must be between 1 and 200 characters.");
        }
        if (!Validation.isValidString(book.getAuthor(), 1, 150)) {
            throw new InvalidBookDataException("Author must be between 1 and 150 characters.");
        }
        if (book.getPublicationYear() < 1400 || book.getPublicationYear() > 2030) {
            throw new InvalidBookDataException("Publication year must be between 1400 and 2030.");
        }
        if (book.getAvailableCopies() < 0) {
            throw new InvalidBookDataException("Available copies cannot be negative.");
        }
    }
}
