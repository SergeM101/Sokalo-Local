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
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/LoginView.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("SOKALO - In-Store Management");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method is the entry point of the Java application.
     * It calls createTables() to set up the database and then launches the JavaFX application.
     */
    public static void main(String[] args) {
        // Create the database tables if they don't exist
        DatabaseUtil.createTables();

        // Launch the JavaFX application
        launch();
    }
}