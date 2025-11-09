package com.network.speedtracking;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

public class MainController {

    @FXML private Label pingLabel;
    @FXML private Label downloadLabel;
    @FXML private Label uploadLabel;
    @FXML private Button startTestButton;
    @FXML private Button viewHistoryButton;

    // ✅ Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/network_speed_tracker";
    private static final String USER = "root";
    private static final String PASSWORD = "7396045254";

    // ✅ Triggered when user clicks “Start Test”
    @FXML
    private void startTest() {
        try {
            // Run Ookla Speedtest CLI
            SpeedTestResult result = SpeedTestService.runSpeedTest();

            // Update UI with Ookla values
            pingLabel.setText(String.format("Ping: %.2f ms", result.getPing()));
            downloadLabel.setText(String.format("Download: %.2f Mbps", result.getDownload()));
            uploadLabel.setText(String.format("Upload: %.2f Mbps", result.getUpload()));

            // Save to MySQL DB
            saveToDatabase(result.getPing(), result.getDownload(), result.getUpload());

            showAlert("Success", "✅ Speed test completed and saved to database!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "❌ Speedtest failed: " + e.getMessage());
        }
    }

    // ✅ Store Ookla test results into MySQL database
    private void saveToDatabase(double ping, double download, double upload) {
        String query = "INSERT INTO speed_records (ping, download, upload, test_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDouble(1, ping);
            pstmt.setDouble(2, download);
            pstmt.setDouble(3, upload);
            pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();

            System.out.println("✅ Speed test result saved to database!");
        } catch (SQLException e) {
            System.out.println("❌ Failed to save data: " + e.getMessage());
        }
    }

    // ✅ Open History window
    @FXML
    private void openHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/history_view.fxml"));
            Stage stage = new Stage();
            stage.setTitle("History");
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Helper alert popup
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
