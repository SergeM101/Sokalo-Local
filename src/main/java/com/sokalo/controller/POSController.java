// in src/main/java/com/sokalo/controller/POSController.java

package com.sokalo.controller;

import com.sokalo.dao.ItemDAO;
import com.sokalo.dao.SaleDAO;
import com.sokalo.dao.SystemLogDAO;
import com.sokalo.model.Item;
import com.sokalo.model.Sale;
import com.sokalo.model.StaffMember;
import com.sokalo.services.ReceiptPrinter;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.time.LocalDateTime;

public class POSController {

    @FXML private TextField barcodeField;
    @FXML private Label totalLabel;
    @FXML private TableView<SaleItemWrapper> saleTableView;
    @FXML private TableColumn<SaleItemWrapper, String> itemNameColumn;
    @FXML private TableColumn<SaleItemWrapper, Integer> quantityColumn;
    @FXML private TableColumn<SaleItemWrapper, Double> subtotalColumn;

    private final ItemDAO itemDAO = new ItemDAO();
    private final SaleDAO saleDAO = new SaleDAO();
    private final SystemLogDAO systemLogDAO = new SystemLogDAO();
    private StaffMember currentUser; // Assume this is passed in
    private ObservableList<SaleItemWrapper> currentSaleItems = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        itemNameColumn.setCellValueFactory(cellData -> cellData.getValue().itemNameProperty());
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantitySoldProperty().asObject());
        subtotalColumn.setCellValueFactory(cellData -> cellData.getValue().subtotalProperty().asObject());
        saleTableView.setItems(currentSaleItems);
        updateSaleTotal();
    }

    @FXML
    void handleBarcodeEntry(ActionEvent event) {
        String barcode = barcodeField.getText();
        if (barcode.isEmpty()) return;

        Item item = itemDAO.findByBarcode(barcode);

        if (item != null && item.getStockQuantity() > 0) {
            boolean itemExistsInCart = false;
            for (SaleItemWrapper wrapper : currentSaleItems) {
                if (wrapper.getItemID() == item.getItemID()) {
                    if (wrapper.getQuantitySold() < item.getStockQuantity()) {
                        wrapper.setQuantitySold(wrapper.getQuantitySold() + 1);
                        saleTableView.refresh();
                    }
                    itemExistsInCart = true;
                    break;
                }
            }
            if (!itemExistsInCart) {
                currentSaleItems.add(new SaleItemWrapper(item, 1));
            }
            updateSaleTotal();
        } else {
            System.out.println("Item not found or out of stock.");
        }
        barcodeField.clear();
    }

    @FXML
    void handleFinalizeSale(ActionEvent event) {
        if (currentSaleItems.isEmpty()) return;

        int currentStaffMemberId = 1; // Placeholder for logged-in user's ID
        double total = Double.parseDouble(totalLabel.getText().split(" ")[0]);
        Sale newSale = new Sale(0, currentStaffMemberId, LocalDateTime.now(), total);

        boolean success = saleDAO.createSale(newSale, currentSaleItems);

        if (success) {
            for (SaleItemWrapper wrapper : currentSaleItems) {
                itemDAO.updateStockQuantity(wrapper.getItemID(), wrapper.getQuantitySold());
            }
            // Log the sale
            String details = "Sale finalized by " + currentUser.getFullName() + ". Total: " + newSale.getTotalAmount() + " FCFA.";
            systemLogDAO.addLog(currentUser.getStaffMemberID(), "FINALIZE_SALE", details);

            ReceiptPrinter.printReceipt(newSale, currentSaleItems);

            currentSaleItems.clear();
            updateSaleTotal();
            System.out.println("Sale finalized successfully!");
        } else {
            System.out.println("Failed to finalize sale.");
        }
    }

    private void updateSaleTotal() {
        double total = 0.0;
        for (SaleItemWrapper wrapper : currentSaleItems) {
            total += wrapper.getSubtotal();
        }
        totalLabel.setText(String.format("%.2f FCFA", total));
    }

    public void initData(StaffMember currentUser) {

    }

    // A helper "wrapper" class to make TableView display data correctly
    public static class SaleItemWrapper {
        private final SimpleIntegerProperty itemID;
        private final SimpleStringProperty itemName;
        private final SimpleIntegerProperty quantitySold;
        private final SimpleDoubleProperty priceAtSale;
        private final SimpleDoubleProperty subtotal;

        public SaleItemWrapper(Item item, int quantity) {
            this.itemID = new SimpleIntegerProperty(item.getItemID());
            this.itemName = new SimpleStringProperty(item.getItemName());
            this.quantitySold = new SimpleIntegerProperty(quantity);
            this.priceAtSale = new SimpleDoubleProperty(item.getSellingPrice());
            this.subtotal = new SimpleDoubleProperty(item.getSellingPrice() * quantity);
        }

        // --- Property Getters for TableView ---
        public SimpleStringProperty itemNameProperty() { return itemName; }
        public SimpleIntegerProperty quantitySoldProperty() { return quantitySold; }
        public SimpleDoubleProperty subtotalProperty() { return subtotal; }

        // --- Standard Getters/Setters ---
        public int getItemID() { return itemID.get(); }
        public int getQuantitySold() { return quantitySold.get(); }
        public double getPriceAtSale() { return priceAtSale.get(); }
        public double getSubtotal() { return subtotal.get(); }
        public void setQuantitySold(int quantity) {
            this.quantitySold.set(quantity);
            this.subtotal.set(this.priceAtSale.get() * quantity);
        }

        public Object getItemName() { return itemName.get();}
    }
}