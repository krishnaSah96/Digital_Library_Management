package com.booksphere.util;

import com.booksphere.model.Book;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Worker Thread: AsyncDiscoveryWorker
 * 
 * Demonstrates:
 * 1. Multithreading (Implements Runnable & Callable for background execution)
 * 2. Non-functional Requirement: "Fast execution (threads)"
 * 3. Asynchronously evaluates book popularity scores, recommendations, and search indexing
 */
public class AsyncDiscoveryWorker implements Runnable, Callable<String> {
    private final String taskName;
    private final List<Book> bookList;
    private volatile boolean completed = false;

    public AsyncDiscoveryWorker(String taskName, List<Book> bookList) {
        this.taskName = taskName;
        this.bookList = bookList;
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try {
            FileManager.appendLog("THREAD", "Starting background thread task: " + taskName);
            
            // Simulate parallel background recommendation indexing
            int indexedCount = 0;
            for (Book b : bookList) {
                // Compute discovery relevance score in background
                double discoveryWeight = b.getRating() * 20.0 + (b.getAvailableCopies() > 0 ? 10.0 : 0.0);
                if (discoveryWeight > 80.0) {
                    indexedCount++;
                }
            }

            // Quick thread yield to allow smooth concurrent UI
            Thread.sleep(80);

            long duration = System.currentTimeMillis() - startTime;
            FileManager.appendLog("THREAD", String.format("Task '%s' completed in %d ms. Indexed %d high-relevance items.",
                    taskName, duration, indexedCount));
            completed = true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            FileManager.appendLog("THREAD", "Task '" + taskName + "' was interrupted.");
        }
    }

    @Override
    public String call() throws Exception {
        run();
        return "Worker [" + taskName + "] finished successfully.";
    }

    public boolean isCompleted() {
        return completed;
    }
}
