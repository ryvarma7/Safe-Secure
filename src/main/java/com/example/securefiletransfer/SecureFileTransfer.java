package com.example.securefiletransfer;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;

public class SecureFileTransfer {

    private static JFrame frame;
    private static CardLayout cardLayout;
    private static JPanel mainPanel;
    private static int userId = -1;
    private static String username = "";

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            System.err.println("Failed to initialize FlatLaf.");
        }

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("SafeShare - Secure File Transfer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setMinimumSize(new Dimension(850, 700));

            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);
            mainPanel.setBackground(UITheme.BACKGROUND_COLOR);

            mainPanel.add(new LoginPanel("user"), "UserLogin");
            mainPanel.add(new LoginPanel("admin"), "AdminLogin");
            mainPanel.add(new RegisterPanel(), "Register");
            
            frame.add(mainPanel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            showPanel("UserLogin");
        });
    }
    
    public static void showPanel(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
    
    public static void loginUser(int id, String name) {
        userId = id;
        username = name;
    }

    public static void logoutUser() {
        userId = -1;
        username = "";
        for(Component c : mainPanel.getComponents()){
            if(c instanceof AdminPanel || c instanceof UserPanel){
                mainPanel.remove(c);
            }
        }
        showPanel("UserLogin");
    }
    
    public static int getUserId() { return userId; }
    public static String getUsername() { return username; }

    public static void showAdminPanel() {
        mainPanel.add(new AdminPanel(), "Admin");
        cardLayout.show(mainPanel, "Admin");
    }
    
    public static void showUserPanel() {
        mainPanel.add(new UserPanel(), "User");
        cardLayout.show(mainPanel, "User");
    }
}