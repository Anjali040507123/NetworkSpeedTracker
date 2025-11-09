package com.network.speedtracking;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.stage.Stage;
import javafx.scene.chart.XYChart.Series;
import javafx.geometry.Side;
import models.SpeedRecord;

import java.sql.*;
import java.time.LocalDateTime;

public class HistoryController {

    @FXML private TableView<SpeedRecord> tableView;
    @FXML private TableColumn<SpeedRecord, Integer> idColumn;
    @FXML private TableColumn<SpeedRecord, Double> pingColumn;
    @FXML private TableColumn<SpeedRecord, Double> downloadColumn;
    @FXML private TableColumn<SpeedRecord, Double> uploadColumn;
    @FXML private TableColumn<SpeedRecord, LocalDateTime> timestampColumn;

    @FXML private LineChart<String, Number> speedChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    @FXML private ComboBox<Integer> recordSelector;

    private static final String URL = "jdbc:mysql://localhost:3306/network_speed_tracker";
    private static final String USER = "root";
    private static final String PASSWORD = "7396045254";

    @FXML
    public void initialize() {
        // ComboBox options
        recordSelector.getItems().addAll(10, 20, 50, 100);
        recordSelector.setValue(10);

        // Table column setup
        idColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));
        pingColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("ping"));
        downloadColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("download"));
        uploadColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("upload"));
        timestampColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("timestamp"));

        // Chart axis setup
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(100);
        yAxis.setTickUnit(20);

        // Ensure legend is visible
        speedChart.setLegendVisible(true);
        speedChart.setLegendSide(Side.BOTTOM);

        // Load initial data
        loadChartData();
    }

    @FXML
    private void loadChartData() {
        Integer limit = recordSelector.getValue();
        if (limit == null) limit = 10;
        ObservableList<SpeedRecord> records = FXCollections.observableArrayList();

        String query = "SELECT * FROM speed_records ORDER BY test_date DESC LIMIT ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, limit);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                records.add(new SpeedRecord(
                        rs.getInt("id"),
                        rs.getDouble("ping"),
                        rs.getDouble("download"),
                        rs.getDouble("upload"),
                        rs.getTimestamp("test_date").toLocalDateTime()
                ));
            }

        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
            return;
        }

        // Update table and chart
        tableView.setItems(records);
        updateChart(records);
    }

    private void updateChart(ObservableList<SpeedRecord> records) {
        speedChart.getData().clear();

        XYChart.Series<String, Number> downloadSeries = new XYChart.Series<>();
        downloadSeries.setName("Download (Mbps)");

        XYChart.Series<String, Number> uploadSeries = new XYChart.Series<>();
        uploadSeries.setName("Upload (Mbps)");

        XYChart.Series<String, Number> pingSeries = new XYChart.Series<>();
        pingSeries.setName("Ping (ms)");

        for (SpeedRecord record : records) {
            String timeLabel = record.getTimestamp().toLocalTime().toString();
            downloadSeries.getData().add(new XYChart.Data<>(timeLabel, record.getDownload()));
            uploadSeries.getData().add(new XYChart.Data<>(timeLabel, record.getUpload()));
            pingSeries.getData().add(new XYChart.Data<>(timeLabel, record.getPing()));
        }

        // Add data & force layout to show legend
        speedChart.getData().addAll(downloadSeries, uploadSeries, pingSeries);
        speedChart.applyCss();
        speedChart.layout();
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
