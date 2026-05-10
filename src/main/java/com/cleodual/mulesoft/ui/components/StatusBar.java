package com.cleodual.mulesoft.ui.components;

import com.cleodual.mulesoft.theme.SalesforceTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Status bar component displayed at the bottom of the application.
 * Shows operational status messages and error notifications.
 */
public class StatusBar extends JPanel {
    private final JLabel statusLabel;

    public StatusBar() {
        setBackground(new Color(39, 40, 44));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, SalesforceTheme.BORDER_PRIMARY),
                new EmptyBorder(8, 14, 8, 14)
        ));
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font(SalesforceTheme.FONT_FAMILY_SANS, Font.PLAIN, SalesforceTheme.FONT_SIZE_SM));
        statusLabel.setForeground(SalesforceTheme.TEXT_SECONDARY);

        add(statusLabel, BorderLayout.WEST);
    }

    /**
     * Updates the status message and color.
     */
    public void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    /**
     * Gets the current status message.
     */
    public String getStatus() {
        return statusLabel.getText();
    }
}

