package com.sokalo.model;

import java.time.LocalDateTime;

public class Shift {

    private int shiftID;
    private int staffMemberID;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double openingCash;
    private double closingCash;
    private int startStock; // Your added field
    private int endStock;   // Your added field
    private String shiftFlag; // e.g., "Valid", "Discrepancy"

    public Shift(int shiftID, int staffMemberID, LocalDateTime startTime, LocalDateTime endTime, double openingCash, double closingCash, int startStock, int endStock, String shiftFlag) {
        this.shiftID = shiftID;
        this.staffMemberID = staffMemberID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.openingCash = openingCash;
        this.closingCash = closingCash;
        this.startStock = startStock;
        this.endStock = endStock;
        this.shiftFlag = shiftFlag;
    }

    // --- Getters and Setters ---

    public int getShiftID() {
        return shiftID;
    }

    public void setShiftID(int shiftID) {
        this.shiftID = shiftID;
    }

    public int getStaffMemberID() {
        return staffMemberID;
    }

    public void setStaffMemberID(int staffMemberID) {
        this.staffMemberID = staffMemberID;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public double getOpeningCash() {
        return openingCash;
    }

    public void setOpeningCash(double openingCash) {
        this.openingCash = openingCash;
    }

    public double getClosingCash() {
        return closingCash;
    }

    public void setClosingCash(double closingCash) {
        this.closingCash = closingCash;
    }

    public int getStartStock() {
        return startStock;
    }

    public void setStartStock(int startStock) {
        this.startStock = startStock;
    }

    public int getEndStock() {
        return endStock;
    }

    public void setEndStock(int endStock) {
        this.endStock = endStock;
    }

    public String getShiftFlag() {
        return shiftFlag;
    }

    public void setShiftFlag(String shiftFlag) {
        this.shiftFlag = shiftFlag;
    }
}
