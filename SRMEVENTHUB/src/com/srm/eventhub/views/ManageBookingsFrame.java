package com.srm.eventhub.views;

import com.srm.eventhub.dao.BookingDAO;
import com.srm.eventhub.model.Booking;
import com.srm.eventhub.model.Event;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManageBookingsFrame extends JDialog {
    private final Event event;
    private BookingDAO bookingDAO;
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private List<Booking> currentBookings;

    public ManageBookingsFrame(Event event) {
        this.event = event;
        this.bookingDAO = new BookingDAO();

        setTitle("Manage Bookings for: " + event.getName());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        String[] columnNames = {"Full Name", "Registration No", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        bookingsTable = new JTable(tableModel);
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(bookingsTable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnApprove = new JButton("Approve");
        JButton btnReject = new JButton("Reject");
        JButton btnRefresh = new JButton("Refresh");
        buttonPanel.add(btnApprove);
        buttonPanel.add(btnReject);
        buttonPanel.add(btnRefresh);
        
        add(new JLabel("Booking Requests for " + event.getName(), SwingConstants.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadBookings();

        btnApprove.addActionListener(e -> updateStatus("Approved"));
        btnReject.addActionListener(e -> updateStatus("Rejected"));
        btnRefresh.addActionListener(e -> loadBookings());
    }

    private void loadBookings() {
        tableModel.setRowCount(0);
        currentBookings = bookingDAO.getBookingsByEvent(event.getId());
        for (Booking booking : currentBookings) {
            Object[] row = {booking.getUserFullName(), booking.getUserRegNo(), booking.getStatus()};
            tableModel.addRow(row);
        }
    }

    private void updateStatus(String newStatus) {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a booking to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Booking selectedBooking = currentBookings.get(selectedRow);
        
        // Prevent updating already approved/rejected bookings if desired
        if (!selectedBooking.getStatus().equals("Pending")) {
            JOptionPane.showMessageDialog(this, "This booking has already been processed.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        boolean success = bookingDAO.updateBookingStatus(selectedBooking.getBookingId(), newStatus);
        if (success) {
            JOptionPane.showMessageDialog(this, "Booking status updated to '" + newStatus + "'.");
            loadBookings(); // Refresh the table
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update status.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}