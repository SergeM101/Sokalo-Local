// in src/main/java/com/sokalo/controller/AddStaffDialogController.java
package com.sokalo.controller;

import com.sokalo.dao.StaffMemberDAO;
import com.sokalo.dao.SystemLogDAO;
import com.sokalo.model.StaffMember;
import com.sokalo.model.enums.StaffRole;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddStaffDialogController {

    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private TextField nameField;
    @FXML private TextField pinField;
    @FXML private ComboBox<StaffRole> roleComboBox;

    private StaffMemberDAO staffMemberDAO;
    private StaffController staffController; // To refresh the table after adding
    private StaffMember currentUser;
    private final SystemLogDAO systemLogDAO = new SystemLogDAO();
    private Stage dialogStage;

    public AddStaffDialogController() {
        this.staffMemberDAO = new StaffMemberDAO();
    }

    // This method will be called by the StaffController to pass itself
    public void setStaffController(StaffController staffController) {
        this.staffController = staffController;
    }

    @FXML
    public void initialize() {
        // Populate the role ComboBox with values from the StaffRole enum
        roleComboBox.setItems(FXCollections.observableArrayList(StaffRole.values()));
    }

    @FXML
    void handleSave(ActionEvent event) {
        String name = nameField.getText();
        String pin = pinField.getText();
        StaffRole role = roleComboBox.getValue();

        if (name.isEmpty() || pin.isEmpty() || role == null) {
            // Add proper error handling (e.g., show an alert)
            System.out.println("All fields are required.");
            return;
        }

        // Create a new StaffMember object (ID is 0 because it will be auto-generated)
        StaffMember newStaff = new StaffMember(0, name, role, pin);
        staffMemberDAO.addStaffMember(newStaff);
        System.out.println("Staff added successfully.");

        systemLogDAO.addLog(currentUser.getStaffMemberID(), "CREATE_STAFF", "Store Manager added a new staff member.");
        dialogStage.setTitle("Add New Staff Member");

        // Refresh the main staff table
        staffController.loadStaffData();

        // Close the dialog
        closeDialog();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}