// in src/main/java/com/sokalo/dao/ShiftDAO.java

package com.sokalo.dao;

import com.sokalo.model.Shift;
import com.sokalo.model.enums.StaffRole;
import com.sokalo.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
    public void endShift(int shiftId, double closingValue, StaffRole role) {
        String sql;
        if (role == StaffRole.CASHIER) {
            sql = "UPDATE Shift SET endTime = ?, closingCash = ? WHERE shiftID = ?";
        } else {
            sql = "UPDATE Shift SET endTime = ?, endStock = ? WHERE shiftID = ?";
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            if (role == StaffRole.CASHIER) {
                pstmt.setDouble(2, closingValue);
            } else {
                pstmt.setInt(2, (int) closingValue);
            }
            pstmt.setInt(3, shiftId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Shift> getAllShifts() {
        String sql = "SELECT * FROM Shift ORDER BY startTime DESC";
        List<Shift> shifts = new ArrayList<>();
        // Logic to fetch all shifts from the database
        // ...
        return shifts;
    }
}