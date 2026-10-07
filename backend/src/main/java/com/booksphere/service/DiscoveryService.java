package com.booksphere.service;

import com.booksphere.model.Book;
import com.booksphere.repository.BookDAO;
import com.booksphere.util.AsyncDiscoveryWorker;
import com.booksphere.util.FileManager;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Service Class: DiscoveryService
 * 
 * Demonstrates:
 * 1. Non-functional Requirement: "Fast execution (threads)"
 * 2. Multi-threaded background indexing via ExecutorService & Threads
 * 3. Smart Discovery Algorithms: Trending, Staff Picks, High-Rated & Category exploration
 */
public class DiscoveryService {
    private final BookDAO bookDAO;
    // Thread pool for concurrent background indexing
    private final ExecutorService executor = Executors.newFixedThreadPool(3);

    public DiscoveryService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    /**
     * Executes parallel background discovery computation
     */
    public void triggerBackgroundReindexing() {
        List<Book> allBooks = bookDAO.findAll();
        AsyncDiscoveryWorker worker = new AsyncDiscoveryWorker("Catalog-AutoIndex", allBooks);
        executor.execute(worker);
    }

    /**
     * Fast Thread-assisted discovery feed generation
     */
    public Map<String, Object> getDiscoveryFeed() {
        long startTime = System.currentTimeMillis();
        List<Book> all = bookDAO.findAll();

        // 1. Trending Books (High rating and active availability)
        List<Book> trending = all.stream()
                .filter(b -> b.getRating() >= 4.6)
                .sorted((a, b) -> Double.compare(b.getRating(), a.getRating()))
                .limit(4)
                .collect(Collectors.toList());

        // 2. Staff Picks / Editor's Choice
        List<Book> staffPicks = all.stream()
                .filter(b -> b.getTags() != null && (b.getTags().contains("Software") || b.getTags().contains("Epic") || b.getTags().contains("Classic")))
                .limit(4)
                .collect(Collectors.toList());

        // 3. New / Modern Releases
        List<Book> modernPicks = all.stream()
                .sorted((a, b) -> Integer.compare(b.getPublicationYear(), a.getPublicationYear()))
                .limit(4)
                .collect(Collectors.toList());

        // Launch an asynchronous audit thread
        executor.execute(() -> {
            FileManager.appendLog("DISCOVERY", "Discovery feed generated with " + trending.size() + " trending and " + staffPicks.size() + " staff picks.");
        });

        long executionTimeMs = System.currentTimeMillis() - startTime;

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("trending", trending);
        result.put("staffPicks", staffPicks);
        result.put("modernPicks", modernPicks);
        result.put("totalDiscovered", all.size());
        result.put("executionTimeMs", executionTimeMs);
        return result;
    }

    public void shutdown() {
        executor.shutdown();
    }
}
