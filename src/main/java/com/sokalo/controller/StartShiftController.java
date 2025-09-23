// in src/main/java/com/sokalo/controller/StartShiftController.java

package com.sokalo.controller;

import com.sokalo.Main;
import com.sokalo.dao.ShiftDAO;
import com.sokalo.model.StaffMember;
import com.sokalo.model.enums.StaffRole;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class StartShiftController {

    @FXML
    private Label titleLabel; // Linked to the main title
    @FXML
    private Label valueLabel; // Linked to the label for the text field
    @FXML
    private TextField openingValueField;

    private StaffMember currentUser;

    private ShiftDAO shiftDAO; // <-- 2. Add a DAO instance variable

    public StartShiftController() {
        this.shiftDAO = new ShiftDAO(); // Initialize it in the constructor
    }
    /**
     * This method receives the logged-in user and customizes the view.
     */
    public void initData(StaffMember staffMember) {
        this.currentUser = staffMember;

        // Customize the text based on the user's role
        if (currentUser.getRole() == StaffRole.CASHIER) {
            titleLabel.setText("Begin Cash Shift");
            valueLabel.setText("Opening Cash Amount:");
            openingValueField.setPromptText("Enter cash amount...");
        } else if (currentUser.getRole() == StaffRole.STOCK_CONTROLLER) {
            titleLabel.setText("Begin Stock Shift");
            valueLabel.setText("Starting Total Stock Count:");
            openingValueField.setPromptText("Enter total item count...");
        }
    }

    /**
     * This handler is called when the "Begin Shift" button is clicked.
     */
    @FXML
    void handleBeginShiftAction(ActionEvent event) {
        String openingValue = openingValueField.getText();
        System.out.println("Beginning shift for " + currentUser.getFullName() + " with value: " + openingValue);

        String openingValueStr = openingValueField.getText();
        if (openingValueStr.isEmpty()) return;

        // TO-DO: Create a ShiftDAO to save the new shift record to the database.
        double openingValueDouble = Double.parseDouble(openingValueStr);
        shiftDAO.startShift(currentUser.getStaffMemberID(), currentUser.getRole(), openingValueDouble);

        // The DAO will need to check the user's role and save either the cash or stock value.
        if (currentUser.getRole() == StaffRole.CASHIER) {
            System.out.println("Cashier opening value: " + openingValueDouble);
        } else if (currentUser.getRole() == StaffRole.STOCK_CONTROLLER) {
            System.out.println("Stock controller opening value: " + openingValueDouble);
        } else {
            System.out.println("Error: Invalid role.");
            return;
        }


        // After saving, close this window and open the MainView
        try {
            shiftDAO.startShift(currentUser.getStaffMemberID(), currentUser.getRole(), Double.parseDouble(openingValue));

            // Close the current shift window
            Stage currentStage = (Stage) openingValueField.getScene().getWindow();
            currentStage.close();

            // Load the MainView
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("view/MainView.fxml"));
            Stage stage = new Stage();
            stage.setTitle("SOKALO Dashboard");
            stage.setScene(new Scene(loader.load()));

            // Pass the user data to the MainController, so it can set up the correct sidebar
            MainController controller = loader.getController();
            controller.initData(currentUser);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}