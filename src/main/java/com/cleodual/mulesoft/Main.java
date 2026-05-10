package com.cleodual.mulesoft;

import com.cleodual.mulesoft.theme.SalesforceTheme;
import com.cleodual.mulesoft.ui.components.*;
import org.mule.encryption.exception.MuleEncryptionException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * Main application window for Mule Secure Properties Desktop.
 * Uses modular UI components with Salesforce design theme.
 */
public class Main extends JFrame {
    private static final String SAMPLE = """
            database:
              password: mySecretPassword
              username: admin
              host: localhost
            api:
              clientId: abc123
              clientSecret: change-me
            environments:
              - dev
              - prod
            """;

    // UI Components
    private ConfigurationPanel configurationPanel;
    private EditorPanel inputPanel;
    private EditorPanel outputPanel;
    private StatusBar statusBar;
    private ActionButtonPanel actionButtonPanel;

    public Main() {
        setTitle("Mule Secure Properties Desktop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(SalesforceTheme.COMPONENT_MIN_WIDTH, SalesforceTheme.COMPONENT_MIN_HEIGHT));
        setLayout(new BorderLayout());
        getContentPane().setBackground(SalesforceTheme.BG_PRIMARY);

        // Initialize components
        configurationPanel = new ConfigurationPanel();
        inputPanel = new EditorPanel("Input", false);
        outputPanel = new EditorPanel("Output", true);
        statusBar = new StatusBar();
        actionButtonPanel = new ActionButtonPanel();

        // Build UI
        add(new TopBar(), BorderLayout.NORTH);
        add(buildWorkspace(), BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        // Attach event listeners
        attachEventListeners();

        pack();
        setLocationRelativeTo(null);
    }

    private JPanel buildWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(SalesforceTheme.SPACING_LG, SalesforceTheme.SPACING_LG));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(SalesforceTheme.SPACING_LG, SalesforceTheme.SPACING_LG, SalesforceTheme.SPACING_LG, SalesforceTheme.SPACING_LG));
        panel.add(buildConfigRail(), BorderLayout.NORTH);
        panel.add(buildEditorWorkspace(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildConfigRail() {
        JPanel rail = new JPanel(new BorderLayout());
        rail.setOpaque(false);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(SalesforceTheme.SPACING_LG, SalesforceTheme.SPACING_XL, SalesforceTheme.SPACING_LG, SalesforceTheme.SPACING_XL));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(configurationPanel);
        content.add(Box.createVerticalStrut(SalesforceTheme.SPACING_MD));
        content.add(actionButtonPanel);

        rail.add(content, BorderLayout.CENTER);
        return rail;
    }

    private JSplitPane buildEditorWorkspace() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, outputPanel);
        splitPane.setOpaque(false);
        splitPane.setBackground(SalesforceTheme.BG_PRIMARY);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerSize(8);
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);
        return splitPane;
    }


    private void attachEventListeners() {
        actionButtonPanel.getEncryptButton().addActionListener(event -> runOperation(true));
        actionButtonPanel.getDecryptButton().addActionListener(event -> runOperation(false));
        actionButtonPanel.getSampleButton().addActionListener(event -> loadSample());
        actionButtonPanel.getCopyButton().addActionListener(event -> copyOutput());
        actionButtonPanel.getClearButton().addActionListener(event -> clearAll());
    }

    private void loadSample() {
        inputPanel.getTextArea().setText(SAMPLE);
        statusBar.setStatus("Sample loaded into the input editor.", SalesforceTheme.TEXT_SECONDARY);
    }

    private void runOperation(boolean encrypt) {
        String secretKey = new String(configurationPanel.getSecretKeyField().getPassword());
        if (secretKey.isBlank()) {
            showError("Secret key is required.");
            return;
        }

        String input = inputPanel.getTextArea().getText();
        if (input.isBlank()) {
            showError("Input is empty.");
            return;
        }

        String algorithm = (String) configurationPanel.getAlgorithmBox().getSelectedItem();
        String mode = (String) configurationPanel.getModeBox().getSelectedItem();
        boolean randomIv = configurationPanel.getRandomIvBox().isSelected();
        boolean quoteOutputValues = configurationPanel.getQuoteOutputBox().isSelected();

        try {
            String result = encrypt
                    ? YamlLikeTransformer.encrypt(input, algorithm, mode, secretKey, randomIv, quoteOutputValues)
                    : YamlLikeTransformer.decrypt(input, algorithm, mode, secretKey, randomIv, quoteOutputValues);

            outputPanel.getTextArea().setText(result);
            statusBar.setStatus((encrypt ? "Encrypted" : "Decrypted") + " successfully.", 
                    encrypt ? SalesforceTheme.BRAND_BLUE : SalesforceTheme.ACCENT_SUCCESS);
        } catch (MuleEncryptionException | IllegalArgumentException ex) {
            showError(ex.getMessage() == null ? "Operation failed." : ex.getMessage());
        }
    }

    private void copyOutput() {
        String value = outputPanel.getTextArea().getText();
        if (value.isBlank()) {
            showError("There is no output to copy.");
            return;
        }

        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(value), null);
        statusBar.setStatus("Output copied to clipboard.", SalesforceTheme.TEXT_SECONDARY);
    }

    private void clearAll() {
        inputPanel.getTextArea().setText("");
        outputPanel.getTextArea().setText("");
        configurationPanel.getSecretKeyField().setText("");
        statusBar.setStatus("Workspace cleared.", SalesforceTheme.TEXT_SECONDARY);
    }

    private void showError(String message) {
        statusBar.setStatus(message, SalesforceTheme.ACCENT_ERROR);
        JOptionPane.showMessageDialog(this, message, "Secure Properties", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            new Main().setVisible(true);
        });
    }
}
