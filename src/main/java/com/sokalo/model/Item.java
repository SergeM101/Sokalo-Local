package com.sokalo.model;

import java.time.LocalDate;

public class Item {

    private int itemID;
    private String itemName;
    private String barcode;
    private double sellingPrice;
    private int stockQuantity;
    private LocalDate expiryDate;
    private String itemSyncStatus; // e.g., "synced", "unsynced"

    public Item(int itemID, String itemName, String barcode, double sellingPrice, int stockQuantity, LocalDate expiryDate, String itemSyncStatus) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.barcode = barcode;
        this.sellingPrice = sellingPrice;
        this.stockQuantity = stockQuantity;
        this.expiryDate = expiryDate;
        this.itemSyncStatus = itemSyncStatus;
    }

    // --- Getters and Setters ---

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getItemSyncStatus() {
        return itemSyncStatus;
    }

    public void setItemSyncStatus(String itemSyncStatus) {
        this.itemSyncStatus = itemSyncStatus;
    }
}
