package com.booksphere.config;

import com.booksphere.repository.BookDAO;
import com.booksphere.repository.BookRepositoryImpl;
import com.booksphere.repository.UserDAO;
import com.booksphere.repository.UserRepositoryImpl;
import com.booksphere.service.BookService;
import com.booksphere.service.BorrowService;
import com.booksphere.service.DiscoveryService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Dependency Injection Configuration
 * Keeps core models, repositories, and services 100% pure Java
 * while providing standard Spring DI for the REST controllers.
 */
@Configuration
public class AppConfig {

    @Bean
    public BookDAO bookDAO() {
        return new BookRepositoryImpl();
    }

    @Bean
    public UserDAO userDAO() {
        return new UserRepositoryImpl();
    }

    @Bean
    public BookService bookService(BookDAO bookDAO) {
        return new BookService(bookDAO);
    }

    @Bean
    public DiscoveryService discoveryService(BookDAO bookDAO) {
        return new DiscoveryService(bookDAO);
    }

    @Bean
    public BorrowService borrowService(BookDAO bookDAO) {
        return new BorrowService(bookDAO);
    }
}
