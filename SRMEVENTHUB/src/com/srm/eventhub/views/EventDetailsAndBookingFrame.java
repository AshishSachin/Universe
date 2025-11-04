package com.srm.eventhub.views;

import com.srm.eventhub.dao.BookingDAO;
import com.srm.eventhub.model.Event;
import com.srm.eventhub.util.FileUtils;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class EventDetailsAndBookingFrame extends JDialog {
    private final int userId;
    private final Event event;
    private BookingDAO bookingDAO;
    private File selectedScreenshot;

    public EventDetailsAndBookingFrame(int userId, Event event) {
        this.userId = userId;
        this.event = event;
        this.bookingDAO = new BookingDAO();

        setTitle("Event Details: " + event.getName());
        setSize(600, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // Main panel with GridBagLayout for alignment
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        mainPanel.add(createLabel("Event Name:"), gbc);
        gbc.gridx = 1; mainPanel.add(createValueLabel(event.getName()), gbc);
        gbc.gridx = 0; gbc.gridy++; mainPanel.add(createLabel("Organized By:"), gbc);
        gbc.gridx = 1; mainPanel.add(createValueLabel(event.getOrganizerClub()), gbc);
        gbc.gridx = 0; gbc.gridy++; mainPanel.add(createLabel("Date & Time:"), gbc);
        gbc.gridx = 1; mainPanel.add(createValueLabel(event.getDate() + " at " + event.getTime()), gbc);
        gbc.gridx = 0; gbc.gridy++; mainPanel.add(createLabel("Venue:"), gbc);
        gbc.gridx = 1; mainPanel.add(createValueLabel(event.getVenue()), gbc);
        gbc.gridx = 0; gbc.gridy++; mainPanel.add(createLabel("Description:"), gbc);
        
        // Description with word wrap
        gbc.gridx = 1;
        JTextArea descriptionArea = new JTextArea(event.getDescription());
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setOpaque(false);
        descriptionArea.setEditable(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 16));
        mainPanel.add(descriptionArea, gbc);

        // QR Code Display
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL), gbc);

        gbc.gridy++;
        JLabel qrPromptLabel = createLabel("Scan QR to Pay & Upload Screenshot to Book");
        qrPromptLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(qrPromptLabel, gbc);

        gbc.gridy++;
        ImageIcon qrIcon = new ImageIcon(new ImageIcon(event.getQrCodePath()).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
        mainPanel.add(new JLabel(qrIcon), gbc);

        // Buttons and Screenshot upload
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel screenshotLabel = new JLabel("No file selected.");
        JButton btnUploadScreenshot = new JButton("Upload Screenshot");
        JButton btnBookTicket = new JButton("Confirm Booking");
        
        // Check if user has already booked this event
        if (bookingDAO.hasUserBooked(userId, event.getId())) {
            btnBookTicket.setText("Already Booked");
            btnBookTicket.setEnabled(false);
            btnUploadScreenshot.setEnabled(false);
        }

        buttonPanel.add(btnUploadScreenshot);
        buttonPanel.add(screenshotLabel);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH); // Upload part at top
        add(btnBookTicket, BorderLayout.SOUTH);

        // Action Listeners
        btnUploadScreenshot.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedScreenshot = fileChooser.getSelectedFile();
                screenshotLabel.setText(selectedScreenshot.getName());
            }
        });

        btnBookTicket.addActionListener(e -> handleBooking());
    }

    private void handleBooking() {
        if (selectedScreenshot == null) {
            JOptionPane.showMessageDialog(this, "Please upload a payment screenshot.", "File Missing", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Copy the screenshot to the project's upload directory
        String savedPath = FileUtils.copyFileToProject(selectedScreenshot, "uploads/screenshots");
        if (savedPath == null) {
            JOptionPane.showMessageDialog(this, "Failed to save screenshot file.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create the booking record in the database
        boolean success = bookingDAO.createBooking(event.getId(), userId, savedPath);
        if (success) {
            JOptionPane.showMessageDialog(this, "Booking request sent! You will be notified upon approval.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create booking.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        return label;
    }
}
