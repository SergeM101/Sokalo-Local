
package com.sokalo.util;

import java.sql.*;

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
                + "startStock INT ,"    // nullable for stock controller
                + "endStock INT ,"      // nullable for stock controller
                + "openingCash REAL ,"  // nullable for cashier
                + "closingCash REAL ,"  // nullable for cashier
                + "shiftFlag TEXT NOT NULL DEFAULT 'ACTIVE' ,"
                + "PRIMARY KEY (shiftID) ,"
                + "FOREIGN KEY (staffMemberID) REFERENCES StaffMember(staffMemberID)"
                + ");";

        String inventoryAdjustmentTable = "CREATE TABLE IF NOT EXISTS InventoryAdjustment ("
                + "adjustmentID INTEGER NOT NULL ,"
                + "staffMemberID INTEGER NOT NULL ,"
                + "itemID INTEGER NOT NULL ,"
                + "quantityChanged INTEGER NOT NULL ,"
                + "reason TEXT NOT NULL ,"
                + "adjustmentTime DATETIME NOT NULL ,"
                + "PRIMARY KEY (adjustmentID) ,"
                + "FOREIGN KEY (staffMemberID) REFERENCES StaffMember(staffMemberID),"
                + "FOREIGN KEY (itemID) REFERENCES Item(itemID)"
                + ");";
        String systemLogTable = "CREATE TABLE IF NOT EXISTS SystemLog ("
                + " logID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " staffMemberID INTEGER,"
                + " action TEXT NOT NULL,"
                + " details TEXT,"
                + " timestamp DATETIME NOT NULL,"
                + " FOREIGN KEY (staffMemberID) REFERENCES StaffMember(staffMemberID)"
                + ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            // Create tables
            stmt.execute(staffMemberTable);
            stmt.execute(itemTable);
            stmt.execute(saleTable);
            stmt.execute(saleItemTable);
            stmt.execute(shiftTable);
            stmt.execute(inventoryAdjustmentTable);
            stmt.execute(systemLogTable);
            System.out.println("Tables created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void seedDatabase() {
        // SQL to check if the StaffMember table is already empty
        String checkSql = "SELECT COUNT(*) FROM StaffMember";
        String insertStaffSql = "INSERT INTO StaffMember(fullName, role, PIN) VALUES(?,?,?)";
        String insertItemSql = "INSERT INTO Item(itemName, barcode, sellingPrice, stockQuantity, expiryDate, itemSyncStatus) VALUES(?,?,?,?,?,?)";

        try (Connection conn = getConnection();
             Statement checkStmt = conn.createStatement();
             ResultSet rs = checkStmt.executeQuery(checkSql)) {

            // Only seed if the table is empty
            if (rs.next() && rs.getInt(1) == 0) {
                System.out.println("Database is empty. Seeding initial data...");

                // Seed a Store Manager
                try (PreparedStatement pstmt = conn.prepareStatement(insertStaffSql)) {
                    pstmt.setString(1, "Admin Manager");
                    pstmt.setString(2, "STORE_MANAGER");
                    pstmt.setString(3, "1234"); // In a real app, hash this
                    pstmt.executeUpdate();
                }

                // Seed a Cashier
                try (PreparedStatement pstmt = conn.prepareStatement(insertStaffSql)) {
                    pstmt.setString(1, "Test Cashier");
                    pstmt.setString(2, "CASHIER");
                    pstmt.setString(3, "5678");
                    pstmt.executeUpdate();
                }

                // Seed some items
                try (PreparedStatement pstmt = conn.prepareStatement(insertItemSql)) {
                    pstmt.setString(1, "Fresh Milk 1L");
                    pstmt.setString(2, "111222333");
                    pstmt.setDouble(3, 1500.0);
                    pstmt.setInt(4, 100);
                    pstmt.setString(5, "2026-10-20");
                    pstmt.setString(6, "synced");
                    pstmt.executeUpdate();
                }
                System.out.println("Seeding complete.");
            }
        } catch (SQLException e) {
            System.out.println("Seeding error: " + e.getMessage());
        }
    }
}
