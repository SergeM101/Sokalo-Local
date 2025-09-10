// in src/main/java/com/sokalo/controller/AuthController.java

package com.sokalo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AuthController {

    @FXML
    private Label welcomeText;

    @FXML
    protected void onLoginButtonClick() {
        welcomeText.setText("Login button was clicked!");
    }
}