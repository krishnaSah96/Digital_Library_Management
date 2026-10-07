package com.booksphere.exception;

/**
 * Custom Exception: BookNotAvailableException
 * Thrown when trying to borrow a book with 0 available copies.
 */
public class BookNotAvailableException extends Exception {
    public BookNotAvailableException(String title) {
        super("All copies of '" + title + "' are currently checked out.");
    }
}
