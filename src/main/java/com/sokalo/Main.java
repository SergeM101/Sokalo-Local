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
        // --- 1. Load the FXML file for the login view ---
        // FXMLLoader loads the user interface layout from an .fxml file.
        // We need to get the URL of our FXML file, which is in the 'resources' folder.
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("..//view//LoginView.fxml"));

        // --- 2. Create a Scene ---
        // A Scene is the container for all content in a scene graph.
        // We create a new scene with the loaded FXML layout, setting its dimensions.
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // --- 3. Configure the Stage (the main window) ---
        stage.setTitle("SOKALO - In-Store Management");
        stage.setScene(scene); // Set the scene for this stage
        stage.show(); // Display the window to the user
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