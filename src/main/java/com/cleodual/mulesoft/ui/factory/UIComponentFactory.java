package com.cleodual.mulesoft.ui.factory;

import com.cleodual.mulesoft.theme.SalesforceTheme;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JToggleButton;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Factory for creating styled UI components following the Salesforce theme.
 * Centralizes component styling to ensure consistency across the application.
 */
public class UIComponentFactory {

    /**
     * Creates a primary action button (e.g., Encrypt, Decrypt).
     */
    public static JButton createPrimaryButton(String text, Color accentColor) {
        StyledButton button = new StyledButton(
                text,
                accentColor,
                accentColor.brighter(),
                new Color(24, 25, 28),
                true
        );
        button.setFont(new Font(SalesforceTheme.FONT_FAMILY_SANS, Font.BOLD, SalesforceTheme.FONT_SIZE_BASE));
        return button;
    }

    /**
     * Creates a secondary action button (e.g., Load Sample, Copy Output).
     */
    public static JButton createSecondaryButton(String text) {
        StyledButton button = new StyledButton(
                text,
                SalesforceTheme.BUTTON_BG_SECONDARY,
                new Color(78, 82, 89),
                SalesforceTheme.TEXT_PRIMARY,
                false
        );
        button.setFont(new Font(SalesforceTheme.FONT_FAMILY_SANS, Font.PLAIN, SalesforceTheme.FONT_SIZE_SM));
        return button;
    }

    /**
     * Creates a styled combo box with Salesforce theme.
     */
    public static JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font(SalesforceTheme.FONT_FAMILY_SANS, Font.PLAIN, SalesforceTheme.FONT_SIZE_BASE));
        comboBox.setForeground(SalesforceTheme.TEXT_PRIMARY);
        comboBox.setBackground(SalesforceTheme.INPUT_BG);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SalesforceTheme.INPUT_BORDER),
                new EmptyBorder(4, 8, 4, 8)
        ));
        comboBox.setUI(new BasicComboBoxUI());
        return comboBox;
    }

    /**
     * Creates a styled password field with Salesforce theme.
     */
    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font(SalesforceTheme.FONT_FAMILY_MONO, Font.PLAIN, SalesforceTheme.FONT_SIZE_BASE));
        field.setForeground(SalesforceTheme.TEXT_PRIMARY);
        field.setBackground(SalesforceTheme.INPUT_BG);
        field.setCaretColor(SalesforceTheme.TEXT_PRIMARY);
        field.setBorder(new EmptyBorder(8, 10, 8, 10));
        return field;
    }

    /**
     * Creates a toggle button for password visibility.
     */
    public static JToggleButton createPasswordToggleButton(JPasswordField targetField) {
        JToggleButton toggle = new JToggleButton("Show");
        toggle.setFocusPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setOpaque(true);
        toggle.setBackground(SalesforceTheme.INPUT_BG);
        toggle.setForeground(SalesforceTheme.TEXT_SECONDARY);
        toggle.setBorder(new EmptyBorder(7, 8, 7, 10));
        toggle.setFont(new Font(SalesforceTheme.FONT_FAMILY_SANS, Font.PLAIN, SalesforceTheme.FONT_SIZE_SM));

        toggle.addActionListener(event -> {
            boolean visible = toggle.isSelected();
            targetField.setEchoChar(visible ? (char) 0 : '\u2022');
            toggle.setText(visible ? "Hide" : "Show");
        });

        return toggle;
    }

    /**
     * Creates a styled checkbox with Salesforce theme.
     */
    public static JCheckBox createCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setOpaque(false);
        checkBox.setForeground(SalesforceTheme.TEXT_PRIMARY);
        checkBox.setFont(new Font(SalesforceTheme.FONT_FAMILY_SANS, Font.PLAIN, SalesforceTheme.FONT_SIZE_SM));
        return checkBox;
    }

    /**
     * Creates a styled panel card following Salesforce design.
     */
    public static JPanel createPanelCard() {
        JPanel panel = new JPanel();
        panel.setBackground(SalesforceTheme.BG_SURFACE);
        panel.setBorder(BorderFactory.createLineBorder(SalesforceTheme.BORDER_PRIMARY));
        return panel;
    }

    /**
     * Creates a panel with specified background color.
     */
    public static JPanel createPanel(Color backgroundColor) {
        JPanel panel = new JPanel();
        panel.setBackground(backgroundColor);
        return panel;
    }
}

