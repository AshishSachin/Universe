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
        setSize(600, 800); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        AppStyles.styleDialog(this);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        AppStyles.styleTransparentPanel(mainPanel);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        
        gbc.insets = new Insets(0, 0, 15, 0); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel bannerLabel = new JLabel();
        try {
            
            ImageIcon bannerIcon = new ImageIcon(new ImageIcon(event.getBannerImagePath())
                    .getImage().getScaledInstance(570, 200, Image.SCALE_SMOOTH)); // 570 wide to fit
            bannerLabel.setIcon(bannerIcon);
            bannerLabel.setBorder(BorderFactory.createLineBorder(AppStyles.COLOR_PRIMARY, 1));
        } catch (Exception e) {
            
            bannerLabel.setText("Banner not available");
            AppStyles.styleLabel(bannerLabel);
            bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
            bannerLabel.setPreferredSize(new Dimension(570, 200));
            bannerLabel.setBorder(BorderFactory.createLineBorder(AppStyles.COLOR_PANEL, 1));
        }
        mainPanel.add(bannerLabel, gbc);

        
        gbc.gridy++; 
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        
        
        gbc.gridx = 0; 
        mainPanel.add(createLabel("Event Name:"), gbc);
        gbc.gridx = 1; mainPanel.add(createValueLabel(event.getName()), gbc);
        
        gbc.gridx = 0; gbc.gridy++; mainPanel.add(createLabel("Organized By:"), gbc);
        gbc.gridx = 1; mainPanel.add(createValueLabel(event.getOrganizerClub()), gbc);
        
        gbc.gridx = 0; gbc.gridy++; mainPanel.add(createLabel("Date & Time:"), gbc);
        gbc.gridx = 1; mainPanel.add(createValueLabel(event.getDate() + " at " + event.getTime()), gbc);
        
        gbc.gridx = 0; gbc.gridy++; mainPanel.add(createLabel("Venue:"), gbc);
        gbc.gridx = 1; mainPanel.add(createValueLabel(event.getVenue()), gbc);
        
        gbc.gridx = 0; gbc.gridy++; mainPanel.add(createLabel("Description:"), gbc);
        
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextArea descriptionArea = new JTextArea(event.getDescription());
        AppStyles.styleTextAreaAsLabel(descriptionArea);
        mainPanel.add(descriptionArea, gbc);

        // --- QR Code Display ---
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(AppStyles.COLOR_PANEL);
        separator.setBackground(AppStyles.COLOR_PANEL);
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(separator, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(8, 10, 8, 10);

        gbc.gridy++;
        JLabel qrPromptLabel = createLabel("Scan QR to Pay & Upload Screenshot to Book");
        qrPromptLabel.setFont(AppStyles.FONT_HEADING);
        qrPromptLabel.setForeground(AppStyles.COLOR_TEXT);
        mainPanel.add(qrPromptLabel, gbc);

        gbc.gridy++;
        ImageIcon qrIcon = new ImageIcon(new ImageIcon(event.getQrCodePath()).getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
        JLabel qrLabel = new JLabel(qrIcon);
        qrLabel.setBorder(BorderFactory.createLineBorder(AppStyles.COLOR_PRIMARY, 2));
        mainPanel.add(qrLabel, gbc);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        AppStyles.styleTransparentPanel(buttonPanel);
        
        JLabel screenshotLabel = new JLabel("No file selected.");
        AppStyles.styleLabel(screenshotLabel);
        
        JButton btnUploadScreenshot = new JButton("Upload Screenshot");
        AppStyles.styleButton(btnUploadScreenshot);
        
        JButton btnBookTicket = new JButton("Confirm Booking");
        AppStyles.styleButton(btnBookTicket);
        
        if (bookingDAO.hasUserBooked(userId, event.getId())) {
            btnBookTicket.setText("Already Booked");
            btnBookTicket.setEnabled(false);
            btnUploadScreenshot.setEnabled(false);
            btnBookTicket.setBackground(AppStyles.COLOR_INPUT_BG);
            btnUploadScreenshot.setBackground(AppStyles.COLOR_INPUT_BG);
        }

        buttonPanel.add(btnUploadScreenshot);
        buttonPanel.add(screenshotLabel);
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(AppStyles.COLOR_BACKGROUND);
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        southPanel.add(btnBookTicket, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.NORTH); 
        add(southPanel, BorderLayout.SOUTH);

        
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

        String savedPath = FileUtils.copyFileToProject(selectedScreenshot, "uploads/screenshots");
        if (savedPath == null) {
            JOptionPane.showMessageDialog(this, "Failed to save screenshot file.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        label.setFont(AppStyles.FONT_LABEL);
        label.setForeground(AppStyles.COLOR_PRIMARY); 
        return label;
    }
    private JLabel createValueLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppStyles.FONT_LABEL);
        label.setForeground(AppStyles.COLOR_TEXT); 
        return label;
    }
}