package com.cleodual.mulesoft.ui.components;

import com.cleodual.mulesoft.theme.SalesforceTheme;
import com.cleodual.mulesoft.ui.factory.UIComponentFactory;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/**
 * Editor panel component for displaying input or output text areas.
 */
public class EditorPanel extends JPanel {
    private final JTextArea textArea;
    private final JPanel header;

    public EditorPanel(String title, boolean isOutput) {
        setLayout(new BorderLayout());
        setBackground(SalesforceTheme.BG_SURFACE);
        setBorder(BorderFactory.createLineBorder(SalesforceTheme.BORDER_PRIMARY));

        // Header
        header = createHeader(title, isOutput);
        add(header, BorderLayout.NORTH);

        // Text Area
        textArea = createTextArea(isOutput);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(textArea.getBackground());

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeader(String title, boolean isOutput) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(SalesforceTheme.BG_SURFACE_ALT);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, SalesforceTheme.BORDER_PRIMARY),
                new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font(SalesforceTheme.FONT_FAMILY_SANS, Font.BOLD, SalesforceTheme.FONT_SIZE_LG));
        titleLabel.setForeground(SalesforceTheme.TEXT_PRIMARY);

        headerPanel.add(titleLabel, BorderLayout.WEST);

        if (isOutput) {
            JPanel headerActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            headerActions.setOpaque(false);
            JButton copyButton = UIComponentFactory.createSecondaryButton("Copy");
            copyButton.setPreferredSize(new Dimension(84, 30));
            headerActions.add(copyButton);
            headerPanel.add(headerActions, BorderLayout.EAST);
        }

        return headerPanel;
    }

    private JTextArea createTextArea(boolean isOutput) {
        JTextArea area = new JTextArea();
        area.setFont(new Font(SalesforceTheme.FONT_FAMILY_MONO, Font.PLAIN, SalesforceTheme.FONT_SIZE_BASE));
        area.setForeground(SalesforceTheme.TEXT_PRIMARY);
        area.setCaretColor(SalesforceTheme.TEXT_PRIMARY);
        area.setSelectionColor(new Color(78, 110, 168));
        area.setLineWrap(false);
        area.setWrapStyleWord(false);
        area.setTabSize(2);
        area.setBorder(new EmptyBorder(14, 14, 14, 14));
        area.setBackground(isOutput ? SalesforceTheme.OUTPUT_BG : SalesforceTheme.EDITOR_BG);

        if (isOutput) {
            area.setEditable(false);
        }

        return area;
    }


    public JTextArea getTextArea() {
        return textArea;
    }

    public JButton getCopyButton() {
        // Find and return the copy button from header if it exists
        if (header.getComponentCount() > 1) {
            JPanel headerActions = (JPanel) header.getComponent(1);
            if (headerActions.getComponentCount() > 0) {
                return (JButton) headerActions.getComponent(0);
            }
        }
        return null;
    }
}

