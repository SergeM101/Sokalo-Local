// in src/main/java/com/sokalo/controller/EndShiftController.java
package com.sokalo.controller;

import com.sokalo.Main;
import com.sokalo.dao.ItemDAO;
import com.sokalo.dao.ShiftDAO;
import com.sokalo.dao.SystemLogDAO;
import com.sokalo.model.Shift;
import com.sokalo.model.StaffMember;
import com.sokalo.model.enums.StaffRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EndShiftController {

    @FXML private Label titleLabel;
    @FXML private Label valueLabel;
    @FXML private TextField closingValueField;

    private StaffMember currentUser;
    private Shift currentShift;
    private final ShiftDAO shiftDAO = new ShiftDAO();
    private final ItemDAO itemDAO = new ItemDAO(); // For stock count
    private final SystemLogDAO systemLogDAO = new SystemLogDAO();
    private Stage dialogStage;

    public void initData(StaffMember staffMember, Shift activeShift) {
        this.currentUser = staffMember;
        this.currentShift = activeShift;

        if (currentUser.getRole() == StaffRole.CASHIER) {
            titleLabel.setText("End Cash Shift");
            valueLabel.setText("Closing Cash Amount:");
        } else if (currentUser.getRole() == StaffRole.STOCK_CONTROLLER) {
            titleLabel.setText("End Stock Shift");
            valueLabel.setText("Ending Total Stock Count:");
        }
    }

    @FXML
    void handleFinalizeShiftAction(ActionEvent event) {
        String closingValueStr = closingValueField.getText();
        if (closingValueStr.isEmpty()) {
            showAlert("Validation Error", "Closing value cannot be empty.");
            return;
        }

        try {
            double closingValue = Double.parseDouble(closingValueStr);
            double expectedValue = 0.0;

            if (currentUser.getRole() == StaffRole.CASHIER) {
                double salesTotal = shiftDAO.calculateSalesTotalForShift(currentShift.getShiftID());
                expectedValue = currentShift.getOpeningCash() + salesTotal;
            } else if (currentUser.getRole() == StaffRole.STOCK_CONTROLLER) {
                // For stock, the expected value is the current total stock in the inventory
                expectedValue = itemDAO.getTotalStockCount();
            }

            // Update the shift in the database
            shiftDAO.endShift(currentShift.getShiftID(), closingValue, expectedValue, currentUser.getRole());

            // Log the closing
            String details = "Shift " + currentShift.getShiftID() + " closed by " + currentUser.getFullName() + ". Total: " + closingValue + " FCFA.";
            systemLogDAO.addLog(currentUser.getStaffMemberID(), "END_SHIFT", details);

            // After finalizing, close the app and show the login screen again
            closeAndReturnToLogin();

        } catch (NumberFormatException e) {
            showAlert("Input Error", "Please enter a valid number.");
        }
    }

    private void closeAndReturnToLogin() {
        try {
            // Close the current window
            Stage currentStage = (Stage) closingValueField.getScene().getWindow();
            currentStage.close();

            // Open a new login window
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/LoginView.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("SOKALO - Staff Login");

            loginStage.setScene(new Scene(loader.load()));
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}