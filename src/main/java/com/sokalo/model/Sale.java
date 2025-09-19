package com.sokalo.model;

import java.time.LocalDateTime;

public class Sale {

    private int saleID;
    private int staffMemberID;
    private LocalDateTime saleTime;
    private double totalAmount;

    public Sale(int saleID, int staffMemberID, LocalDateTime saleTime, double totalAmount) {
        this.saleID = saleID;
        this.staffMemberID = staffMemberID;
        this.saleTime = saleTime;
        this.totalAmount = totalAmount;
    }

    // --- Getters and Setters ---

    public int getSaleID() {
        return saleID;
    }

    public void setSaleID(int saleID) {
        this.saleID = saleID;
    }

    public int getStaffMemberID() {
        return staffMemberID;
    }

    public void setStaffMemberID(int staffMemberID) {
        this.staffMemberID = staffMemberID;
    }

    public LocalDateTime getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(LocalDateTime saleTime) {
        this.saleTime = saleTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
