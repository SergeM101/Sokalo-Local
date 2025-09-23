// in src/main/java/com/sokalo/controller/AdjustmentsLogController.java
package com.sokalo.controller;

import com.sokalo.dao.InventoryAdjustmentDAO;
import com.sokalo.model.InventoryAdjustment;
import com.sokalo.services.ReportGenerator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

public class AdjustmentsLogController {
    @FXML private Button printAdjustmentsButton;
    @FXML private TableView<InventoryAdjustment> adjustmentsTableView;
    @FXML private TableColumn<InventoryAdjustment, Integer> adjIdColumn;
    @FXML private TableColumn<InventoryAdjustment, Integer> itemIdColumn;
    @FXML private TableColumn<InventoryAdjustment, Integer> staffIdColumn;
    @FXML private TableColumn<InventoryAdjustment, Integer> quantityColumn;
    @FXML private TableColumn<InventoryAdjustment, String> reasonColumn;
    @FXML private TableColumn<InventoryAdjustment, LocalDateTime> timeColumn;

    private final InventoryAdjustmentDAO adjustmentDAO = new InventoryAdjustmentDAO();

    @FXML
    public void initialize() {
        // Link columns to the InventoryAdjustment model's properties
        adjIdColumn.setCellValueFactory(new PropertyValueFactory<>("adjustmentID"));
        itemIdColumn.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        staffIdColumn.setCellValueFactory(new PropertyValueFactory<>("staffMemberID"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantityChanged"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("adjustmentTime"));

        // Load data from the database
        adjustmentsTableView.setItems(FXCollections.observableArrayList(adjustmentDAO.getAllAdjustments()));
    }

    @FXML void handleAdjustmentsReport(javafx.event.ActionEvent event) {
        ReportGenerator.printReport(adjustmentsTableView, "Adjustments Log Report");
    }
}