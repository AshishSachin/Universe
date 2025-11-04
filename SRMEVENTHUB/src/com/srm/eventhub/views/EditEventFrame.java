package com.srm.eventhub.views;

import com.srm.eventhub.dao.EventDAO;
import com.srm.eventhub.model.Event;
import com.srm.eventhub.util.FileUtils;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class EditEventFrame extends JDialog {
    private final Event event; 
    private final OrganizerDashboardFrame parentDashboard;
    private JTextField tfName, tfDate, tfTime, tfVenue;
    private JTextArea taDescription;
    private JLabel lblQrCodePath, lblBannerPath;
    private File selectedQrCodeFile, selectedBannerFile;
    private String originalQrPath, originalBannerPath;
    private EventDAO eventDAO;

    public EditEventFrame(Event event, OrganizerDashboardFrame parent) {
        this.event = event;
        this.parentDashboard = parent;
        this.eventDAO = new EventDAO();
        this.originalQrPath = event.getQrCodePath();
        this.originalBannerPath = event.getBannerImagePath();

        setTitle("Edit Event: " + event.getName());
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

        gbc.gridy++;
        JLabel lblBanner = new JLabel("Event Banner:");
        AppStyles.styleLabel(lblBanner);
        formPanel.add(lblBanner, gbc);

        // --- Fields (Pre-filled with event data) ---
        gbc.gridx = 1; gbc.gridy = 0;
        tfName = new JTextField(event.getName(), 20);
        AppStyles.styleTextField(tfName);
        formPanel.add(tfName, gbc);

        gbc.gridy++;
        tfDate = new JTextField(event.getDate(), 20);
        AppStyles.styleTextField(tfDate);
        formPanel.add(tfDate, gbc);
        
        gbc.gridy++;
        tfTime = new JTextField(event.getTime(), 20);
        AppStyles.styleTextField(tfTime);
        formPanel.add(tfTime, gbc);

        gbc.gridy++;
        tfVenue = new JTextField(event.getVenue(), 20);
        AppStyles.styleTextField(tfVenue);
        formPanel.add(tfVenue, gbc);

        gbc.gridy++;
        taDescription = new JTextArea(event.getDescription(), 5, 20);
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

        
        gbc.gridy++;
        JPanel qrPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        AppStyles.styleTransparentPanel(qrPanel);
        JButton btnUploadQr = new JButton("Upload New");
        AppStyles.styleButton(btnUploadQr);
        lblQrCodePath = new JLabel(new File(originalQrPath).getName()); 
        AppStyles.styleLabel(lblQrCodePath);
        qrPanel.add(btnUploadQr);
        qrPanel.add(lblQrCodePath);
        formPanel.add(qrPanel, gbc);
        
        
        gbc.gridy++;
        JPanel bannerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        AppStyles.styleTransparentPanel(bannerPanel);
        JButton btnUploadBanner = new JButton("Upload New");
        AppStyles.styleButton(btnUploadBanner);
        lblBannerPath = new JLabel(new File(originalBannerPath).getName()); 
        AppStyles.styleLabel(lblBannerPath);
        bannerPanel.add(btnUploadBanner);
        bannerPanel.add(lblBannerPath);
        formPanel.add(bannerPanel, gbc);
        
        
        JButton btnSaveChanges = new JButton("Save Changes");
        AppStyles.styleButton(btnSaveChanges);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(AppStyles.COLOR_BACKGROUND);
        southPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        southPanel.add(btnSaveChanges, BorderLayout.CENTER);

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
        
        btnSaveChanges.addActionListener(e -> updateEvent());
    }

    private void updateEvent() {
        String name = tfName.getText();
        String date = tfDate.getText();
        String time = tfTime.getText();
        String venue = tfVenue.getText();
        String description = taDescription.getText();

        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || venue.isEmpty() || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        
        String qrCodePath = originalQrPath;
        if (selectedQrCodeFile != null) {
            qrCodePath = FileUtils.copyFileToProject(selectedQrCodeFile, "uploads/qrcodes");
            if (qrCodePath == null) {
                JOptionPane.showMessageDialog(this, "Failed to save new QR Code file.", "File Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        String bannerPath = originalBannerPath;
        if (selectedBannerFile != null) {
            bannerPath = FileUtils.copyFileToProject(selectedBannerFile, "uploads/banners");
            if (bannerPath == null) {
                JOptionPane.showMessageDialog(this, "Failed to save new Banner file.", "File Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        
        boolean success = eventDAO.updateEvent(event.getId(), name, description, date, time, venue, qrCodePath, bannerPath);
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Event updated successfully!");
            parentDashboard.loadEvents(); 
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update event.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}