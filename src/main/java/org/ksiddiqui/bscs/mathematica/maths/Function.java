package org.ksiddiqui.bscs.mathematica.maths;

import org.ksiddiqui.bscs.mathematica.maths.exceptions.FunctionException;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;


/******************************************************************************************
 *                                                                                         *
 *       Function class ..........                                                         *
 *                                                                                         *
 *******************************************************************************************/


public class Function implements Serializable {

    final String bracketError = "Bracketing Error";
    final String literalError = "Expression Literal Error";
    final String expressionError = "Expression invalid format Error";
    final String variableError = "Invalid variable Error";
    final String variablesError = "Invalid variable-value pair Error";
    final String noVariableError = "No variable Error";
    final String noParsedExpressinoError = "No parsed expression Error";
    final String unknownError = "Unknown Error";

    final private char numberRepresentative = '\u0200';

    public String Infix;
    public double Result;
    private String Postfix;
    private Vector Numbers;
    private boolean Degrees;

    /*****************************************************************************
     *            Constructors ...........                                        *
     *****************************************************************************/


    public Function(String infixStr) throws Exception {
        this(infixStr, false);
    }

    Function(String infixStr, boolean deg) throws Exception {
        Infix = new String(infixStr);
        Degrees = deg;
        Numbers = new Vector();
        int check = 0;
        char chr;

        for (int i = 0; i < Infix.length(); i++) {
            chr = Infix.charAt(i);

            if (chr == '(') check++;
            else if (chr == ')') check--;
        }
        if (check != 0) throw new FunctionException(bracketError);

        toPostfix();
    }

    /*****************************************************************************
     *            Private Methods .......                                         *
     *****************************************************************************/

    private int Precession(Character chra) {
        char chr = chra.charValue();

        return Precession(chr);
    }

    private int Precession(char chr) {
        int index;
        String operators = new String(")(+-*/^\\");
        String functions = new String("sctal");

        if ((index = operators.indexOf(chr)) != -1) return (index);
        else if ((index = functions.indexOf(chr)) != -1) return (10);

        return ((int) -1);
    }


    /*****************************************************************************
     *            Public Method : Infix to Postfix Convertor ....                 *
     *****************************************************************************/

    public void toPostfix() throws Exception {
        Postfix = new String();
        String operands = new String("0123456789.");
        String operators = new String("-+*/^\\sctal");
        String number = new String();
        String str1, str2, str3;

        Stack opStack = new Stack();
        char[] infixCopy;
        char[] infix;
        char variable = numberRepresentative, tmpChar;

        boolean negFlag = false;
        int i = 0;
        int totalChars = Infix.length() + 2;
        int pre1 = 0, pre2 = 0;
        int state = 0;

        infix = Infix.toCharArray();
        infixCopy = new char[totalChars];
        infixCopy[0] = '(';
        for (i = 0; i < (totalChars - 2); i++) {
            infixCopy[i + 1] = infix[i];
        }
        infixCopy[i + 1] = ')';


        for (int count = 1; count < (totalChars - 1); count++)
            /*START*/ {
            tmpChar = infixCopy[count];
            str1 = Character.toString(infixCopy[count - 1]);
            str2 = Character.toString(infixCopy[count + 0]);
            str3 = Character.toString(infixCopy[count + 1]);


            /**/
            if (tmpChar == '(') {
                if (state == 2 || state == 3 || state == 4)
                    throw new FunctionException(expressionError);

                opStack.push(tmpChar);
                state = 1;
            }
            /**/
            else if (tmpChar == ')') {
                if (state == 5)
                    throw new FunctionException(expressionError);

                char chr = (Character) opStack.pop();
                while (chr != '(') {
                    Postfix = Postfix.concat(String.valueOf(chr));
                    chr = (Character) opStack.pop();
                }

                state = 2;
            }
            /**/
            else if (tmpChar == '-' && (count == 1 || state == 5 || state == 1)) {
                negFlag = true;
            }
            /**/
            else if (tmpChar >= 'A' && tmpChar <= 'Z') {
                if (state == 2 || state == 3 || state == 4)
                    throw new FunctionException(expressionError);

                Postfix = Postfix.concat(str2);
                state = 3;
            }
            /**/
            else if (operands.indexOf(str2) != -1) {
                if (state == 2 || state == 3)
                    throw new FunctionException(expressionError);

                number = "";
                String power = "0";
                boolean isSciNotation = false, isReal = false, isPowerSign = false;
                while (true) {
                    if (isSciNotation) power = power + String.valueOf(tmpChar);
                    else if (!isSciNotation) number = number + String.valueOf(tmpChar);
                    count++;

                    tmpChar = infixCopy[count];
                    if (tmpChar == 'E' && !isSciNotation) {
                        isSciNotation = true;
                        power = "";
                        count++;
                        tmpChar = infixCopy[count];
                    } else if (tmpChar == 'E' && isSciNotation) throw new FunctionException(expressionError);
                    else if (tmpChar == '.' && !isReal && !isSciNotation) isReal = true;
                    else if (tmpChar == '.' && (isReal || isSciNotation)) throw new FunctionException(expressionError);
                    else if (operators.indexOf(String.valueOf(tmpChar)) != -1 || tmpChar == ')') {
                        count--;
                        break;
                    }
                }
                double numberX = Double.parseDouble(number);
                Postfix = Postfix.concat(String.valueOf(variable));
                variable++;
                if (negFlag) {
                    numberX = numberX * (-1);
                    negFlag = false;
                }
                numberX = numberX * Math.pow(10, Double.parseDouble(power));
                Numbers.addElement(numberX);

                state = 4;
            }
            /**/
            else if (operators.indexOf(str2) != -1) {
                pre1 = Precession(tmpChar);
                if ((state == 1 || state == 5) && pre1 < 10) throw new FunctionException(expressionError);

                if (!opStack.empty()) pre2 = Precession((Character) opStack.peek());
                else pre2 = 0;

                while (pre1 < pre2 && !opStack.empty()) {
                    Postfix = Postfix.concat(((Character) opStack.pop()).toString());
                    if (!opStack.empty())
                        pre2 = Precession((Character) opStack.peek());
                }
                if (pre1 < 10) opStack.push(tmpChar);
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
                    opStack.push(ch);
                }
                state = 5;
            }
            /*END*/
        }

