package com.example.securefiletransfer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class PasswordStrengthDots extends JPanel {

    private int strengthLevel = 0; // 0 to 5

    private static final Color VERY_WEAK_COLOR = new Color(220, 53, 69);      // Red
    private static final Color WEAK_COLOR = new Color(240, 100, 50);          // Orange-Red
    private static final Color MEDIUM_COLOR = new Color(255, 193, 7);         // Yellow
    private static final Color STRONG_COLOR = new Color(132, 204, 22);        // Lime Green
    private static final Color VERY_STRONG_COLOR = new Color(40, 167, 69);    // Green
    private static final Color EMPTY_COLOR = new Color(224, 224, 224);        // Light Gray

    private static final int DOT_COUNT = 5;
    private static final int DOT_DIAMETER = 10;
    private static final int DOT_GAP = 6;

    public PasswordStrengthDots() {
        int width = (DOT_DIAMETER * DOT_COUNT) + (DOT_GAP * (DOT_COUNT - 1));
        setPreferredSize(new Dimension(width, DOT_DIAMETER));
        setOpaque(false);
    }

    public void setStrengthLevel(int level) {
        this.strengthLevel = Math.max(0, Math.min(DOT_COUNT, level));
        repaint(); // Redraw the component when the level changes
    }
    
    // --- START: ADD THIS METHOD ---
    public int getStrengthLevel() {
        return this.strengthLevel;
    }
    // --- END: ADD THIS METHOD ---

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determine the color based on the current strength
        Color fillColor = switch (strengthLevel) {
            case 1 -> VERY_WEAK_COLOR;
            case 2 -> WEAK_COLOR;
            case 3 -> MEDIUM_COLOR;
            case 4 -> STRONG_COLOR;
            case 5 -> VERY_STRONG_COLOR;
            default -> EMPTY_COLOR;
        };

        // Draw the 5 dots
        for (int i = 0; i < DOT_COUNT; i++) {
            int x = i * (DOT_DIAMETER + DOT_GAP);
            if (i < strengthLevel) {
                g2d.setColor(fillColor);
                g2d.fillOval(x, 0, DOT_DIAMETER, DOT_DIAMETER);
            } else {
                g2d.setColor(EMPTY_COLOR);
                g2d.fillOval(x, 0, DOT_DIAMETER, DOT_DIAMETER);
            }
        }
    }
}