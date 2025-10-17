package com.example.securefiletransfer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends JPanel {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final String role;

    public LoginPanel(String role) {
        this.role = role;
        setLayout(new BorderLayout());
        setBackground(UITheme.BACKGROUND_COLOR);

        // --- Left Branding Panel ---
        String subtitle = "user".equals(role) ? "User Portal" : "Administrator Portal";
        add(UITheme.createBrandingPanel(subtitle), BorderLayout.WEST);

        // --- Right Form Panel ---
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setOpaque(false); // Make it transparent
        add(rightPanel, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.PANEL_BACKGROUND_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(221, 221, 221)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String title = "user".equals(role) ? "User Login" : "Administrator Login";
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UITheme.TITLE_FONT);
        titleLabel.setForeground(UITheme.HEADER_COLOR);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 20, 10); // More space below title
        formPanel.add(titleLabel, gbc);
        
        gbc.insets = new Insets(10, 10, 10, 10); // Reset insets
        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(UITheme.LABEL_FONT);
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        usernameField = new JTextField(20);
        usernameField.setFont(UITheme.LABEL_FONT);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.LINE_END;
        gbc.fill = GridBagConstraints.NONE;
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(UITheme.LABEL_FONT);
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        passwordField = new JPasswordField(20);
        passwordField.setFont(UITheme.LABEL_FONT);
        formPanel.add(passwordField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setOpaque(false);
        JButton loginButton = new JButton("Login");
        UITheme.styleButton(loginButton, UITheme.PRIMARY_COLOR, "");
        buttonPanel.add(loginButton);

        if ("user".equals(role)) {
            JButton registerButton = new JButton("Register");
            JButton adminLoginButton = new JButton("Admin Login");
            buttonPanel.add(registerButton);
            buttonPanel.add(adminLoginButton);
            registerButton.addActionListener(e -> SecureFileTransfer.showPanel("Register"));
            adminLoginButton.addActionListener(e -> SecureFileTransfer.showPanel("AdminLogin"));
        } else {
            JButton userLoginButton = new JButton("User Login");
            buttonPanel.add(userLoginButton);
            userLoginButton.addActionListener(e -> SecureFileTransfer.showPanel("UserLogin"));
        }

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 10, 10, 10); // More space above buttons
        formPanel.add(buttonPanel, gbc);
        
        rightPanel.add(formPanel, new GridBagConstraints()); // Add formPanel to the center of rightPanel
        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String sql = "SELECT id, role FROM users WHERE username = ? AND password = ? AND role = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, this.role);
            try(ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    SecureFileTransfer.loginUser(rs.getInt("id"), username);
                    if ("admin".equals(this.role)) SecureFileTransfer.showAdminPanel();
                    else SecureFileTransfer.showUserPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid credentials for this role.", "Login Failed", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}