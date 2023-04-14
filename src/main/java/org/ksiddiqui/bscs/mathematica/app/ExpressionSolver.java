package org.ksiddiqui.bscs.mathematica.app;

/******************************************************************************************
 *                                                                                         *
 *       Expression Solver Class ..........                                                *
 *                                                                                         *
 *******************************************************************************************/


public class ExpressionSolver {

    final String bracketError = "Bracketing Error";
    final String literalError = "Expression Literal Error";
    final String unknownError = "Unknown Error";


    public String Infix;
    public double Result;
    private String Postfix;
    private double[] Numbers;
    private int NumberElements;
    private boolean Degrees;

    /*****************************************************************************
     *            Constructors ...........                                        *
     *****************************************************************************/

    ExpressionSolver() {
        Numbers = new double[26];
        NumberElements = 0;
        Degrees = false;
    }

    ExpressionSolver(String infixStr) throws ExpressionSolverException {
        this(infixStr, false);
    }

    ExpressionSolver(String infixStr, boolean deg) throws ExpressionSolverException {
        Infix = new String(infixStr);
        Degrees = deg;
        Numbers = new double[26];
        NumberElements = 0;
        int check = 0;
        char chr;

        for (int i = 0; i < Infix.length(); i++) {
            chr = Infix.charAt(i);
            if (chr == '(') check++;
            else if (chr == ')') check--;
        }
        if (check != 0) throw new ExpressionSolverException(bracketError);

    }

    /*****************************************************************************
     *            Private Methods .......                                         *
     *****************************************************************************/

    private int Precession(String str) {
        int index;
        String operators = new String(")(-+*/^\\");
        String functions = new String("sctal");

        if ((index = operators.indexOf(str)) != -1) return (index);
        else if ((index = functions.indexOf(str)) != -1) return ((int) 10);

        return ((int) -1);
    }


    /*****************************************************************************
     *            Public Method : Infix to Postfix Convertor ....                 *
     *****************************************************************************/

    public int toPostfix() {
        Postfix = new String();
        String operands = new String("0123456789.");
        String operators = new String("-+*/^\\sctal");
        String number = new String();
        String str1, str2, str3;

        char[] opStack = new char[30];
        char[] infixCopy;
        char[] infix;
        char variable = 'A', tmpChar;

        boolean negFlag = false;
        int NumberIndex = 0, stackTop = -1, i = 0;
        int totalChars = Infix.length() + 2;
        int pre1 = 0, pre2 = 0;

        infix = Infix.toCharArray();
        infixCopy = new char[totalChars];
        infixCopy[0] = '(';
        for (i = 0; i < (totalChars - 2); i++) {
            infixCopy[i + 1] = infix[i];
        }
        infixCopy[i + 1] = ')';


        for (int count = 1; count < (totalChars - 1); count++) {
            tmpChar = infixCopy[count];
            str1 = new Character(infixCopy[count - 1]).toString();
            str2 = new Character(infixCopy[count + 0]).toString();
            str3 = new Character(infixCopy[count + 1]).toString();


            if (tmpChar == '(') {
                stackTop++;
                opStack[stackTop] = tmpChar;
            } else if (tmpChar == ')') {
                while (opStack[stackTop] != '(') {
                    Postfix = Postfix.concat(new Character(opStack[stackTop]).toString());
                    stackTop--;
                }
                stackTop--;
            } else if (tmpChar == '-' && (count == 1 || operators.indexOf(str1) != -1 || infixCopy[count - 1] == '(')) {
                negFlag = true;
            } else if (operands.indexOf(str2) != -1) {
                number = number.concat(new Character(tmpChar).toString());
                if (operators.indexOf(str3) != -1 || totalChars == (count + 2) || infixCopy[count + 1] == '(' || infixCopy[count + 1] == ')') {
                    Numbers[NumberIndex] = Double.parseDouble(number);
                    NumberIndex++;
                    number = new String();
                    Postfix = Postfix.concat(new Character(variable).toString());
                    variable++;
                    if (negFlag) {
                        Numbers[NumberIndex - 1] = Numbers[NumberIndex - 1] * (-1);
                        negFlag = false;
                    }
                }
            } else if (operators.indexOf(str2) != -1) {
                pre1 = Precession(str2);
                if (stackTop > -1) pre2 = Precession(new Character(opStack[stackTop]).toString());
                else pre2 = 0;

                while (pre1 < pre2 && stackTop > -1) {
                    Postfix = Postfix.concat(new Character(opStack[stackTop]).toString());
                    stackTop--;
                    if (stackTop > -1)
                        pre2 = Precession(new Character(opStack[stackTop]).toString());
                }
                stackTop++;
                if (pre1 < 10) opStack[stackTop] = tmpChar;
                else {
                    int inc = 0;
                    char ch = 'a';
                    char tmpChar1 = infixCopy[count + 1];
                    //check for each function literal in a else-if construction
                    if (tmpChar == 's' || tmpChar == 'c' || tmpChar == 't' || tmpChar == 'l') {
                        ch = tmpChar;
                        inc = 2;
                    } else if (tmpChar == 'a' && tmpChar1 == 's') {
                        ch = 'a';
                        inc = 3;
                    } else if (tmpChar == 'a' && tmpChar1 == 'c') {
                        ch = 'b';
                        inc = 3;
                    } else if (tmpChar == 'a' && tmpChar1 == 't') {
                        ch = 'd';
                        inc = 3;
                    } else if (tmpChar == 'a' && tmpChar1 == 'l') {
                        ch = 'e';
                        inc = 3;
                    }

                    count = count + inc;
                    opStack[stackTop] = ch;
                }
            }
        }

        while (stackTop > -1) {
            Postfix = Postfix.concat(new Character(opStack[stackTop]).toString());
            stackTop--;
        }
        NumberElements = NumberIndex - 1;
        return NumberElements;
    }

