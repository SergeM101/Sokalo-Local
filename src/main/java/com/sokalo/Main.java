// in src/main/java/com/sokalo/Main.java

package com.sokalo;

import com.sokalo.util.DatabaseUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    /**
     * The start method is the main entry point for all JavaFX applications.
     * @param stage The primary window of the application.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // The path must match the folder structure inside your 'resources' directory
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/SplashView.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("SOKALO"); // A simple title for the splash
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method is the entry point of the Java application.
     * It calls createTables() to set up the database and then launches the JavaFX application.
     */
    public static void main(String[] args) {
        DatabaseUtil.createTables(); // Creates tables if they don't exist
        DatabaseUtil.seedDatabase();  // <-- Seeds the database with some sample data

        launch(); // Launches the JavaFX application
    }

}