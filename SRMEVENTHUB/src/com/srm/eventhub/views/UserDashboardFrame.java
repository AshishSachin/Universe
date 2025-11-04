package com.srm.eventhub.views;

import com.srm.eventhub.dao.EventDAO;
import com.srm.eventhub.model.Event;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer; 
import java.awt.*;
import java.awt.event.MouseAdapter; 
import java.awt.event.MouseEvent; 
import java.awt.event.MouseMotionAdapter; 
import java.util.List;

public class UserDashboardFrame extends JFrame {
    private final int userId;
    private EventDAO eventDAO;
    private JTable eventsTable;
    private DefaultTableModel tableModel;
    
    private int hoveredRow = -1; 

    public UserDashboardFrame(int userId) {
        this.userId = userId;
        this.eventDAO = new EventDAO();

        setTitle("User Dashboard - Upcoming Events");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        AppStyles.styleFrame(this);

       
        JLabel titleLabel = new JLabel("Upcoming Events", SwingConstants.CENTER);
        AppStyles.styleTitle(titleLabel);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        
        String[] columnNames = {"Event Name", "Club", "Date", "Time", "Venue"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        eventsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        
        AppStyles.styleTable(eventsTable, scrollPane);

        
        eventsTable.setDefaultRenderer(Object.class, new HoverCellRenderer());

       
        eventsTable.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = eventsTable.rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    hoveredRow = row;
                    eventsTable.repaint(); 
                }
            }
        });
        
        eventsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                eventsTable.repaint(); 
            }
            
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int selectedRow = eventsTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        try {
                            List<Event> events = eventDAO.getAllEvents(); 
                            Event selectedEvent = events.get(selectedRow);
                            new EventDetailsAndBookingFrame(userId, selectedEvent).setVisible(true);
                        } catch (IndexOutOfBoundsException ex) {
                            
                            System.err.println("Error getting selected event: " + ex.getMessage());
                        }
                    }
                }
            }
           
        });


        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        AppStyles.styleTransparentPanel(buttonPanel);
        
        
        
        JButton btnLogout = new JButton("Logout");
        AppStyles.styleButton(btnLogout);
        
        buttonPanel.add(btnLogout);

        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadEvents();

        
        
        btnLogout.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
    }

    private void loadEvents() {
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

    
    private class HoverCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isSelected) {
                c.setBackground(AppStyles.COLOR_PRIMARY);
                c.setForeground(AppStyles.COLOR_TEXT);
            } else if (row == hoveredRow) {
                c.setBackground(AppStyles.COLOR_PRIMARY_LIGHT.darker()); 
                c.setForeground(AppStyles.COLOR_TEXT);
            } else {
                c.setBackground(AppStyles.COLOR_PANEL);
                c.setForeground(AppStyles.COLOR_TEXT);
            }
            
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

            return c;
        }
    }
}