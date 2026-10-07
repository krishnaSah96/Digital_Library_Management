package com.booksphere.model;

/**
 * Abstract Base Class: LibraryItem
 * 
 * Demonstrates:
 * 1. Abstraction (Abstract class with abstract methods)
 * 2. Encapsulation (Private fields with getter/setter methods)
 * 3. Static Members (Item counter)
 */
public abstract class LibraryItem {
    // Static member tracking total library items registered
    private static int totalItemsCount = 0;

    // Encapsulated private attributes
    private int id;
    private String title;
    private String author;
    private String category;
    private int publicationYear;
    private int availableCopies;
    private int totalCopies;

    // Default Constructor
    public LibraryItem() {
        totalItemsCount++;
    }

    // Parameterized Constructor
    public LibraryItem(int id, String title, String author, String category, int publicationYear, int copies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.publicationYear = publicationYear;
        this.availableCopies = copies;
        this.totalCopies = copies;
        totalItemsCount++;
    }

    // Abstract Methods (demonstrating Abstraction)
    public abstract String getItemType();
    public abstract String displayDetails();
    public abstract double calculateLateFee(int daysOverdue);

    // Concrete Methods with encapsulation
    public boolean borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    // Getters and Setters (Encapsulation)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public static int getTotalItemsCount() {
        return totalItemsCount;
    }
}
