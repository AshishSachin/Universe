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
    private JLabel lblBannerPath; 
    private File selectedBannerFile; 
    private EventDAO eventDAO;

    public CreateEventFrame(int organizerId, OrganizerDashboardFrame parent) {
        this.organizerId = organizerId;
        this.parentDashboard = parent;
        this.eventDAO = new EventDAO();

        setTitle("Create New Event");
        setSize(500, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        
        AppStyles.styleDialog(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        AppStyles.styleTransparentPanel(formPanel);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Labels ---
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblName = new JLabel("Event Name:");
        AppStyles.styleLabel(lblName);
        formPanel.add(lblName, gbc);

        gbc.gridy++;
        JLabel lblDate = new JLabel("Date (YYYY-MM-DD):");
        AppStyles.styleLabel(lblDate);
        formPanel.add(lblDate, gbc);
        
        gbc.gridy++;
        JLabel lblTime = new JLabel("Time (HH:MM:SS):");
        AppStyles.styleLabel(lblTime);
        formPanel.add(lblTime, gbc);

        gbc.gridy++;
        JLabel lblVenue = new JLabel("Venue:");
        AppStyles.styleLabel(lblVenue);
        formPanel.add(lblVenue, gbc);

        gbc.gridy++;
        JLabel lblDesc = new JLabel("Description:");
        AppStyles.styleLabel(lblDesc);
        formPanel.add(lblDesc, gbc);
        
        gbc.gridy++;
        JLabel lblQR = new JLabel("UPI QR Code:");
        AppStyles.styleLabel(lblQR);
        formPanel.add(lblQR, gbc);

        gbc.gridy++; // <-- NEW
        JLabel lblBanner = new JLabel("Event Banner:");
        AppStyles.styleLabel(lblBanner);
        formPanel.add(lblBanner, gbc);

        // --- Fields ---
        gbc.gridx = 1; gbc.gridy = 0;
        tfName = new JTextField(20);
        AppStyles.styleTextField(tfName);
        formPanel.add(tfName, gbc);

        gbc.gridy++;
        tfDate = new JTextField(20);
        AppStyles.styleTextField(tfDate);
        formPanel.add(tfDate, gbc);
        
        gbc.gridy++;
        tfTime = new JTextField(20);
        AppStyles.styleTextField(tfTime);
        formPanel.add(tfTime, gbc);

        gbc.gridy++;
        tfVenue = new JTextField(20);
        AppStyles.styleTextField(tfVenue);
        formPanel.add(tfVenue, gbc);

        gbc.gridy++;
        taDescription = new JTextArea(5, 20);
        taDescription.setBackground(AppStyles.COLOR_INPUT_BG);
        taDescription.setForeground(AppStyles.COLOR_TEXT);
        taDescription.setCaretColor(AppStyles.COLOR_PRIMARY);
        taDescription.setSelectionColor(AppStyles.COLOR_PRIMARY);
        taDescription.setSelectedTextColor(AppStyles.COLOR_TEXT);
        taDescription.setBorder(AppStyles.BORDER_TEXTFIELD_PADDING);
        taDescription.setFont(AppStyles.FONT_LABEL);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        
        JScrollPane descScrollPane = new JScrollPane(taDescription);
        descScrollPane.setBorder(BorderFactory.createEmptyBorder());
        formPanel.add(descScrollPane, gbc);

        // --- QR Upload ---
        gbc.gridy++;
        JPanel qrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        AppStyles.styleTransparentPanel(qrPanel);
        JButton btnUploadQr = new JButton("Upload Image");
        AppStyles.styleButton(btnUploadQr);
        lblQrCodePath = new JLabel("No file selected.");
        AppStyles.styleLabel(lblQrCodePath);
        qrPanel.add(btnUploadQr);
        qrPanel.add(lblQrCodePath);
        formPanel.add(qrPanel, gbc);
        
        
        gbc.gridy++;
        JPanel bannerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        AppStyles.styleTransparentPanel(bannerPanel);
        JButton btnUploadBanner = new JButton("Upload Image");
        AppStyles.styleButton(btnUploadBanner);
        lblBannerPath = new JLabel("No file selected.");
        AppStyles.styleLabel(lblBannerPath);
        bannerPanel.add(btnUploadBanner);
        bannerPanel.add(lblBannerPath);
        formPanel.add(bannerPanel, gbc);
        
        
        JButton btnCreate = new JButton("Create Event");
        AppStyles.styleButton(btnCreate);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(AppStyles.COLOR_BACKGROUND);
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        southPanel.add(btnCreate, BorderLayout.CENTER);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        btnUploadQr.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedQrCodeFile = fileChooser.getSelectedFile();
                lblQrCodePath.setText(selectedQrCodeFile.getName());
            }
        });
        
        
        btnUploadBanner.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedBannerFile = fileChooser.getSelectedFile();
                lblBannerPath.setText(selectedBannerFile.getName());
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

        
        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || venue.isEmpty() || description.isEmpty() || selectedQrCodeFile == null || selectedBannerFile == null) {
            JOptionPane.showMessageDialog(this, "All fields, a QR code, and a banner image are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        String qrCodePath = FileUtils.copyFileToProject(selectedQrCodeFile, "uploads/qrcodes");
        String bannerPath = FileUtils.copyFileToProject(selectedBannerFile, "uploads/banners"); // <-- NEW
        
        if (qrCodePath == null || bannerPath == null) {
             JOptionPane.showMessageDialog(this, "Failed to save image files.", "File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        boolean success = eventDAO.createEvent(organizerId, name, description, date, time, venue, qrCodePath, bannerPath);
        if (success) {
            JOptionPane.showMessageDialog(this, "Event created successfully!");
            parentDashboard.loadEvents(); 
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create event.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}