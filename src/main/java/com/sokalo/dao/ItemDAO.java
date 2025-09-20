// in src/main/java/com/sokalo/dao/ItemDAO.java

package com.sokalo.dao;

import com.sokalo.model.Item;
import com.sokalo.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    /**
     * Finds a single item by its unique barcode.
     * @param barcode The barcode to search for.
     * @return an Item object if found, otherwise null.
     */
    public Item findByBarcode(String barcode) {
        String sql = "SELECT * FROM Item WHERE barcode = ?";
        Item item = null;

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, barcode);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                item = new Item(
                        rs.getInt("itemID"),
                        rs.getString("itemName"),
                        rs.getString("barcode"),
                        rs.getDouble("sellingPrice"),
                        rs.getInt("stockQuantity"),
                        rs.getDate("expiryDate") != null ? rs.getDate("expiryDate").toLocalDate() : null,
                        rs.getString("itemSyncStatus")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return item;
    }

    /**
     * Retrieves all items from the database.
     * @return A List of all Item objects.
     */
    public List<Item> getAllItems() {
        String sql = "SELECT * FROM Item";
        List<Item> items = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Item item = new Item(
                        rs.getInt("itemID"),
                        rs.getString("itemName"),
                        rs.getString("barcode"),
                        rs.getDouble("sellingPrice"),
                        rs.getInt("stockQuantity"),
                        rs.getDate("expiryDate") != null ? rs.getDate("expiryDate").toLocalDate() : null,
                        rs.getString("itemSyncStatus")
                );
                items.add(item);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return items;
    }

    public void updateStockQuantity(int itemId, int quantityToDecrement) {
        // We use a subquery to prevent the stock from going below zero
        String sql = "UPDATE Item SET stockQuantity = stockQuantity - ? WHERE itemID = ? AND stockQuantity >= ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantityToDecrement);
            pstmt.setInt(2, itemId);
            pstmt.setInt(3, quantityToDecrement); // Ensure stock is sufficient
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // You would add other methods here later like addItem(), updateItemStock(), etc.
    /**
     * Adds a new item to the database.
     * @param item The Item object to add.
     */
    public void addItem(Item item) {
        String sql = "INSERT INTO Item(itemName, barcode, sellingPrice, stockQuantity, expiryDate, itemSyncStatus) VALUES(?,?,?,?,?,?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, item.getItemName());
            pstmt.setString(2, item.getBarcode());
            pstmt.setDouble(3, item.getSellingPrice());
            pstmt.setInt(4, item.getStockQuantity());
            pstmt.setDate(5, java.sql.Date.valueOf(item.getExpiryDate()));
            pstmt.setString(6, "unsynced"); // New items always need to be synced
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Deletes an item from the database by its ID.
     * @param itemId The ID of the item to delete.
     */
    public void deleteItem(int itemId) {
        String sql = "DELETE FROM Item WHERE itemID = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, itemId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}