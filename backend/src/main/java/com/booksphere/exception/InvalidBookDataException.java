package com.booksphere.exception;

/**
 * Custom Exception: InvalidBookDataException
 * Thrown when validation fails for book attributes (e.g., negative copies, invalid ISBN).
 */
public class InvalidBookDataException extends RuntimeException {
    public InvalidBookDataException(String message) {
        super(message);
    }
}
