package calculator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class InfixEval {
    public static String evaluateExpression(Stack<Object> exprStack, AngleType angleType) {
        Stack<Operator> oprStack = new Stack<Operator>();
        Stack<Object> numStack = new Stack<Object>();
        if (exprStack.isEmpty())
            return "";

        for (Object elem : exprStack) {
            if (elem instanceof Double || elem instanceof Long) {
                numStack.push(elem);
            } else if (elem instanceof Operator) {
                Operator operator = (Operator) elem;
                String opr = operator.opr;

                if (operator.operatorType == OperatorType.CONSTANT) {
                    numStack.push(operator.compute.operate(0, 0, angleType));
                    continue;
                }

                if (oprStack.isEmpty()) {
                    oprStack.push(operator);
                } else if (opr.equals("(")) {
                    oprStack.push(operator);
                } else if (opr.equals(")")) {
                    while (!oprStack.peek().opr.equals("(") && !oprStack.isEmpty()) {
                        process(numStack, oprStack, angleType);
                    }
                    if (!oprStack.isEmpty())
                        oprStack.pop();
                } else {
                    while (precedence(operator) < (oprStack.isEmpty() ? 0 : precedence(oprStack.peek()))
                            && !oprStack.isEmpty()) {
                        process(numStack, oprStack, angleType);
                    }
                    oprStack.push(operator);
                }
            }
            // System.out.println(i + " " + numStack);
            // System.out.println(i++ + " " + oprStack);
        }

        while (!oprStack.isEmpty()) {
            process(numStack, oprStack, angleType);
        }

        return Utils.toAppropriateNumberFormat(numStack.pop().toString()).toString();
    }

    private static void process(Stack<Object> numStack, Stack<Operator> oprStack, AngleType angleType) {
        Object operandA, operandB;
        Operator opr = oprStack.pop();

        try {
            if (!numStack.isEmpty())
                operandB = numStack.pop();
            else
                return;
            if (!numStack.isEmpty())
                operandA = numStack.pop();
            else
                operandA = 0;

            Double numA = Double.parseDouble(operandA.toString());
            Double numB = Double.parseDouble(operandB.toString());

            if (opr.operatorType == OperatorType.UNARY) {
                numStack.push(numA);
                numStack.push(opr.compute.operate(0, numB, angleType));
                // Logger.logSpaced(numStack);
                return;
            }

            numStack.push(opr.compute.operate(numA, numB, angleType));
        } catch (UnsupportedOperationException uoe) {
            Logger.error("Factorial of numbers greater than 20 cause overflow");
        } catch (Exception e) {
            Logger.error("Unexpected error occurred");
        }
    }

    private static int precedence(Operator opr) {
        ArrayList<String> oprs = new ArrayList<String>(Arrays.asList("@", // Parantheses placeholder
                "^", "x", "/", "%", "+", "-"));

        if (opr.opr.equals("(") || opr.opr.equals(")"))
            return 0;

        if (opr.opr.equals("!"))
            return 97;

        if (opr.operatorType == OperatorType.UNARY)
            return 96;

        if (!oprs.contains(opr.opr))
            return 95;

        return (90 - oprs.indexOf(opr.opr));
    }

}