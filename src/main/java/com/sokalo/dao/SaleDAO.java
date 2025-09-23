// in src/main/java/com/sokalo/dao/SaleDAO.java

package com.sokalo.dao;

import com.sokalo.controller.POSController.SaleItemWrapper;
import com.sokalo.model.Sale;
import com.sokalo.util.DatabaseUtil;

import java.sql.*;
import java.util.List;

public class SaleDAO {

    public boolean createSale(Sale sale, List<SaleItemWrapper> saleItems) {
        String saleSQL = "INSERT INTO Sale(staffMemberID, saleTime, totalAmount) VALUES(?,?,?)";
        String saleItemSQL = "INSERT INTO Sale_Item(saleID, itemID, quantitySold, sellingPriceAtSale) VALUES(?,?,?,?)";

        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert the main Sale record
            PreparedStatement salePstmt = conn.prepareStatement(saleSQL, Statement.RETURN_GENERATED_KEYS);
            salePstmt.setInt(1, sale.getStaffMemberID());
            salePstmt.setTimestamp(2, Timestamp.valueOf(sale.getSaleTime()));
            salePstmt.setDouble(3, sale.getTotalAmount());

            if (salePstmt.executeUpdate() == 0) {
                conn.rollback();
                return false;
            }

            int saleId;
            try (ResultSet generatedKeys = salePstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    saleId = generatedKeys.getInt(1);
                } else {
                    conn.rollback();
                    return false;
                }
            }

            // 2. Insert each Sale_Item record
            PreparedStatement saleItemPstmt = conn.prepareStatement(saleItemSQL);
            for (SaleItemWrapper wrapper : saleItems) {
                saleItemPstmt.setInt(1, saleId);
                saleItemPstmt.setInt(2, wrapper.getItemID());
                saleItemPstmt.setInt(3, wrapper.getQuantitySold());
                saleItemPstmt.setDouble(4, wrapper.getPriceAtSale()); // This will now work
                saleItemPstmt.addBatch();
            }
            saleItemPstmt.executeBatch();

            conn.commit(); // Commit the transaction
            return true;

        } catch (SQLException e) {
            System.out.println("Sale creation failed: " + e.getMessage());
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            return false;
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    /**
     * Calculates the total revenue from all sales in the current month.
     * @return The total monthly revenue.
     */
    public double getMonthlyRevenue() {
        String sql = "SELECT SUM(totalAmount) FROM Sale WHERE strftime('%Y-%m', saleTime) = strftime('%Y-%m', 'now')";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Calculates the total sales amount for a specific cashier for today.
     * @param staffMemberId The cashier's ID.
     * @return The total sales amount.
     */
    public double getSalesTotalForCashier(int staffMemberId) {
        String sql = "SELECT SUM(totalAmount) FROM Sale WHERE staffMemberID = ? AND date(saleTime) = date('now')";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffMemberId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Counts the number of transactions for a specific cashier for today.
     * @param staffMemberId The cashier's ID.
     * @return The number of transactions.
     */
    public int getTransactionCountForCashier(int staffMemberId) {
        String sql = "SELECT COUNT(*) FROM Sale WHERE staffMemberID = ? AND date(saleTime) = date('now')";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffMemberId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Calculates the total number of individual items sold by a specific cashier today.
     * @param staffMemberId The cashier's ID.
     * @return The total quantity of items sold.
     */
    public int getItemsSoldCountForCashier(int staffMemberId) {
        String sql = "SELECT SUM(si.quantitySold) FROM Sale_Item si " +
                "JOIN Sale s ON si.saleID = s.saleID " +
                "WHERE s.staffMemberID = ? AND date(s.saleTime) = date('now')";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, staffMemberId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Calculates the total number of individual items sold by all cashiers today.
     * @return The total quantity of items sold today.
     */
    public int getTotalItemsSoldCountToday() {
        String sql = "SELECT SUM(si.quantitySold) FROM Sale_Item si " +
                "JOIN Sale s ON si.saleID = s.saleID " +
                "WHERE date(s.saleTime) = date('now')";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}