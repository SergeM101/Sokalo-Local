// in src/main/java/com/sokalo/controller/ShiftsController.java
package com.sokalo.controller;

import com.sokalo.dao.ShiftDAO;
import com.sokalo.model.Shift;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
// ... add TableColumn imports ...

public class ShiftsController {
    @FXML private TableView<Shift> shiftsTableView;
    // ... @FXML for all your TableColumns ...

    private ShiftDAO shiftDAO = new ShiftDAO();

    @FXML
    public void initialize() {
        // ... set up all your PropertyValueFactory for each column ...

        // Load the data
        shiftsTableView.setItems(FXCollections.observableArrayList(shiftDAO.getAllShifts()));
    }
}