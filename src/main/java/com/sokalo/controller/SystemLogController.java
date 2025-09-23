// in src/main/java/com/sokalo/controller/SystemLogController.java
package com.sokalo.controller;

import com.sokalo.dao.SystemLogDAO;
import com.sokalo.model.SystemLog;
import com.sokalo.services.ReportGenerator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
// ... import TableColumn ...

public class SystemLogController {
    @FXML private TableView<SystemLog> logTableView;
    // ... @FXML for your columns ...
    @FXML private TableColumn<SystemLog, String> actionColumn;
    @FXML private TableColumn<SystemLog, String> detailsColumn;
    @FXML private TableColumn<SystemLog, String> staffColumn;
    @FXML private TableColumn<SystemLog, String> timestampColumn;
    @FXML private Button printReportButton;

    private final SystemLogDAO logDAO = new SystemLogDAO();

    @FXML
    public void initialize() {
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));
        detailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        staffColumn.setCellValueFactory(new PropertyValueFactory<>("staffMember"));
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        // Load the data
        logTableView.setItems(FXCollections.observableArrayList(logDAO.getAllLogs()));
    }
    

    @FXML void handlePrintReportClick(javafx.event.ActionEvent actionEvent) {
        ReportGenerator.printReport(logTableView, "System Log Report");
    }
}