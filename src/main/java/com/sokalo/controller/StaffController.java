// in src/main/java/com/sokalo/controller/StaffController.java
package com.sokalo.controller;

import com.sokalo.dao.StaffMemberDAO;
import com.sokalo.model.StaffMember;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StaffController {

    public Button addStaffButton;
    @FXML
    private TableView<StaffMember> staffTableView;
    @FXML
    private TableColumn<StaffMember, Integer> idColumn;
    @FXML
    private TableColumn<StaffMember, String> nameColumn;
    @FXML
    private TableColumn<StaffMember, String> roleColumn;

    // A list to hold the data for the table
    private ObservableList<StaffMember> staffList = FXCollections.observableArrayList();
    private StaffMemberDAO staffMemberDAO;

    public StaffController() {
        this.staffMemberDAO = new StaffMemberDAO(); // Create an instance of the DAO
    }

    /**
     * This method is called automatically when the FXML is loaded.
     * It sets up the table columns to display the correct data.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("staffMemberID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        loadStaffData(); // Load the data when the view is initialized
    }

    private void loadStaffData() {
        // 2. Use the DAO to get all staff from the database
        staffList.setAll(staffMemberDAO.getAllStaff());
        staffTableView.setItems(staffList);
    }

    @FXML
    void handleAddStaffAction(ActionEvent event) {
        // This is the final step: when we build the "Add Staff" form,
        // it will call the staffMemberDAO.addStaffMember() method.
        System.out.println("Add Staff button clicked!");
    }
}