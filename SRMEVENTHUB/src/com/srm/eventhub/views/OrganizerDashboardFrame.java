package com.srm.eventhub.views;

import com.srm.eventhub.dao.EventDAO;
import com.srm.eventhub.model.Event;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrganizerDashboardFrame extends JFrame {
    private final int organizerId;
    private EventDAO eventDAO;
    private JTable eventsTable;
    private DefaultTableModel tableModel;

    public OrganizerDashboardFrame(int organizerId) {
        this.organizerId = organizerId;
        this.eventDAO = new EventDAO();

        setTitle("Organizer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("My Events", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        String[] columnNames = {"Event Name", "Date", "Time", "Venue"};
        tableModel = new DefaultTableModel(columnNames, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        eventsTable = new JTable(tableModel);
        eventsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(eventsTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnCreateEvent = new JButton("Create New Event");
        JButton btnManageBookings = new JButton("Manage Bookings");
        JButton btnRefresh = new JButton("Refresh List");
        JButton btnLogout = new JButton("Logout");
        buttonPanel.add(btnCreateEvent);
        buttonPanel.add(btnManageBookings);
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnLogout);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEvents();

        btnCreateEvent.addActionListener(e -> new CreateEventFrame(organizerId, this).setVisible(true));
        
        btnManageBookings.addActionListener(e -> {
            int selectedRow = eventsTable.getSelectedRow();
            if (selectedRow >= 0) {
                 List<Event> events = eventDAO.getEventsByOrganizer(organizerId);
                 Event selectedEvent = events.get(selectedRow);
                 new ManageBookingsFrame(selectedEvent).setVisible(true);
            } else {
                 JOptionPane.showMessageDialog(this, "Please select an event to manage its bookings.", "No Event Selected", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnLogout.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
        
        btnRefresh.addActionListener(e -> loadEvents());
    }

    public void loadEvents() {
        tableModel.setRowCount(0);
        List<Event> events = eventDAO.getEventsByOrganizer(organizerId);
        for (Event event : events) {
            Object[] row = {event.getName(), event.getDate(), event.getTime(), event.getVenue()};
            tableModel.addRow(row);
        }
    }
}