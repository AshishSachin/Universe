package com.srm.eventhub.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.srm.eventhub.db.DBConnection;

public class OrganizerDAO {
    private final Connection con = DBConnection.getConnection();

    public boolean registerOrganizer(String fullName, String email, String password, String regNo, String department, String clubName) {
        String query = "INSERT INTO organizers (full_name, email, password, reg_no, department, club_name) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, fullName);
            pst.setString(2, email);
            pst.setString(3, password); // Hash passwords in production!
            pst.setString(4, regNo);
            pst.setString(5, department);
            pst.setString(6, clubName);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer validateOrganizer(String email, String password) {
        String query = "SELECT organizer_id FROM organizers WHERE email = ? AND password = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("organizer_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}