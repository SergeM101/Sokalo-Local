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
    @FXML private Button dashboardButton, posButton, inventoryButton, staffButton, shiftsButton, adjustmentsButton, aboutUsButton, endShiftButton;

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

    @FXML
    void handleEndShiftClick(ActionEvent event) {
        if (currentUser.getRole() == StaffRole.STORE_MANAGER) {
            closeAndReturnToLogin();
        } else {
            try {
                Shift activeShift = shiftDAO.findActiveShiftForUser(currentUser.getStaffMemberID());
                if (activeShift == null) {
                    System.out.println("Error: No active shift found for this user.");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/EndShiftView.fxml"));
                Stage dialogStage = new Stage();
                dialogStage.setTitle("End Shift");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setScene(new Scene(loader.load()));

                EndShiftController controller = loader.getController();
                controller.initData(currentUser, activeShift);

                dialogStage.showAndWait();
                closeAndReturnToLogin();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void loadView(String fxmlFile) {
        loadView(fxmlFile, false);
    }

    private void loadView(String fxmlFile, boolean needsUserData) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/" + fxmlFile));
            Pane view = loader.load();
            if (needsUserData) {
                Object controller = loader.getController();
                if (controller instanceof InventoryController) {
                    ((InventoryController) controller).initData(currentUser);
                }// --- ADD THIS ELSE IF BLOCK ---

 /*               else if (controller instanceof StaffController) {
                    ((StaffController) controller).initData(currentUser);
                }

  */
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