package com.booksphere.util;

import com.booksphere.model.Book;
import com.booksphere.model.EBook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility Class: FileManager
 * 
 * Demonstrates:
 * 1. File Handling (BufferedReader, BufferedWriter, File, Path)
 * 2. Strings, StringBuilder and StringBuffer usage
 * 3. Exception Handling (IOException, FileNotFoundException)
 * 4. Data persistence to data/backup.txt and data/logs.txt
 */
public class FileManager {
    private static final String DEFAULT_BACKUP_PATH = "data/backup.txt";
    private static final String DEFAULT_LOGS_PATH = "data/logs.txt";
    private static final DateTimeFormatter LOG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String resolvePath(String relativePath) {
        Path p = Paths.get(relativePath);
        if (Files.exists(p)) {
            return p.toString();
        }
        // If running from inside backend/
        Path sub = Paths.get("..", relativePath);
        if (Files.exists(sub)) {
            return sub.toString();
        }
        // Fallback inside current dir data/
        Path parentData = Paths.get("data", Paths.get(relativePath).getFileName().toString());
        return parentData.toString();
    }

    /**
     * Loads books from data/backup.txt using BufferedReader and String manipulation
     */
    public static List<Book> loadBooksFromBackup() {
        List<Book> books = new ArrayList<>();
        String filePath = resolvePath(DEFAULT_BACKUP_PATH);
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("[FileManager] Warning: Backup file not found at " + filePath + ". Starting with empty dataset.");
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip comments and empty lines
                }

                String[] parts = line.split("\\|", -1);
                if (parts.length >= 11) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String type = parts[1].trim();
                        String title = parts[2].trim();
                        String author = parts[3].trim();
                        String category = parts[4].trim();
                        int year = Integer.parseInt(parts[5].trim());
                        int copies = Integer.parseInt(parts[6].trim());
                        double rating = Double.parseDouble(parts[7].trim());
                        String isbn = parts[8].trim();
                        int pages = Integer.parseInt(parts[9].trim());
                        String publisher = parts[10].trim();
                        boolean isEbook = parts.length > 11 && Boolean.parseBoolean(parts[11].trim());
                        double fileSizeMb = parts.length > 12 && !parts[12].trim().isEmpty() ? Double.parseDouble(parts[12].trim()) : 0.0;
                        String format = parts.length > 13 ? parts[13].trim() : "NONE";
                        String tags = parts.length > 14 ? parts[14].trim() : category;
                        String summary = parts.length > 15 ? parts[15].trim() : "Classic library record.";
                        String cover = parts.length > 16 ? parts[16].trim() : "";

                        if (isEbook || "EBOOK".equalsIgnoreCase(type)) {
                            EBook ebook = new EBook(id, title, author, category, year, copies, isbn, pages, publisher, rating, summary, cover, tags, fileSizeMb, format);
                            books.add(ebook);
                        } else {
                            Book book = new Book(id, title, author, category, year, copies, isbn, pages, publisher, rating, summary, cover, tags);
                            books.add(book);
                        }
                    } catch (NumberFormatException nfe) {
                        System.err.println("[FileManager] Error parsing line: " + line + " -> " + nfe.getMessage());
                    }
                }
            }
            appendLog("INFO", "Successfully loaded " + books.size() + " books from " + filePath);
        } catch (IOException e) {
            System.err.println("[FileManager] IO Error reading backup: " + e.getMessage());
            appendLog("ERROR", "Failed to load backup: " + e.getMessage());
        }
        return books;
    }

    /**
     * Saves books to backup.txt using StringBuilder and BufferedWriter
     */
    public static synchronized boolean saveBooksToBackup(List<Book> books) {
        String filePath = resolvePath(DEFAULT_BACKUP_PATH);
        File file = new File(filePath);

        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // Utilizing StringBuilder for high-performance string concatenation
        StringBuilder sb = new StringBuilder();
        sb.append("# =====================================================================\n");
        sb.append("# BookSphere Digital Library Data Backup - Auto-Saved\n");
        sb.append("# Format: ID|TYPE|TITLE|AUTHOR|CATEGORY|YEAR|COPIES|RATING|ISBN|PAGES|PUBLISHER|IS_EBOOK|FILE_SIZE_MB|FORMAT|TAGS|SUMMARY|COVER_URL\n");
        sb.append("# =====================================================================\n");

        for (Book b : books) {
            boolean isEbook = b instanceof EBook;
            double fileSize = isEbook ? ((EBook) b).getFileSizeMb() : 0.0;
            String format = isEbook ? ((EBook) b).getDownloadFormat() : "NONE";

            sb.append(b.getId()).append("|")
              .append(isEbook ? "EBOOK" : "BOOK").append("|")
              .append(b.getTitle()).append("|")
              .append(b.getAuthor()).append("|")
              .append(b.getCategory()).append("|")
              .append(b.getPublicationYear()).append("|")
              .append(b.getAvailableCopies()).append("|")
              .append(b.getRating()).append("|")
              .append(b.getIsbn()).append("|")
              .append(b.getPages()).append("|")
              .append(b.getPublisher()).append("|")
              .append(isEbook).append("|")
              .append(fileSize).append("|")
              .append(format).append("|")
              .append(b.getTags() != null ? b.getTags() : "").append("|")
              .append(b.getSummary() != null ? b.getSummary().replace("\n", " ") : "").append("|")
              .append(b.getCoverImageUrl() != null ? b.getCoverImageUrl() : "").append("\n");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(sb.toString());
            appendLog("INFO", "Backup saved successfully with " + books.size() + " records.");
            return true;
        } catch (IOException e) {
            System.err.println("[FileManager] IO Error saving backup: " + e.getMessage());
            appendLog("ERROR", "Failed to save backup: " + e.getMessage());
            return false;
        }
    }

    /**
     * Appends an audit log message to data/logs.txt using thread-safe StringBuffer
     */
    public static synchronized void appendLog(String level, String message) {
        String logPath = resolvePath(DEFAULT_LOGS_PATH);
        File file = new File(logPath);

        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        // Demonstrating StringBuffer for thread-safe log formatting
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append("[")
                 .append(LocalDateTime.now().format(LOG_DATE_FORMAT))
                 .append("] [")
                 .append(level)
                 .append("] ")
                 .append(message)
                 .append("\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(logBuffer.toString());
        } catch (IOException e) {
            System.err.println("[FileManager] Failed to write log: " + e.getMessage());
        }
    }

    /**
     * Reads recent logs from data/logs.txt
     */
    public static List<String> readRecentLogs(int maxLines) {
        List<String> logs = new ArrayList<>();
        String logPath = resolvePath(DEFAULT_LOGS_PATH);
        File file = new File(logPath);

        if (!file.exists()) {
            return logs;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            List<String> allLines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    allLines.add(line);
                }
            }
            int start = Math.max(0, allLines.size() - maxLines);
            for (int i = start; i < allLines.size(); i++) {
                logs.add(allLines.get(i));
            }
        } catch (IOException e) {
            System.err.println("[FileManager] Error reading logs: " + e.getMessage());
        }
        return logs;
    }
}
