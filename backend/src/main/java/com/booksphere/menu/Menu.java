package com.booksphere.menu;

import com.booksphere.exception.BookNotFoundException;
import com.booksphere.exception.InvalidBookDataException;
import com.booksphere.model.Book;
import com.booksphere.model.BorrowRecord;
import com.booksphere.model.EBook;
import com.booksphere.service.BookService;
import com.booksphere.service.BorrowService;
import com.booksphere.service.DiscoveryService;
import com.booksphere.util.AsyncDiscoveryWorker;
import com.booksphere.util.FileManager;
import com.booksphere.util.Validation;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Console Menu: Menu
 * 
 * Satisfies:
 * 1. Non-functional Requirement: Console-based
 * 2. Non-functional Requirement: Fast execution (threads)
 * 3. Non-functional Requirement: Secure input (Validation & sanitization)
 * 4. Non-functional Requirement: Error handling (Custom & standard Exceptions)
 * 5. Functional Requirements: Add, Search, Delete, Update records
 */
public class Menu {
    private final BookService bookService;
    private final DiscoveryService discoveryService;
    private final BorrowService borrowService;
    private final Scanner scanner;

    public Menu(BookService bookService, DiscoveryService discoveryService, BorrowService borrowService) {
        this.bookService = bookService;
        this.discoveryService = discoveryService;
        this.borrowService = borrowService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        printWelcomeBanner();

        while (running) {
            printMainMenu();
            int choice = readIntegerInput("Enter your choice [0-9]: ", 0, 9);

            try {
                switch (choice) {
                    case 1 -> displayAllBooks();
                    case 2 -> addNewBookInteractive();
                    case 3 -> searchBooksInteractive();
                    case 4 -> updateBookInteractive();
                    case 5 -> deleteBookInteractive();
                    case 6 -> runSmartDiscoveryWithThreads();
                    case 7 -> borrowBookInteractive();
                    case 8 -> returnBookInteractive();
                    case 9 -> viewAuditLogsAndStats();
                    case 0 -> {
                        System.out.println("\n[BookSphere] Saving state and exiting console interface. Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("[Warning] Invalid choice. Please select from the menu.");
                }
            } catch (Exception e) {
                // Non-functional requirement: Error Handling
                System.err.println("\n[Error Occurred] " + e.getMessage());
                FileManager.appendLog("ERROR", "Console error: " + e.getMessage());
            }

            if (running) {
                System.out.println("\nPress Enter to return to main menu...");
                scanner.nextLine();
            }
        }
    }

    private void printWelcomeBanner() {
        System.out.println("===============================================================================");
        System.out.println("   ____              _    ____        _                     ");
        System.out.println("  | __ )  ___   ___ | | _/ ___| _ __ | |__   ___ _ __ ___   ");
        System.out.println("  |  _ \\ / _ \\ / _ \\| |/ /\\___ \\| '_ \\| '_ \\ / _ \\ '__/ _ \\  ");
        System.out.println("  | |_) | (_) | (_) |   <  ___) | |_) | | | |  __/ | |  __/  ");
        System.out.println("  |____/ \\___/ \\___/|_|\\_\\|____/| .__/|_| |_|\\___|_|  \\___|  ");
        System.out.println("                                |_|                           ");
        System.out.println("   BookSphere Digital Library Management and Book Discovery System");
        System.out.println("   High-Performance Enterprise Library Engine");
        System.out.println("===============================================================================");
    }

    private void printMainMenu() {
        System.out.println("\n+------------------ MAIN CONSOLE MENU ------------------+");
        System.out.println("|  1. View All Books in Catalog                         |");
        System.out.println("|  2. Add New Book Record         (Functional: Add)     |");
        System.out.println("|  3. Search Books                (Functional: Search)  |");
        System.out.println("|  4. Update Book Details         (Functional: Update)  |");
        System.out.println("|  5. Delete Book Record          (Functional: Delete)  |");
        System.out.println("|  6. Run Smart Discovery Engine  (Multithreaded)       |");
        System.out.println("|  7. Borrow / Check-Out a Book                         |");
        System.out.println("|  8. Return a Borrowed Book                            |");
        System.out.println("|  9. View Audit Logs & System Statistics               |");
        System.out.println("|  0. Exit System                                       |");
        System.out.println("+-------------------------------------------------------+");
    }

    private void displayAllBooks() {
        System.out.println("\n--- [CATALOG RECORDS] ---");
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No records found in catalog.");
            return;
        }

        System.out.printf("%-4s | %-32s | %-18s | %-16s | %-5s | %-7s%n",
                "ID", "Title", "Author", "Category", "Year", "Copies");
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Book b : books) {
            String title = b.getTitle().length() > 30 ? b.getTitle().substring(0, 27) + "..." : b.getTitle();
            String author = b.getAuthor().length() > 16 ? b.getAuthor().substring(0, 14) + ".." : b.getAuthor();
            System.out.printf("%-4d | %-32s | %-18s | %-16s | %-5d | %d/%-5d%n",
                    b.getId(), title, author, b.getCategory(), b.getPublicationYear(),
                    b.getAvailableCopies(), b.getTotalCopies());
        }
        System.out.println("Total Records: " + books.size());
    }

