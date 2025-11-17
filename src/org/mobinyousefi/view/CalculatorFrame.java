package com.mobinyousefi.scientificcalculator.view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing-based user interface for the scientific calculator.
 */
public class CalculatorFrame extends JFrame {

    private final JTextField displayField;
    private final List<AbstractButton> buttons;

    public CalculatorFrame() {
        super("Scientific Calculator");
        this.buttons = new ArrayList<>();
        this.displayField = new JTextField("0");
        initializeUi();
    }

    private void initializeUi() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        setMinimumSize(new Dimension(420, 360));

        displayField.setEditable(false);
        displayField.setHorizontalAlignment(SwingConstants.RIGHT);
        displayField.setFont(displayField.getFont().deriveFont(Font.BOLD, 24f));
        displayField.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(displayField, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(6, 6, 4, 4));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Row 1
        buttonPanel.add(createButton("MC", false));
        buttonPanel.add(createButton("MR", false));
        buttonPanel.add(createButton("C", true));
        buttonPanel.add(createButton("CE", true));
        buttonPanel.add(createButton("<-", true));
        buttonPanel.add(createButton("+/-", true));

        // Row 2
        buttonPanel.add(createButton("7", true));
        buttonPanel.add(createButton("8", true));
        buttonPanel.add(createButton("9", true));
        buttonPanel.add(createButton("/", true));
        buttonPanel.add(createButton("sin", true));
        buttonPanel.add(createButton("cos", true));

        // Row 3
        buttonPanel.add(createButton("4", true));
        buttonPanel.add(createButton("5", true));
        buttonPanel.add(createButton("6", true));
        buttonPanel.add(createButton("*", true));
        buttonPanel.add(createButton("tan", true));
        buttonPanel.add(createButton("sqrt", true));

        // Row 4
        buttonPanel.add(createButton("1", true));
        buttonPanel.add(createButton("2", true));
        buttonPanel.add(createButton("3", true));
        buttonPanel.add(createButton("-", true));
        buttonPanel.add(createButton("x^2", true));
        buttonPanel.add(createButton("x^y", true));

        // Row 5
        buttonPanel.add(createButton("0", true));
        buttonPanel.add(createButton(".", true));
        buttonPanel.add(createButton("=", true));
        buttonPanel.add(createButton("+", true));
        buttonPanel.add(createButton("log", true));
        buttonPanel.add(createButton("ln", true));

        // Row 6
        buttonPanel.add(createButton("1/x", true));
        buttonPanel.add(createButton("%", true));
        buttonPanel.add(createButton("π", true));
        buttonPanel.add(createButton("e", true));
        buttonPanel.add(createButton("", false));
        buttonPanel.add(createButton("", false));

        add(buttonPanel, BorderLayout.CENTER);

        JButton defaultButton = findButtonByLabel("=");
        if (defaultButton != null) {
            getRootPane().setDefaultButton(defaultButton);
        }

        pack();
        setLocationRelativeTo(null);
    }

    private JButton createButton(String label, boolean enabled) {
        JButton button = new JButton(label);
        button.setEnabled(enabled && label != null && !label.isEmpty());
        button.setFocusable(false);
        button.setFont(button.getFont().deriveFont(Font.PLAIN, 16f));
        if (button.isEnabled()) {
            button.setActionCommand(label);
            buttons.add(button);
        }
        return button;
    }

    private JButton findButtonByLabel(String label) {
        for (AbstractButton b : buttons) {
            if (label.equals(b.getText()) && b instanceof JButton) {
                return (JButton) b;
            }
        }
        return null;
    }

    /**
     * Updates the text shown in the calculator display.
     */
    public void setDisplayText(String text) {
        displayField.setText(text);
    }

    /**
     * Registers a common ActionListener for all interactive buttons.
     */
    public void registerActionListener(java.awt.event.ActionListener listener) {
        for (AbstractButton button : buttons) {
            button.addActionListener(listener);
        }
    }
}
