package com.srm.eventhub.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.srm.eventhub.db.DBConnection;
import com.srm.eventhub.model.Event;

public class EventDAO {
    private final Connection con = DBConnection.getConnection();

    public boolean createEvent(int organizerId, String name, String description, String date, String time, String venue, String qrCodePath, String bannerImagePath) {
        String query = "INSERT INTO events (organizer_id, event_name, description, event_date, event_time, venue, qr_code_path, banner_image_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, organizerId);
            pst.setString(2, name);
            pst.setString(3, description);
            pst.setDate(4, Date.valueOf(date)); // Assuming date is in "YYYY-MM-DD" format
            pst.setTime(5, Time.valueOf(time)); // Assuming time is in "HH:MM:SS" format
            pst.setString(6, venue);
            pst.setString(7, qrCodePath);
            pst.setString(8, bannerImagePath); 
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- NEW: updateEvent METHOD ---
    public boolean updateEvent(int eventId, String name, String description, String date, String time, String venue, String qrCodePath, String bannerImagePath) {
        String query = "UPDATE events SET " +
                       "event_name = ?, " +
                       "description = ?, " +
                       "event_date = ?, " +
                       "event_time = ?, " +
                       "venue = ?, " +
                       "qr_code_path = ?, " +
                       "banner_image_path = ? " +
                       "WHERE event_id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, name);
            pst.setString(2, description);
            pst.setDate(3, Date.valueOf(date));
            pst.setTime(4, Time.valueOf(time));
            pst.setString(5, venue);
            pst.setString(6, qrCodePath);
            pst.setString(7, bannerImagePath);
            pst.setInt(8, eventId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- NEW: deleteEvent METHOD ---
    public boolean deleteEvent(int eventId) {
        // We should also delete associated bookings to maintain database integrity
        String deleteBookingsQuery = "DELETE FROM bookings WHERE event_id = ?";
        String deleteEventQuery = "DELETE FROM events WHERE event_id = ?";
        
        try (PreparedStatement pstBookings = con.prepareStatement(deleteBookingsQuery);
             PreparedStatement pstEvent = con.prepareStatement(deleteEventQuery)) {
            
            con.setAutoCommit(false); // Start transaction
            
            // Delete associated bookings first
            pstBookings.setInt(1, eventId);
            pstBookings.executeUpdate();
            
            // Delete the event
            pstEvent.setInt(1, eventId);
            int rowsAffected = pstEvent.executeUpdate();
            
            con.commit(); // Commit transaction
            con.setAutoCommit(true);
            
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                con.rollback(); // Rollback on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String query = "SELECT e.*, o.club_name FROM events e JOIN organizers o ON e.organizer_id = o.organizer_id";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                events.add(new Event(
                    rs.getInt("event_id"),
                    rs.getString("event_name"),
                    rs.getString("description"),
                    rs.getDate("event_date").toString(),
                    rs.getTime("event_time").toString(),
                    rs.getString("venue"),
                    rs.getString("qr_code_path"),
                    rs.getString("banner_image_path"),
                    rs.getString("club_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public List<Event> getEventsByOrganizer(int organizerId) {
        List<Event> events = new ArrayList<>();
        String query = "SELECT e.*, o.club_name FROM events e JOIN organizers o ON e.organizer_id = o.organizer_id WHERE e.organizer_id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, organizerId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                     events.add(new Event(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("description"),
                        rs.getDate("event_date").toString(),
                        rs.getTime("event_time").toString(),
                        rs.getString("venue"),
                        rs.getString("qr_code_path"),
                        rs.getString("banner_image_path"),
                        rs.getString("club_name")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }
}