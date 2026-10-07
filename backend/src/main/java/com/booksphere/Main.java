package com.booksphere;

import com.booksphere.menu.Menu;
import com.booksphere.repository.BookRepositoryImpl;
import com.booksphere.service.BookService;
import com.booksphere.service.BorrowService;
import com.booksphere.service.DiscoveryService;
import com.booksphere.util.DBConnection;
import com.booksphere.util.FileManager;

/**
 * Standalone Console Entry Point: Main
 * 
 * Allows direct execution in any Java IDE (IntelliJ IDEA, Eclipse, VS Code)
 * or terminal command line:
 *     java com.booksphere.Main
 * 
 * Demonstrates:
 * 1. Object instantiation and dependency wiring
 * 2. Pure Core Java execution without external runtime dependencies
 * 3. Production Library Core Architecture
 */
public class Main {
    public static void main(String[] args) {
        FileManager.appendLog("SYSTEM", "Initializing BookSphere Standalone Console System...");

        // Initialize Database Connection / Singleton
        DBConnection dbConn = DBConnection.getInstance();
        System.out.println("[DB Status] " + dbConn.getStatus());

        // Wire Repositories and Services
        BookRepositoryImpl bookRepository = new BookRepositoryImpl();
        BookService bookService = new BookService(bookRepository);
        DiscoveryService discoveryService = new DiscoveryService(bookRepository);
        BorrowService borrowService = new BorrowService(bookRepository);

        // Launch Console Menu
        Menu consoleMenu = new Menu(bookService, discoveryService, borrowService);
        consoleMenu.start();

        // Graceful shutdown of worker thread pool
        discoveryService.shutdown();
        FileManager.appendLog("SYSTEM", "BookSphere Console Session Ended.");
    }
}
