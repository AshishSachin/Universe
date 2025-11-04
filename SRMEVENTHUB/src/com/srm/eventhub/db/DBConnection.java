package com.srm.eventhub.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnection {
    
    private static final String URL = "jdbc:mysql://localhost:3306/srm_event_db";
    private static final String USER = "root"; 
    private static final String PASSWORD = "2006Sachin"; 
    private static Connection connection = null;

   
    private DBConnection() {}

   
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                try {
                    
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                } catch (ClassNotFoundException e) {
                    System.err.println("MySQL JDBC Driver not found.");
                    JOptionPane.showMessageDialog(null, "Database driver not found. Please add the MySQL Connector JAR to the project's libraries.", "Driver Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    System.exit(1);
                } catch (SQLException e) {
                    System.err.println("Failed to connect to the database.");
                    JOptionPane.showMessageDialog(null, "Could not connect to the database. Please check your connection settings and ensure MySQL is running.", "Connection Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}