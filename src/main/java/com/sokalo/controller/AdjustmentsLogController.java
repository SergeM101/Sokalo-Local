// in src/main/java/com/sokalo/controller/AdjustmentsLogController.java
package com.sokalo.controller;

import com.sokalo.dao.InventoryAdjustmentDAO;
import com.sokalo.model.InventoryAdjustment;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
// ... add TableColumn imports ...

public class AdjustmentsLogController {
    @FXML private TableView<InventoryAdjustment> adjustmentsTableView;
    // ... @FXML for all your TableColumns ...

    private InventoryAdjustmentDAO adjustmentDAO = new InventoryAdjustmentDAO();

    @FXML
    public void initialize() {
        // ... set up all your PropertyValueFactory for each column ...

        // Load the data
        adjustmentsTableView.setItems(FXCollections.observableArrayList(adjustmentDAO.getAllAdjustments()));
    }
}