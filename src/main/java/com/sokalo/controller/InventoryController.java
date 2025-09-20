// in src/main/java/com/sokalo/controller/InventoryController.java

package com.sokalo.controller;

import com.sokalo.Main;
import com.sokalo.dao.ItemDAO;
import com.sokalo.model.Item;
import com.sokalo.model.StaffMember;
import com.sokalo.model.enums.StaffRole;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class InventoryController {

    @FXML private TableView<Item> inventoryTableView;
    @FXML private TableColumn<Item, Integer> idColumn;
    @FXML private TableColumn<Item, String> nameColumn;
    @FXML private TableColumn<Item, String> barcodeColumn;
    @FXML private TableColumn<Item, Double> priceColumn;
    @FXML private TableColumn<Item, Integer> quantityColumn;
    @FXML private TableColumn<Item, LocalDate> expiryDateColumn; // New column
    @FXML private TableColumn<Item, Void> actionColumn;

    @FXML private Button addItemButton; // Need to ensure this fx:id exists in FXML

    private ItemDAO itemDAO;
    private ObservableList<Item> itemList = FXCollections.observableArrayList();
    private StaffMember currentUser; // To store the logged-in user

    public InventoryController() {
        this.itemDAO = new ItemDAO();
    }

    /**
     * Called by MainController to pass the logged-in user.
     * Use this to set up role-based UI visibility.
     */
    public void initData(StaffMember currentUser) {
        this.currentUser = currentUser;
        setupRolePermissions();
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("itemID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        barcodeColumn.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("sellingPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        expiryDateColumn.setCellValueFactory(new PropertyValueFactory<>("expiryDate")); // Link to ExpiryDate

        // --- Setup for the custom "Action" (Delete) column ---
        Callback<TableColumn<Item, Void>, TableCell<Item, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Item, Void> call(final TableColumn<Item, Void> param) {
                final TableCell<Item, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Delete");
                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Item item = getTableView().getItems().get(getIndex());
                            System.out.println("Deleting item: " + item.getItemName());
                            // TO-DO: Add a confirmation dialog and "reason for delete" input here
                            itemDAO.deleteItem(item.getItemID());
                            loadItemData(); // Refresh the table
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);

        loadItemData(); // Load initial data
    }

    /**
     * Determines which controls are visible based on the current user's role.
     */
    private void setupRolePermissions() {
        // Only Stock Controllers can add/delete items
        boolean isStockController = (currentUser.getRole() == StaffRole.STOCK_CONTROLLER);
        addItemButton.setVisible(isStockController);
        actionColumn.setVisible(isStockController);
    }

    // This needs to be public so AddItemDialogController can call it
    public void loadItemData() {
        itemList.setAll(itemDAO.getAllItems());
        inventoryTableView.setItems(itemList);
    }

    /**
     * Handles the "Add New Item" button click, opening the dialog.
     */
    @FXML
    void handleAddItemAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/com/sokalo/view/AddItemDialog.fxml"));
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add New Item");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(loader.load()));

            AddItemDialogController controller = loader.getController();
            controller.setInventoryController(this); // Pass this controller for refreshing

            dialogStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}