package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Javafx extends Application {
    @Override
    public void start(Stage stage) {
        try {
            // Ensure the path matches the location of mainMenu.fxml in the resources directory
            Parent root = FXMLLoader.load(getClass().getResource("/mainMenu.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML file", e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}