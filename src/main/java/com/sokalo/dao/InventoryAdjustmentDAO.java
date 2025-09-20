// in src/main/java/com/sokalo/dao/InventoryAdjustmentDAO.java

package com.sokalo.dao;

import com.sokalo.model.InventoryAdjustment;
import com.sokalo.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InventoryAdjustmentDAO {

    /**
     * Adds a new inventory adjustment record to the database.
     * @param adjustment The InventoryAdjustment object to save.
     */
    public void addAdjustment(InventoryAdjustment adjustment) {
        String sql = "INSERT INTO InventoryAdjustment(itemID, staffMemberID, quantityChanged, reason, adjustmentTime) VALUES(?,?,?,?,?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, adjustment.getItemID());
            pstmt.setInt(2, adjustment.getStaffMemberID());
            pstmt.setInt(3, adjustment.getQuantityChanged());
            pstmt.setString(4, adjustment.getReason());
            pstmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // You could add a method here later to get all adjustments for a report
    public List<InventoryAdjustment> getAllAdjustments() {
        String sql = "SELECT * FROM InventoryAdjustment ORDER BY adjustmentTime DESC";
        List<InventoryAdjustment> adjustments = new ArrayList<>();
        // Logic to fetch all adjustments from the database
        // ...
        return adjustments;
    }
}