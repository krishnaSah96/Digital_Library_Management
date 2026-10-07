package com.booksphere.service;

import com.booksphere.exception.BookNotAvailableException;
import com.booksphere.exception.BookNotFoundException;
import com.booksphere.model.Book;
import com.booksphere.model.BorrowRecord;
import com.booksphere.repository.BookDAO;
import com.booksphere.util.FileManager;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Service Class: BorrowService
 * 
 * Demonstrates:
 * 1. Transaction handling for library checkouts and returns
 * 2. Polymorphic fee calculation (using book.calculateLateFee())
 * 3. Thread-safe collection handling
 */
public class BorrowService {
    private final BookDAO bookDAO;
    private final List<BorrowRecord> activeRecords = new CopyOnWriteArrayList<>();
    private int nextRecordId = 1;

    public BorrowService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
        initSampleRecords();
    }

    private void initSampleRecords() {
        BorrowRecord r1 = new BorrowRecord(nextRecordId++, 1, "Clean Code", 1, "Aarav Patel");
        r1.setBorrowDate(LocalDate.now().minusDays(18));
        r1.setDueDate(LocalDate.now().minusDays(4));
        // Overdue record showing late fee calculation
        r1.setStatus("OVERDUE");
        r1.setFineAmount(2.00); // 4 days * 0.50
        activeRecords.add(r1);

        BorrowRecord r2 = new BorrowRecord(nextRecordId++, 3, "Dune", 2, "Priya Sharma");
        r2.setBorrowDate(LocalDate.now().minusDays(5));
        r2.setDueDate(LocalDate.now().plusDays(9));
        activeRecords.add(r2);
    }

    public synchronized BorrowRecord borrowBook(int bookId, int userId, String userName) 
            throws BookNotFoundException, BookNotAvailableException {
        Book book = bookDAO.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new BookNotAvailableException(book.getTitle());
        }

        // Decrement available copies
        book.borrowCopy();
        bookDAO.update(book);

        BorrowRecord record = new BorrowRecord(nextRecordId++, bookId, book.getTitle(), userId, userName != null ? userName : "Guest Reader");
        activeRecords.add(record);

        FileManager.appendLog("BORROW", String.format("User '%s' checked out '%s' (ID #%d). Remaining copies: %d",
                record.getUserName(), book.getTitle(), book.getId(), book.getAvailableCopies()));

        return record;
    }

    public synchronized BorrowRecord returnBook(int recordId) throws Exception {
        BorrowRecord record = activeRecords.stream()
                .filter(r -> r.getRecordId() == recordId)
                .findFirst()
                .orElseThrow(() -> new Exception("Loan record #" + recordId + " not found."));

        if ("RETURNED".equalsIgnoreCase(record.getStatus())) {
            throw new Exception("This book has already been returned.");
        }

        Book book = bookDAO.findById(record.getBookId()).orElse(null);
        if (book != null) {
            book.returnCopy();
            bookDAO.update(book);

            // Calculate overdue days if any
            LocalDate now = LocalDate.now();
            record.setReturnDate(now);
            if (now.isAfter(record.getDueDate())) {
                int overdueDays = (int) ChronoUnit.DAYS.between(record.getDueDate(), now);
                // Polymorphic late fee calculation
                double fee = book.calculateLateFee(overdueDays);
                record.setFineAmount(fee);
                record.setStatus("RETURNED_LATE");
            } else {
                record.setFineAmount(0.0);
                record.setStatus("RETURNED");
            }

            FileManager.appendLog("RETURN", String.format("Record #%d returned for '%s'. Fine: $%.2f",
                    recordId, book.getTitle(), record.getFineAmount()));
        }

        return record;
    }

    public List<BorrowRecord> getActiveRecords() {
        return new ArrayList<>(activeRecords);
    }
}
