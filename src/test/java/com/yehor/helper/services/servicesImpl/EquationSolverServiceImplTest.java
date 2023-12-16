package com.yehor.helper.services.servicesImpl;

import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

class EquationSolverServiceImplTest {

    @Test
    void processExamples_From_TestTask() {
        String[] validExampleEquations = {
                "2*x+5=17", "-1.3*5/x=1.2", "2*x*x=10", "2*(x+5+x)+5=10", "17=2*x+5"
        };

        Double[] roots = {
                6.0, -5.41666666667, 2.2360679775, -1.25, 6.0
        };

        for (int i = 0; i < validExampleEquations.length; i++) {
            assertTrue(EquationSolverServiceImpl.solve(
                    validExampleEquations[i],
                    roots[i]
            ));
        }
    }

    @Test
    void validate_ValidEquationOperators_PlusMinus_ReturnsTrue() {
        String validEquation = "2*x+5+-1";
        assertTrue(EquationSolverServiceImpl.validate(validEquation));
    }

    @Test
    void validate_ValidEquationOperators_PlusPlus_ReturnsTrue() {
        String validEquation = "2*x+5++1";
        assertTrue(EquationSolverServiceImpl.validate(validEquation));
    }

    @Test
    void validate_ValidEquationOperators_MultiplyPlus_ReturnsTrue() {
        String validEquation = "2*x+5*+1";
        assertTrue(EquationSolverServiceImpl.validate(validEquation));
    }

    @Test
    void validate_ValidEquationOperators_MultiplyMinus_ReturnsTrue() {
        String validEquation = "2*x+5*-1";
        assertTrue(EquationSolverServiceImpl.validate(validEquation));
    }

    @Test
    void validate_ValidEquationOperators_DividePlus_ReturnsTrue() {
        String validEquation = "2*x+5/+1";
        assertTrue(EquationSolverServiceImpl.validate(validEquation));
    }

    @Test
    void validate_ValidEquationOperators_DivideMinus_ReturnsTrue() {
        String validEquation = "2*x+5/-1";
        assertTrue(EquationSolverServiceImpl.validate(validEquation));
    }

    @Test
    void validate_ValidEquationOperators_PlusMinus_WithNegativeRoot_ReturnsTrue() {
        int root = -1;
        String validEquation = "2*x+5+-" + root;
        assertTrue(EquationSolverServiceImpl.validate(validEquation));
    }

    @Test
    void validate_ValidEquationOperators_PlusPlus_WithNegativeRoot_ReturnsTrue() {
        int root = -1;
        String validEquation = "2*x+5++" + root;
        assertTrue(EquationSolverServiceImpl.validate(validEquation));
    }

    @Test
    void validate_ValidEquationOperators_MinusMinus_WithNegativeRoot_ReturnsTrue() {
        int root = -1;
        String validEquation = "2*x+5--" + root;
        assertTrue(EquationSolverServiceImpl.validate(validEquation));
    }

    @Test
    void validate_InvalidEquationOperators_ReturnsFalse() {
        String invalidEquation = "2*x+5+17-*-1";
        assertFalse(EquationSolverServiceImpl.validate(invalidEquation));
    }

    @Test
    void preValidate_ValidExpression_LHS_ReturnsTrue() {
        String validExpression = "2*x+5=17";
        assertTrue(EquationSolverServiceImpl.preValidate(validExpression));
    }

    @Test
    void preValidate_ValidExpression_RHS_ReturnsTrue() {
        String validExpression = "17=2*x+5";
        assertTrue(EquationSolverServiceImpl.preValidate(validExpression));
    }

    @Test
    void preValidate_ValidExpression_Both_ReturnsTrue() {
        String validExpression = "17--77*x=2*x+5";
        assertTrue(EquationSolverServiceImpl.preValidate(validExpression));
    }

    @Test
    void preValidate_InvalidExpression_ReturnsFalse() {
        String invalidExpression = "17-*-x=2*x+5";
        assertFalse(EquationSolverServiceImpl.preValidate(invalidExpression));
    }

    @Test
    void solveRHS_ValidEquationAndRoot_ReturnsTrue() {
        String equation = "17=2*x+5";
        double x = 6.0;
        assertTrue(EquationSolverServiceImpl.solveRHS(equation.split("="), x));
    }

    @Test
    void solveRHS_InvalidEquationAndRoot_ReturnsFalse() {
        String equation = "17=2*x+*5";
        double x = 3.0;
        assertThrows(EmptyStackException.class, () -> {
            EquationSolverServiceImpl.solveRHS(equation.split("="), x);
        });
    }

    @Test
    void solveRHS_InvalidEquation_ButInvalidRoot_ReturnsFalse() {
        String equation = "17=2*x+5";
        double x = 3.0;
        assertFalse(EquationSolverServiceImpl.solveRHS(equation.split("="), x));
    }

    @Test
    void solveLHS_ValidEquationAndRoot_ReturnsTrue() {
        String equation = "2*x+5=17";
        double x = 6.0;
        assertTrue(EquationSolverServiceImpl.solveLHS(equation.split("="), x));
    }

    @Test
    void solveLHS_InvalidEquationAndRoot_ReturnsFalse() {
        String equation = "2+*x+5=17";
        double x = 11.0;
        assertThrows(EmptyStackException.class, () -> {
            EquationSolverServiceImpl.solveLHS(equation.split("="), x);
        });
    }

    @Test
    void isNumeric_ValidNumber_ReturnsTrue() {
        String validNumber = "123.45";
        assertTrue(EquationSolverServiceImpl.isNumeric(validNumber));
    }

    @Test
    void isNumeric_ValidNumber_WithSpace_ReturnsTrue() {
        String validNumber = "   123";
        assertTrue(EquationSolverServiceImpl.isNumeric(validNumber));
    }

    @Test
    void isNumeric_InvalidNumber_ReturnsFalse() {
        String invalidNumber = "123.45a";
        assertFalse(EquationSolverServiceImpl.isNumeric(invalidNumber));
    }
}