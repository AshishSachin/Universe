package com.srm.eventhub.views;

import javax.swing.*;
import com.srm.eventhub.dao.UserDAO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserLoginFrame extends JFrame {

    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private UserDAO userDAO;

    public UserLoginFrame() {
        userDAO = new UserDAO();
        setTitle("User Login");
        setSize(450, 350); 
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
        
        JLabel titleLabel = new JLabel("User Portal Login", SwingConstants.CENTER);
        AppStyles.styleTitle(titleLabel); 
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
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

        // Action Listeners
        btnLogin.addActionListener(e -> handleLogin());

        btnSignUp.addActionListener(e -> {
            new UserSignUpFrame().setVisible(true);
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
            JOptionPane.showMessageDialog(this, "Email and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Integer userId = userDAO.validateUser(email, password);

        if (userId != null) {
            JOptionPane.showMessageDialog(this, "Login Successful!");
            new UserDashboardFrame(userId).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}