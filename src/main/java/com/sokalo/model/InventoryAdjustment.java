package com.sokalo.model;

import java.time.LocalDateTime;

public class InventoryAdjustment {

    private int adjustmentID;
    private int itemID;
    private int staffMemberID;
    private int quantityChanged;
    private String reason;
    private LocalDateTime adjustmentTime;

    public InventoryAdjustment(int adjustmentID, int itemID, int staffMemberID, int quantityChanged, String reason, LocalDateTime adjustmentTime) {
        this.adjustmentID = adjustmentID;
        this.itemID = itemID;
        this.staffMemberID = staffMemberID;
        this.quantityChanged = quantityChanged;
        this.reason = reason;
        this.adjustmentTime = adjustmentTime;
    }

    // --- Getters and Setters ---

    public int getAdjustmentID() {
        return adjustmentID;
    }

    public void setAdjustmentID(int adjustmentID) {
        this.adjustmentID = adjustmentID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getStaffMemberID() {
        return staffMemberID;
    }

    public void setStaffMemberID(int staffMemberID) {
        this.staffMemberID = staffMemberID;
    }

    public int getQuantityChanged() {
        return quantityChanged;
    }

    public void setQuantityChanged(int quantityChanged) {
        this.quantityChanged = quantityChanged;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getAdjustmentTime() {
        return adjustmentTime;
    }

    public void setAdjustmentTime(LocalDateTime adjustmentTime) {
        this.adjustmentTime = adjustmentTime;
    }

}
