package calculator;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

class Calculator {
    ArrayList<Button> digitsButtons = new ArrayList<Button>();
    ArrayList<Button> oprButtons = new ArrayList<Button>();
    ArrayList<Button> specialOprButtons = new ArrayList<Button>();
    Object[] digits = new Object[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, "." };
    String[] oprs = new String[] { "+", "-", "x", "/", "%", "sin", "cos", "tan", "ln", "log", "!", "√", "^", "e", "π",
            "(", ")", ":)" };
    String[] specialOprs = new String[] { "=", "C", "DEL", "RAD", "ANS", "TYPE OR TAP" };
    Frame calculatorFrame = new Frame("Jeril's Calculator");
    TextArea calculatorScreen = new TextArea();
    TextField angleTypeIndicator = new TextField();
    TextArea shortcuts = new TextArea();
    String currentExpression = "";
    AngleType angleType = AngleType.DEGREE;
    Dimension calcScreenDimension = new Dimension(600, 100);
    Dimension buttonDimension = new Dimension(100, 50);
    Dimension calcDimension = new Dimension(800, 800);
    Stack<Object> expressionStack = new Stack<Object>();
    String previousEval = "";
    Stack<ExpressionStore> expressionStores = new Stack<ExpressionStore>();
    int currentStore = 0;
    boolean storeBrowsingUnderway = false;
    Factorial factorial = new Factorial();
    Map<String, Integer> shortcutKeys = new LinkedHashMap<String, Integer>() {
        private static final long serialVersionUID = 1L;
        {
            put("C", KeyEvent.VK_F1);
            put("DEL", KeyEvent.VK_F2);
            put("RAD/DEG", KeyEvent.VK_F3);
            put("ANS", KeyEvent.VK_F4);
            put("ROOT", KeyEvent.VK_F5);
            put("PI", KeyEvent.VK_F6);
            put("PREV-EXPR", KeyEvent.VK_UP);
            put("NEXT-EXPR", KeyEvent.VK_DOWN);
            put("CLOSE", KeyEvent.VK_ESCAPE);
            put("EQUALS", KeyEvent.VK_ENTER);
        }
    };

