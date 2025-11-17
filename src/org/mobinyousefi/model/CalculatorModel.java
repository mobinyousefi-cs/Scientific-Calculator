package com.mobinyousefi.scientificcalculator.model;

/**
 * Core computation model for the scientific calculator.
 *
 * This class is intentionally stateful and mimics the behaviour of
 * a physical calculator: it keeps track of the current input, a stored
 * accumulator value, and a pending binary operator.
 */
public class CalculatorModel {

    public enum BinaryOperator {
        ADD, SUBTRACT, MULTIPLY, DIVIDE, POWER
    }

    public enum UnaryOperator {
        SQRT, SQUARE, RECIPROCAL, SIN, COS, TAN, LOG10, LN, NEGATE, PERCENT
    }

    private String currentInput;
    private double storedValue;
    private BinaryOperator pendingOperator;
    private boolean resetInputOnNextDigit;
    private boolean errorState;

    public CalculatorModel() {
        clearAll();
    }

    /**
     * Resets all internal state.
     */
    public void clearAll() {
        currentInput = "0";
        storedValue = 0.0;
        pendingOperator = null;
        resetInputOnNextDigit = false;
        errorState = false;
    }

    /**
     * Clears only the current entry (similar to CE on a desktop calculator).
     */
    public void clearEntry() {
        currentInput = "0";
        errorState = false;
    }

    /**
     * Deletes the last character of the current input.
     */
    public void backspace() {
        if (resetInputOnNextDigit || errorState) {
            return;
        }
        if (currentInput.length() > 1) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
        } else {
            currentInput = "0";
        }
    }

    /**
     * Appends a digit to the current input.
     */
    public void appendDigit(char digit) {
        if (!Character.isDigit(digit)) {
            throw new IllegalArgumentException("Digit expected");
        }
        if (resetInputOnNextDigit || errorState) {
            currentInput = "0";
            resetInputOnNextDigit = false;
            errorState = false;
        }
        if ("0".equals(currentInput)) {
            currentInput = String.valueOf(digit);
        } else {
            currentInput += digit;
        }
    }

    /**
     * Adds a decimal point to the current input if it does not already have one.
     */
    public void appendDecimalPoint() {
        if (resetInputOnNextDigit || errorState) {
            currentInput = "0";
            resetInputOnNextDigit = false;
            errorState = false;
        }
        if (!currentInput.contains(".")) {
            currentInput += ".";
        }
    }

    /**
     * Applies or queues a binary operator (like +, -, *, /, x^y).
     */
    public void applyBinaryOperator(BinaryOperator operator) {
        if (errorState) {
            return;
        }
        double inputValue = parseCurrentInput();
        if (pendingOperator == null) {
            storedValue = inputValue;
        } else if (!resetInputOnNextDigit) {
            storedValue = computeBinary(pendingOperator, storedValue, inputValue);
            currentInput = formatNumber(storedValue);
        }
        pendingOperator = operator;
        resetInputOnNextDigit = true;
    }

    /**
     * Finalises the current pending binary operation.
     */
    public void evaluate() {
        if (errorState) {
            return;
        }
        if (pendingOperator == null) {
            return;
        }
        double inputValue = parseCurrentInput();
        storedValue = computeBinary(pendingOperator, storedValue, inputValue);
        currentInput = formatNumber(storedValue);
        pendingOperator = null;
        resetInputOnNextDigit = true;
    }

    /**
     * Applies a unary operation (sqrt, sin, cos, tan, log, ln, etc.)
     * directly to the current input value.
     */
    public void applyUnaryOperator(UnaryOperator operator) {
        if (errorState) {
            return;
        }
        double inputValue = parseCurrentInput();
        double result;
        switch (operator) {
            case SQRT:
                if (inputValue < 0) {
                    setError();
                    return;
                }
                result = Math.sqrt(inputValue);
                break;
            case SQUARE:
                result = inputValue * inputValue;
                break;
            case RECIPROCAL:
                if (inputValue == 0.0) {
                    setError();
                    return;
                }
                result = 1.0 / inputValue;
                break;
            case SIN:
                // Trigonometric functions are interpreted in DEGREES by default.
                result = Math.sin(Math.toRadians(inputValue));
                break;
            case COS:
                result = Math.cos(Math.toRadians(inputValue));
                break;
            case TAN:
                result = Math.tan(Math.toRadians(inputValue));
                break;
            case LOG10:
                if (inputValue <= 0.0) {
                    setError();
                    return;
                }
                result = Math.log10(inputValue);
                break;
            case LN:
                if (inputValue <= 0.0) {
                    setError();
                    return;
                }
                result = Math.log(inputValue);
                break;
            case NEGATE:
                result = -inputValue;
                break;
            case PERCENT:
                result = inputValue / 100.0;
                break;
            default:
                throw new IllegalStateException("Unexpected operator: " + operator);
        }
        currentInput = formatNumber(result);
        resetInputOnNextDigit = true;
    }

    /**
     * Replaces the current input with a mathematical constant (e.g., π, e).
     */
    public void insertConstant(double constantValue) {
        currentInput = formatNumber(constantValue);
        resetInputOnNextDigit = true;
        errorState = false;
    }

    private double parseCurrentInput() {
        try {
            return Double.parseDouble(currentInput);
        } catch (NumberFormatException ex) {
            setError();
            return 0.0;
        }
    }

    private double computeBinary(BinaryOperator operator, double left, double right) {
        double result;
        switch (operator) {
            case ADD:
                result = left + right;
                break;
            case SUBTRACT:
                result = left - right;
                break;
            case MULTIPLY:
                result = left * right;
                break;
            case DIVIDE:
                if (right == 0.0) {
                    setError();
                    return 0.0;
                }
                result = left / right;
                break;
            case POWER:
                result = Math.pow(left, right);
                break;
            default:
                throw new IllegalStateException("Unknown operator: " + operator);
        }
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            setError();
            return 0.0;
        }
        return result;
    }

    private void setError() {
        errorState = true;
        currentInput = "Error";
        storedValue = 0.0;
        pendingOperator = null;
        resetInputOnNextDigit = true;
    }

    private String formatNumber(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return "Error";
        }
        String text = String.format(java.util.Locale.US, "%.12f", value);
        // Trim trailing zeros and an eventual decimal point
        int i = text.length() - 1;
        while (i > 0 && text.charAt(i) == '0') {
            i--;
        }
        if (text.charAt(i) == '.') {
            i--;
        }
        return text.substring(0, i + 1);
    }

    /**
     * @return current value to be shown on the calculator display.
     */
    public String getDisplayValue() {
        return currentInput;
    }
}
