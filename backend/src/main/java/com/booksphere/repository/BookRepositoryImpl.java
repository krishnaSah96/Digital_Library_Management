package com.booksphere.repository;

import com.booksphere.model.Book;
import com.booksphere.util.FileManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation: BookRepositoryImpl
 * 
 * Demonstrates:
 * 1. Interface Implementation (Implements BookDAO)
 * 2. Collections (ArrayList / CopyOnWriteArrayList for thread safety)
 * 3. File backup integration
 * 4. Control statements and Loops
 */
public class BookRepositoryImpl implements BookDAO {
    // Thread-safe collection for book records
    private final List<Book> bookStore = new CopyOnWriteArrayList<>();
    private int nextId = 1;

    public BookRepositoryImpl() {
        // Load initial state from backup.txt
        List<Book> loaded = FileManager.loadBooksFromBackup();
        if (loaded.isEmpty()) {
            initSampleData();
        } else {
            bookStore.addAll(loaded);
            for (Book b : loaded) {
                if (b.getId() >= nextId) {
                    nextId = b.getId() + 1;
                }
            }
        }
    }

    private void initSampleData() {
        // Fallback default seed books if backup is empty
        save(new Book(1, "Clean Code", "Robert C. Martin", "Computer Science", 2008, 4, "978-0132350884"));
        save(new Book(2, "Effective Java", "Joshua Bloch", "Computer Science", 2018, 3, "978-0134685991"));
        save(new Book(3, "Dune", "Frank Herbert", "Science Fiction", 1965, 5, "978-0441013593"));
    }

    @Override
    public synchronized Book save(Book book) {
        if (book.getId() <= 0) {
            book.setId(nextId++);
        } else if (book.getId() >= nextId) {
            nextId = book.getId() + 1;
        }

        // Check if item exists to update or insert
        int existingIndex = -1;
        for (int i = 0; i < bookStore.size(); i++) {
            if (bookStore.get(i).getId() == book.getId()) {
                existingIndex = i;
                break;
            }
        }

        if (existingIndex >= 0) {
            bookStore.set(existingIndex, book);
        } else {
            bookStore.add(book);
        }

        FileManager.saveBooksToBackup(new ArrayList<>(bookStore));
        return book;
    }

    @Override
    public Optional<Book> findById(int id) {
        for (Book b : bookStore) {
            if (b.getId() == id) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        if (isbn == null) return Optional.empty();
        String clean = isbn.trim().toLowerCase();
        for (Book b : bookStore) {
            if (b.getIsbn() != null && b.getIsbn().toLowerCase().contains(clean)) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(bookStore);
    }

    @Override
    public List<Book> search(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }
        String q = query.trim().toLowerCase();
        List<Book> results = new ArrayList<>();
        for (Book b : bookStore) {
            boolean matchTitle = b.getTitle() != null && b.getTitle().toLowerCase().contains(q);
            boolean matchAuthor = b.getAuthor() != null && b.getAuthor().toLowerCase().contains(q);
            boolean matchCategory = b.getCategory() != null && b.getCategory().toLowerCase().contains(q);
            boolean matchIsbn = b.getIsbn() != null && b.getIsbn().toLowerCase().contains(q);
            boolean matchTags = b.getTags() != null && b.getTags().toLowerCase().contains(q);

            if (matchTitle || matchAuthor || matchCategory || matchIsbn || matchTags) {
                results.add(b);
            }
        }
        return results;
    }

    @Override
    public List<Book> findByCategory(String category) {
        if (category == null || category.trim().isEmpty() || "All".equalsIgnoreCase(category)) {
            return findAll();
        }
        List<Book> results = new ArrayList<>();
        for (Book b : bookStore) {
            if (b.getCategory() != null && b.getCategory().equalsIgnoreCase(category.trim())) {
                results.add(b);
            }
        }
        return results;
    }

    @Override
    public synchronized boolean update(Book updatedBook) {
        for (int i = 0; i < bookStore.size(); i++) {
            if (bookStore.get(i).getId() == updatedBook.getId()) {
                bookStore.set(i, updatedBook);
                FileManager.saveBooksToBackup(new ArrayList<>(bookStore));
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean deleteById(int id) {
        boolean removed = bookStore.removeIf(b -> b.getId() == id);
        if (removed) {
            FileManager.saveBooksToBackup(new ArrayList<>(bookStore));
        }
        return removed;
    }

    @Override
    public int count() {
        return bookStore.size();
    }
}
