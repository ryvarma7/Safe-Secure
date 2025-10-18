package com.example.securefiletransfer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

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

    // ✅ Initialize DB — keeps your existing logic
    private static void initializeDatabase() throws SQLException {
        try (Connection conn = getConnection()) {
            String checkColumn = "SELECT COUNT(*) FROM information_schema.columns " +
                               "WHERE table_schema = 'secure_file_transfer' " +
                               "AND table_name = 'requests' " +
                               "AND column_name = 'expiry_time'";
                               
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery(checkColumn);
            rs.next();
            if (rs.getInt(1) == 0) {
                String alterTable = "ALTER TABLE requests " +
                                  "ADD COLUMN expiry_time TIMESTAMP NULL, " +
                                  "ADD COLUMN otp VARCHAR(10) NULL, " +
                                  "ADD COLUMN previous_request_id INT NULL, " +
                                  "MODIFY COLUMN status ENUM('pending', 'approved', 'rejected', 'expired') " +
                                  "NOT NULL DEFAULT 'pending'";
                stmt.executeUpdate(alterTable);
                
                String addForeignKey = "ALTER TABLE requests " +
                                     "ADD CONSTRAINT fk_previous_request " +
                                     "FOREIGN KEY (previous_request_id) " +
                                     "REFERENCES requests(id) ON DELETE SET NULL";
                stmt.executeUpdate(addForeignKey);
            }
        }
    }

    // ✅ Connection with retries
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
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SQLException("Connection attempt interrupted", ie);
                    }
                }
            }
        }
        throw new SQLException("Failed to get database connection after 3 attempts", lastException);
    }

    // ✅ Fetch user email by ID
    public String getUserEmailById(int userId) {
        String email = null;
        String sql = "SELECT email FROM users WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return email;
    }

    // ✅ Fetch user ID from a request
    public int getUserIdFromRequest(int requestId) {
        String sql = "SELECT user_id FROM requests WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ✅ Generate a 6-digit OTP
    public static String generateOTP() {
        Random rand = new Random();
        int otp = 100000 + rand.nextInt(900000);
        return String.valueOf(otp);
    }

    // ✅ Save OTP and expiry
    public boolean saveOtp(int requestId, String otp) {
        String sql = "UPDATE requests SET otp = ?, expiry_time = DATE_ADD(NOW(), INTERVAL 10 MINUTE) WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, otp);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ✅ Approve request and send OTP to user’s registered email
    public boolean approveRequest(int requestId) {
        int userId = getUserIdFromRequest(requestId);
        if (userId == -1) {
            System.out.println("❌ User not found for request ID: " + requestId);
            return false;
        }

        // Generate and save OTP
        String otp = generateOTP();
        if (!saveOtp(requestId, otp)) {
            System.out.println("❌ Failed to save OTP.");
            return false;
        }

        // Fetch user email
        String userEmail = getUserEmailById(userId);
        if (userEmail == null) {
            System.out.println("❌ Email not found for user ID: " + userId);
            return false;
        }

        // Send email
        String subject = "Your File Access OTP";
        String message = "Hello,\n\nYour OTP to access the approved file is: " + otp +
                         "\n\nIt will expire in 10 minutes.\n\n– SafeSecure Team";

        EmailSender.sendEmail(userEmail, subject, message);
        System.out.println("✅ OTP sent successfully to: " + userEmail);
        return true;
    }

    // ✅ Verify OTP (optional)
    public boolean verifyOtp(int requestId, String enteredOtp) {
        String sql = "SELECT otp FROM requests WHERE id = ? AND expiry_time > NOW()";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedOtp = rs.getString("otp");
                return storedOtp.equals(enteredOtp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
