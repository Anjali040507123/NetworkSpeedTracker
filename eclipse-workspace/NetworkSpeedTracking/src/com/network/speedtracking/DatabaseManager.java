package com.network.speedtracking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/network_speed_tracker";
    private static final String USER = "root"; // your MySQL username
    private static final String PASSWORD = "7396045254"; // your MySQL password

    public static void insertSpeedRecord(double ping, double download, double upload) {
        String query = "INSERT INTO speed_records (ping, download, upload) VALUES (?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDouble(1, ping);
            stmt.setDouble(2, download);
            stmt.setDouble(3, upload);
            stmt.executeUpdate();

            System.out.println("✅ Speed test result saved to database!");

        } catch (SQLException e) {
            System.out.println("❌ Database connection failed: " + e.getMessage());
        }
    }
}
