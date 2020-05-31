package calculator;

import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

enum AngleType {
    DEGREE, RADIAN
}

interface Operation {
    Double operate(double a, double b, AngleType angleType);
}

enum OperatorType {
    UNARY, BINARY, NIL, CONSTANT
}

class ExpressionStore {
    String expression;
    Stack<Object> expressionStack;

    ExpressionStore(String expr, Stack<Object> stack) {
        this.expression = expr;
        this.expressionStack = stack;
    }
}

class IndexedElement {
    int start;
    int end;
    Object elem;

    IndexedElement(int start, int end, Object elem) {
        this.start = start;
        this.end = end;
        this.elem = elem;
    }

    public String toString() {
        return elem.toString() + " from " + start + " to " + end;
    }
}

class Operator {
    public String opr;
    public Operation compute;
    public OperatorType operatorType;
    private Factorial factorial;

    public String toString() {
        return opr;
    }

    public Operator(String opr, Factorial f) {
        this.opr = opr;
        this.factorial = f;

        switch (opr) {
            case "+":
                compute = (a, b, angle) -> a + b;
                operatorType = OperatorType.BINARY;
                break;
            case "-":
                compute = (a, b, angle) -> a - b;
                operatorType = OperatorType.BINARY;
                break;
            case "x":
                compute = (a, b, angle) -> a * b;
                operatorType = OperatorType.BINARY;
                break;
            case "/":
                compute = (a, b, angle) -> a / b;
                operatorType = OperatorType.BINARY;
                break;
            case "!":
                compute = (a, b, angle) -> (double) factorial.fact((int) b);
                operatorType = OperatorType.UNARY;
                break;
            case "%":
                compute = (a, b, angle) -> a % b;
                operatorType = OperatorType.BINARY;
                break;
            case "√":
                compute = (a, b, angle) -> Math.sqrt(b);
                operatorType = OperatorType.UNARY;
                break;
            case "^":
                compute = (a, b, angle) -> Math.pow(a, b);
                operatorType = OperatorType.BINARY;
                break;
            case "e":
                compute = (a, b, angle) -> Math.exp(1);
                operatorType = OperatorType.CONSTANT;
                break;
            case "π":
                compute = (a, b, angle) -> Math.PI;
                operatorType = OperatorType.CONSTANT;
                break;
            case "(":
                compute = (a, b, angle) -> a;
                operatorType = OperatorType.NIL;
                break;
            case ")":
                compute = (a, b, angle) -> a;
                operatorType = OperatorType.NIL;
                break;
            case "sin":
                compute = (a, b, angle) -> angle == AngleType.RADIAN ? Math.sin(b) : Math.sin(Math.toRadians(b));
                operatorType = OperatorType.UNARY;
                break;
            case "cos":
                compute = (a, b, angle) -> angle == AngleType.RADIAN ? Math.cos(b) : Math.cos(Math.toRadians(b));
                operatorType = OperatorType.UNARY;
                break;
            case "tan":
                compute = (a, b, angle) -> angle == AngleType.RADIAN ? Math.tan(b) : Math.tan(Math.toRadians(b));
                operatorType = OperatorType.UNARY;
                break;
            case "ln":
                compute = (a, b, angle) -> Math.log(b);
                operatorType = OperatorType.UNARY;
                break;
            case "log":
                compute = (a, b, angle) -> Math.log10(b);
                operatorType = OperatorType.UNARY;
                break;

            default:
                compute = (a, b, angle) -> null;
                operatorType = OperatorType.NIL;
        }
    }
}

public class Utils {
    public static Object toAppropriateNumberFormat(String num) {
        double doubleNum = Double.parseDouble(num);
        if (Math.floor(doubleNum) == doubleNum && !Double.isInfinite(doubleNum)) {
            return (long) doubleNum;
        }
        return doubleNum;
    }

