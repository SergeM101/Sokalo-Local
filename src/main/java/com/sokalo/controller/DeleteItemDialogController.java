package com.sokalo.controller;

import com.sokalo.dao.InventoryAdjustmentDAO;
import com.sokalo.dao.ItemDAO;
import com.sokalo.dao.SystemLogDAO;
import com.sokalo.model.InventoryAdjustment;
import com.sokalo.model.Item;
import com.sokalo.model.StaffMember;
import com.sokalo.model.enums.InventoryAdjustmentReason;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.time.LocalDateTime;

public class DeleteItemDialogController {

    @FXML private Label itemNameLabel; // fx:id for the item name
    @FXML private ComboBox<InventoryAdjustmentReason> reasonComboBox; // Changed from TextField

    private Item itemToDelete;
    private InventoryController inventoryController;
    private final ItemDAO itemDAO = new ItemDAO();
    private final InventoryAdjustmentDAO adjustmentDAO = new InventoryAdjustmentDAO();
    private final SystemLogDAO systemLogDAO = new SystemLogDAO();
    private StaffMember currentUser;

    @FXML
    public void initialize() {
        // Populate the ComboBox with values from your new enum
        reasonComboBox.setItems(FXCollections.observableArrayList(InventoryAdjustmentReason.values()));
    }

    /**
     * Called by the InventoryController to pass the item being deleted.
     */
    public void initData(Item item, InventoryController controller) {
        this.itemToDelete = item;
        this.inventoryController = controller;
        // Set the label text to the name of the item being deleted
        itemNameLabel.setText("Reason for deleting: " + item.getItemName());
    }

    @FXML
    void handleConfirmDelete(ActionEvent event) {
        InventoryAdjustmentReason reason = reasonComboBox.getValue();
        if (reason == null) {
            System.out.println("A reason must be selected.");

            // Replace the TO-DO comment with:
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Reason Selected");
            alert.setContentText("Please select a reason for deleting this item.");
            alert.showAndWait();
            return;
        }

        // 1. Log the deletion as a negative inventory adjustment
        int staffId = 1; // Placeholder for the logged-in user's ID
        InventoryAdjustment adj = new InventoryAdjustment(
                0,
                itemToDelete.getItemID(),
                staffId,
                -itemToDelete.getStockQuantity(), // Quantity is negative
                reason.name(), // Convert enum to string
                LocalDateTime.now()
        );
        adjustmentDAO.addAdjustment(adj);

        // 2. Delete the item from the inventory
        itemDAO.deleteItem(itemToDelete.getItemID());

        // Log the deletion
        String details = "Stock Controller " + currentUser.getFullName() + " deleted an item.";
        systemLogDAO.addLog(currentUser.getStaffMemberID(), "DELETE_ITEM", details);

        // 3. Refresh the main table and close the dialog
        inventoryController.loadItemData();
        closeDialog();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) reasonComboBox.getScene().getWindow();
        stage.close();
    }
}