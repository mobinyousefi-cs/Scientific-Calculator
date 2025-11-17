package com.mobinyousefi.scientificcalculator;

import com.mobinyousefi.scientificcalculator.model.CalculatorModel;
import com.mobinyousefi.scientificcalculator.view.CalculatorFrame;
import com.mobinyousefi.scientificcalculator.controller.CalculatorController;

import javax.swing.SwingUtilities;

/**
 * Entry point for the Scientific Calculator application.
 */
public final class ScientificCalculatorApp {

    private ScientificCalculatorApp() {
        // Not instantiable
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CalculatorModel model = new CalculatorModel();
            CalculatorFrame frame = new CalculatorFrame();
            new CalculatorController(model, frame);
            frame.setVisible(true);
        });
    }
}
