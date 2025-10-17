package com.example.securefiletransfer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class UITheme {

    public static final Color PRIMARY_COLOR = new Color(69, 123, 157);
    public static final Color BACKGROUND_COLOR = new Color(241, 250, 238);
    public static final Color PANEL_BACKGROUND_COLOR = Color.WHITE;
    public static final Color HEADER_COLOR = new Color(29, 53, 87);
    public static final Color FONT_COLOR = new Color(51, 51, 51);
    public static final Color APPROVE_COLOR = new Color(40, 167, 69);
    public static final Color REJECT_COLOR = new Color(220, 53, 69);
    public static final Color VIEW_COLOR = new Color(23, 162, 184);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font TABLE_HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font CARD_VALUE_FONT = new Font("Segoe UI", Font.BOLD, 36);
    public static final Font CARD_TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);

    public static final Border PADDED_BORDER = BorderFactory.createEmptyBorder(15, 15, 15, 15);

    public static final String ICON_APPROVE = "✔";
    public static final String ICON_REJECT = "✖";
    public static final String ICON_UPLOAD = "⬆";
    public static final String ICON_VIEW = "👁";
    public static final String ICON_DETAILS = "ℹ";
    public static final String ICON_DOWNLOAD = "⬇";
    public static final String ICON_LOGOUT = "⮫";
    public static final String ICON_REQUEST = "✉";

    public static JPanel createDashboardCard(String title, JLabel valueLabel, Color bgColor) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(bgColor);
        card.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel titleLabel = new JLabel(title.toUpperCase());
        titleLabel.setFont(CARD_TITLE_FONT);
        titleLabel.setForeground(Color.WHITE);
        valueLabel.setFont(CARD_VALUE_FONT);
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.LEFT);
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    public static void styleButton(JButton button, Color backgroundColor, String icon) {
        if (!icon.isEmpty()) {
            button.setText(icon + " " + button.getText());
        }
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleTable(JTable table, JScrollPane scrollPane) {
        table.setRowHeight(32);
        table.setFont(LABEL_FONT);
        table.setGridColor(new Color(230, 230, 230));
        table.getTableHeader().setFont(TABLE_HEADER_FONT);
        table.getTableHeader().setBackground(HEADER_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.setSelectionBackground(PRIMARY_COLOR.brighter());
        table.setSelectionForeground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(221, 221, 221)));
    }

    public static JPanel createBrandingPanel(String subtitle) {
        JPanel brandingPanel = new JPanel(new GridBagLayout());
        brandingPanel.setBackground(HEADER_COLOR); // Use the dark blue header color
        brandingPanel.setPreferredSize(new Dimension(350, 0)); // Set a fixed width

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel appNameLabel = new JLabel("SafeShare");
        appNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        appNameLabel.setForeground(Color.WHITE);
        brandingPanel.add(appNameLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 20, 10, 20);
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        subtitleLabel.setForeground(Color.LIGHT_GRAY);
        brandingPanel.add(subtitleLabel, gbc);

        return brandingPanel;
    }
}