// in src/main/java/com/sokalo/controller/ShiftsController.java
package com.sokalo.controller;

import com.sokalo.dao.ShiftDAO;
import com.sokalo.model.Shift;
import com.sokalo.model.enums.StaffRole;
import com.sokalo.services.ReportGenerator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.event.ActionEvent;
import java.time.LocalDateTime;

public class ShiftsController {

    // Print report button
    @FXML private Button printShiftsButton;

    // Table and columns for Cashier Shifts
    @FXML private TableView<Shift> cashierShiftsTableView;
    @FXML private TableColumn<Shift, Integer> cashShiftIdCol;
    @FXML private TableColumn<Shift, String> cashStaffNameCol; // Assuming you join to get names
    @FXML private TableColumn<Shift, LocalDateTime> cashStartTimeCol;
    @FXML private TableColumn<Shift, LocalDateTime> cashCloseTimeCol;
    @FXML private TableColumn<Shift, Double> openingCashCol;
    @FXML private TableColumn<Shift, Double> closingCashCol;
    @FXML private TableColumn<Shift, String> cashFlagCol;

    // Table and columns for Stock Controller Shifts
    @FXML private TableView<Shift> stockShiftsTableView;
    @FXML private TableColumn<Shift, Integer> stockShiftIdCol;
    @FXML private TableColumn<Shift, String> stockStaffNameCol;
    @FXML private TableColumn<Shift, LocalDateTime> stockStartTimeCol;
    @FXML private TableColumn<Shift, LocalDateTime> stockCloseTimeCol;
    @FXML private TableColumn<Shift, Integer> startStockCol;
    @FXML private TableColumn<Shift, Integer> endStockCol;
    @FXML private TableColumn<Shift, String> stockFlagCol;

    private final ShiftDAO shiftDAO = new ShiftDAO();

    @FXML
    public void initialize() {
        // --- Setup for Cashier Table ---
        cashShiftIdCol.setCellValueFactory(new PropertyValueFactory<>("shiftID"));
        // cashStaffNameCol requires a wrapper or a join in the DAO
        cashStaffNameCol.setCellValueFactory(new PropertyValueFactory<>("staffMemberName"));
        cashStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        cashCloseTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        openingCashCol.setCellValueFactory(new PropertyValueFactory<>("openingCash"));
        closingCashCol.setCellValueFactory(new PropertyValueFactory<>("closingCash"));
        cashFlagCol.setCellValueFactory(new PropertyValueFactory<>("shiftFlag"));

        // --- Setup for Stock Controller Table ---
        stockShiftIdCol.setCellValueFactory(new PropertyValueFactory<>("shiftID"));
        // stockStaffNameCol requires a wrapper or a join in the DAO
        stockStaffNameCol.setCellValueFactory(new PropertyValueFactory<>("staffMemberName"));
        stockStartTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        stockCloseTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        startStockCol.setCellValueFactory(new PropertyValueFactory<>("startStock"));
        endStockCol.setCellValueFactory(new PropertyValueFactory<>("endStock"));
        stockFlagCol.setCellValueFactory(new PropertyValueFactory<>("shiftFlag"));

        // Load the data into both tables
        loadShiftData();
    }

    private void loadShiftData() {
        cashierShiftsTableView.setItems(FXCollections.observableArrayList(shiftDAO.getShiftsByRole(StaffRole.CASHIER)));
        stockShiftsTableView.setItems(FXCollections.observableArrayList(shiftDAO.getShiftsByRole(StaffRole.STOCK_CONTROLLER)));
    }

    // This method is to print out the report

    @FXML void handleShiftsReport(javafx.event.ActionEvent event){
        ReportGenerator.printReport(cashierShiftsTableView, "Cashier Shifts Report");
        ReportGenerator.printReport(stockShiftsTableView, "Stock Controller Shifts Report");
    }
}