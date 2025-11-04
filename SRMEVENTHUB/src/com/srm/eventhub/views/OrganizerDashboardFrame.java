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
    private List<Event> currentEvents; // <-- NEW: Store the loaded events

    public OrganizerDashboardFrame(int organizerId) {
        this.organizerId = organizerId;
        this.eventDAO = new EventDAO();

        setTitle("Organizer Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        AppStyles.styleFrame(this);

        JLabel titleLabel = new JLabel("My Events", SwingConstants.CENTER);
        AppStyles.styleTitle(titleLabel);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        String[] columnNames = {"Event Name", "Date", "Time", "Venue"};
        tableModel = new DefaultTableModel(columnNames, 0) {
             @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        eventsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        
        AppStyles.styleTable(eventsTable, scrollPane);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        AppStyles.styleTransparentPanel(buttonPanel);
        
        JButton btnCreateEvent = new JButton("Create New Event");
        AppStyles.styleButton(btnCreateEvent);
        
        JButton btnManageBookings = new JButton("Manage Bookings");
        AppStyles.styleButton(btnManageBookings);
        
        // --- NEW BUTTONS ---
        JButton btnEditEvent = new JButton("Edit Selected");
        AppStyles.styleButton(btnEditEvent);
        
        JButton btnDeleteEvent = new JButton("Delete Selected");
        AppStyles.styleButton(btnDeleteEvent);
        btnDeleteEvent.setBackground(AppStyles.COLOR_ERROR); // Make delete red
        btnDeleteEvent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDeleteEvent.setBackground(AppStyles.COLOR_ERROR.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDeleteEvent.setBackground(AppStyles.COLOR_ERROR);
            }
        });
        
        JButton btnRefresh = new JButton("Refresh");
        AppStyles.styleButton(btnRefresh);
        
        JButton btnLogout = new JButton("Logout");
        AppStyles.styleButton(btnLogout);
        
        buttonPanel.add(btnCreateEvent);
        buttonPanel.add(btnManageBookings);
        buttonPanel.add(btnEditEvent); // <-- ADDED
        buttonPanel.add(btnDeleteEvent); // <-- ADDED
        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnLogout);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEvents();

        // --- ACTION LISTENERS FOR NEW BUTTONS ---
        
        btnCreateEvent.addActionListener(e -> new CreateEventFrame(organizerId, this).setVisible(true));
        
        btnEditEvent.addActionListener(e -> {
            int selectedRow = eventsTable.getSelectedRow();
            if (selectedRow >= 0) {
                 Event selectedEvent = currentEvents.get(selectedRow); // Get from our stored list
                 new EditEventFrame(selectedEvent, this).setVisible(true);
            } else {
                 JOptionPane.showMessageDialog(this, "Please select an event to edit.", "No Event Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnDeleteEvent.addActionListener(e -> {
            int selectedRow = eventsTable.getSelectedRow();
            if (selectedRow >= 0) {
                Event selectedEvent = currentEvents.get(selectedRow);
                int choice = JOptionPane.showConfirmDialog(
                    this, 
                    "Are you sure you want to delete the event: '" + selectedEvent.getName() + "'?\nThis will also delete all associated bookings.",
                    "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION, 
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (choice == JOptionPane.YES_OPTION) {
                    boolean success = eventDAO.deleteEvent(selectedEvent.getId());
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Event deleted successfully.");
                        loadEvents(); // Refresh list
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete event.", "Database Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                 JOptionPane.showMessageDialog(this, "Please select an event to delete.", "No Event Selected", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        btnManageBookings.addActionListener(e -> {
            int selectedRow = eventsTable.getSelectedRow();
            if (selectedRow >= 0) {
                 Event selectedEvent = currentEvents.get(selectedRow); // Get from our stored list
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
        currentEvents = eventDAO.getEventsByOrganizer(organizerId); // <-- Store events
        for (Event event : currentEvents) {
            Object[] row = {event.getName(), event.getDate(), event.getTime(), event.getVenue()};
            tableModel.addRow(row);
        }
    }
}