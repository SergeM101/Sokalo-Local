// in src/main/java/com/sokalo/dao/ShiftDAO.java

package com.sokalo.dao;

import com.sokalo.model.Shift;
import com.sokalo.model.enums.StaffRole;
import com.sokalo.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShiftDAO {

    /**
     * Starts a new shift for a staff member and saves it to the database.
     * @param staffMemberId The ID of the staff member starting the shift.
     * @param role The role of the staff member.
     * @param openingValue The starting cash or stock count.
     */
    public void startShift(int staffMemberId, StaffRole role, double openingValue) {
        String sql = "INSERT INTO Shift(staffMemberID, startTime, openingCash, startStock) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, staffMemberId);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));

            // Save the value in the correct column based on the user's role
            if (role == StaffRole.CASHIER) {
                pstmt.setDouble(3, openingValue);
                pstmt.setNull(4, java.sql.Types.INTEGER); // Set startStock to null
            } else if (role == StaffRole.STOCK_CONTROLLER) {
                pstmt.setNull(3, java.sql.Types.DOUBLE); // Set openingCash to null
                pstmt.setInt(4, (int) openingValue);
            }
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // You would add other methods here later, like:
    // Updated endShift method
    public void endShift(int shiftId, double closingValue, double expectedValue, StaffRole role) {
        String flag = (Math.abs(closingValue - expectedValue) < 0.01) ? "Valid" : "Discrepancy";
        String sql;

        if (role == StaffRole.CASHIER) {
            sql = "UPDATE Shift SET endTime = ?, closingCash = ?, shiftFlag = ? WHERE shiftID = ?";
        } else {
            sql = "UPDATE Shift SET endTime = ?, endStock = ?, shiftFlag = ? WHERE shiftID = ?";
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            if (role == StaffRole.CASHIER) {
                pstmt.setDouble(2, closingValue);
            } else {
                pstmt.setInt(2, (int) closingValue);
            }
            pstmt.setString(3, flag);
            pstmt.setInt(4, shiftId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // You'll need a method to get the current active shift for a user
    public Shift findActiveShiftForUser(int staffMemberId) {
        // SQL to get the latest shift for a user that has not ended
        // ...
        return null; // return the Shift object
    }

    // You'll need a method to calculate the expected cash total for a shift
    public double calculateSalesTotalForShift(int shiftId) {
        // SQL to SUM(totalAmount) from the Sale table where the sale happened during the shift
        // ...
        return 0.0; // return the total
    }
    // You'll need a method to calculate the expected stock total for a shift
    public double calculateStockForShift(int shiftId) {
        return 0.0;
    }

    /**
     * Retrieves all shifts for a specific role.
     * @param role The role to filter by (CASHIER or STOCK_CONTROLLER).
     * @return A List of Shift objects.
     */
    public List<Shift> getShiftsByRole(StaffRole role) {
        String sql = "SELECT s.*, sm.fullName FROM Shift s " +
                "JOIN StaffMember sm ON s.staffMemberID = sm.staffMemberID " +
                "WHERE sm.role = ? ORDER BY s.startTime DESC";
        List<Shift> shifts = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, role.name());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                // Logic to create Shift objects from the result set
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return shifts;
    }
}