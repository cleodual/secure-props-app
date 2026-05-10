package com.cleodual.mulesoft.ui.components;

import com.cleodual.mulesoft.theme.SalesforceTheme;
import com.cleodual.mulesoft.ui.factory.UIComponentFactory;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Configuration panel for encryption/decryption settings.
 * Contains algorithm selection, mode, secret key, and options.
 */
public class ConfigurationPanel extends JPanel {
    private JComboBox<String> algorithmBox;
    private JComboBox<String> modeBox;
    private JPasswordField secretKeyField;
    private JToggleButton passwordToggleButton;
    private JCheckBox randomIvBox;
    private JCheckBox quoteOutputBox;

    public ConfigurationPanel() {
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        algorithmBox = UIComponentFactory.createComboBox(new String[]{"AES", "Blowfish", "DES", "DESede", "RC2", "RSA"});
        modeBox = UIComponentFactory.createComboBox(new String[]{"CBC", "CFB", "ECB", "OFB"});
        secretKeyField = UIComponentFactory.createPasswordField();
        passwordToggleButton = UIComponentFactory.createPasswordToggleButton(secretKeyField);
        randomIvBox = UIComponentFactory.createCheckBox("Use random IVs");
        quoteOutputBox = UIComponentFactory.createCheckBox("Double quote output values");

        setPreferredDefaults();
    }

    private void layoutComponents() {
        setBackground(SalesforceTheme.BG_SURFACE);
        setBorder(BorderFactory.createLineBorder(SalesforceTheme.BORDER_PRIMARY));
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(0, 154));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(14, 16, 14, 16));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Fields row
        JPanel fieldsRow = createFieldsRow();
        content.add(fieldsRow);

        add(content, BorderLayout.CENTER);
    }

    private JPanel createFieldsRow() {
        JPanel fieldsRow = new JPanel(new GridBagLayout());
        fieldsRow.setOpaque(false);
        fieldsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 0, 14);

        // Algorithm
        gbc.gridx = 0;
        gbc.weightx = 0.22;
        fieldsRow.add(createLabeledField("Algorithm", algorithmBox), gbc);

        // Mode
        gbc.gridx = 1;
        gbc.weightx = 0.18;
        fieldsRow.add(createLabeledField("Mode", modeBox), gbc);

        // Secret Key
        gbc.gridx = 2;
        gbc.weightx = 0.38;
        fieldsRow.add(createLabeledField("Secret Key", createPasswordFieldPanel()), gbc);

        // Checkboxes
        gbc.gridx = 3;
        gbc.weightx = 0.22;
        gbc.insets = new Insets(25, 0, 0, 0);
        fieldsRow.add(createCheckboxPanel(), gbc);

        return fieldsRow;
    }

    private JPanel createLabeledField(String label, Component field) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font(SalesforceTheme.FONT_FAMILY_SANS, Font.BOLD, SalesforceTheme.FONT_SIZE_SM));
        titleLabel.setForeground(SalesforceTheme.TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, SalesforceTheme.INPUT_HEIGHT));

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(7));
        panel.add(field);

        return panel;
    }

    private JPanel createPasswordFieldPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setOpaque(true);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        panel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(SalesforceTheme.INPUT_BORDER),
                new EmptyBorder(0, 0, 0, 0)
        ));
        panel.setBackground(SalesforceTheme.EDITOR_BG);

        secretKeyField.setBorder(new EmptyBorder(8, 10, 8, 4));
        panel.add(secretKeyField, BorderLayout.CENTER);
        panel.add(passwordToggleButton, BorderLayout.EAST);

        return panel;
    }

    private JPanel createCheckboxPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(randomIvBox);
        panel.add(Box.createVerticalStrut(6));
        panel.add(quoteOutputBox);
        return panel;
    }

    private void setPreferredDefaults() {
        algorithmBox.setSelectedItem("AES");
        modeBox.setSelectedItem("CBC");
        randomIvBox.setSelected(false);
        quoteOutputBox.setSelected(true);
        secretKeyField.setEchoChar('\u2022');
        if (passwordToggleButton != null) {
            passwordToggleButton.setSelected(false);
            passwordToggleButton.setText("Show");
        }
    }


    // Getters
    public JComboBox<String> getAlgorithmBox() {
        return algorithmBox;
    }

    public JComboBox<String> getModeBox() {
        return modeBox;
    }

    public JPasswordField getSecretKeyField() {
        return secretKeyField;
    }

    public JCheckBox getRandomIvBox() {
        return randomIvBox;
    }

    public JCheckBox getQuoteOutputBox() {
        return quoteOutputBox;
    }

    public JToggleButton getPasswordToggleButton() {
        return passwordToggleButton;
    }
}