    public static Stack<Object> parseExpression(Stack<Object> expressionStack, String currentExpression,
            Factorial factorial) {
        try {
            Pattern numPattern = Pattern.compile("\\d+\\.{0,1}\\d*");
            Pattern oprPattern = Pattern.compile("\\+|-|x|/|%|sin|cos|tan|ln|log|√|!|\\^|e|π|\\(|\\)");
            Pattern unaryOprPattern = Pattern.compile("^[-\\+]|(?<=[-\\+])[-\\+]|(?<=[\\(])[-\\+]");
            Matcher numMatcher = numPattern.matcher(currentExpression);
            Matcher oprMatcher = oprPattern.matcher(currentExpression);
            Matcher unaryOprMatcher = unaryOprPattern.matcher(currentExpression);
            ArrayList<IndexedElement> matchedNums = new ArrayList<IndexedElement>();
            ArrayList<IndexedElement> matchedOprs = new ArrayList<IndexedElement>();
            ArrayList<IndexedElement> matchedUnaryOprs = new ArrayList<IndexedElement>();
            ArrayList<Integer> unaryStarts = new ArrayList<Integer>();
            expressionStack.clear();
            while (numMatcher.find()) {
                for (int j = 0; j <= numMatcher.groupCount(); j++) {
                    matchedNums.add(new IndexedElement(numMatcher.start(), numMatcher.end() - 1,
                            Utils.toAppropriateNumberFormat(numMatcher.group(j))));
                }
            }

            while (unaryOprMatcher.find()) {
                for (int j = 0; j <= oprMatcher.groupCount(); j++) {
                    matchedUnaryOprs.add(new IndexedElement(unaryOprMatcher.start(), unaryOprMatcher.end() - 1,
                            unaryOprMatcher.group(j)));
                    unaryStarts.add(unaryOprMatcher.start());
                }
            }

            while (oprMatcher.find()) {
                for (int j = 0; j <= oprMatcher.groupCount(); j++) {
                    if (!unaryStarts.contains(oprMatcher.start()))
                        matchedOprs
                                .add(new IndexedElement(oprMatcher.start(), oprMatcher.end() - 1, oprMatcher.group(j)));
                }
            }

            Logger.log("Unary + and -: " + matchedUnaryOprs);
            Logger.log("Other operators: " + matchedOprs);

            ArrayList<IndexedElement> matchedNumsCopy = new ArrayList<IndexedElement>(matchedNums);
            ArrayList<IndexedElement> matchedOprsCopy = new ArrayList<IndexedElement>(matchedOprs);
            ArrayList<IndexedElement> matchedUnaryOprsCopy = new ArrayList<IndexedElement>(matchedUnaryOprs);
            while (!matchedNumsCopy.isEmpty() || !matchedOprsCopy.isEmpty() || !matchedUnaryOprsCopy.isEmpty()) {
                IndexedElement currentNum = null;
                IndexedElement currentOpr = null;
                IndexedElement currentUnaryOpr = null;

                if (!matchedOprsCopy.isEmpty()) {
                    currentOpr = matchedOprsCopy.get(0);
                }
                if (!matchedNumsCopy.isEmpty()) {
                    currentNum = matchedNumsCopy.get(0);
                }
                if (!matchedUnaryOprsCopy.isEmpty()) {
                    currentUnaryOpr = matchedUnaryOprsCopy.get(0);
                }

                if (currentNum != null) {
                    if ((currentNum.start < (currentOpr != null ? currentOpr.start : currentNum.start + 1))
                            && currentNum.start < (currentUnaryOpr != null ? currentUnaryOpr.start
                                    : currentNum.start + 1)) {
                        expressionStack.push(currentNum.elem);
                        matchedNumsCopy.remove(0);
                        currentNum = matchedNumsCopy.isEmpty() ? null : matchedNumsCopy.get(0);
                    }
                }

                if (currentOpr != null) {
                    if (currentOpr.start < (currentNum != null ? currentNum.start : currentOpr.start + 1)
                            && currentOpr.start < (currentUnaryOpr != null ? currentUnaryOpr.start
                                    : currentOpr.start + 1)) {
                        expressionStack.push(new Operator(String.valueOf(currentOpr.elem), factorial));
                        matchedOprsCopy.remove(0);
                        currentOpr = matchedOprsCopy.isEmpty() ? null : matchedOprsCopy.get(0);
                    }
                }

                if (currentUnaryOpr != null) {
                    if (currentUnaryOpr.start < (currentNum != null ? currentNum.start : currentUnaryOpr.start + 1)
                            && currentUnaryOpr.start < (currentOpr != null ? currentOpr.start
                                    : currentUnaryOpr.start + 1)) {
                        Operator opr = new Operator(String.valueOf(currentUnaryOpr.elem), factorial);
                        opr.operatorType = OperatorType.UNARY;
                        expressionStack.push(opr);
                        matchedUnaryOprsCopy.remove(0);
                        currentUnaryOpr = matchedUnaryOprsCopy.isEmpty() ? null : matchedUnaryOprsCopy.get(0);
                    }
                }
            }

            if (!matchedNumsCopy.isEmpty()) {
                for (IndexedElement num : matchedNumsCopy) {
                    expressionStack.push(num.elem);
                }
            }
            if (!matchedOprsCopy.isEmpty()) {
                for (IndexedElement opr : matchedOprsCopy) {
                    expressionStack.push(new Operator(String.valueOf(opr.elem), factorial));
                }
            }
            if (!matchedUnaryOprsCopy.isEmpty()) {
                for (IndexedElement opr : matchedUnaryOprsCopy) {
                    Operator op = new Operator(String.valueOf(opr.elem), factorial);
                    op.operatorType = OperatorType.UNARY;
                    expressionStack.push(op);
                }
            }

            return expressionStack;
        } catch (NumberFormatException nfe) {
            Logger.error("There seems to be some invalid characters in the expression.");
            return null;
        }
    }
}