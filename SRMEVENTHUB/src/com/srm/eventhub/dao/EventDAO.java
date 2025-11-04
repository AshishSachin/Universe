package com.srm.eventhub.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.srm.eventhub.db.DBConnection;
import com.srm.eventhub.model.Event;

public class EventDAO {
    private final Connection con = DBConnection.getConnection();

    public boolean createEvent(int organizerId, String name, String description, String date, String time, String venue, String qrCodePath) {
        String query = "INSERT INTO events (organizer_id, event_name, description, event_date, event_time, venue, qr_code_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, organizerId);
            pst.setString(2, name);
            pst.setString(3, description);
            pst.setDate(4, Date.valueOf(date)); // Assuming date is in "YYYY-MM-DD" format
            pst.setTime(5, Time.valueOf(time)); // Assuming time is in "HH:MM:SS" format
            pst.setString(6, venue);
            pst.setString(7, qrCodePath);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        // JOIN query to get the club name from the organizers table
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