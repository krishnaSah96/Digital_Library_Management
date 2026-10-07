package com.booksphere;

import com.booksphere.menu.Menu;
import com.booksphere.service.BookService;
import com.booksphere.service.BorrowService;
import com.booksphere.service.DiscoveryService;
import com.booksphere.util.FileManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

/**
 * Main Application: BooksphereApplication
 * 
 * Digital Library Management and Book Discovery System
 * Supports:
 * 1. Spring Boot Web REST API mode (default port 8080) for React Frontend
 * 2. Optional Console CLI mode when invoked with '--console' flag
 */
@SpringBootApplication
public class BooksphereApplication {

    public static void main(String[] args) {
        FileManager.appendLog("SYSTEM", "Starting BookSphere Spring Boot Backend Engine...");
        SpringApplication.run(BooksphereApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx,
                                               BookService bookService,
                                               DiscoveryService discoveryService,
                                               BorrowService borrowService) {
        return args -> {
            boolean runConsole = Arrays.asList(args).contains("--console");
            if (runConsole) {
                System.out.println("[BooksphereApplication] Launching interactive console menu...");
                Menu menu = new Menu(bookService, discoveryService, borrowService);
                menu.start();
            } else {
                System.out.println("=======================================================================");
                System.out.println(">>> BookSphere Spring Boot REST API is running at: http://localhost:8080");
                System.out.println(">>> React Frontend endpoint: http://localhost:5173");
                System.out.println(">>> Note: Run with '--console' to launch the interactive CLI menu.");
                System.out.println("=======================================================================");
            }
        };
    }
}
