package com.example.securefiletransfer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class StatusCellRenderer extends DefaultTableCellRenderer {

    private static final Color PENDING_BG = new Color(255, 193, 7);
    private static final Color APPROVED_BG = new Color(40, 167, 69);
    private static final Color REJECTED_BG = new Color(220, 53, 69);
    private static final Color DEFAULT_BG = Color.LIGHT_GRAY;

    public StatusCellRenderer() {
        super();
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value instanceof String) {
            String status = (String) value;
            JLabel label = (JLabel) cellComponent;
            label.setText(status.toUpperCase());
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
            label.setOpaque(true);

            switch (status.toLowerCase()) {
                case "pending":
                    label.setBackground(PENDING_BG);
                    break;
                case "approved":
                    label.setBackground(APPROVED_BG);
                    break;
                case "rejected":
                    label.setBackground(REJECTED_BG);
                    break;
                default:
                    label.setBackground(DEFAULT_BG);
                    label.setForeground(Color.BLACK);
                    break;
            }
        }
        return cellComponent;
    }
}