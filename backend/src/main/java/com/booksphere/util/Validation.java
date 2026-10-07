package com.booksphere.util;

import java.util.regex.Pattern;

/**
 * Utility Class: Validation
 * 
 * Demonstrates:
 * 1. Static utility methods
 * 2. Secure Input handling (sanitizing strings, preventing injection)
 * 3. Regular Expression validation for ISBN and Email
 * 4. Boundary and Type safety checks
 */
public class Validation {
    private static final Pattern ISBN_PATTERN = Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Private constructor to prevent instantiation (Utility class pattern)
    private Validation() {}

    /**
     * Sanitizes raw user input by stripping potentially hazardous script or injection characters
     */
    public static String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim()
                .replaceAll("<[^>]*>", "") // strip HTML tags
                .replaceAll("['\";\\\\]", ""); // strip SQL meta-characters
    }

    /**
     * Validates non-empty string with maximum length
     */
    public static boolean isValidString(String str, int minLen, int maxLen) {
        if (str == null) return false;
        String trimmed = str.trim();
        return trimmed.length() >= minLen && trimmed.length() <= maxLen;
    }

    /**
     * Validates ISBN format
     */
    public static boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) return false;
        String clean = isbn.trim();
        return ISBN_PATTERN.matcher(clean).matches() || clean.length() >= 10;
    }

    /**
     * Validates email address format
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates integer within acceptable bounds
     */
    public static boolean isWithinRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Validates positive integer
     */
    public static boolean isPositive(int value) {
        return value > 0;
    }

    /**
     * Validates rating between 0.0 and 5.0
     */
    public static boolean isValidRating(double rating) {
        return rating >= 0.0 && rating <= 5.0;
    }
}
