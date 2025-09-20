// in src/main/java/com/sokalo/controller/MainController.java

package com.sokalo.controller;

import com.sokalo.model.StaffMember;
import com.sokalo.model.enums.StaffRole;
import com.sokalo.util.FXMLViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane mainPane;

    // Link all your sidebar buttons from the FXML to these variables
    @FXML private Button dashboardButton;
    @FXML private Button posButton;
    @FXML private Button inventoryButton;
    @FXML private Button staffButton;
    @FXML private Button shiftsButton;
    @FXML private Button adjustmentsButton;
    @FXML private Button endShiftButton;

    private StaffMember currentUser;

    /**
     * This special method is called by the AuthController after login.
     * It receives the logged-in user and configures the UI for their role.
     */
    public void initData(StaffMember staffMember) {
        this.currentUser = staffMember;

        // Hide all buttons by default
        dashboardButton.setVisible(false);
        posButton.setVisible(false);
        inventoryButton.setVisible(false);
        staffButton.setVisible(false);
        shiftsButton.setVisible(false);
        adjustmentsButton.setVisible(false);
        endShiftButton.setVisible(false);

        // --- THE FIX IS HERE ---
        // Switch on the StaffRole enum directly
        switch (currentUser.getRole()) {
            case CASHIER:
                dashboardButton.setVisible(true);
                posButton.setVisible(true);
                inventoryButton.setVisible(true);
                endShiftButton.setVisible(true);
                endShiftButton.setText("End Shift");
                break;
            case STOCK_CONTROLLER:
                dashboardButton.setVisible(true);
                inventoryButton.setVisible(true);
                endShiftButton.setVisible(true);
                endShiftButton.setText("End Shift");
                break;
            case STORE_MANAGER:
                dashboardButton.setVisible(true);
                staffButton.setVisible(true);
                shiftsButton.setVisible(true);
                adjustmentsButton.setVisible(true);
                inventoryButton.setVisible(true);
                endShiftButton.setVisible(true);
                endShiftButton.setText("Logout");
                break;
        }
    }


    // --- Event Handlers ---
    // These are the methods linked to your buttons' "On Action" property.

    @FXML
    void handleDashboardClick(ActionEvent event) {
        System.out.println("Dashboard button clicked by: " + currentUser.getFullName());
        // Here you would load the DashboardView.fxml
    }

    @FXML
    void handleInventoryClick(ActionEvent event) {
        System.out.println("Inventory button clicked by: " + currentUser.getFullName());

        // 2. Load the InventoryView.fxml into the center of the BorderPane
        try {
            Pane view = FXMLViewLoader.getPage("InventoryView.fxml");
            mainPane.setCenter(view);

            // This is an advanced step for later:
            // You might need to get the controller to pass data to it, like this:
            // FXMLLoader loader = new FXMLLoader(...)
            // Pane view = loader.load();
            // InventoryController controller = loader.getController();
            // controller.initData(currentUser);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void handlePOSClick(ActionEvent event) {
        System.out.println("POS button clicked by: " + currentUser.getFullName());
    }

    @FXML
    void handleStaffClick(ActionEvent event) {
        System.out.println("Staff button clicked by: " + currentUser.getFullName());
    }

    @FXML
    void handleShiftsClick(ActionEvent event) {
        System.out.println("Shifts button clicked by: " + currentUser.getFullName());
    }

    @FXML
    void handleAdjustmentsClick(ActionEvent event) {
        System.out.println("Adjustments button clicked by: " + currentUser.getFullName());
    }

    @FXML
    void handleEndShiftClick(ActionEvent event) {
        System.out.println("End Shift/Logout button clicked by: " + currentUser.getFullName());
    }
}