package com.sokalo.model;

public class SystemLog {
    private int logID;
    private int staffMemberID;
    private String action;
    private String details;
    private String timestamp;

    public int getLogID() {
        return logID;
    }
    public void setLogID(int logID) {
        this.logID = logID;
    }
    public int getStaffMemberID() {
        return staffMemberID;
    }
    public void setStaffMemberID(int staffMemberID) {
        this.staffMemberID = staffMemberID;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getDetails() {
        return details;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
