package com.booksphere.model;

/**
 * Concrete Subclass: Book
 * 
 * Demonstrates:
 * 1. Single Inheritance (extends LibraryItem)
 * 2. Polymorphism (Constructor Overloading & Method Overriding)
 * 3. Encapsulation (Private fields with validation in setters)
 * 4. Strings and StringBuilder usage
 */
public class Book extends LibraryItem {
    private String isbn;
    private int pages;
    private String publisher;
    private double rating;
    private String summary;
    private String coverImageUrl;
    private String tags;

    // Default Constructor
    public Book() {
        super();
        this.isbn = "";
        this.rating = 0.0;
        this.coverImageUrl = "";
        this.tags = "";
    }

    // Overloaded Constructor 1: Basic attributes
    public Book(int id, String title, String author, String category, int publicationYear, int copies, String isbn) {
        super(id, title, author, category, publicationYear, copies);
        this.isbn = isbn;
        this.pages = 250;
        this.publisher = "General Publishing";
        this.rating = 4.0;
        this.summary = "A captivating library volume.";
        this.coverImageUrl = "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&auto=format&fit=crop";
        this.tags = category;
    }

    // Overloaded Constructor 2: Full attributes
    public Book(int id, String title, String author, String category, int publicationYear, int copies,
                String isbn, int pages, String publisher, double rating, String summary,
                String coverImageUrl, String tags) {
        super(id, title, author, category, publicationYear, copies);
        this.isbn = isbn;
        this.pages = pages;
        this.publisher = publisher;
        this.rating = rating;
        this.summary = summary;
        this.coverImageUrl = coverImageUrl;
        this.tags = tags;
    }

    // Polymorphism: Overriding abstract method from LibraryItem
    @Override
    public String getItemType() {
        return "Physical Book";
    }

    // Polymorphism: Overriding displayDetails with StringBuilder
    @Override
    public String displayDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[%s #%d] %s by %s\n", getItemType(), getId(), getTitle(), getAuthor()));
        sb.append(String.format("   Category: %s | Year: %d | Pages: %d | Rating: %.1f★\n",
                getCategory(), getPublicationYear(), getPages(), getRating()));
        sb.append(String.format("   ISBN: %s | Publisher: %s\n", getIsbn(), getPublisher()));
        sb.append(String.format("   Availability: %d / %d copies remaining\n", getAvailableCopies(), getTotalCopies()));
        sb.append(String.format("   Tags: %s\n", getTags()));
        sb.append(String.format("   Summary: %s", getSummary()));
        return sb.toString();
    }

    // Polymorphism: Method Overriding for late fee calculation
    @Override
    public double calculateLateFee(int daysOverdue) {
        if (daysOverdue <= 0) return 0.0;
        // Standard physical book fee: $0.50 per day
        return daysOverdue * 0.50;
    }

    // Polymorphism: Method Overloading for late fee calculation with custom daily rate
    public double calculateLateFee(int daysOverdue, double customDailyRate) {
        if (daysOverdue <= 0) return 0.0;
        return daysOverdue * customDailyRate;
    }

    // Getters and Setters
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = Math.max(0.0, Math.min(5.0, rating));
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return String.format("Book[id=%d, title='%s', author='%s', category='%s', copies=%d/%d, rating=%.1f]",
                getId(), getTitle(), getAuthor(), getCategory(), getAvailableCopies(), getTotalCopies(), rating);
    }
}
