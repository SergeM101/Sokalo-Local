// in src/main/java/com/sokalo/dao/StaffMemberDAO.java

package com.sokalo.dao;

import com.sokalo.model.StaffMember;
import com.sokalo.model.enums.StaffRole;
import com.sokalo.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffMemberDAO {

    /**
     * Finds a staff member by their username and PIN.
     * @param username The username entered by the user.
     * @param pin The PIN entered by the user.
     * @return a StaffMember object if found, otherwise null.
     */
    public StaffMember findByUsernameAndPin(String username, String pin) {
        String sql = "SELECT * FROM StaffMember WHERE fullName = ? AND PIN = ?";
        StaffMember staffMember = null;

        // Using try-with-resources to ensure the connection is closed automatically
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters for the query to prevent SQL injection
            pstmt.setString(1, username);
            pstmt.setString(2, pin); // In a real app, you would hash the PIN

            ResultSet rs = pstmt.executeQuery();

            // Check if we found a user
            if (rs.next()) {
                // Create a StaffMember object from the database data
                staffMember = new StaffMember(
                        rs.getInt("staffMemberID"),
                        rs.getString("fullName"),
                        StaffRole.valueOf(rs.getString("role")), // Convert String to Enum
                        rs.getString("PIN")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return staffMember;
    }

    // You would add other methods here later, like:
    public void addStaffMember(StaffMember staffMember) {
        String sql = "INSERT INTO StaffMember(fullName, role, PIN) VALUES(?,?,?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, staffMember.getFullName());
            pstmt.setString(2, staffMember.getRole().name()); // Convert enum to string
            pstmt.setString(3, staffMember.getPIN());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // Get a full list of all staff members
    /**
     * Retrieves all staff members from the database.
     * @return A List of StaffMember objects.
     */
    public List<StaffMember> getAllStaff() {
        String sql = "SELECT * FROM StaffMember";
        List<StaffMember> staffList = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                StaffMember staffMember = new StaffMember(
                        rs.getInt("staffMemberID"),
                        rs.getString("fullName"),
                        StaffRole.valueOf(rs.getString("role")),
                        rs.getString("PIN")
                );
                staffList.add(staffMember);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return staffList;
    }

    // public void updateStaffMember(StaffMember staffMember) { ... }
}