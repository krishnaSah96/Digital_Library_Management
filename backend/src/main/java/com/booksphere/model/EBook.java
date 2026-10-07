package com.booksphere.model;

/**
 * Multilevel Inheritance: EBook extends Book (which extends LibraryItem)
 * 
 * Demonstrates:
 * 1. Multilevel Inheritance
 * 2. Overriding Methods
 * 3. Specific digital media attributes (fileSizeMb, format, downloadCount)
 */
public class EBook extends Book {
    private double fileSizeMb;
    private String downloadFormat; // PDF, EPUB, MOBI
    private int downloadCount;

    public EBook() {
        super();
        this.fileSizeMb = 1.5;
        this.downloadFormat = "EPUB";
        this.downloadCount = 0;
    }

    public EBook(int id, String title, String author, String category, int publicationYear, int copies,
                 String isbn, int pages, String publisher, double rating, String summary,
                 String coverImageUrl, String tags, double fileSizeMb, String downloadFormat) {
        super(id, title, author, category, publicationYear, copies, isbn, pages, publisher, rating, summary, coverImageUrl, tags);
        this.fileSizeMb = fileSizeMb;
        this.downloadFormat = downloadFormat;
        this.downloadCount = 0;
    }

    @Override
    public String getItemType() {
        return "Digital E-Book (" + downloadFormat + ")";
    }

    @Override
    public String displayDetails() {
        StringBuilder sb = new StringBuilder(super.displayDetails());
        sb.append("\n");
        sb.append(String.format("   Digital Spec: Format: %s | Size: %.1f MB | Total Downloads: %d",
                downloadFormat, fileSizeMb, downloadCount));
        return sb.toString();
    }

    @Override
    public double calculateLateFee(int daysOverdue) {
        // Digital e-books automatically expire, but nominal archival renewal charge: $0.15/day
        if (daysOverdue <= 0) return 0.0;
        return daysOverdue * 0.15;
    }

    public void incrementDownloads() {
        this.downloadCount++;
    }

    public double getFileSizeMb() {
        return fileSizeMb;
    }

    public void setFileSizeMb(double fileSizeMb) {
        this.fileSizeMb = fileSizeMb;
    }

    public String getDownloadFormat() {
        return downloadFormat;
    }

    public void setDownloadFormat(String downloadFormat) {
        this.downloadFormat = downloadFormat;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }
}