        while (!opStack.empty()) {
            Postfix = Postfix.concat(((Character) opStack.pop()).toString());
        }
    }

    /*****************************************************************************
     *            Public Method : Postfix Expression Evaluator ......             *
     *****************************************************************************/

    public double evaluator(char[] variables, double[] values) throws Exception {
        Hashtable ht = new Hashtable();
        Character htKey = null;
        Double htValue = null;
        double resultValue = 0;

        if (variables.length != values.length) throw new FunctionException(variablesError);

        for (int i = 0; i < variables.length; i++) {
            htKey = variables[i];
            htValue = values[i];
            ht.put(htKey, htValue);
        }

        resultValue = evaluator(ht);

        return resultValue;
    }

    public double evaluator(Map values) throws Exception {
        if (Postfix == null || Postfix.length() == 0) throw new FunctionException(noParsedExpressinoError);

        Stack numbers = new Stack();
        double result = 0;
        int NumberIndex = 0;
        double mul = 1;
        double mulx;
        Character chrObj;

        if (Degrees) mul = Math.PI / 180;
        mulx = 1 / mul;

        //System.out.print(Postfix+" : ");
        for (int count = 0; count < Postfix.length(); count++) {
            if (Postfix.charAt(count) >= 'A' && Postfix.charAt(count) <= 'Z') {
                chrObj = Postfix.charAt(count);
                numbers.push(values.get(chrObj));
            } else if (Postfix.charAt(count) >= numberRepresentative) {
                numbers.push(Numbers.get(NumberIndex));
                NumberIndex++;
            } else {
                double n1 = (Double) numbers.pop();
                double n2 = 0;
                if ("-+*/^\\".indexOf(Postfix.charAt(count)) != -1)
                    n2 = (Double) numbers.pop();

                switch (Postfix.charAt(count)) {
                    case '-' -> {
                        result = n2 - n1;
                    } case '+' -> {
                        result = n2 + n1;
                    } case '*' -> {
                        result = n2 * n1;
                    } case '/' -> {
                        result = n2 / n1;
                    } case '^' -> {
                        result = Math.pow(n2, n1);
                    } case '\\' -> {
                        result = n2 % n1;
                    } case 's' -> {
                        result = Math.sin(mul * n1);
                    } case 'c' -> {
                        result = Math.cos(mul * n1);
                    } case 't' -> {
                        result = Math.tan(mul * n1);
                    } case 'l' -> {
                        result = Math.log(n1);
                    } case 'a' -> {
                        result = mulx * Math.asin(n1);
                    } case 'b' -> {
                        result = mulx * Math.acos(n1);
                    } case 'd' -> {
                        result = mulx * Math.atan(n1);
                    } case 'e' -> {
                        result = Math.exp(n1);
                    }
                }
                numbers.push(result);
            }
        }
        result = (Double) numbers.pop();
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
     *            Public Method : Getting Postfix Notation                        *
     *****************************************************************************/

    public String getPostfix() throws Exception {
        if (Postfix == null || Postfix.length() == 0) throw new FunctionException(noParsedExpressinoError);

        StringBuffer postfixStr = new StringBuffer();
        int numberIndex = 0;

        for (int count = 0; count < Postfix.length(); count++) {
            postfixStr = postfixStr.append(' ');

            if (Postfix.charAt(count) >= 'A' && Postfix.charAt(count) <= 'Z') {
                postfixStr = postfixStr.append(String.valueOf(Postfix.charAt(count)));
            } else if (Postfix.charAt(count) >= numberRepresentative) {
                postfixStr = postfixStr.append(((Double) Numbers.get(numberIndex)).toString());
                numberIndex++;
            } else {
                switch (Postfix.charAt(count)) {
                    case '-' -> {
                        postfixStr = postfixStr.append('-');
                    } case '+' -> {
                        postfixStr = postfixStr.append('+');
                    } case '*' -> {
                        postfixStr = postfixStr.append('*');
                    } case '/' -> {
                        postfixStr = postfixStr.append('/');
                    } case '^' -> {
                        postfixStr = postfixStr.append('^');
                    } case '\\' -> {
                        postfixStr = postfixStr.append('\\');
                    } case 's' -> {
                        postfixStr = postfixStr.append("Sin");
                    } case 'c' -> {
                        postfixStr = postfixStr.append("Cos");
                    } case 't' -> {
                        postfixStr = postfixStr.append("Tan");
                    } case 'l' -> {
                        postfixStr = postfixStr.append("Log");
                    } case 'a' -> {
                        postfixStr = postfixStr.append("aSin");
                    } case 'b' -> {
                        postfixStr = postfixStr.append("aCos");
                    } case 'd' -> {
                        postfixStr = postfixStr.append("aTan");
                    } case 'e' -> {
                        postfixStr = postfixStr.append("aLog");
                    }
                }
            }
        }
        return postfixStr.toString();
    }

    /*****************************************************************************
     *            Public Method : Getting varaibles in the function ......        *
     *****************************************************************************/

    public char[] getVariables() throws Exception {
        char[] var = new char[26];
        char[] variables = new char[0];
        char tmpChar;
        int len = Postfix.length();
        int count = 0, j;

        for (int i = 0; i < len; i++) {
            tmpChar = Postfix.charAt(i);
            if (tmpChar >= 'A' && tmpChar <= 'Z') {
                for (j = 0; j < count; j++) {
                    if (var[j] == tmpChar) break;
                }
                if (j == count) {
                    var[count] = tmpChar;
                    count++;
                }
            }
        }

        if (count > 0) {
            variables = new char[count];
            for (int i = 0; i < count; i++) variables[i] = var[i];
        }

        return variables;
    }


    public boolean isAnyVariable() throws Exception {
        boolean isVariable = false;
        int len = Infix.length();
        char tmpChar;

        for (int i = 0; i < len; i++) {
            tmpChar = Infix.charAt(i);
            if (tmpChar >= 'A' && tmpChar <= 'Z') {
                isVariable = true;
                break;
            }
        }

        return isVariable;
    }

    /*****************************************************************************
     *            Public Method : Derivative of the function ....                 *
     *****************************************************************************/


    public double firstDerivative(char[] variables, double[] values, char variable, double h) throws Exception {
        Hashtable ht = new Hashtable();
        Character htKey = null;
        Double htValue = null;
        double resultValue = 0;

        if (variables.length != values.length) throw new FunctionException(variablesError);

        for (int i = 0; i < variables.length; i++) {
            htKey = variables[i];
            htValue = values[i];
            ht.put(htKey, htValue);
        }

        resultValue = firstDerivative(ht, variable, h);

        return resultValue;
    }

    public double firstDerivative(Hashtable values, char variable, double h) throws Exception {
        Character key = variable;
        if (values == null) throw new FunctionException(noVariableError);
        if (!values.containsKey(key)) throw new FunctionException(variableError);
        if (Postfix == null || Postfix.length() == 0) throw new FunctionException(noParsedExpressinoError);

        Double _value = (Double) values.get(key);
        double atValue1, atValue2;
        atValue1 = _value - h;
        atValue2 = _value + h;

        values.remove(key);
        values.put(key, atValue1);
        double valueAt1 = evaluator(values);

        values.remove(key);
        values.put(key, atValue2);
        double valueAt2 = evaluator(values);

        double result = (valueAt2 - valueAt1) / (2 * h);

        return result;
    }

    public double secondDerivative(char[] variables, double[] values, char variable, double h) throws Exception {
        Hashtable ht = new Hashtable();
        Character htKey = null;
        Double htValue = null;
        double resultValue = 0;

        if (variables.length != values.length) throw new FunctionException(variablesError);

        for (int i = 0; i < variables.length; i++) {
            htKey = variables[i];
            htValue = values[i];
            ht.put(htKey, htValue);
        }

        resultValue = secondDerivative(ht, variable, h);

        return resultValue;

    }

    public double secondDerivative(Hashtable values, char variable, double h) throws Exception {
        Character key = variable;
        if (values == null) throw new FunctionException(noVariableError);
        if (!values.containsKey(key)) throw new FunctionException(variableError);
        if (Postfix == null || Postfix.length() == 0) throw new FunctionException(noParsedExpressinoError);

        Double _value = (Double) values.get(key);
        double atValue1, atValue2, atValue3;
        atValue1 = _value - h;
        atValue2 = _value;
        atValue3 = _value + h;

        values.remove(key);
        values.put(key, atValue1);
        double valueAt1 = evaluator(values);

        values.remove(key);
        values.put(key, atValue2);
        double valueAt2 = evaluator(values);

        values.remove(key);
        values.put(key, atValue3);
        double valueAt3 = evaluator(values);

        double result = (valueAt1 - (2 * valueAt2) + valueAt3) / (h * h);

        return result;
    }


    /*****************************************************************************
     *            Public Method : Misc. function ....                             *
     *****************************************************************************/

    public String toString() {
        return Infix;
    }

// END OF CLASS
}


