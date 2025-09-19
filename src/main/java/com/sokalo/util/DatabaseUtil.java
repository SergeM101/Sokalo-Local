
package com.sokalo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseUtil {

    // The name of our database file
    private static final String DATABASE_URL = "jdbc:sqlite:sokalo_local.db";

    /**
     * Establishes a connection to the SQLite database.
     * @return a Connection object to the database
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Creates all necessary tables if they don't already exist.
     * This method should be called once when the application starts.
     */
    public static void createTables() {
        // SQL statements for creating new tables
        String staffMemberTable = "CREATE TABLE IF NOT EXISTS StaffMember ("
                + " staffMemberID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " fullName TEXT NOT NULL,"
                + " role TEXT NOT NULL,"
                + " PIN TEXT NOT NULL"
                + ");";

        String itemTable = "CREATE TABLE IF NOT EXISTS Item ("
                + " itemID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " itemName TEXT NOT NULL,"
                + " barcode TEXT UNIQUE,"
                + " sellingPrice REAL NOT NULL,"
                + " stockQuantity INTEGER NOT NULL,"
                + " expiryDate DATE,"
                + " itemSyncStatus TEXT NOT NULL DEFAULT 'unsynced'"
                + ");";

        String saleTable = "CREATE TABLE IF NOT EXISTS Sale ("
                + " saleID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " staffMemberID INTEGER NOT NULL,"
                + " saleTime DATETIME NOT NULL,"
                + " totalAmount REAL NOT NULL,"
                + " FOREIGN KEY (staffMemberID) REFERENCES StaffMember(staffMemberID)"
                + ");";

        String saleItemTable = "CREATE TABLE IF NOT EXISTS Sale_Item ("
                + " saleID INTEGER NOT NULL,"
                + " itemID INTEGER NOT NULL,"
                + " quantitySold INTEGER NOT NULL,"
                + " sellingPriceAtSale REAL NOT NULL,"
                + " PRIMARY KEY (saleID, itemID),"
                + " FOREIGN KEY (saleID) REFERENCES Sale(saleID),"
                + " FOREIGN KEY (itemID) REFERENCES Item(itemID)"
                + ");";

        // ... (You would add the CREATE TABLE statements for Shift and InventoryAdjustment here too)

        String shiftTable = "CREATE TABLE IF NOT EXISTS Shift ("
                + "shiftID INTEGER NOT NULL ,"
                + "staffMemberID INTEGER NOT NULL ,"
                + "startTime DATETIME NOT NULL ,"
                + "startStock INT NOT NULL ,"
                + "endStock INT NOT NULL ,"
                + "openingCash REAL NOT NULL ,"
                + "closingCash REAL NOT NULL ,"
                + "shiftFlag TEXT NOT NULL ,"
                + "PRIMARY KEY (shiftID) ,"
                + "FOREIGN KEY (staffMemberID) REFERENCES StaffMember(staffMemberID)";

        String inventoryAdjustmentTable = "CREATE TABLE IF NOT EXISTS InventoryAdjustment ("
                + "adjustmentID INTEGER NOT NULL ,"
                + "staffMemberID INTEGER NOT NULL ,"
                + "itemID INTEGER NOT NULL ,"
                + "quantityChanged INTEGER NOT NULL ,"
                + "reason TEXT NOT NULL ,"
                + "adjustmentTime DATETIME NOT NULL ,"
                + "PRIMARY KEY (adjustmentID) ,"
                + "FOREIGN KEY (staffMemberID) REFERENCES StaffMember(staffMemberID),"
                + "FOREIGN KEY (itemID) REFERENCES Item(itemID)";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Create tables
            stmt.execute(staffMemberTable);
            stmt.execute(itemTable);
            stmt.execute(saleTable);
            stmt.execute(saleItemTable);
            // ... (execute statements for other tables)
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
