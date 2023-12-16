package com.yehor.helper.services.servicesImpl;

import java.util.Stack;
import java.util.Objects;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EquationSolverServiceImpl {

    private static String sanitize(String equation, double x) {

        // Check before X replacing
        // It ensures that 5++-1 will not corrupt the program
        // when x=-1 and expression is 5++x etc.
        equation = equation.replaceAll("\\+-", "-"); // Handling +- as -
        equation = equation.replaceAll("\\++", "+"); // Handling ++ as +
        equation = equation.replaceAll("--", "+"); // Handling -- as +

        equation = equation.replaceAll("x", String.valueOf(x));

        // Check after X replacing
        equation = equation.replaceAll("\\+-", "-"); // Handling +- as -
        equation = equation.replaceAll("\\++", "+"); // Handling ++ as +
        equation = equation.replaceAll("--", "+"); // Handling -- as +
        equation = equation.replaceAll("/-", "/(-1)/"); // Handling division by negative as multiplication by -1
        equation = equation.replaceAll("\\*-", "*(-1)*"); // Handling multiplication by negative as multiplication by -1
        equation = equation.replaceAll("/\\+", "/1*"); // Handling division by positive as multiplication by 1
        equation = equation.replaceAll("\\*\\+", "*1*"); // Handling multiplication by positive as multiplication by 1s
        return equation;
    }

    public static boolean validate(String equation) {
        // Check if brackets and operators are written correctly
        return checkParentheses(equation) && checkOperators(equation);
    }

    private static boolean checkParentheses(String equation) {
        int openCount = 0;
        for (char ch : equation.toCharArray()) {
            if (ch == '(') {
                openCount++;
            } else if (ch == ')') {
                if (openCount == 0) {
                    return false; // closed bracket without the correspondent open one
                }
                openCount--;
            }
        }
        return openCount == 0; // Returns true, if the quantity of open and closed brackets is equal
    }

    private static boolean checkOperators(String equation) {
        // Check correctness of operators
        // Returns true, if operators are written correctly, else - false
        for (int i = 0; i < equation.length() - 1; i++) {
            char current = equation.charAt(i);
            char next = equation.charAt(i + 1);

            // Check sequential operators (except for - і +, which are allowed)
            if (("+-*/".indexOf(current) != -1 && "*/".indexOf(next) != -1) ||
                    ("-".indexOf(current) != -1 && "*/+".indexOf(next) != -1)) {
                return false;
            }
        }
        return true;
    }

    private static double evaluateExpression(String expression) {
        // log.info(expression);
        Stack<Double> operands = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length()
                        && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                i--;

                operands.push(Double.parseDouble(sb.toString()));
            } else if (c == '(') {
                operators.push(c);
            } else if (c == ')') {
                while (operators.peek() != '(') {
                    double result = applyOperator(
                            operators.pop(),
                            operands.pop(),
                            operands.pop());

                    operands.push(result);
                }
                operators.pop();
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (c == '-' && (i == 0 || expression.charAt(i - 1) == '(')) {
                    operands.push(0.0); // Add 0 before unary minus
                }

                while (!operators.empty() && hasPrecedence(c, operators.peek())) {
                    char arg1 = operators.pop();
                    double arg2 = operands.pop();
                    double arg3 = operands.pop();

                    double result = applyOperator(
                            arg1,
                            arg2,
                            arg3);

                    operands.push(result);
                }
                operators.push(c);
            }
        }

        while (!operators.empty()) {
            double result = applyOperator(operators.pop(), operands.pop(), operands.pop());
            operands.push(result);
        }

        return operands.pop();
    }

    private static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return (op1 != '*' && op1 != '/') || (op2 != '+' && op2 != '-');
    }

    private static double applyOperator(char operator, double b, double a) {

        switch (operator) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
        }

        return 0.0;
    }

    public static boolean preValidate(String expression) {
        // Performs only validation part
        String[] splittedExpr = expression.split("=");

        if (!validate(splittedExpr[0]) || !validate(splittedExpr[1]) ||
                Objects.isNull(splittedExpr[0]) || Objects.isNull(splittedExpr[1])) {
            log.info(expression + " - is not valid");
            return false;
        }

        return true;
    }

    public static boolean solve(String expression, double x) {
        String[] splittedExpr = expression.split("=");

        if(splittedExpr.length > 2) {
            log.info(expression + " - is not valid");
            return false;
        }

        if (!validate(splittedExpr[0]) || !validate(splittedExpr[1]) ||
                Objects.isNull(splittedExpr[0]) || Objects.isNull(splittedExpr[1])) {
            log.info(expression + " - is not valid");
            return false;
        }

        return !isNumeric(splittedExpr[0]) ? solveLHS(splittedExpr, x) : solveRHS(splittedExpr, x);
    }

    public static boolean solveRHS(String[] splittedExpr, double x) {
        // Solves the right-hand side equation
        String equation = splittedExpr[1];

        // Sanitize the equation string
        equation = sanitize(equation, x);

        double result = 0;

        try {
            result = evaluateExpression(equation);
            log.info(equation);
        } catch (ArithmeticException e) {
            log.info(splittedExpr[1] + " - is not valid");
            e.printStackTrace();
            return false;
        }
        log.info("Result of computing " + equation + " = " + result);

        // Checks if the entered root is correct
        boolean isRoot = Math.abs(Double.parseDouble(splittedExpr[0]) - result) <= Math.pow(10, -9);
        log.info("isRoot: " + isRoot);
        return isRoot;
    }

    public static boolean solveLHS(String[] splittedExpr, double x) {
        // Solves the left-hand side equation
        String equation = splittedExpr[0];

        // Sanitize an equation string
        equation = sanitize(equation, x);

        double result = 0;

        try {
            result = evaluateExpression(equation);
            log.info(equation);
        } catch (ArithmeticException e) {
            log.info(splittedExpr[0] + " - is not valid");
            e.printStackTrace();
            return false;
        }
        log.info("Result of computing " + equation + " = " + result);

        // Checks if the entered root is correct
        boolean isRoot = Math.abs(Double.parseDouble(splittedExpr[1]) - result) <= Math.pow(10, -9);
        log.info("isRoot: " + isRoot);
        return isRoot;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
