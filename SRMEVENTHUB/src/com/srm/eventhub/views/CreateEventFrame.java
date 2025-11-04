package com.srm.eventhub.views;

import com.srm.eventhub.dao.EventDAO;
import com.srm.eventhub.util.FileUtils;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class CreateEventFrame extends JDialog {
    private final int organizerId;
    private final OrganizerDashboardFrame parentDashboard;
    private JTextField tfName, tfDate, tfTime, tfVenue;
    private JTextArea taDescription;
    private JLabel lblQrCodePath;
    private File selectedQrCodeFile;
    private EventDAO eventDAO;

    public CreateEventFrame(int organizerId, OrganizerDashboardFrame parent) {
        this.organizerId = organizerId;
        this.parentDashboard = parent;
        this.eventDAO = new EventDAO();

        setTitle("Create New Event");
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(new JLabel("Event Name:"), gbc);
        gbc.gridx = 1; tfName = new JTextField(20); formPanel.add(tfName, gbc);

        gbc.gridx = 0; gbc.gridy++; formPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1; tfDate = new JTextField(20); formPanel.add(tfDate, gbc);
        
        gbc.gridx = 0; gbc.gridy++; formPanel.add(new JLabel("Time (HH:MM:SS):"), gbc);
        gbc.gridx = 1; tfTime = new JTextField(20); formPanel.add(tfTime, gbc);

        gbc.gridx = 0; gbc.gridy++; formPanel.add(new JLabel("Venue:"), gbc);
        gbc.gridx = 1; tfVenue = new JTextField(20); formPanel.add(tfVenue, gbc);

        gbc.gridx = 0; gbc.gridy++; formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1; taDescription = new JTextArea(5, 20);
        formPanel.add(new JScrollPane(taDescription), gbc);

        gbc.gridx = 0; gbc.gridy++; formPanel.add(new JLabel("UPI QR Code:"), gbc);
        gbc.gridx = 1;
        JPanel qrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnUploadQr = new JButton("Upload Image");
        lblQrCodePath = new JLabel("No file selected.");
        qrPanel.add(btnUploadQr);
        qrPanel.add(lblQrCodePath);
        formPanel.add(qrPanel, gbc);
        
        JButton btnCreate = new JButton("Create Event");

        add(formPanel, BorderLayout.CENTER);
        add(btnCreate, BorderLayout.SOUTH);

        btnUploadQr.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedQrCodeFile = fileChooser.getSelectedFile();
                lblQrCodePath.setText(selectedQrCodeFile.getName());
            }
        });
        
        btnCreate.addActionListener(e -> createEvent());
    }

    private void createEvent() {
        String name = tfName.getText();
        String date = tfDate.getText();
        String time = tfTime.getText();
        String venue = tfVenue.getText();
        String description = taDescription.getText();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || venue.isEmpty() || description.isEmpty() || selectedQrCodeFile == null) {
            JOptionPane.showMessageDialog(this, "All fields and a QR code are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String qrCodePath = FileUtils.copyFileToProject(selectedQrCodeFile, "uploads/qrcodes");
        if (qrCodePath == null) {
             JOptionPane.showMessageDialog(this, "Failed to save QR Code file.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = eventDAO.createEvent(organizerId, name, description, date, time, venue, qrCodePath);
        if (success) {
            JOptionPane.showMessageDialog(this, "Event created successfully!");
            parentDashboard.loadEvents(); // Refresh the dashboard
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create event.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}