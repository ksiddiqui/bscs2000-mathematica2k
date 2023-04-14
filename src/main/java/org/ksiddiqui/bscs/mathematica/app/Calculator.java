package org.ksiddiqui.bscs.mathematica.app;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator {

    static JLabel Display;
    static JTextArea History;

    static double number;
    static boolean decipoint;
    static boolean displaycls;
    static char opperator;
    static char sign;
    static int noOfChar;
    static boolean radians;

    public Calculator() {
        number = 0.0;
        decipoint = false;
        opperator = '+';
        sign = ' ';
        noOfChar = 2;
        displaycls = false;
        radians = true;

        JPanel mp = new JPanel(new BorderLayout());
        mp.setBorder(new EmptyBorder(5, 5, 5, 5));

        History = new JTextArea(" -( HISTORY )---\n", 10, 20);
        JScrollPane HistoryPad = new JScrollPane(History);
        HistoryPad.setBorder(new BevelBorder(BevelBorder.LOWERED));

        Display = new JLabel(" 0", JLabel.RIGHT);
        JButton[] numKeys = new JButton[12];  // '0-9', '+/-', '.'
        JButton[] oppKeys = new JButton[8];   // + - x / \ ^ < =
        JButton[] funcKeys = new JButton[12];  // sin, cos, tan, arcsine - arctan

        Font font1 = new Font("Serif", Font.PLAIN, 10);
        JButton clear = new JButton("Clear");
        clear.setFont(font1);
        JButton newdis = new JButton("New");
        newdis.setFont(font1);
        JButton copyHistory = new JButton("CopyH");
        copyHistory.setFont(font1);
        JButton copyField = new JButton("CopyF");
        copyField.setFont(font1);
        JButton pasteField = new JButton("PasteF");
        pasteField.setFont(font1);
        JToggleButton degrees = new JToggleButton("Degrees");
        degrees.setFont(font1);

        JPanel numPad = new JPanel(new GridLayout(4, 3));
        JPanel oppPad = new JPanel(new GridLayout(6, 1));
        JPanel funcPad = new JPanel(new GridLayout(3, 4));
        JPanel m1 = new JPanel(new BorderLayout());
        JPanel m2 = new JPanel(new BorderLayout());
        m2.setBorder(new EmptyBorder(0, 0, 0, 5));
        JPanel m3 = new JPanel(new GridLayout(3, 2));

        ImageIcon image = new ImageIcon("calculator.gif");
        JLabel imglabel = new JLabel(image);

        // Display Label settings
        Display.setBorder((Border) new EtchedBorder());

        // NumKeys Initialization
        numPad.setBorder((Border) new BevelBorder(BevelBorder.RAISED));
        char name = '0';
        for (int n = 0; n < 10; n++, name++) {
            numKeys[n] = new JButton(Integer.toString(n));
            numKeys[n].setMnemonic(name);
            numKeys[n].setActionCommand(Integer.toString(n));
            numKeys[n].addActionListener(new numPadListener());
            numKeys[n].setBackground(Color.yellow);
            numKeys[n].setFont(font1);
        }
        numPad.add(numKeys[7]);
        numPad.add(numKeys[8]);
        numPad.add(numKeys[9]);
        numPad.add(numKeys[4]);
        numPad.add(numKeys[5]);
        numPad.add(numKeys[6]);
        numPad.add(numKeys[1]);
        numPad.add(numKeys[2]);
        numPad.add(numKeys[3]);
        numPad.add(numKeys[0]);
        numKeys[10] = new JButton(".");
        numKeys[10].setMnemonic('.');
        numPad.add(numKeys[10]);
        numKeys[10].setActionCommand(".");
        numKeys[10].setBackground(Color.yellow);
        numKeys[10].addActionListener(new numPadListener());
        numKeys[10].setFont(font1);
        numKeys[11] = new JButton("-");
        numPad.add(numKeys[11]);
        numKeys[11].setActionCommand("-");
        numKeys[11].addActionListener(new xoppPadListener());
        numKeys[11].setBackground(Color.yellow);
        numKeys[11].setFont(font1);

        // oppKeys Initialization
        oppPad.setBorder((Border) new BevelBorder(BevelBorder.RAISED));
        oppKeys[0] = new JButton("+");  // oppKeys[0].setMnemonic('+'); oppKeys[0].setActionCommand("+");
        oppKeys[1] = new JButton("-");  // oppKeys[1].setMnemonic('-'); oppKeys[1].setActionCommand("-");
        oppKeys[2] = new JButton("*");  // oppKeys[2].setMnemonic('*'); oppKeys[2].setActionCommand("*");
        oppKeys[3] = new JButton("/"); // oppKeys[3].setMnemonic('/'); oppKeys[3].setActionCommand("/");
        oppKeys[4] = new JButton("\\");  // oppKeys[4].setMnemonic('\'); oppKeys[4].setActionCommand("\");
        oppKeys[5] = new JButton("^");  // oppKeys[5].setMnemonic('^'); oppKeys[5].setActionCommand("^");
        oppKeys[6] = new JButton("=");  // oppKeys[6].setMnemonic('='); oppKeys[6].setActionCommand("=");
        oppKeys[7] = new JButton("<");  // oppKeys[7].setMnemonic('<'); oppKeys[7].setActionCommand("<");
        for (int n = 0; n < 8; n++) {
            oppKeys[n].setBackground(Color.cyan);
            oppKeys[n].setFont(font1);
            if (n < 6) oppKeys[n].addActionListener(new oppPadListener());
            else oppKeys[n].addActionListener(new xoppPadListener());
            oppPad.add(oppKeys[n]);
        }

        // funcKeys Initialization
        funcPad.setBorder((Border) new SoftBevelBorder(BevelBorder.RAISED));
        funcKeys[0] = new JButton("Sin");
        funcKeys[0].setActionCommand("0");
        funcKeys[1] = new JButton("Cos");
        funcKeys[1].setActionCommand("1");
        funcKeys[2] = new JButton("Tan");
        funcKeys[2].setActionCommand("2");
        funcKeys[3] = new JButton("Log");
        funcKeys[3].setActionCommand("3");
        funcKeys[4] = new JButton("aSin");
        funcKeys[4].setActionCommand("4");
        funcKeys[5] = new JButton("aCos");
        funcKeys[5].setActionCommand("5");
        funcKeys[6] = new JButton("aTan");
        funcKeys[6].setActionCommand("6");
        funcKeys[7] = new JButton("aLog");
        funcKeys[7].setActionCommand("7");
        funcKeys[8] = new JButton("SqrT");
        funcKeys[8].setActionCommand("8");
        funcKeys[9] = new JButton();
        funcKeys[10] = new JButton("PI");
        funcKeys[10].setActionCommand("A");
        funcKeys[11] = new JButton("E");
        funcKeys[11].setActionCommand("B");
        for (int n = 0; n < 12; n++) {
            funcKeys[n].setBackground(Color.pink);
            funcKeys[n].setFont(font1);
            funcKeys[n].addActionListener(new funcPadListener());
            funcPad.add(funcKeys[n]);
        }

        // Misc. buttons Initialization
        newdis.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Display.setText(" 0");
                displaycls = false;
                decipoint = false;
                sign = ' ';
                opperator = '+';
                noOfChar = 2;
                number = 0.0;

                History.append("\n\n(NEW Calculations)");
            }
        });

        degrees.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                radians = !radians;
            }
        });

        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                History.setText(" -( HISTORY )---\n");
            }
        });

        copyHistory.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                History.copy();
            }
        });

        copyField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField qwe = new JTextField(Display.getText());
                qwe.selectAll();
                qwe.copy();
            }
        });

        pasteField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField qwe = new JTextField();
                String tmp;
                double a;

                qwe.paste();
                try {
                    a = Double.parseDouble(qwe.getText());
                } catch (Exception ex) {
                    a = 0.0;
                }

                if (a < 0) {
                    sign = '-';
                    tmp = Double.toString(a);
                } else {
                    sign = ' ';
                    tmp = " ".concat(Double.toString(a));
                }

                Display.setText(tmp);
                if (tmp.indexOf(".") == -1) decipoint = false;
                else decipoint = true;
                noOfChar = tmp.length();

            }
        });

        // Setting components
        m1.add(Display, BorderLayout.NORTH);
        m1.add(numPad, BorderLayout.CENTER);
        m1.add(oppPad, BorderLayout.WEST);
        m1.add(funcPad, BorderLayout.SOUTH);

        m3.add(clear);
        m3.add(copyHistory);
        m3.add(newdis);
        m3.add(degrees);
        m3.add(copyField);
        m3.add(pasteField);

        m2.add(HistoryPad, BorderLayout.CENTER);
        m2.add(m3, BorderLayout.SOUTH);
        m2.setPreferredSize(new Dimension(220, 294));
        m2.setMaximumSize(new Dimension(220, 294));

        mp.add(m1, BorderLayout.CENTER);
        mp.add(m2, BorderLayout.WEST);
        mp.add(imglabel, BorderLayout.EAST);

        Mathematica.MainTab.addTab("Calculator", mp);
    }

    class numPadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (displaycls == true) {
                Display.setText(" 0");
                displaycls = false;
                decipoint = false;
                sign = ' ';
                noOfChar = 2;
            }

            String commstr = e.getActionCommand();
            char comm = commstr.charAt(0);
            String strdisplay = Display.getText();
            String tmp;

            if (noOfChar >= 22) return;

            if (comm == '0' || comm == '1' || comm == '2' || comm == '3' || comm == '4' || comm == '5'
                    || comm == '6' || comm == '7' || comm == '8' || comm == '9') {
                tmp = strdisplay.concat(commstr);
                Display.setText(tmp);
                noOfChar++;
            } else if (comm == '.' && decipoint == false) {
                tmp = strdisplay.concat(commstr);
                Display.setText(tmp);
                decipoint = true;
                noOfChar++;
            }

            return;
            // End of Listener method .........
        }
    }


    class oppPadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String commstr = e.getActionCommand();
            char comm = commstr.charAt(0);
            String strdisplay = Display.getText();
            String tmp;

            double dtmp = Double.parseDouble(strdisplay);
            double numberx = number;

            switch (opperator) {
                case '+': {
                    number = number + dtmp;
                    break;
                }
                case '-': {
                    number = number - dtmp;
                    break;
                }
                case '*': {
                    number = number * dtmp;
                    break;
                }
                case '/': {
                    number = number / dtmp;
                    break;
                }
                case '\\': {
                    number = number % dtmp;
                    break;
                }
                case '^': {
                    number = Math.pow(number, dtmp);
                    break;
                }
            }

            if (number < 0) {
                sign = '-';
                tmp = Double.toString(number);
            } else {
                sign = ' ';
                tmp = " ".concat(Double.toString(number));
            }
            Display.setText(tmp);

            History.append("\n  " + Double.toString(numberx) + " " + opperator + " " + strdisplay);

            opperator = comm;
            displaycls = true;

            return;
        }
    }


    class xoppPadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String commstr = e.getActionCommand();
            char comm = commstr.charAt(0);
            String strdisplay = Display.getText();
            String tmp;

            if (comm == '=') {
                double dtmp = Double.parseDouble(strdisplay);
                double numberx = number;

                switch (opperator) {
                    case '+': {
                        number = number + dtmp;
                        break;
                    }
                    case '-': {
                        number = number - dtmp;
                        break;
                    }
                    case '*': {
                        number = number * dtmp;
                        break;
                    }
                    case '/': {
                        number = number / dtmp;
                        break;
                    }
                    case '\\': {
                        number = number % dtmp;
                        break;
                    }
                    case '^': {
                        number = Math.pow(number, dtmp);
                        break;
                    }
                }

                if (number < 0) {
                    sign = '-';
                    tmp = Double.toString(number);
                } else {
                    sign = ' ';
                    tmp = " ".concat(Double.toString(number));
                }
                Display.setText(tmp);

                History.append("\n  " + Double.toString(numberx) + " " + opperator + " " + strdisplay);
                History.append("\n       " + commstr + tmp);

                opperator = '+';
                number = 0.0;
                displaycls = true;
            } else if (comm == '-') {
                if (sign == ' ') {
                    sign = '-';
                    tmp = strdisplay.replace(' ', '-');
                    Display.setText(tmp);
                } else {
                    sign = ' ';
                    tmp = strdisplay.replace('-', ' ');
                    Display.setText(tmp);
                }
            } else if (comm == '<' && Display.getText().length() > 1) {
                char[] strdisplay1 = strdisplay.toCharArray();
                char[] strdisplay2;
                int strlen = strdisplay.length();

                if (strdisplay1[strlen - 1] == '.') decipoint = false;
                strdisplay2 = new char[strlen - 1];
                for (int n = 0; n <= (strlen - 2); n++)
                    strdisplay2[n] = strdisplay1[n];

                tmp = new String(strdisplay2);
                Display.setText(tmp);
                noOfChar--;
            }
            return;
        }
    }


    class funcPadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String commstr = e.getActionCommand();
            char comm = commstr.charAt(0);
            String strdisplay = Display.getText();
            String tmp;
            String name = null;
            double anglex = 1;
            double dtmp = Double.parseDouble(strdisplay);
            double ans = 0;

            if (!radians) anglex = Math.PI / 180;

            switch (comm) {
                case '0': {
                    ans = Math.sin(anglex * dtmp);
                    name = new String("Sine of =");
                    break;
                }
                case '1': {
                    ans = Math.cos(anglex * dtmp);
                    name = new String("Cosine of =");
                    break;
                }
                case '2': {
                    ans = Math.tan(anglex * dtmp);
                    name = new String("Tangent of =");
                    break;
                }
                case '3': {
                    ans = Math.log(dtmp);
                    name = new String("Log of =");
                    break;
                }
                case '4': {
                    ans = (1 / anglex) * Math.asin(dtmp);
                    name = new String("arcSine of =");
                    break;
                }
                case '5': {
                    ans = (1 / anglex) * Math.acos(dtmp);
                    name = new String("arcCosine of =");
                    break;
                }
                case '6': {
                    ans = (1 / anglex) * Math.atan(dtmp);
                    name = new String("arcTangent of =");
                    break;
                }
                case '7': {
                    ans = Math.exp(dtmp);
                    name = new String("Exponent of =");
                    break;
                }
                case '8': {
                    ans = Math.sqrt(dtmp);
                    name = new String("Squart root of =");
                    break;
                }
                case 'A': {
                    ans = Math.PI;
                    name = new String("Value of PI =");
                    break;
                }
                case 'B': {
                    ans = Math.E;
                    name = new String("Value of exponent 'e'=");
                    break;
                }
            }

            if (ans < 0) {
                sign = '-';
                tmp = Double.toString(ans);
            } else {
                sign = ' ';
                tmp = " ".concat(Double.toString(ans));
            }
            Display.setText(tmp);

            History.append("\n  " + name + tmp);

            if (tmp.indexOf(".") == -1) decipoint = false;
            else decipoint = true;
            noOfChar = tmp.length();

            return;
        }

    }


}
