// in src/main/java/com/sokalo/controller/DashboardController.java
package com.sokalo.controller;

import com.sokalo.dao.ItemDAO;
import com.sokalo.dao.SaleDAO;
import com.sokalo.dao.StaffMemberDAO;
import com.sokalo.model.StaffMember;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardController {

    // --- FXML UI Elements ---
    @FXML private Label card1Title, card1Value;
    @FXML private Label card2Title, card2Value;
    @FXML private Label card3Title, card3Value;
    @FXML private VBox chartContainer; // A VBox to hold our chart(s)

    // --- DAOs for data access ---
    private final SaleDAO saleDAO = new SaleDAO();
    private final ItemDAO itemDAO = new ItemDAO();
    private final StaffMemberDAO staffMemberDAO = new StaffMemberDAO();

    private StaffMember currentUser;

    public void initData(StaffMember currentUser) {
        this.currentUser = currentUser;
        loadDashboardData();
    }

    private void loadDashboardData() {
        switch (currentUser.getRole()) {
            case STORE_MANAGER:
                loadManagerDashboard();
                break;
            case CASHIER:
                loadCashierDashboard();
                break;
            case STOCK_CONTROLLER:
                loadStockControllerDashboard();
                break;
        }
    }

    private void loadManagerDashboard() {
        // --- Populate Cards ---
        card1Title.setText("Total Monthly Revenue");
        card1Value.setText(String.format("%.0f FCFA", saleDAO.getMonthlyRevenue()));

        card2Title.setText("Items Low on Stock");
        card2Value.setText(String.format("%d Items", itemDAO.getLowStockCount()));

        card3Title.setText("Active Staff");
        card3Value.setText(String.format("%d Members", staffMemberDAO.getActiveStaffCount()));

        // --- Populate Charts ---
        chartContainer.getChildren().clear(); // Clear any existing charts

        // Sales Line Chart
        LineChart<String, Number> salesChart = new LineChart<>(new CategoryAxis(), new NumberAxis());
        salesChart.setTitle("Overall Sales (Last 7 Days)");
        XYChart.Series<String, Number> salesSeries = new XYChart.Series<>();
        salesSeries.setName("Sales");
        // salesSeries.setData(saleDAO.getWeeklySalesData()); // Fetch real data
        salesSeries.getData().add(new XYChart.Data<>("Mon", 45000));
        salesSeries.getData().add(new XYChart.Data<>("Tue", 52000));
        // ... add more data points
        salesChart.getData().add(salesSeries);

        // Inventory Bar Chart
        BarChart<String, Number> inventoryChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        inventoryChart.setTitle("Total Inventory by Category");
        XYChart.Series<String, Number> inventorySeries = new XYChart.Series<>();
        inventorySeries.setName("Stock Count");
        // inventorySeries.setData(itemDAO.getStockCountByCategory()); // Fetch real data
        inventorySeries.getData().add(new XYChart.Data<>("Dairy", 150));
        inventorySeries.getData().add(new XYChart.Data<>("Bakery", 80));
        // ... add more categories
        inventoryChart.getData().add(inventorySeries);

        chartContainer.getChildren().addAll(salesChart, inventoryChart);
    }

    private void loadCashierDashboard() {
        card1Title.setText("My Sales Today");
        card1Value.setText(String.format("%.0f FCFA", saleDAO.getSalesTotalForCashier(currentUser.getStaffMemberID())));

        card2Title.setText("My Transactions Today");
        card2Value.setText(String.format("%d", saleDAO.getTransactionCountForCashier(currentUser.getStaffMemberID())));

        card3Title.setText("Items Sold Today");
        card3Value.setText(String.format("%d", saleDAO.getItemsSoldCountForCashier(currentUser.getStaffMemberID())));

        // --- Populate Chart ---
        chartContainer.getChildren().clear();
        BarChart<String, Number> itemsSoldChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        itemsSoldChart.setTitle("Top 5 Items I Sold Today");
        XYChart.Series<String, Number> itemsSeries = new XYChart.Series<>();
        // itemsSeries.setData(saleDAO.getTopSoldItemsForCashier(currentUser.getStaffMemberID()));
        itemsSeries.getData().add(new XYChart.Data<>("Milk", 20));
        itemsSeries.getData().add(new XYChart.Data<>("Bread", 15));
        // ... add more items
        itemsSoldChart.getData().add(itemsSeries);

        chartContainer.getChildren().add(itemsSoldChart);
    }

    private void loadStockControllerDashboard() {
        card1Title.setText("Total Items in Stock");
        card1Value.setText(String.format("%d", itemDAO.getTotalStockCount()));

        card2Title.setText("Items Nearing Expiry");
        card2Value.setText(String.format("%d", itemDAO.getExpiringSoonCount()));

        card3Title.setText("Items Added Today");
        card3Value.setText(String.format("%d", itemDAO.getItemsAddedTodayCount()));

        // --- Populate Chart ---
        chartContainer.getChildren().clear();
        BarChart<String, Number> stockMovementChart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        stockMovementChart.setTitle("Stock Movement (Today)");

        XYChart.Series<String, Number> inSeries = new XYChart.Series<>();
        inSeries.setName("Items In");
        inSeries.getData().add(new XYChart.Data<>("Stock", itemDAO.getItemsAddedTodayCount()));

        XYChart.Series<String, Number> outSeries = new XYChart.Series<>();
        outSeries.setName("Items Out (Sales)");
        outSeries.getData().add(new XYChart.Data<>("Stock", saleDAO.getTotalItemsSoldCountToday()));

        stockMovementChart.getData().addAll(inSeries, outSeries);
        chartContainer.getChildren().add(stockMovementChart);
    }
}