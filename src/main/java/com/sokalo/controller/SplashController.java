// in src/main/java/com/sokalo/controller/SplashController.java

package com.sokalo.controller;

import com.sokalo.Main;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

public class SplashController {

    @FXML
    private Label statusLabel;
    @FXML
    private ProgressBar progressBar;

    @FXML
    public void initialize() {
        // Create a background task for loading
        Task<Void> loadingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Simulate loading tasks and update progress
                updateProgress(0, 100);
                Platform.runLater(() -> statusLabel.setText("Checking database..."));
                Thread.sleep(1000);
                updateProgress(50, 100);

                Platform.runLater(() -> statusLabel.setText("Loading resources..."));
                Thread.sleep(1000);
                updateProgress(100, 100);

                return null;
            }
        };

        // When the task finishes successfully, load the login screen
        loadingTask.setOnSucceeded(event -> {
            loadLoginScreen();
        });

        // Bind the progress bar's progress property to the task's progress
        progressBar.progressProperty().bind(loadingTask.progressProperty());

        // Start the background task
        new Thread(loadingTask).start();
    }

    private void loadLoginScreen() {
        try {
            Stage currentStage = (Stage) statusLabel.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/LoginView.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("SOKALO - Staff Login");
            loginStage.setScene(new Scene(loader.load()));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}