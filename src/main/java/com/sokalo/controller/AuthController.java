package com.sokalo.controller;

import com.sokalo.Main;
import com.sokalo.dao.StaffMemberDAO;
import com.sokalo.model.StaffMember;
import com.sokalo.model.enums.StaffRole;
import com.sokalo.dao.SystemLogDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthController {


    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField pinField;

    @FXML
    private Label statusLabel;

    private StaffMemberDAO staffMemberDAO;
    private SystemLogDAO systemLogDAO;

    // A constructor to initialize the DAO
    public AuthController() {
        this.staffMemberDAO = new StaffMemberDAO();
        this.systemLogDAO = new SystemLogDAO();
    }

    /**
     * Handles the login button action.
     * It will authenticate the user and then load the correct next view
     * based on the user's role.
     */
    @FXML
    protected void handleLoginButtonAction() {
        String username = usernameField.getText();
        String pin = pinField.getText();

        // To call user attributes for authentication
        StaffMember loggedInUser = staffMemberDAO.findByUsernameAndPin(username, pin);

        if (loggedInUser != null) {
            String details = "User '" + loggedInUser.getFullName() + "' logged in successfully.";
            systemLogDAO.addLog(loggedInUser.getStaffMemberID(), "USER_LOGIN", details);
            try {
                // Close the current login window first
                Stage currentStage = (Stage) pinField.getScene().getWindow();
                currentStage.close();

                // Check the user's role and load the appropriate next screen
                if (loggedInUser.getRole() == StaffRole.STORE_MANAGER) {
                    loadMainView(loggedInUser);

                } else {
                    loadStartShiftView(loggedInUser);
                }
            } catch (Exception e) {
                e.printStackTrace();
                statusLabel.setText("Error: " + e.getMessage());
                systemLogDAO.addLog(loggedInUser.getStaffMemberID(), "USER_LOGIN", "Failed to load next screen.");
                // In a real app, you would show an error dialog here
            }
        } else {
            statusLabel.setText("Invalid username or PIN.");
        }
    }

    /**
     * Helper method to load the main application window.
     * @param user The authenticated staff member.
     */
    private void loadMainView(StaffMember user) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/MainView.fxml"));
        Stage stage = new Stage();
        stage.setTitle("SOKALO Dashboard");
        stage.setScene(new Scene(loader.load()));

        MainController controller = loader.getController();
        controller.initData(user); // Pass user data to the MainController

        stage.show();
    }

    /**
     * Helper method to load the start shift reconciliation window.
     * @param user The authenticated staff member.
     */
    private void loadStartShiftView(StaffMember user) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/StartShiftView.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Begin Shift");
        stage.setScene(new Scene(loader.load()));

        StartShiftController controller = loader.getController();
        controller.initData(user); // Pass user data to the StartShiftController

        stage.show();
    }
}