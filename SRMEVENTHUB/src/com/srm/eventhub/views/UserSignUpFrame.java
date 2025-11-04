package com.srm.eventhub.views;

import com.srm.eventhub.dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class UserSignUpFrame extends JFrame {

    private JTextField tfFullName, tfEmail, tfRegNo, tfDepartment;
    private JPasswordField pfPassword;
    private UserDAO userDAO;

    public UserSignUpFrame() {
        userDAO = new UserDAO();
        setTitle("User Sign Up");
        setSize(450, 450); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        
        AppStyles.styleFrame(this);

        JPanel panel = new JPanel(new GridBagLayout());
        AppStyles.styleTransparentPanel(panel);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Create User Account", SwingConstants.CENTER);
        AppStyles.styleTitle(titleLabel);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        
        
        JLabel lblFullName = new JLabel("Full Name:");
        AppStyles.styleLabel(lblFullName);
        panel.add(lblFullName, gbc);
        
        gbc.gridy++;
        JLabel lblEmail = new JLabel("SRM Email:");
        AppStyles.styleLabel(lblEmail);
        panel.add(lblEmail, gbc);

        gbc.gridy++;
        JLabel lblPassword = new JLabel("Password:");
        AppStyles.styleLabel(lblPassword);
        panel.add(lblPassword, gbc);
        
        gbc.gridy++;
        JLabel lblRegNo = new JLabel("Registration No (RA...):");
        AppStyles.styleLabel(lblRegNo);
        panel.add(lblRegNo, gbc);
        
        gbc.gridy++;
        JLabel lblDept = new JLabel("Department:");
        AppStyles.styleLabel(lblDept);
        panel.add(lblDept, gbc);

        
        gbc.gridx = 1;
        gbc.gridy = 1;
        tfFullName = new JTextField(20);
        AppStyles.styleTextField(tfFullName);
        panel.add(tfFullName, gbc);
        
        gbc.gridy++;
        tfEmail = new JTextField(20);
        AppStyles.styleTextField(tfEmail);
        panel.add(tfEmail, gbc);
        
        gbc.gridy++;
        pfPassword = new JPasswordField(20);
        AppStyles.stylePasswordField(pfPassword);
        panel.add(pfPassword, gbc);
        
        gbc.gridy++;
        tfRegNo = new JTextField(20);
        AppStyles.styleTextField(tfRegNo);
        panel.add(tfRegNo, gbc);
        
        gbc.gridy++;
        tfDepartment = new JTextField(20);
        AppStyles.styleTextField(tfDepartment);
        panel.add(tfDepartment, gbc);

        // --- Buttons ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        AppStyles.styleTransparentPanel(buttonPanel);
        
        JButton btnSignUp = new JButton("Sign Up");
        AppStyles.styleButton(btnSignUp);
        
        JButton btnLogin = new JButton("Back to Login");
        AppStyles.styleButton(btnLogin);
        
        buttonPanel.add(btnSignUp);
        buttonPanel.add(btnLogin);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnSignUp.addActionListener(e -> handleSignUp());
        btnLogin.addActionListener(e -> {
            new UserLoginFrame().setVisible(true);
            dispose();
        });
    }

    private void handleSignUp() {
        String fullName = tfFullName.getText();
        String email = tfEmail.getText();
        String password = new String(pfPassword.getPassword());
        String regNo = tfRegNo.getText();
        String department = tfDepartment.getText();

        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || regNo.isEmpty() || department.isEmpty()) {
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

        boolean success = userDAO.registerUser(fullName, email, password, regNo, department);
        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            new UserLoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Email or Registration No may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}