    // Functional Requirement 1: User can add records
    private void addNewBookInteractive() {
        System.out.println("\n--- [ADD NEW BOOK RECORD] ---");
        String title = readSecureString("Enter Book Title: ", 2, 150);
        String author = readSecureString("Enter Author Name: ", 2, 100);
        String category = readSecureString("Enter Category (e.g. Computer Science, Sci-Fi): ", 2, 50);
        int year = readIntegerInput("Enter Publication Year (1800-2026): ", 1800, 2026);
        int copies = readIntegerInput("Enter Total Copies (1-100): ", 1, 100);
        String isbn = readSecureString("Enter ISBN (e.g., 978-0132350884): ", 5, 25);
        int pages = readIntegerInput("Enter Number of Pages: ", 1, 3000);
        String publisher = readSecureString("Enter Publisher Name: ", 2, 100);
        double rating = readDoubleInput("Enter Rating (0.0 to 5.0): ", 0.0, 5.0);
        String tags = readSecureString("Enter Tags (comma-separated): ", 2, 100);
        String summary = readSecureString("Enter Brief Summary: ", 5, 500);

        System.out.print("Enter Book Cover Image URL (or press Enter for default): ");
        String coverUrlInput = scanner.nextLine().trim();
        String coverImageUrl = coverUrlInput.isEmpty() 
                ? "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=500&auto=format&fit=crop" 
                : coverUrlInput;

        System.out.print("Is this an E-Book? (y/n): ");
        String isEbookChoice = scanner.nextLine().trim();

        Book newBook;
        if ("y".equalsIgnoreCase(isEbookChoice)) {
            double sizeMb = readDoubleInput("Enter E-Book File Size (MB): ", 0.1, 500.0);
            String format = readSecureString("Enter Format (PDF / EPUB / MOBI): ", 2, 10).toUpperCase();
            newBook = new EBook(0, title, author, category, year, copies, isbn, pages, publisher, rating, summary,
                    coverImageUrl, tags, sizeMb, format);
        } else {
            newBook = new Book(0, title, author, category, year, copies, isbn, pages, publisher, rating, summary,
                    coverImageUrl, tags);
        }

        Book saved = bookService.addBook(newBook);
        System.out.println("\n[Success] Record successfully added to BookSphere!");
        System.out.println(saved.displayDetails());
    }

    // Functional Requirement 2: User can search records
    private void searchBooksInteractive() {
        System.out.println("\n--- [SEARCH RECORDS] ---");
        System.out.print("Enter search keyword (Title, Author, ISBN, Category or Tag): ");
        String query = scanner.nextLine().trim();

        List<Book> matches = bookService.searchBooks(query);
        System.out.println("\nSearch matched " + matches.size() + " book(s):");
        for (Book b : matches) {
            System.out.println("-------------------------------------------------------");
            System.out.println(b.displayDetails());
        }
    }

    // Functional Requirement 3: User can update records
    private void updateBookInteractive() throws BookNotFoundException {
        System.out.println("\n--- [UPDATE BOOK RECORD] ---");
        int id = readIntegerInput("Enter Book ID to update: ", 1, 999999);
        Book existing = bookService.getBookById(id);

        System.out.println("Currently: " + existing.getTitle() + " by " + existing.getAuthor());
        System.out.print("Enter New Title (leave blank to keep current): ");
        String title = scanner.nextLine().trim();
        if (!title.isEmpty()) existing.setTitle(Validation.sanitizeInput(title));

        System.out.print("Enter New Author (leave blank to keep current): ");
        String author = scanner.nextLine().trim();
        if (!author.isEmpty()) existing.setAuthor(Validation.sanitizeInput(author));

        System.out.print("Enter New Available Copies (or -1 to keep current): ");
        String copiesStr = scanner.nextLine().trim();
        if (!copiesStr.isEmpty()) {
            try {
                int copies = Integer.parseInt(copiesStr);
                if (copies >= 0) existing.setAvailableCopies(copies);
            } catch (NumberFormatException ignored) {}
        }

        System.out.print("Enter New Cover Image URL (leave blank to keep current): ");
        String coverInput = scanner.nextLine().trim();
        if (!coverInput.isEmpty()) {
            existing.setCoverImageUrl(coverInput);
        }

        Book updated = bookService.updateBook(id, existing);
        System.out.println("\n[Success] Book ID #" + id + " has been updated!");
        System.out.println(updated.displayDetails());
    }

