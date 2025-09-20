// in src/main/java/com/sokalo/util/FXMLViewLoader.java

package com.sokalo.util;

import com.sokalo.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class FXMLViewLoader {
    public static Pane getPage(String fxmlFile) throws IOException {
        return new FXMLLoader(Main.class.getResource("/com/sokalo/view/" + fxmlFile)).load();
    }
}