    Map<String, String> shortcutKeyMeanings = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;
        {
            put("C", "Clear Screen");
            put("DEL", "Delete last button entry");
            put("RAD/DEG", "Change angle Type");
            put("ANS", "Last Result");
            put("EQUALS", "Compute");
            put("ROOT", "√ (Square Root)");
            put("PI", "π");
            put("PREV-EXPR", "Previous Expression");
            put("NEXT-EXPR", "Next Expression");
            put("CLOSE", "Close");
        }
    };

    public Calculator() {
        createDigitButtons();
        createOprButtons();

        createButtonFunctionalities();
        placeButtons();
        placeTextFields();
        addKeyboardListener();
        clearScreen();
    }

    private void createDigitButtons() {
        for (Object digit : digits) {
            digitsButtons.add(new Button(String.valueOf(digit)));
        }
    }

    private void createOprButtons() {
        for (String opr : oprs) {
            oprButtons.add(new Button(opr));
        }

        for (String specialOpr : specialOprs) {
            specialOprButtons.add(new Button(specialOpr));
        }
    }

    private void createButtonFunctionalities() {
        final boolean[] setDouble = new boolean[] { false };
        Color defaultButtonColor = new Color(189, 189, 189);

        for (Button button : digitsButtons) {
            button.setBackground(defaultButtonColor);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    char input = button.getLabel().charAt(0);
                    listenToDigitInput(input, setDouble);
                }
            });
        }

        for (Button button : oprButtons) {
            button.setBackground(defaultButtonColor);
            switch (button.getLabel()) {
                case "+":
                case "-":
                case "x":
                case "/":
                case "%":
                case "√":
                case "^":
                case "e":
                case "π":
                case "!":
                case "(":
                case ")":
                case "sin":
                case "cos":
                case "tan":
                case "ln":
                case "log":
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            expressionStack.push(new Operator(button.getLabel(), factorial));
                            showExpression(currentExpression + button.getLabel());
                            Logger.logSpaced(expressionStack);
                        }
                    });
                    break;

                case ":)":
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            showExpression("");
                            calculatorScreen.setText("Made by Jeril Monzi Jacob\nGithub:\nhttps://github.com/jerilMJ");
                        }
                    });
                    break;

                default:
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            clearScreen();
                            calculatorScreen.setText("??");
                        }
                    });
                    break;
            }
        }

        for (

        Button button : specialOprButtons) {
            switch (button.getLabel()) {
                case "C":
                    button.setBackground(new Color(239, 154, 154));
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            oprCFunction();
                        }
                    });
                    break;
                case "DEL":
                    button.setBackground(new Color(239, 154, 154));
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            oprDelFunction();
                        }
                    });
                    break;
                case "RAD":
                    button.setBackground(new Color(144, 202, 249));
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            oprRadDegFunction();
                        }
                    });
                    break;
                case "=":
                    button.setBackground(new Color(165, 214, 167));
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            oprEqualsFunction();
                        }
                    });
                    break;
                case "ANS":
                    button.setBackground(new Color(197, 225, 165));
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            oprAnsFunction();
                        }
                    });
                    break;

                case "TYPE OR TAP":
                    button.setBackground(defaultButtonColor);
                    button.setFocusable(false);
                    break;

                default:
                    showExpression("");
                    calculatorScreen.setText("??");
            }
        }
    }

    private void oprCFunction() {
        expressionStack.clear();
        clearScreen();
    }

    private void oprDelFunction() {
        if (!expressionStack.isEmpty())
            expressionStack.pop();
        clearScreen();
        showParsedExpression();
        Logger.logSpaced(expressionStack);
    }

    private void oprRadDegFunction() {
        Button button = (Button) Arrays.stream(specialOprButtons.toArray())
                .filter(b -> ((Button) b).getLabel() == "RAD" || ((Button) b).getLabel() == "DEG").findFirst().get();

        if (angleType == AngleType.DEGREE) {
            angleTypeIndicator.setText("RAD");
            angleType = AngleType.RADIAN;
            button.setLabel("DEG");
        } else {
            angleTypeIndicator.setText("DEG");
            angleType = AngleType.DEGREE;
            button.setLabel("RAD");
        }
    }

    private void oprEqualsFunction() {
        clearScreen();
        computeResult();
        Logger.logSpaced(expressionStack);
    }

    private void oprAnsFunction() {
        if (!previousEval.isEmpty())
            pushToStack(Double.parseDouble(previousEval));
        showExpression(currentExpression + previousEval);
    }

    private void computeResult() {
        String res = InfixEval.evaluateExpression(expressionStack, angleType);
        previousEval = res;
        cacheResult();
        expressionStack.clear();
        pushToStack(Double.parseDouble(res));
        Logger.logSpaced("Result: " + res);
        if (Double.valueOf(res).isInfinite() || Double.valueOf(res).isNaN()) {
            Logger.error("Result of Infinity (or NaN) may be caused by very large numbers\nwhich Java's double "
                    + "is incapable of holding or because your input was not formatted correctly "
                    + "(incorrect operator position).");
        }
        showExpression(res);
    }

    private void cacheResult() {
        expressionStores.push(new ExpressionStore(currentExpression, expressionStack));
    }

    private void showParsedExpression() {
        String expr = "";
        for (Object elem : expressionStack) {
            expr += elem.toString();
        }
        showExpression(expr);
    }

    private void addKeyboardListener() {
        calculatorScreen.addKeyListener(new KeyAdapter() {
            Map<Integer, Boolean> pressed = new HashMap<Integer, Boolean>();
            Map<Integer, Boolean> released = new HashMap<Integer, Boolean>();
            KeyEvent previousKey = null;
            ArrayList<Character> allowed = new ArrayList<Character>(Arrays.asList('1', '2', '3', '4', '5', '6', '7',
                    '8', '9', '0', '.', '+', '-', 'x', '/', '%', '√', '^', 'e', 'π', '(', ')'));
            StringBuilder sb = new StringBuilder(currentExpression);

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                char lastChar = '?';
                char secondLastChar = '?';
                if (!pressed.getOrDefault(code, false) || code == KeyEvent.VK_BACK_SPACE
                        || code == KeyEvent.VK_DELETE) {
                    if (sb.length() > 0) {
                        lastChar = sb.charAt(sb.length() - 1);
                    }
                    if (sb.length() > 1) {
                        secondLastChar = sb.charAt(sb.length() - 2);
                    }
                    char prevPressed = '?';
                    if (previousKey != null)
                        prevPressed = String.valueOf(previousKey.getKeyChar()).toLowerCase().charAt(0);
                    char curPressed = String.valueOf(e.getKeyChar()).toLowerCase().charAt(0);
                    pressed.put(Integer.valueOf(code), Boolean.valueOf(true));
                    released.put(Integer.valueOf(code), Boolean.valueOf(false));
                    if (code == KeyEvent.VK_ENTER) {
                        long start = System.nanoTime();
                        expressionStack = Utils.parseExpression(expressionStack, currentExpression, factorial);
                        long end = System.nanoTime();
                        Logger.logSpaced("Stack: " + expressionStack);
                        computeResult();
                        Logger.log("Computation Took: " + String.valueOf((end - start) / 1000000) + "ms");
                        Logger.logSpaced(
                                "-----------------------------------------------------------------------------------");
                        Logger.log(
                                "-----------------------------------------------------------------------------------\n\n");
                    } else if (code == KeyEvent.VK_SHIFT) {
                    } else if (code == KeyEvent.VK_BACK_SPACE) {
                        sb.delete(0, sb.length());
                        sb.insert(0, currentExpression);
                        if (sb.length() > 0 && calculatorScreen.getCaretPosition() > 0)
                            sb.deleteCharAt(calculatorScreen.getCaretPosition() - 1);
                        currentExpression = sb.toString();
                    } else if (code == KeyEvent.VK_DELETE) {
                        sb.delete(0, sb.length());
                        sb.insert(0, currentExpression);
                        if (calculatorScreen.getCaretPosition() < sb.length())
                            sb.deleteCharAt(calculatorScreen.getCaretPosition());
                        currentExpression = sb.toString();
                    } else if (code == KeyEvent.VK_HOME) {
                        calculatorScreen.setCaretPosition(0);
                    } else if (code == KeyEvent.VK_END) {
                        calculatorScreen.setCaretPosition(currentExpression.length());
                    } else if (e.getKeyChar() == '!' && Character.isDigit(lastChar)) {
                        currentExpression += '!';
                    } else if (code == KeyEvent.VK_L && prevPressed != 'l') {
                        currentExpression += curPressed;
                    } else if (code == KeyEvent.VK_O
                            && (prevPressed == 'l' || prevPressed == 'c' || lastChar == 'l' || lastChar == 'c')) {
                        currentExpression += curPressed;
                    } else if (code == KeyEvent.VK_G && (prevPressed == 'o' || lastChar == 'o')
                            && secondLastChar == 'l') {
                        currentExpression += curPressed;
                    } else if (code == KeyEvent.VK_S && (prevPressed != 's'
                            || (prevPressed == 'o' || lastChar == 'o' && secondLastChar == 'c'))) {
                        currentExpression += curPressed;
                    } else if (code == KeyEvent.VK_I && (prevPressed == 's' || lastChar == 's')) {
                        currentExpression += curPressed;
                    } else if (code == KeyEvent.VK_N && (prevPressed == 'i' || prevPressed == 'l' || prevPressed == 'a'
                            || lastChar == 'i' || lastChar == 'l' || lastChar == 'a')) {
                        currentExpression += curPressed;
                    } else if (code == KeyEvent.VK_C && prevPressed != 'c') {
                        currentExpression += curPressed;
                    } else if (code == KeyEvent.VK_T && prevPressed != 't') {
                        currentExpression += curPressed;
                    } else if (code == KeyEvent.VK_A && (prevPressed == 't' || lastChar == 't')) {
                        currentExpression += curPressed;
                    } else if (code == KeyEvent.VK_MULTIPLY || (pressed.getOrDefault(KeyEvent.VK_SHIFT, false)
                            && pressed.getOrDefault(KeyEvent.VK_8, false))) {
                        currentExpression += 'x';
                    } else if (code == shortcutKeys.get("C")) {
                        oprCFunction();
                    } else if (code == shortcutKeys.get("DEL")) {
                        oprDelFunction();
                    } else if (code == shortcutKeys.get("RAD/DEG")) {
                        oprRadDegFunction();
                    } else if (code == shortcutKeys.get("ANS")) {
                        oprAnsFunction();
                    } else if (code == shortcutKeys.get("EQUALS")) {
                        oprEqualsFunction();
                    } else if (code == shortcutKeys.get("PI")) {
                        currentExpression += "π";
                    } else if (code == shortcutKeys.get("ROOT")) {
                        currentExpression += "√";
                    } else if (code == shortcutKeys.get("PREV-EXPR")) {
                        if (!storeBrowsingUnderway && !expressionStores.isEmpty()) {
                            currentStore = expressionStores.size() - 1;
                            storeBrowsingUnderway = true;
                            browseStore();
                        } else if (storeBrowsingUnderway && currentStore - 1 >= 0) {
                            currentStore--;
                            browseStore();
                        }
                    } else if (code == shortcutKeys.get("NEXT-EXPR")) {
                        if (storeBrowsingUnderway && currentStore + 1 < expressionStores.size()) {
                            currentStore++;
                            browseStore();
                        } else {
                            clearScreen();
                        }
                    } else if (code == shortcutKeys.get("CLOSE")) {
                        calculatorFrame.dispose();
                    } else {
                        if (allowed.contains(Character.valueOf(e.getKeyChar()))) {
                            currentExpression += e.getKeyChar();
                        }
                    }
                } else {
                    showExpression(currentExpression);
                    calculatorScreen.setCaretPosition(sb.length());
                }
                sb.delete(0, sb.length());
                sb.insert(0, currentExpression);
            }

            private void browseStore() {
                expressionStack = expressionStores.get(currentStore).expressionStack;
                showExpression(expressionStores.get(currentStore).expression);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                ArrayList<Integer> keysToBeIgnored = new ArrayList<Integer>(
                        Arrays.asList(KeyEvent.VK_HOME, KeyEvent.VK_END, KeyEvent.VK_DELETE, KeyEvent.VK_BACK_SPACE,
                                KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT));
                ArrayList<Integer> storeBrowseKeys = new ArrayList<Integer>(
                        Arrays.asList(shortcutKeys.get("PREV-EXPR"), shortcutKeys.get("NEXT-EXPR")));
                int code = e.getKeyCode();

                if (keysToBeIgnored.contains(code) && !released.getOrDefault(code, false)) {
                    released.put(Integer.valueOf(code), Boolean.valueOf(true));
                    pressed.put(Integer.valueOf(code), Boolean.valueOf(false));
                    previousKey = e;
                }

                if (!storeBrowseKeys.contains(code) && !released.getOrDefault(code, false)) {
                    storeBrowsingUnderway = false;
                }

                if (!released.getOrDefault(code, false)) {
                    released.put(Integer.valueOf(code), Boolean.valueOf(true));
                    pressed.put(Integer.valueOf(code), Boolean.valueOf(false));
                    showExpression(currentExpression);
                    calculatorScreen.setCaretPosition(sb.length());
                    previousKey = e;
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

        });
    }

    private void pushToStack(Object res) {
        if (res instanceof Double) {
            if (Math.floor((double) res) == (double) res && !Double.isInfinite((double) res)) {
                expressionStack.push(Math.round((double) res));
                return;
            }
        }
        expressionStack.push(res);
    }

    private void clearScreen() {
        showExpression("");
    }

    private void showExpression(String expr) {
        calculatorScreen.requestFocus();
        currentExpression = expr;
        calculatorScreen.setText(currentExpression);
    }

    private void listenToDigitInput(char input, boolean[] setDouble) {
        if (input == '.') {
            if (!expressionStack.isEmpty())
                if (expressionStack.peek() instanceof Double)
                    return;
        }

        showExpression(currentExpression + input);
        if (input == '.') {
            setDouble[0] = true;
        } else {
            if (!expressionStack.isEmpty()) {
                if (setDouble[0]) {
                    Object lastObj = expressionStack.get(expressionStack.size() - 1);
                    long num;
                    if (lastObj instanceof Long) {
                        num = (long) lastObj;
                        double res = Double.parseDouble(String.valueOf(num) + '.' + input);
                        expressionStack.set(expressionStack.size() - 1, res);
                    } else {
                        num = 0;
                        double res = Double.parseDouble(String.valueOf(num) + '.' + input);
                        pushToStack(res);
                    }
                    setDouble[0] = false;
                } else {
                    Object lastObject = expressionStack.get(expressionStack.size() - 1);
                    if (lastObject instanceof Double) {
                        expressionStack.set(expressionStack.size() - 1,
                                Double.parseDouble(String.valueOf(lastObject) + input));
                    } else if (lastObject instanceof Long) {
                        expressionStack.set(expressionStack.size() - 1,
                                Long.parseLong(String.valueOf(lastObject) + input));
                    } else if (lastObject instanceof Long) {
                        pushToStack(Long.parseLong(String.valueOf(input)));
                    } else {
                        pushToStack(Long.parseLong(String.valueOf(input)));
                    }
                }
            } else {
                if (setDouble[0]) {
                    long num;
                    if (!expressionStack.isEmpty()) {
                        num = (long) expressionStack.get(expressionStack.size() - 1);
                        double res = Double.parseDouble(String.valueOf(num) + "." + input);
                        expressionStack.set(expressionStack.size() - 1, res);
                    } else {
                        num = 0;
                        double res = Double.parseDouble(String.valueOf(num) + "." + input);
                        pushToStack(res);
                    }

                    setDouble[0] = false;
                } else {
                    pushToStack(Long.parseLong(String.valueOf(input)));
                }
            }
            Logger.logSpaced(expressionStack);
        }
    }

    private void placeButtons() {
        int count = 0;
        int spacing = 25;
        int xMultiplier = (int) (calcDimension.width / buttonDimension.width / 2);
        int yMultiplier = (int) (calcDimension.height / buttonDimension.height / 2);
        for (Button button : digitsButtons) {
            if (count == 3) {
                count = 0;
                xMultiplier = (int) (calcDimension.width / buttonDimension.width / 2);
                yMultiplier += 1;
            }

            button.setBounds(
                    new Rectangle(buttonDimension.width * xMultiplier - (int) (buttonDimension.width * 3) - spacing,
                            buttonDimension.height * yMultiplier + spacing, (int) buttonDimension.width,
                            buttonDimension.height));
            calculatorFrame.add(button);

            xMultiplier += 1;
            count += 1;
        }

        for (Button button : specialOprButtons) {
            if (count == 3) {
                count = 0;
                xMultiplier = (int) (calcDimension.width / buttonDimension.width / 2);
                yMultiplier += 1;
            }

            if (button.getLabel() == "C") {
                button.setBounds(
                        new Rectangle(buttonDimension.width * xMultiplier - (int) (buttonDimension.width * 3) - spacing,
                                buttonDimension.height * yMultiplier + spacing, (int) (buttonDimension.width / 2),
                                buttonDimension.height));
                count = -1;
            } else if (button.getLabel() == "DEL") {
                button.setBounds(new Rectangle(
                        buttonDimension.width * xMultiplier - (int) (buttonDimension.width * 3) - spacing * 3,
                        buttonDimension.height * yMultiplier + spacing, (int) (buttonDimension.width / 2),
                        buttonDimension.height));
                xMultiplier -= 1;
            } else if (button.getLabel() == "TYPE OR TAP") {
                button.setBounds(
                        new Rectangle(buttonDimension.width * xMultiplier - (int) (buttonDimension.width * 3) - spacing,
                                buttonDimension.height * yMultiplier + spacing, (int) (buttonDimension.width * 3),
                                buttonDimension.height));
                xMultiplier -= 1;
            } else {
                button.setBounds(
                        new Rectangle(buttonDimension.width * xMultiplier - (int) (buttonDimension.width * 3) - spacing,
                                buttonDimension.height * yMultiplier + spacing, (int) buttonDimension.width,
                                buttonDimension.height));
            }
            calculatorFrame.add(button);

            xMultiplier += 1;
            count += 1;
        }

        count = 0;
        xMultiplier = (int) (calcDimension.width / buttonDimension.width / 2);
        yMultiplier = (int) (calcDimension.height / buttonDimension.height / 2);
        for (

        Button button : oprButtons) {
            if (count == 3) {
                count = 0;
                xMultiplier = (int) (calcDimension.width / buttonDimension.width / 2);
                yMultiplier += 1;
            }

            button.setBounds(new Rectangle(buttonDimension.width * xMultiplier + spacing,
                    buttonDimension.height * yMultiplier + spacing, (int) buttonDimension.width,
                    buttonDimension.height));

            calculatorFrame.add(button);

            xMultiplier += 1;
            count += 1;
        }
    }

    public void placeTextFields() {
        angleTypeIndicator.setText("DEG");
        angleTypeIndicator.setFocusable(false);
        angleTypeIndicator.setEditable(false);
        angleTypeIndicator.setBounds(new Rectangle(
                (int) (calcDimension.width / 2) - (int) (calcScreenDimension.width / 2) + 5,
                (int) (calcDimension.height / 2) - (int) (calcScreenDimension.height) + calcScreenDimension.height - 45,
                50, 25));
        angleTypeIndicator.setBackground(new Color(245, 245, 245));
        angleTypeIndicator.setForeground(new Color(117, 117, 117));

        calculatorScreen
                .setBounds(new Rectangle((int) (calcDimension.width / 2) - (int) (calcScreenDimension.width / 2),
                        (int) (calcDimension.height / 2) - (int) (calcScreenDimension.height),
                        calcScreenDimension.width, calcScreenDimension.height));
        calculatorScreen.setBackground(new Color(245, 245, 245));

        String separator = String.format("%-12s %-30s", "",
                "\n-------------------------------------------------------------\n");
        String shortcutsString = "\n";
        for (Map.Entry<String, Integer> sc : shortcutKeys.entrySet()) {
            String s = String.format("%-12s %-30s : %s", "", shortcutKeyMeanings.get(sc.getKey()),
                    KeyEvent.getKeyText(sc.getValue()));
            shortcutsString += s;
            shortcutsString += separator;
        }
        shortcuts.setRows(30);
        shortcuts.setBackground(new Color(33, 33, 33));
        shortcuts.setForeground(new Color(159, 168, 218));
        shortcuts.setColumns(1);
        shortcuts.setText(shortcutsString);
        shortcuts.setFocusable(false);
        shortcuts.setEditable(false);
        Logger.log(shortcutsString);

        Button shortcutTitle = new Button("SHORTCUT KEYS");
        shortcutTitle.setFocusable(false);
        shortcutTitle.setForeground(new Color(159, 168, 218));
        shortcutTitle
                .setBounds(new Rectangle(20, (int) (calcDimension.height / 2) - (int) (calcScreenDimension.height * 2)
                        - calcScreenDimension.height - 50, calcDimension.width - 40, 50));
        shortcuts.setBounds(new Rectangle(20,
                (int) (calcDimension.height / 2) - (int) (calcScreenDimension.height * 2) - calcScreenDimension.height,
                calcDimension.width - 40, 180));

        calculatorFrame.add(angleTypeIndicator);
        calculatorFrame.add(shortcutTitle);
        calculatorFrame.add(shortcuts);
        calculatorFrame.add(calculatorScreen);
    }

    public void showFrame() {
        calculatorFrame.setSize(calcDimension);
        calculatorFrame.setLayout(null);
        calculatorFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                    calculatorFrame.dispose();
            }
        });
        calculatorFrame.addWindowListener(new WindowListener() {
            public void windowOpened(WindowEvent we) {
            }

            public void windowClosing(WindowEvent we) {
                calculatorFrame.dispose();
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }
        });
        calculatorFrame.setBackground(new Color(33, 33, 33));
        calculatorFrame.setFont(new Font("Courier", Font.BOLD, 20));
        calculatorFrame.setVisible(true);
    }
}
