package com.booksphere.controller;

import com.booksphere.exception.BookNotFoundException;
import com.booksphere.model.Book;
import com.booksphere.service.BookService;
import com.booksphere.service.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller: BookController
 * Exposes Web API endpoints consumed by the React Frontend.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final DiscoveryService discoveryService;

    @Autowired
    public BookController(BookService bookService, DiscoveryService discoveryService) {
        this.bookService = bookService;
        this.discoveryService = discoveryService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(bookService.getBookById(id));
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "") String category) {
        return ResponseEntity.ok(bookService.searchBooks(query, category));
    }

    @GetMapping("/discover")
    public ResponseEntity<Map<String, Object>> getDiscoveryFeed() {
        return ResponseEntity.ok(discoveryService.getDiscoveryFeed());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(bookService.getLibraryStats());
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            Book saved = bookService.addBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable int id, @RequestBody Book book) {
        try {
            Book updated = bookService.updateBook(id, book);
            return ResponseEntity.ok(updated);
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable int id) {
        try {
            boolean deleted = bookService.deleteBook(id);
            return ResponseEntity.ok(Map.of("success", deleted, "message", "Book ID #" + id + " deleted."));
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
