package com.example.securefiletransfer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/secure_file_transfer";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sql@2112";
    
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            initializeDatabase();
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }

    private static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection()) {
            // Check if expiry_time column exists
            String checkColumn = "SELECT COUNT(*) FROM information_schema.columns " +
                               "WHERE table_schema = 'secure_file_transfer' " +
                               "AND table_name = 'requests' " +
                               "AND column_name = 'expiry_time'";
                               
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(checkColumn);
            rs.next();
            if (rs.getInt(1) == 0) {
                // Add new columns
                String alterTable = "ALTER TABLE requests " +
                                  "ADD COLUMN expiry_time TIMESTAMP NULL, " +
                                  "ADD COLUMN previous_request_id INT NULL, " +
                                  "MODIFY COLUMN status ENUM('pending', 'approved', 'rejected', 'expired') " +
                                  "NOT NULL DEFAULT 'pending'";
                stmt.executeUpdate(alterTable);
                
                // Add foreign key
                String addForeignKey = "ALTER TABLE requests " +
                                     "ADD CONSTRAINT fk_previous_request " +
                                     "FOREIGN KEY (previous_request_id) " +
                                     "REFERENCES requests(id) ON DELETE SET NULL";
                stmt.executeUpdate(addForeignKey);
            }
        }
    }

    public static Connection getConnection() throws SQLException {
        int retries = 3;
        SQLException lastException = null;
        
        while (retries > 0) {
            try {
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                conn.setAutoCommit(true);
                return conn;
            } catch (SQLException e) {
                lastException = e;
                retries--;
                if (retries > 0) {
                    try {
                        Thread.sleep(1000); // Wait 1 second before retrying
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Connection attempt interrupted", ie);
                    }
                }
            }
        }
        
        throw new SQLException("Failed to get database connection after 3 attempts", lastException);
    }
}