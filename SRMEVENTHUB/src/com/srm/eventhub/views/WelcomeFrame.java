package com.srm.eventhub.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class WelcomeFrame extends JFrame {

    public WelcomeFrame() {
        setTitle("Welcome to SRM Event Hub");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 51, 102));
        JLabel titleLabel = new JLabel("SRM Event Hub");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Body Panel
        JPanel bodyPanel = new JPanel();
        bodyPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel promptLabel = new JLabel("Please select your portal:");
        promptLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        
        JButton userPortalButton = new JButton("General User Portal");
        styleButton(userPortalButton);
        JButton organizerPortalButton = new JButton("Event Organizer Portal");
        styleButton(organizerPortalButton);
        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        bodyPanel.add(promptLabel, gbc);
        
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        bodyPanel.add(userPortalButton, gbc);
        
        gbc.gridx = 1;
        bodyPanel.add(organizerPortalButton, gbc);

        // Add panels to frame
        add(headerPanel, BorderLayout.NORTH);
        add(bodyPanel, BorderLayout.CENTER);

        // Action Listeners
        userPortalButton.addActionListener(e -> {
            new UserLoginFrame().setVisible(true);
            dispose();
        });

        organizerPortalButton.addActionListener(e -> {
            new OrganizerLoginFrame().setVisible(true);
            dispose();
        });
    }
    
    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
