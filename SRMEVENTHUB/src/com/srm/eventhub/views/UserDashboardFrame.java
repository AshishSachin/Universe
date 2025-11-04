package com.srm.eventhub.views;

import com.srm.eventhub.dao.EventDAO;
import com.srm.eventhub.model.Event;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserDashboardFrame extends JFrame {
    private final int userId;
    private EventDAO eventDAO;
    private JTable eventsTable;
    private DefaultTableModel tableModel;

    public UserDashboardFrame(int userId) {
        this.userId = userId;
        this.eventDAO = new EventDAO();

        setTitle("User Dashboard - Upcoming Events");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header
        JLabel titleLabel = new JLabel("Upcoming Events", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Table setup
        String[] columnNames = {"Event Name", "Club", "Date", "Time", "Venue"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        eventsTable = new JTable(tableModel);
        eventsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eventsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        eventsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(eventsTable);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnViewDetails = new JButton("View Details & Book Ticket");
        JButton btnLogout = new JButton("Logout");
        buttonPanel.add(btnViewDetails);
        buttonPanel.add(btnLogout);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Populate table with events
        loadEvents();

        // Action Listeners
        btnViewDetails.addActionListener(e -> {
            int selectedRow = eventsTable.getSelectedRow();
            if (selectedRow >= 0) {
                // We need to get the original Event object, not just the table data
                List<Event> events = eventDAO.getAllEvents();
                Event selectedEvent = events.get(selectedRow);
                new EventDetailsAndBookingFrame(userId, selectedEvent).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select an event from the list.", "No Event Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnLogout.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
    }

    private void loadEvents() {
        // Clear existing rows
        tableModel.setRowCount(0);
        List<Event> events = eventDAO.getAllEvents();
        for (Event event : events) {
            Object[] row = {
                event.getName(),
                event.getOrganizerClub(),
                event.getDate(),
                event.getTime(),
                event.getVenue()
            };
            tableModel.addRow(row);
        }
    }
}
