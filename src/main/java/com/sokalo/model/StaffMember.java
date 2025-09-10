package com.sokalo.model;

// We will create this enum soon
// import com.sokalo.model.enums.StaffRole;

public class StaffMember {

    private int staffMemberID;
    private String fullName;
    private String role; // We'll change this to the StaffRole enum later
    private String PIN;

    // A constructor to create new StaffMember objects
    public StaffMember(int staffMemberID, String fullName, String role, String PIN) {
        this.staffMemberID = staffMemberID;
        this.fullName = fullName;
        this.role = role;
        this.PIN = PIN;
    }

    // --- Getters and Setters ---
    // These methods allow you to access and modify the private fields safely.

    public int getStaffMemberID() {
        return staffMemberID;
    }

    public void setStaffMemberID(int staffMemberID) {
        this.staffMemberID = staffMemberID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPIN() {
        return PIN;
    }

    public void setPIN(String PIN) {
        this.PIN = PIN;
    }
}