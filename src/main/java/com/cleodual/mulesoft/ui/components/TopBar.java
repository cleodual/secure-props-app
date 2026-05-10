package com.cleodual.mulesoft.ui.components;

import com.cleodual.mulesoft.theme.SalesforceTheme;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

/**
 * Top bar component displaying the application title and branding.
 */
public class TopBar extends JPanel {

    public TopBar() {
        initializeUI();
    }

    private void initializeUI() {
        setBackground(new java.awt.Color(39, 40, 44));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, SalesforceTheme.BORDER_PRIMARY),
                new EmptyBorder(14, 18, 14, 18)
        ));
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Mule Secure Properties");
        title.setFont(new Font(SalesforceTheme.FONT_FAMILY_SANS, Font.BOLD, SalesforceTheme.FONT_SIZE_XL));
        title.setForeground(SalesforceTheme.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(title, BorderLayout.WEST);
    }
}

