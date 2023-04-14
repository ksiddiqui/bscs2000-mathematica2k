package org.ksiddiqui.bscs.mathematica.app;

public class ExpressionSolverException extends Exception {

    static String exceptionNumber;

    ExpressionSolverException(String exceptionNo) {
        exceptionNumber = exceptionNo;
    }

    public String getException() {
        return exceptionNumber;
    }
}
