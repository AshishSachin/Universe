package com.srm.eventhub.dao;

import com.srm.eventhub.db.DBConnection;
import com.srm.eventhub.model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private final Connection con = DBConnection.getConnection();

    public boolean createBooking(int eventId, int userId, String screenshotPath) {
        String query = "INSERT INTO bookings (event_id, user_id, screenshot_path, status) VALUES (?, ?, ?, 'Pending')";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, eventId);
            pst.setInt(2, userId);
            pst.setString(3, screenshotPath);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Booking> getBookingsByEvent(int eventId) {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT b.booking_id, b.event_id, b.user_id, u.full_name, u.reg_no, b.status " +
                       "FROM bookings b JOIN users u ON b.user_id = u.user_id WHERE b.event_id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, eventId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    bookings.add(new Booking(
                        rs.getInt("booking_id"),
                        rs.getInt("event_id"),
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("reg_no"),
                        rs.getString("status")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public boolean updateBookingStatus(int bookingId, String status) {
        String query = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, status);
            pst.setInt(2, bookingId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

     public boolean hasUserBooked(int userId, int eventId) {
        String query = "SELECT 1 FROM bookings WHERE user_id = ? AND event_id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);
            pst.setInt(2, eventId);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // Returns true if a record is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}