    // Functional Requirement 4: User can delete records
    private void deleteBookInteractive() throws BookNotFoundException {
        System.out.println("\n--- [DELETE BOOK RECORD] ---");
        int id = readIntegerInput("Enter Book ID to delete: ", 1, 999999);
        Book b = bookService.getBookById(id);

        System.out.print("Are you sure you want to permanently delete '" + b.getTitle() + "'? (yes/no): ");
        String confirm = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(confirm)) {
            bookService.deleteBook(id);
            System.out.println("[Success] Book record #" + id + " deleted successfully.");
        } else {
            System.out.println("[Cancelled] Deletion cancelled.");
        }
    }

    // Multithreaded Discovery Engine
    private void runSmartDiscoveryWithThreads() {
        System.out.println("\n--- [DISCOVERY ENGINE (FAST THREAD EXECUTION)] ---");
        System.out.println("Spawning concurrent worker thread for background indexing...");

        // Demonstrate manual thread invocation
        Thread discoveryThread = new Thread(new AsyncDiscoveryWorker("ManualConsoleWorker", bookService.getAllBooks()));
        discoveryThread.start();

        Map<String, Object> feed = discoveryService.getDiscoveryFeed();
        System.out.println("Engine calculation finished in: " + feed.get("executionTimeMs") + " ms.");

        @SuppressWarnings("unchecked")
        List<Book> trending = (List<Book>) feed.get("trending");
        System.out.println("\n>> TRENDING BOOKS (Highest Reader Satisfaction):");
        for (Book b : trending) {
            System.out.printf("   * %s by %s [%.1f★] (%s)\n", b.getTitle(), b.getAuthor(), b.getRating(), b.getCategory());
        }

        @SuppressWarnings("unchecked")
        List<Book> staffPicks = (List<Book>) feed.get("staffPicks");
        System.out.println("\n>> CURATED STAFF PICKS:");
        for (Book b : staffPicks) {
            System.out.printf("   * %s [%s] - %s\n", b.getTitle(), b.getCategory(), b.getTags());
        }
    }

    private void borrowBookInteractive() {
        System.out.println("\n--- [BORROW / CHECK-OUT BOOK] ---");
        int bookId = readIntegerInput("Enter Book ID to borrow: ", 1, 999999);
        String userName = readSecureString("Enter Borrower's Name: ", 2, 50);

        try {
            BorrowRecord record = borrowService.borrowBook(bookId, 1, userName);
            System.out.println("\n[Success] Book checked out!");
            System.out.println("Loan Record ID: #" + record.getRecordId());
            System.out.println("Book: " + record.getBookTitle());
            System.out.println("Due Date: " + record.getDueDate() + " (14 days)");
        } catch (Exception e) {
            System.err.println("[Borrow Failed] " + e.getMessage());
        }
    }

    private void returnBookInteractive() {
        System.out.println("\n--- [RETURN BOOK] ---");
        int recordId = readIntegerInput("Enter Loan Record ID to return: ", 1, 999999);
        try {
            BorrowRecord rec = borrowService.returnBook(recordId);
            System.out.println("\n[Success] Book returned successfully.");
            System.out.println("Status: " + rec.getStatus());
            if (rec.getFineAmount() > 0) {
                System.out.printf("Late Fee Overdue Notice: $%.2f%n", rec.getFineAmount());
            } else {
                System.out.println("No late fees charged. Thank you!");
            }
        } catch (Exception e) {
            System.err.println("[Return Failed] " + e.getMessage());
        }
    }

    private void viewAuditLogsAndStats() {
        System.out.println("\n--- [SYSTEM AUDIT LOGS & BACKUP STATUS] ---");
        Map<String, Object> stats = bookService.getLibraryStats();
        System.out.println("Total Titles: " + stats.get("totalUniqueTitles"));
        System.out.println("Total Copies: " + stats.get("totalCopies"));
        System.out.println("Available:    " + stats.get("availableCopies"));
        System.out.println("Borrowed:     " + stats.get("borrowedCopies"));
        System.out.println("E-Books:      " + stats.get("ebooksCount"));

        System.out.println("\nRecent Activity Logs (data/logs.txt):");
        List<String> logs = FileManager.readRecentLogs(6);
        for (String l : logs) {
            System.out.println("  " + l);
        }
    }

    // Non-functional requirement: Secure Input Helpers
    private String readSecureString(String prompt, int minLen, int maxLen) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            String sanitized = Validation.sanitizeInput(input);
            if (Validation.isValidString(sanitized, minLen, maxLen)) {
                return sanitized;
            }
            System.out.printf("[Invalid Input] Must be between %d and %d characters. Please try again.\n", minLen, maxLen);
        }
    }

    private int readIntegerInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int val = Integer.parseInt(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("[Out of bounds] Please enter a number between %d and %d.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("[Invalid Number] Please enter a valid integer.");
            }
        }
    }

    private double readDoubleInput(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double val = Double.parseDouble(line);
                if (val >= min && val <= max) {
                    return val;
                }
                System.out.printf("[Out of bounds] Please enter a value between %.1f and %.1f.\n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("[Invalid Number] Please enter a valid decimal number.");
            }
        }
    }
}
