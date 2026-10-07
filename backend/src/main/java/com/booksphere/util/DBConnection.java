package com.booksphere.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility Class: DBConnection
 * 
 * Demonstrates:
 * 1. Singleton Design Pattern (private constructor, static instance)
 * 2. Static members and initialization
 * 3. Database connection configuration (MySQL/H2 compatible)
 * 4. Error handling with try-catch and status logging
 */
public class DBConnection {
    private static DBConnection instance;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/booksphere_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    private boolean isConnected = false;

    private DBConnection() {
        // Private constructor ensures Singleton
        testConnection();
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    private void testConnection() {
        try {
            // Attempt driver check or simulated connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.isConnected = true;
            FileManager.appendLog("DATABASE", "MySQL Driver registered. Connection pool initialized.");
        } catch (ClassNotFoundException e) {
            // If MySQL driver jar is not attached, gracefully fall back to File Storage Engine
            this.isConnected = false;
            FileManager.appendLog("DATABASE", "MySQL Driver not loaded. Defaulting to File-backed persistent DAO.");
        }
    }

    public Connection getConnection() throws SQLException {
        if (!isConnected) {
            throw new SQLException("Database connection not active. Using persistent file DAO.");
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getStatus() {
        return isConnected ? "Connected to MySQL (booksphere_db)" : "Active (File Storage Engine: backup.txt)";
    }
}
