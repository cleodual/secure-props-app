package com.cleodual.mulesoft;

import org.mule.encryption.exception.MuleEncryptionException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

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

    private static final Color APP_BG = new Color(30, 31, 34);
    private static final Color SURFACE = new Color(43, 45, 48);
    private static final Color SURFACE_ALT = new Color(53, 54, 58);
    private static final Color PANEL_EDGE = new Color(61, 63, 66);
    private static final Color EDITOR_BG = new Color(36, 37, 41);
    private static final Color OUTPUT_BG = new Color(33, 41, 39);
    private static final Color TEXT_PRIMARY = new Color(223, 225, 229);
    private static final Color TEXT_MUTED = new Color(146, 151, 161);
    private static final Color TEXT_SUBTLE = new Color(119, 124, 132);
    private static final Color ACCENT = new Color(76, 141, 246);
    private static final Color ACCENT_GREEN = new Color(88, 171, 115);
    private static final Color ACCENT_RED = new Color(204, 102, 102);
    private static final Color INPUT_BORDER = new Color(78, 81, 88);

    private JComboBox<String> algorithmBox;
    private JComboBox<String> modeBox;
    private JPasswordField secretKeyField;
    private JToggleButton passwordToggleButton;
    private JCheckBox randomIvBox;
    private JCheckBox quoteOutputBox;
    private JTextArea inputArea;
    private JTextArea outputArea;
    private JLabel statusLabel;

    public Main() {
        setTitle("Mule Secure Properties Desktop");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1180, 760));
        setLayout(new BorderLayout());
        getContentPane().setBackground(APP_BG);

        add(buildTopBar(), BorderLayout.NORTH);
        add(buildWorkspace(), BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setPreferredDefaults();
    }

    private JPanel buildTopBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(39, 40, 44));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, PANEL_EDGE),
                new EmptyBorder(14, 18, 14, 18)
        ));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Mule Secure Properties");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        titlePanel.add(title);

        panel.add(titlePanel, BorderLayout.WEST);
        return panel;
    }

    private JPanel buildWorkspace() {
        JPanel panel = new JPanel(new BorderLayout(14, 14));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(14, 14, 14, 14));
        panel.add(buildConfigRail(), BorderLayout.NORTH);
        panel.add(buildEditorWorkspace(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildConfigRail() {
        JPanel rail = createPanelCard();
        rail.setLayout(new BorderLayout());
        rail.setPreferredSize(new Dimension(0, 154));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(14, 16, 14, 16));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        algorithmBox = createComboBox(new String[]{"AES", "Blowfish", "DES", "DESede", "RC2", "RSA"});
        modeBox = createComboBox(new String[]{"CBC", "CFB", "ECB", "OFB"});
        secretKeyField = createPasswordField();
        passwordToggleButton = createPasswordToggle();
        randomIvBox = createCheckBox("Use random IVs");
        quoteOutputBox = createCheckBox("Double quote output values");

        JPanel fieldsRow = new JPanel(new GridBagLayout());
        fieldsRow.setOpaque(false);
        fieldsRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        GridBagConstraints fieldGbc = new GridBagConstraints();
        fieldGbc.gridy = 0;
        fieldGbc.fill = GridBagConstraints.HORIZONTAL;
        fieldGbc.insets = new Insets(0, 0, 0, 14);

        fieldGbc.gridx = 0;
        fieldGbc.weightx = 0.22;
        fieldsRow.add(createLabeledField("Algorithm", algorithmBox), fieldGbc);

        fieldGbc.gridx = 1;
        fieldGbc.weightx = 0.18;
        fieldsRow.add(createLabeledField("Mode", modeBox), fieldGbc);

        fieldGbc.gridx = 2;
        fieldGbc.weightx = 0.38;
        fieldsRow.add(createLabeledField("Secret Key", createPasswordFieldPanel()), fieldGbc);

        fieldGbc.gridx = 3;
        fieldGbc.weightx = 0.22;
        fieldGbc.insets = new Insets(25, 0, 0, 0);
        fieldsRow.add(createInlineCheckPanel(), fieldGbc);

        content.add(fieldsRow);
        content.add(Box.createVerticalStrut(12));
        content.add(buildActionButtons());

        rail.add(content, BorderLayout.CENTER);
        return rail;
    }

    private JPanel buildActionButtons() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panel.setOpaque(false);

        JButton encryptButton = createPrimaryButton("Encrypt", ACCENT);
        encryptButton.addActionListener(event -> runOperation(true));

        JButton decryptButton = createPrimaryButton("Decrypt", ACCENT_GREEN);
        decryptButton.addActionListener(event -> runOperation(false));

        JButton sampleButton = createSecondaryButton("Load Sample");
        sampleButton.addActionListener(event -> {
            inputArea.setText(SAMPLE);
            setStatus("Sample loaded into the input editor.", TEXT_MUTED);
        });

        JButton copyButton = createSecondaryButton("Copy Output");
        copyButton.addActionListener(event -> copyOutput());

        JButton clearButton = createSecondaryButton("Clear Workspace");
        clearButton.addActionListener(event -> clearAll());

        encryptButton.setPreferredSize(new Dimension(132, 42));
        decryptButton.setPreferredSize(new Dimension(132, 42));
        sampleButton.setPreferredSize(new Dimension(132, 40));
        copyButton.setPreferredSize(new Dimension(132, 40));
        clearButton.setPreferredSize(new Dimension(156, 40));

        panel.add(encryptButton);
        panel.add(decryptButton);
        panel.add(sampleButton);
        panel.add(copyButton);
        panel.add(clearButton);

        return panel;
    }

    private JSplitPane buildEditorWorkspace() {
        inputArea = createEditorArea(false);
        outputArea = createEditorArea(true);
        outputArea.setEditable(false);

        JPanel inputPanel = createEditorPanel("Input", inputArea, false);
        JPanel outputPanel = createEditorPanel("Output", outputArea, true);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, inputPanel, outputPanel);
        splitPane.setOpaque(false);
        splitPane.setBackground(APP_BG);
        splitPane.setBorder(BorderFactory.createEmptyBorder());
        splitPane.setDividerSize(8);
        splitPane.setResizeWeight(0.5);
        splitPane.setContinuousLayout(true);
        return splitPane;
    }

    private JPanel createEditorPanel(String title, JTextArea area, boolean output) {
        JPanel panel = createPanelCard();
        panel.setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(SURFACE_ALT);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, PANEL_EDGE),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLabel.setForeground(TEXT_PRIMARY);

        header.add(titleLabel, BorderLayout.WEST);

        JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        headerActions.setOpaque(false);
        if (output) {
            JButton copyButton = createSecondaryButton("Copy");
            copyButton.setPreferredSize(new Dimension(84, 30));
            copyButton.addActionListener(event -> copyOutput());
            headerActions.add(copyButton);
        }
        header.add(headerActions, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(area.getBackground());

        panel.add(header, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildStatusBar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(39, 40, 44));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, PANEL_EDGE),
                new EmptyBorder(8, 14, 8, 14)
        ));

        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_MUTED);

        panel.add(statusLabel, BorderLayout.WEST);
        return panel;
    }

    private JPanel createPanelCard() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createLineBorder(PANEL_EDGE));
        return panel;
    }

    private JPanel createLabeledField(String title, Component field) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_PRIMARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));

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
                BorderFactory.createLineBorder(INPUT_BORDER),
                new EmptyBorder(0, 0, 0, 0)
        ));
        panel.setBackground(EDITOR_BG);

        secretKeyField.setBorder(new EmptyBorder(8, 10, 8, 4));
        panel.add(secretKeyField, BorderLayout.CENTER);
        panel.add(passwordToggleButton, BorderLayout.EAST);
        return panel;
    }

    private JPanel createInlineCheckPanel() {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(randomIvBox);
        panel.add(Box.createVerticalStrut(6));
        panel.add(quoteOutputBox);
        return panel;
    }

    private JPanel createSeparator() {
        JPanel separator = new JPanel();
        separator.setPreferredSize(new Dimension(0, 1));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator.setBackground(PANEL_EDGE);
        return separator;
    }

    private JButton createPrimaryButton(String text, Color accentColor) {
        StyledButton button = new StyledButton(text, accentColor, accentColor.brighter(), new Color(24, 25, 28), true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        StyledButton button = new StyledButton(text, new Color(60, 63, 68), new Color(78, 82, 89), TEXT_PRIMARY, false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return button;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBackground(EDITOR_BG);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BORDER),
                new EmptyBorder(4, 8, 4, 8)
        ));
        comboBox.setUI(new BasicComboBoxUI());
        return comboBox;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Consolas", Font.PLAIN, 13));
        field.setForeground(TEXT_PRIMARY);
        field.setBackground(EDITOR_BG);
        field.setCaretColor(TEXT_PRIMARY);
        field.setBorder(new EmptyBorder(8, 10, 8, 10));
        return field;
    }

    private JToggleButton createPasswordToggle() {
        JToggleButton toggle = new JToggleButton("Show");
        toggle.setFocusPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setOpaque(true);
        toggle.setBackground(EDITOR_BG);
        toggle.setForeground(TEXT_MUTED);
        toggle.setBorder(new EmptyBorder(7, 8, 7, 10));
        toggle.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
        toggle.addActionListener(event -> {
            boolean visible = toggle.isSelected();
            secretKeyField.setEchoChar(visible ? (char) 0 : '\u2022');
            toggle.setText(visible ? "Hide" : "Show");
            setStatus(visible ? "Secret key is visible." : "Secret key is hidden.", TEXT_MUTED);
        });
        return toggle;
    }

    private JCheckBox createCheckBox(String text) {
        JCheckBox checkBox = new JCheckBox(text);
        checkBox.setOpaque(false);
        checkBox.setForeground(TEXT_PRIMARY);
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return checkBox;
    }

    private JTextArea createEditorArea(boolean output) {
        JTextArea area = new JTextArea();
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setForeground(TEXT_PRIMARY);
        area.setCaretColor(TEXT_PRIMARY);
        area.setSelectionColor(new Color(78, 110, 168));
        area.setLineWrap(false);
        area.setWrapStyleWord(false);
        area.setTabSize(2);
        area.setBorder(new EmptyBorder(14, 14, 14, 14));
        area.setBackground(output ? OUTPUT_BG : EDITOR_BG);
        return area;
    }

    private void setPreferredDefaults() {
        algorithmBox.setSelectedItem("AES");
        modeBox.setSelectedItem("CBC");
        randomIvBox.setSelected(false);
        quoteOutputBox.setSelected(false);
        secretKeyField.setEchoChar('\u2022');
        if (passwordToggleButton != null) {
            passwordToggleButton.setSelected(false);
            passwordToggleButton.setText("Show");
        }
    }

    private void runOperation(boolean encrypt) {
        String secretKey = new String(secretKeyField.getPassword());
        if (secretKey.isBlank()) {
            showError("Secret key is required.");
            return;
        }

        String input = inputArea.getText();
        if (input.isBlank()) {
            showError("Input is empty.");
            return;
        }

        String algorithm = (String) algorithmBox.getSelectedItem();
        String mode = (String) modeBox.getSelectedItem();
        boolean randomIv = randomIvBox.isSelected();
        boolean quoteOutputValues = quoteOutputBox.isSelected();

        try {
            String result = encrypt
                    ? YamlLikeTransformer.encrypt(input, algorithm, mode, secretKey, randomIv, quoteOutputValues)
                    : YamlLikeTransformer.decrypt(input, algorithm, mode, secretKey, randomIv, quoteOutputValues);

            outputArea.setText(result);
            setStatus((encrypt ? "Encrypted" : "Decrypted") + " successfully.", encrypt ? ACCENT : ACCENT_GREEN);
        } catch (MuleEncryptionException | IllegalArgumentException ex) {
            showError(ex.getMessage() == null ? "Operation failed." : ex.getMessage());
        }
    }

    private void copyOutput() {
        String value = outputArea.getText();
        if (value.isBlank()) {
            showError("There is no output to copy.");
            return;
        }

        Toolkit.getDefaultToolkit()
                .getSystemClipboard()
                .setContents(new StringSelection(value), null);
        setStatus("Output copied to clipboard.", TEXT_MUTED);
    }

    private void clearAll() {
        inputArea.setText("");
        outputArea.setText("");
        secretKeyField.setText("");
        setPreferredDefaults();
        setStatus("Workspace cleared.", TEXT_MUTED);
    }

    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    private void showError(String message) {
        setStatus(message, ACCENT_RED);
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

    private static final class StyledButton extends JButton {
        private final Color fillColor;
        private final Color borderColor;
        private final Color textColor;
        private final boolean primary;

        private StyledButton(String text, Color fillColor, Color borderColor, Color textColor, boolean primary) {
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
}
