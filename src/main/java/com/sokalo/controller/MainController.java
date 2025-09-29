// in src/main/java/com/sokalo/controller/MainController.java

package com.sokalo.controller;

import com.sokalo.Main;
import com.sokalo.dao.ShiftDAO; // <-- ADD THIS IMPORT
import com.sokalo.model.Shift;
import com.sokalo.model.StaffMember;
import com.sokalo.model.enums.StaffRole;
import com.sokalo.util.FXMLViewLoader;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;     // <-- ADD THIS IMPORT
import javafx.scene.Scene;         // <-- ADD THIS IMPORT
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;      // <-- ADD THIS IMPORT
import javafx.stage.Stage;         // <-- ADD THIS IMPORT

import java.io.IOException;

public class MainController {

    @FXML private BorderPane mainPane;
    @FXML private Button dashboardButton, posButton, inventoryButton, staffButton, shiftsButton, adjustmentsButton, systemLogButton, aboutUsButton, endShiftButton;

    private StaffMember currentUser;
    private final ShiftDAO shiftDAO = new ShiftDAO(); // <-- ADD THIS DAO INSTANCE

    public void initData(StaffMember staffMember) {
        this.currentUser = staffMember;
        setupSidebar();
        handleDashboardClick(null);
    }

    private void setupSidebar() {
        // Hide all buttons by default
        dashboardButton.setVisible(false);
        posButton.setVisible(false);
        inventoryButton.setVisible(false);
        staffButton.setVisible(false);
        shiftsButton.setVisible(false);
        adjustmentsButton.setVisible(false);
        systemLogButton.setVisible(false);
        aboutUsButton.setVisible(false);
        endShiftButton.setVisible(false);

        // Show buttons based on role
        switch (currentUser.getRole()) {
            case CASHIER:
                dashboardButton.setVisible(true);
                posButton.setVisible(true);
                inventoryButton.setVisible(true);
                aboutUsButton.setVisible(true);
                endShiftButton.setVisible(true);
                endShiftButton.setText("End Shift");
                break;
            case STOCK_CONTROLLER:
                dashboardButton.setVisible(true);
                inventoryButton.setVisible(true);
                staffButton.setVisible(true);
                endShiftButton.setVisible(true);
                endShiftButton.setText("End Shift");
                break;
            case STORE_MANAGER:
                dashboardButton.setVisible(true);
                staffButton.setVisible(true);
                shiftsButton.setVisible(true);
                adjustmentsButton.setVisible(true);
                inventoryButton.setVisible(true);
                systemLogButton.setVisible(true);
                aboutUsButton.setVisible(true);
                endShiftButton.setVisible(true);
                endShiftButton.setText("Logout");
                break;
        }
    } // <-- THE SETUP METHOD ENDS HERE

    // --- All other methods are now OUTSIDE setupSidebar() ---

    @FXML
    void handleDashboardClick(ActionEvent event) {
        loadView("DashboardView.fxml");
    }

    @FXML
    void handleInventoryClick(ActionEvent event) {
        loadView("InventoryView.fxml", true);
    }

    @FXML
    void handlePOSClick(ActionEvent event) {
        loadView("POSView.fxml");
    }

    @FXML
    void handleStaffClick(ActionEvent event) {
        loadView("StaffView.fxml");
    }

    @FXML
    void handleShiftsClick(ActionEvent event) {
        loadView("ShiftsView.fxml");
    }

    @FXML
    void handleAdjustmentsClick(ActionEvent event) {
        loadView("AdjustmentsLogView.fxml");
    }

    @FXML void handleAboutUsClick(ActionEvent event) { loadView("AboutUsView.fxml");}

    @FXML void handlesystemLogsClick (ActionEvent actionEvent) {
        // First, check if a user is actually logged in.
        if (currentUser == null) {
            System.out.println("Error: No user is logged in.");
            return;
        }

        // Action for Store Manager: Logout
        if (currentUser.getRole() == StaffRole.STORE_MANAGER) {
            closeAndReturnToLogin();
        }
        // Action for Cashier or Stock Controller: Open End Shift Dialog
        else {
            try {
                Shift activeShift = shiftDAO.findActiveShiftForUser(currentUser.getStaffMemberID());
                if (activeShift == null) {
                    System.out.println("Error: No active shift found for this user to end.");
                    // Optionally show an Alert to the user
                    return;
                }

                // Load the EndShiftView as a pop-up dialog
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/EndShiftView.fxml"));
                Stage dialogStage = new Stage();
                dialogStage.setTitle("End Shift");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner((Stage) mainPane.getScene().getWindow()); // Set owner window
                dialogStage.setScene(new Scene(loader.load()));

                // Pass the necessary data to the EndShiftController
                EndShiftController controller = loader.getController();
                controller.initData(currentUser, activeShift);

                // Show the dialog and wait for it to be closed
                dialogStage.showAndWait();

                // After the dialog is closed, the main window will also close and return to login.
                // The closeAndReturnToLogin() is called from within the EndShiftController.
                Stage mainStage = (Stage) mainPane.getScene().getWindow();
                mainStage.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleEndShiftClick(ActionEvent event) { loadView("EndShiftView.fxml");}

    private void loadView(String fxmlFile) {
        loadView(fxmlFile, false);
    }

    private void loadView(String fxmlFile, boolean needsUserData) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/" + fxmlFile));
            Pane view = loader.load();

            // After loading, get the controller and pass the data
            if (needsUserData) {
                Object controller = loader.getController();
                if (controller instanceof StaffController) {
                    ((StaffController) controller).initData(currentUser);
                } else if (controller instanceof InventoryController) {
                    ((InventoryController) controller).initData(currentUser);
                } else if (controller instanceof POSController) {
                    ((POSController) controller).initData(currentUser);
                } else if (controller instanceof DashboardController) {
                    ((DashboardController) controller).initData(currentUser);
                }
            }

            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeAndReturnToLogin() {
        try {
            Stage currentStage = (Stage) mainPane.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/LoginView.fxml"));
            Stage loginStage = new Stage();
            loginStage.setTitle("SOKALO - Staff Login");
            loginStage.setScene(new Scene(loader.load()));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}