/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author Admin
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {
  private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=NangInvest;encrypt=true;trustServerCertificate=true";
  private static final String DB_USER = "sa";
  private static final String DB_PASSWORD = "Hieu0935828519";

  private DatabaseUtil() {
    // Private constructor to prevent instantiation
  }
  
  static {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("SQL Server JDBC Driver loaded");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load SQL Server JDBC Driver: " + e.getMessage());
        }
    }

  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
  }
}