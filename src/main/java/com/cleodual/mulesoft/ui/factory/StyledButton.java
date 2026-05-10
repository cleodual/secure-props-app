package com.cleodual.mulesoft.ui.factory;

import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * A custom styled button component following Salesforce design patterns.
 * Provides primary and secondary button styles with smooth hover and press effects.
 */
class StyledButton extends JButton {
    private final Color fillColor;
    private final Color borderColor;
    private final Color textColor;
    private final boolean primary;

    StyledButton(String text, Color fillColor, Color borderColor, Color textColor, boolean primary) {
        super(text);
        this.fillColor = fillColor;
        this.borderColor = borderColor;
        this.textColor = textColor;
        this.primary = primary;

        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(textColor);
        setBackground(fillColor);
        setBorder(new EmptyBorder(10, 14, 10, 14));
        setPreferredSize(new Dimension(0, primary ? 38 : 36));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color paint = fillColor;
        if (!isEnabled()) {
            paint = new Color(72, 74, 79);
        } else if (getModel().isPressed()) {
            paint = fillColor.darker();
        } else if (getModel().isRollover()) {
            paint = fillColor.brighter();
        }

        g2.setColor(paint);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
        g2.dispose();

        super.paintComponent(graphics);
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setFocusPainted(false);
        setForeground(textColor);
    }
}

