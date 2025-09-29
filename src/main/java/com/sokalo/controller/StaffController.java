// in src/main/java/com/sokalo/controller/StaffController.java
package com.sokalo.controller;

import com.sokalo.Main;
import com.sokalo.dao.StaffMemberDAO;
import com.sokalo.dao.SystemLogDAO;
import com.sokalo.model.StaffMember;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class StaffController {

    @FXML private Button addStaffButton;
    @FXML private TableColumn<StaffMember, Void> actionColumn;
    @FXML private TableView<StaffMember> staffTableView;
    @FXML private TableColumn<StaffMember, Integer> idColumn;
    @FXML private TableColumn<StaffMember, String> nameColumn;
    @FXML private TableColumn<StaffMember, String> roleColumn;

    // A list to hold the data for the table
    private ObservableList<StaffMember> staffList = FXCollections.observableArrayList();
    private StaffMemberDAO staffMemberDAO;
    private StaffMember currentUser;
    private final SystemLogDAO systemLogDAO = new SystemLogDAO();

    public StaffController() {
        this.staffMemberDAO = new StaffMemberDAO(); // Create an instance of the DAO
    }

    // This method receives the data from MainController
    public void initData(StaffMember currentUser) {
        this.currentUser = currentUser;
        // Now it's safe to load data
        loadStaffData();
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
        if (currentUser != null) {
            this.systemLogDAO.addLog(currentUser.getStaffMemberID(), "STAFF_CONTROLLER_INIT", "StaffController initialized.");
        } // Do not log without a currentUser to avoid NullPointerException

        // --- Setup for the custom "Actions" column ---
        Callback<TableColumn<StaffMember, Void>, TableCell<StaffMember, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<StaffMember, Void> call(final TableColumn<StaffMember, Void> param) {
                final TableCell<StaffMember, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Delete");
                    {
                        // The action that happens when the delete button is clicked
                        btn.setOnAction((ActionEvent event) -> {
                            // Get the StaffMember object from the current row
                            StaffMember staffMember = getTableView().getItems().get(getIndex());
                            System.out.println("Deleting staff: " + staffMember.getFullName());
                            // Log the action BEFORE deleting
                            String details = "Store Manager deleted staff member: " + staffMember.getFullName() + " (ID: " + staffMember.getStaffMemberID() + ")";
                            if (currentUser != null) {
                                systemLogDAO.addLog(currentUser.getStaffMemberID(), "DELETE_STAFF", details);
                            }

                            // Call the DAO to delete it from the database
                            staffMemberDAO.deleteStaffMember(staffMember.getStaffMemberID());

                            // Refresh the table view
                            loadStaffData();
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            // Place the button in the cell
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);

        loadStaffData(); // Load the data when the view is initialized
    }

    public void loadStaffData() {
        // 2. Use the DAO to get all staff from the database
        staffList.setAll(staffMemberDAO.getAllStaff());
        staffTableView.setItems(staffList);
    }

    @FXML
    void handleAddStaffAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/AddStaffDialog.fxml"));
            Stage dialogStage = new Stage();

            // This makes the dialog block the main window until it's closed
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            // Pass this controller to the dialog controller so it can call the refresh method
            AddStaffDialogController controller = loader.getController();
            controller.setStaffController(this);

            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}