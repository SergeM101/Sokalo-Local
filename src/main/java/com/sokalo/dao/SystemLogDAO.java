package com.sokalo.dao;

import com.sokalo.model.SystemLog;

import java.util.ArrayList;
import java.util.List;

// in src/main/java/com/sokalo/dao/SystemLogDAO.java
public class SystemLogDAO {
    // Method to ADD a new log entry
    public void addLog(int staffMemberId, String action, String details) {
        String sql = "INSERT INTO SystemLog(staffMemberID, action, details, timestamp) VALUES(?,?,?,?)";
        // ... (database logic to insert a new log)
    }

    // Method to GET all log entries for the view
    public List<SystemLog> getAllLogs() {
        String sql = "SELECT * FROM SystemLog ORDER BY timestamp DESC";
        // ... (database logic to fetch all logs)
        return new ArrayList<>(); // return the list
    }
}