    /*****************************************************************************
     *            Public Method : Postfix Expression Evaluator ......             *
     *****************************************************************************/

    public double Evaluator() {
        String variables = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        double[] numbers = new double[15];
        double result = 0;
        int stackTop = -1, NumberIndex = 0;
        double mul = 1;
        double mulx;

        if (Degrees) mul = Math.PI / 180;
        mulx = 1 / mul;

        //System.out.print(Postfix+" : ");
        for (int count = 0; count < Postfix.length(); count++) {
            if (variables.indexOf(Postfix.substring(count, count + 1)) != -1) {
                stackTop++;
                numbers[stackTop] = Numbers[NumberIndex];
                NumberIndex++;
            } else {
                switch (Postfix.charAt(count)) {
                    case '-': {
                        result = numbers[stackTop - 1] - numbers[stackTop];
                        break;
                    }
                    case '+': {
                        result = numbers[stackTop - 1] + numbers[stackTop];
                        break;
                    }
                    case '*': {
                        result = numbers[stackTop - 1] * numbers[stackTop];
                        break;
                    }
                    case '/': {
                        result = numbers[stackTop - 1] / numbers[stackTop];
                        break;
                    }
                    case '^': {
                        result = Math.pow(numbers[stackTop - 1], numbers[stackTop]);
                        break;
                    }
                    case '\\': {
                        result = numbers[stackTop - 1] % numbers[stackTop];
                        break;
                    }

                    case 's': {
                        result = Math.sin(mul * numbers[stackTop]);
                        stackTop++;
                        break;
                    }
                    case 'c': {
                        result = Math.cos(mul * numbers[stackTop]);
                        stackTop++;
                        break;
                    }
                    case 't': {
                        result = Math.tan(mul * numbers[stackTop]);
                        stackTop++;
                        break;
                    }
                    case 'l': {
                        result = Math.log(numbers[stackTop]);
                        stackTop++;
                        break;
                    }
                    case 'a': {
                        result = mulx * Math.asin(numbers[stackTop]);
                        stackTop++;
                        break;
                    }
                    case 'b': {
                        result = mulx * Math.acos(numbers[stackTop]);
                        stackTop++;
                        break;
                    }
                    case 'd': {
                        result = mulx * Math.atan(numbers[stackTop]);
                        stackTop++;
                        break;
                    }
                    case 'e': {
                        result = Math.exp(numbers[stackTop]);
                        stackTop++;
                        break;
                    }
                }
                stackTop = stackTop - 1;
                numbers[stackTop] = result;
            }
        }
        Result = result;
        return result;
    }

    public boolean isDegree() {
        return Degrees;
    }

    /*****************************************************************************
     *            Public Method : Setting and quering degree flag .......         *
     *****************************************************************************/

    public void setDegree(boolean d) {
        Degrees = d;
    }

    /*****************************************************************************
     *            Public Method : Setting new Infix Expression .......            *
     *****************************************************************************/

    public void Infix(String infixStr) throws ExpressionSolverException {
        Infix = new String(infixStr);
        int check = 0;
        char chr;

        for (int i = 0; i < Infix.length(); i++) {
            chr = Infix.charAt(i);
            if (chr == '(') check++;
            else if (chr == ')') check--;
        }
        if (check != 0) throw new ExpressionSolverException(bracketError);

    }

    /*****************************************************************************
     *            Public Method : Getting Postfix Notation                        *
     *****************************************************************************/

    public String Postfix() {
        StringBuffer postfixStr = new StringBuffer();
        String variables = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        int NumberIndex = 0;

        for (int count = 0; count < Postfix.length(); count++) {
            postfixStr = postfixStr.append(' ');
            if (variables.indexOf(Postfix.substring(count, count + 1)) != -1) {
                postfixStr = postfixStr.append(Numbers[NumberIndex]);
                NumberIndex++;
            } else {
                switch (Postfix.charAt(count)) {
                    case '-': {
                        postfixStr = postfixStr.append('-');
                        break;
                    }
                    case '+': {
                        postfixStr = postfixStr.append('+');
                        break;
                    }
                    case '*': {
                        postfixStr = postfixStr.append('*');
                        break;
                    }
                    case '/': {
                        postfixStr = postfixStr.append('/');
                        break;
                    }
                    case '^': {
                        postfixStr = postfixStr.append('^');
                        break;
                    }
                    case '\\': {
                        postfixStr = postfixStr.append('\\');
                        break;
                    }

                    case 's': {
                        postfixStr = postfixStr.append("sin");
                        break;
                    }
                    case 'c': {
                        postfixStr = postfixStr.append("cos");
                        break;
                    }
                    case 't': {
                        postfixStr = postfixStr.append("tan");
                        break;
                    }
                    case 'l': {
                        postfixStr = postfixStr.append("log");
                        break;
                    }
                    case 'a': {
                        postfixStr = postfixStr.append("asin");
                        break;
                    }
                    case 'b': {
                        postfixStr = postfixStr.append("acos");
                        break;
                    }
                    case 'd': {
                        postfixStr = postfixStr.append("atan");
                        break;
                    }
                    case 'e': {
                        postfixStr = postfixStr.append("alog");
                        break;
                    }
                }
            }
        }
        return postfixStr.toString();
    }

// END OF CLASS
}

