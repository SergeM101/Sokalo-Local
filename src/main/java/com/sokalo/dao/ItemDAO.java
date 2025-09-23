// in src/main/java/com/sokalo/dao/ItemDAO.java

package com.sokalo.dao;

import com.sokalo.model.Item;
import com.sokalo.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    /**
     * Inserts a new item or updates the quantity of an existing item if the barcode matches.
     * @param item The item to be added or updated.
     */
    public void upsertItem(Item item) {
        Item existingItem = findByBarcode(item.getBarcode());

        if (existingItem != null) {
            // Item exists, so update its quantity
            String sql = "UPDATE Item SET stockQuantity = stockQuantity + ? WHERE itemID = ?";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, item.getStockQuantity());
                pstmt.setInt(2, existingItem.getItemID());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error updating item quantity: " + e.getMessage());
            }
        } else {
            // Item does not exist, so insert a new one
            String sql = "INSERT INTO Item(itemName, barcode, sellingPrice, stockQuantity, expiryDate, itemSyncStatus) VALUES(?,?,?,?,?,?)";
            try (Connection conn = DatabaseUtil.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, item.getItemName());
                pstmt.setString(2, item.getBarcode());
                pstmt.setDouble(3, item.getSellingPrice());
                pstmt.setInt(4, item.getStockQuantity());
                pstmt.setDate(5, java.sql.Date.valueOf(item.getExpiryDate()));
                pstmt.setString(6, "unsynced");
                pstmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error inserting new item: " + e.getMessage());
            }
        }
    }

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

    /**
     * Decrements the stock quantity of a specific item after a sale.
     * @param itemId The ID of the item to update.
     * @param quantityToDecrement The number of items sold.
     */
    public void updateStockQuantity(int itemId, int quantityToDecrement) {
        String sql = "UPDATE Item SET stockQuantity = stockQuantity - ? WHERE itemID = ? AND stockQuantity >= ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantityToDecrement);
            pstmt.setInt(2, itemId);
            pstmt.setInt(3, quantityToDecrement);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Inserts a new item into the database.
     * @param item The item to be added.
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
            pstmt.setString(6, "unsynced");
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error inserting new item: " + e.getMessage());
        }
    }

    /**
     * Calculates the total count of all items in stock.
     * @return The sum of stockQuantity for all items.
     */
    public int getTotalStockCount() {
        String sql = "SELECT SUM(stockQuantity) as totalStock FROM Item";
        int totalStock = 0;
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                totalStock = rs.getInt("totalStock");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return totalStock;
    }

    public int getLowStockCount() {
        String sql = "SELECT COUNT(*) FROM Item WHERE stockQuantity < 10";
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

    /**
     * Counts items that are expiring within the next 7 days.
     * @return The count of expiring items.
     */
    public int getExpiringSoonCount() {
        String sql = "SELECT COUNT(*) FROM Item WHERE expiryDate BETWEEN date('now') AND date('now', '+7 days')";
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

    /**
     * Counts items that were added today via inventory adjustments.
     * @return The count of items added today.
     */
    public int getItemsAddedTodayCount() {
        // This query checks for positive adjustments made today
        String sql = "SELECT SUM(quantityChanged) FROM InventoryAdjustment WHERE quantityChanged > 0 AND date(adjustmentTime) = date('now')";
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