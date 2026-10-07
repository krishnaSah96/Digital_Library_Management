package com.booksphere.controller;

import com.booksphere.model.BorrowRecord;
import com.booksphere.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller: BorrowController
 * Manages loan transactions and return operations.
 */
@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    private final BorrowService borrowService;

    @Autowired
    public BorrowController(BorrowService borrowService) {
        this.borrowService = borrowService;
    }

    @GetMapping("/records")
    public ResponseEntity<List<BorrowRecord>> getAllRecords() {
        return ResponseEntity.ok(borrowService.getActiveRecords());
    }

    @PostMapping
    public ResponseEntity<?> borrowBook(@RequestBody Map<String, Object> payload) {
        try {
            int bookId = Integer.parseInt(payload.get("bookId").toString());
            int userId = payload.containsKey("userId") ? Integer.parseInt(payload.get("userId").toString()) : 1;
            String userName = payload.getOrDefault("userName", "Student Reader").toString();

            BorrowRecord record = borrowService.borrowBook(bookId, userId, userName);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/return/{recordId}")
    public ResponseEntity<?> returnBook(@PathVariable int recordId) {
        try {
            BorrowRecord record = borrowService.returnBook(recordId);
            return ResponseEntity.ok(record);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
