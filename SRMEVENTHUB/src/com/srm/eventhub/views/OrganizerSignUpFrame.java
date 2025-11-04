package com.srm.eventhub.views;

import com.srm.eventhub.dao.OrganizerDAO;
import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class OrganizerSignUpFrame extends JFrame {
    private JTextField tfFullName, tfEmail, tfRegNo, tfDepartment, tfClubName;
    private JPasswordField pfPassword;
    private OrganizerDAO organizerDAO;

    public OrganizerSignUpFrame() {
        organizerDAO = new OrganizerDAO();
        setTitle("Organizer Sign Up");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create Organizer Account", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("SRM Email:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Password:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Registration No (RA...):"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Department:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Club Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        tfFullName = new JTextField(20);
        panel.add(tfFullName, gbc);
        gbc.gridy++;
        tfEmail = new JTextField(20);
        panel.add(tfEmail, gbc);
        gbc.gridy++;
        pfPassword = new JPasswordField(20);
        panel.add(pfPassword, gbc);
        gbc.gridy++;
        tfRegNo = new JTextField(20);
        panel.add(tfRegNo, gbc);
        gbc.gridy++;
        tfDepartment = new JTextField(20);
        panel.add(tfDepartment, gbc);
        gbc.gridy++;
        tfClubName = new JTextField(20);
        panel.add(tfClubName, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSignUp = new JButton("Sign Up");
        JButton btnLogin = new JButton("Back to Login");
        buttonPanel.add(btnSignUp);
        buttonPanel.add(btnLogin);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSignUp.addActionListener(e -> handleSignUp());
        btnLogin.addActionListener(e -> {
            new OrganizerLoginFrame().setVisible(true);
            dispose();
        });
    }

    private void handleSignUp() {
        String fullName = tfFullName.getText();
        String email = tfEmail.getText();
        String password = new String(pfPassword.getPassword());
        String regNo = tfRegNo.getText();
        String department = tfDepartment.getText();
        String clubName = tfClubName.getText();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || regNo.isEmpty() || department.isEmpty() || clubName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!email.toLowerCase().endsWith("@srmist.edu.in")) {
            JOptionPane.showMessageDialog(this, "Please use a valid SRMIST email.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!Pattern.matches("^RA\\d{13}$", regNo.toUpperCase())) {
            JOptionPane.showMessageDialog(this, "Invalid Registration Number format. Must be RA followed by 13 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = organizerDAO.registerOrganizer(fullName, email, password, regNo, department, clubName);
        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            new OrganizerLoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Email or Registration No may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}