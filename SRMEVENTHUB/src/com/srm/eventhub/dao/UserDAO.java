package com.srm.eventhub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.srm.eventhub.db.DBConnection;

public class UserDAO {
    private final Connection con = DBConnection.getConnection();

    public boolean registerUser(String fullName, String email, String password, String regNo, String department) {
        String query = "INSERT INTO users (full_name, email, password, reg_no, department) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, fullName);
            pst.setString(2, email);
            pst.setString(3, password); // In a real app, HASH the password before storing!
            pst.setString(4, regNo);
            pst.setString(5, department);
            int rowsAffected = pst.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Unique constraint violation (e.g., email or reg_no already exists)
            e.printStackTrace();
            return false;
        }
    }

    public Integer validateUser(String email, String password) {
        String query = "SELECT user_id FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
