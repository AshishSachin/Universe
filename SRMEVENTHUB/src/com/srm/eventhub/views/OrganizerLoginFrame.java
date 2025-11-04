package com.srm.eventhub.views;

import javax.swing.*;
import com.srm.eventhub.dao.OrganizerDAO;
import java.awt.*;

public class OrganizerLoginFrame extends JFrame {
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private OrganizerDAO organizerDAO;

    public OrganizerLoginFrame() {
        organizerDAO = new OrganizerDAO();
        setTitle("Organizer Login");
        setSize(450, 350); // Adjusted size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        
        AppStyles.styleFrame(this);

        
        JPanel panel = new JPanel(new GridBagLayout());
        AppStyles.styleTransparentPanel(panel); 
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Organizer Portal Login", SwingConstants.CENTER);
        AppStyles.styleTitle(titleLabel); 
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel emailLabel = new JLabel("SRM Email:");
        AppStyles.styleLabel(emailLabel);
        panel.add(emailLabel, gbc);
        
        gbc.gridx = 1;
        tfEmail = new JTextField(20);
        AppStyles.styleTextField(tfEmail);
        panel.add(tfEmail, gbc);
        
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel passwordLabel = new JLabel("Password:");
        AppStyles.styleLabel(passwordLabel);
        panel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        pfPassword = new JPasswordField(20);
        AppStyles.stylePasswordField(pfPassword);
        panel.add(pfPassword, gbc);

        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        AppStyles.styleTransparentPanel(buttonPanel);
        
        JButton btnLogin = new JButton("Login");
        AppStyles.styleButton(btnLogin);
        
        JButton btnSignUp = new JButton("Create Account");
        AppStyles.styleButton(btnSignUp);
        
        JButton btnBack = new JButton("Back to Welcome");
        AppStyles.styleButton(btnBack);
        
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnSignUp);
        buttonPanel.add(btnBack);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> handleLogin());
        btnSignUp.addActionListener(e -> {
            new OrganizerSignUpFrame().setVisible(true);
            dispose();
        });
        btnBack.addActionListener(e -> {
            new WelcomeFrame().setVisible(true);
            dispose();
        });
    }

    private void handleLogin() {
        String email = tfEmail.getText();
        String password = new String(pfPassword.getPassword());
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer organizerId = organizerDAO.validateOrganizer(email, password);
        if (organizerId != null) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new OrganizerDashboardFrame(organizerId).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}