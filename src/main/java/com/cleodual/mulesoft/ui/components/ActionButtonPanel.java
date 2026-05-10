package com.cleodual.mulesoft.ui.components;

import com.cleodual.mulesoft.theme.SalesforceTheme;
import com.cleodual.mulesoft.ui.factory.UIComponentFactory;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.FlowLayout;

/**
 * Action button panel containing primary and secondary action buttons.
 */
public class ActionButtonPanel extends JPanel {
    private final JButton encryptButton;
    private final JButton decryptButton;
    private final JButton sampleButton;
    private final JButton copyButton;
    private final JButton clearButton;

    public ActionButtonPanel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.LEFT, 12, 0));

        // Create buttons
        encryptButton = UIComponentFactory.createPrimaryButton("Encrypt", SalesforceTheme.BRAND_BLUE);
        decryptButton = UIComponentFactory.createPrimaryButton("Decrypt", SalesforceTheme.ACCENT_SUCCESS);
        sampleButton = UIComponentFactory.createSecondaryButton("Load Sample");
        copyButton = UIComponentFactory.createSecondaryButton("Copy Output");
        clearButton = UIComponentFactory.createSecondaryButton("Clear Workspace");

        // Set sizes
        encryptButton.setPreferredSize(new Dimension(132, 42));
        decryptButton.setPreferredSize(new Dimension(132, 42));
        sampleButton.setPreferredSize(new Dimension(132, 40));
        copyButton.setPreferredSize(new Dimension(132, 40));
        clearButton.setPreferredSize(new Dimension(156, 40));

        // Add to panel
        add(encryptButton);
        add(decryptButton);
        add(sampleButton);
        add(copyButton);
        add(clearButton);
    }

    public JButton getEncryptButton() {
        return encryptButton;
    }

    public JButton getDecryptButton() {
        return decryptButton;
    }

    public JButton getSampleButton() {
        return sampleButton;
    }

    public JButton getCopyButton() {
        return copyButton;
    }

    public JButton getClearButton() {
        return clearButton;
    }
}

