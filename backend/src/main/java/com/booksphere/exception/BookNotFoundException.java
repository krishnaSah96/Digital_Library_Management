package com.booksphere.exception;

/**
 * Custom Exception: BookNotFoundException
 * Thrown when an inquiry or operation is performed on a non-existent book.
 */
public class BookNotFoundException extends Exception {
    public BookNotFoundException(String message) {
        super(message);
    }

    public BookNotFoundException(int bookId) {
        super("Book with ID #" + bookId + " could not be found in the catalog.");
    }
}
