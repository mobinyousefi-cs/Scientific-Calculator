package com.mobinyousefi.scientificcalculator.controller;

import com.mobinyousefi.scientificcalculator.model.CalculatorModel;
import com.mobinyousefi.scientificcalculator.model.CalculatorModel.BinaryOperator;
import com.mobinyousefi.scientificcalculator.model.CalculatorModel.UnaryOperator;
import com.mobinyousefi.scientificcalculator.view.CalculatorFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Connects the calculator view and model.
 */
public class CalculatorController implements ActionListener {

    private final CalculatorModel model;
    private final CalculatorFrame frame;

    public CalculatorController(CalculatorModel model, CalculatorFrame frame) {
        this.model = model;
        this.frame = frame;
        this.frame.registerActionListener(this);
        updateDisplay();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd == null || cmd.isEmpty()) {
            return;
        }

        if (isDigit(cmd)) {
            model.appendDigit(cmd.charAt(0));
        } else if (".".equals(cmd)) {
            model.appendDecimalPoint();
        } else if ("+".equals(cmd)) {
            model.applyBinaryOperator(BinaryOperator.ADD);
        } else if ("-".equals(cmd)) {
            model.applyBinaryOperator(BinaryOperator.SUBTRACT);
        } else if ("*".equals(cmd)) {
            model.applyBinaryOperator(BinaryOperator.MULTIPLY);
        } else if ("/".equals(cmd)) {
            model.applyBinaryOperator(BinaryOperator.DIVIDE);
        } else if ("x^y".equals(cmd)) {
            model.applyBinaryOperator(BinaryOperator.POWER);
        } else if ("=".equals(cmd)) {
            model.evaluate();
        } else if ("C".equals(cmd)) {
            model.clearAll();
        } else if ("CE".equals(cmd)) {
            model.clearEntry();
        } else if ("<-".equals(cmd)) {
            model.backspace();
        } else if ("+/-".equals(cmd)) {
            model.applyUnaryOperator(UnaryOperator.NEGATE);
        } else if ("sqrt".equals(cmd)) {
            model.applyUnaryOperator(UnaryOperator.SQRT);
        } else if ("x^2".equals(cmd)) {
            model.applyUnaryOperator(UnaryOperator.SQUARE);
        } else if ("1/x".equals(cmd)) {
            model.applyUnaryOperator(UnaryOperator.RECIPROCAL);
        } else if ("%".equals(cmd)) {
            model.applyUnaryOperator(UnaryOperator.PERCENT);
        } else if ("sin".equals(cmd)) {
            model.applyUnaryOperator(UnaryOperator.SIN);
        } else if ("cos".equals(cmd)) {
            model.applyUnaryOperator(UnaryOperator.COS);
        } else if ("tan".equals(cmd)) {
            model.applyUnaryOperator(UnaryOperator.TAN);
        } else if ("log".equals(cmd)) {
            model.applyUnaryOperator(UnaryOperator.LOG10);
        } else if ("ln".equals(cmd)) {
            model.applyUnaryOperator(UnaryOperator.LN);
        } else if ("π".equals(cmd)) {
            model.insertConstant(Math.PI);
        } else if ("e".equals(cmd)) {
            model.insertConstant(Math.E);
        } else {
            // Unknown command: ignore for now
        }

        updateDisplay();
    }

    private boolean isDigit(String cmd) {
        return cmd.length() == 1 && Character.isDigit(cmd.charAt(0));
    }

    private void updateDisplay() {
        frame.setDisplayText(model.getDisplayValue());
    }
}
