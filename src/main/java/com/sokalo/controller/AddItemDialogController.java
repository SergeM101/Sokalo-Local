// in src/main/java/com/sokalo/controller/AddItemDialogController.java
package com.sokalo.controller;

import com.sokalo.dao.ItemDAO;
import com.sokalo.model.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddItemDialogController {

    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    @FXML private TextField itemNameField;
    @FXML private TextField barcodeField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private DatePicker expiryDatePicker;

    private ItemDAO itemDAO;
    private InventoryController inventoryController; // To refresh the table

    public AddItemDialogController() {
        this.itemDAO = new ItemDAO();
    }

    public void setInventoryController(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }

    @FXML
    void handleSave(ActionEvent event) {
        String itemName = itemNameField.getText();
        String barcode = barcodeField.getText();
        double sellingPrice = Double.parseDouble(priceField.getText()); // Add error handling
        int stockQuantity = Integer.parseInt(quantityField.getText()); // Add error handling
        LocalDate expiryDate = expiryDatePicker.getValue();

        // Basic validation
        if (itemName.isEmpty() || barcode.isEmpty() || expiryDate == null) {
            System.out.println("Please fill all required fields.");
            // TO-DO: Show an alert
            return;
        }

        Item newItem = new Item(0, itemName, barcode, sellingPrice, stockQuantity, expiryDate, "unsynced");
        itemDAO.addItem(newItem);

        inventoryController.loadItemData(); // Refresh the main inventory table
        closeDialog();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) itemNameField.getScene().getWindow();
        stage.close();
    }
}