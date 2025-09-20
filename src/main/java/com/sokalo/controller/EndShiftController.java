// in src/main/java/com/sokalo/controller/EndShiftController.java
package com.sokalo.controller;

import com.sokalo.model.StaffMember;
import com.sokalo.model.enums.StaffRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EndShiftController {

    @FXML private Label titleLabel;
    @FXML private Label valueLabel;
    @FXML private TextField closingValueField;

    private StaffMember currentUser;
    // We will need to pass the current shift ID as well
    private int currentShiftId;

    public void initData(StaffMember staffMember, int shiftId) {
        this.currentUser = staffMember;
        this.currentShiftId = shiftId;

        // Customize the view based on the role
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
        System.out.println("Finalizing shift " + currentShiftId + " with value: " + closingValueStr);

        // TO-DO: Use ShiftDAO to update the shift record with the end time and closing value.
        // Also, perform the validation logic here to set the shiftFlag.

        // After finalizing, close the app or go back to the login screen.
        Stage stage = (Stage) closingValueField.getScene().getWindow();
        stage.close();
    }
}