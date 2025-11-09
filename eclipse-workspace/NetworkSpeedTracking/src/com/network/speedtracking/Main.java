package com.network.speedtracking;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Correct way to load the FXML file from resources folder
        	Parent root = FXMLLoader.load(getClass().getResource("/main_view.fxml"));
            Scene scene = new Scene(root);
            stage.setTitle("Network Speed Tracker");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // print full details if something goes wrong
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
