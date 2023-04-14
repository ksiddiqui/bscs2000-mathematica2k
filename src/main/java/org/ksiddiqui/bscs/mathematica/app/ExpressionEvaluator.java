package org.ksiddiqui.bscs.mathematica.app;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExpressionEvaluator {

    static JTextField expression;
    static JTextField result;
    static ExpressionSolver ExpS;
    static boolean degreeFlag = false;
    private String Postfix;

    ExpressionEvaluator() {
        JPanel mp = new JPanel(new BorderLayout());
        mp.setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel m1 = new JPanel(new BorderLayout());
        JPanel m2 = new JPanel(new BorderLayout());
        JPanel m3 = new JPanel();
        JPanel m4 = new JPanel();
        JPanel m5 = new JPanel(new GridLayout(2, 1));
        JPanel m6 = new JPanel(new GridLayout(1, 2));
        JPanel m7 = new JPanel(new GridLayout(1, 2));
        m3.setBorder(new TitledBorder("Type expression"));
        m4.setBorder(new TitledBorder("Expression Literals Types"));
        m5.setBorder(new TitledBorder("Result"));

        result = new JTextField(20);
        expression = new JTextField(40);
        ImageIcon titleImage = new ImageIcon("expression.gif");
        JLabel title = new JLabel(titleImage);

        String literalsStr = new String(" - Numeric literals :\n"
                + "    0,  1,  2,  3, ...,  9\n\n"
                + " - Opperator literals :\n"
                + "    +,  -,  *,  /,  \\,  ^\n\n"
                + " - Bracketing literals :\n"
                + "    (,  )\n\n"
                + " - Function literals :\n"
                + "    sin,  cos,  tan,  asin,  acos,  atan,\n"
                + "    log,  alog");
        JTextArea literalsBox = new JTextArea(literalsStr, 6, 20);
        literalsBox.setEditable(false);
        literalsBox.setFont(new Font("Serif", Font.BOLD, 13));
        literalsBox.setBorder(new BevelBorder(BevelBorder.LOWERED));

        JToggleButton degrees = new JToggleButton("Degrees");
        JButton pasteExp = new JButton("Paste");
        JButton copyAns = new JButton("Copy");
        JButton getPostfix = new JButton("Postfix Exp.");
        JButton go = new JButton("Execute");

        Postfix = new String(" ");

        // Setting Components Events

        getPostfix.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result.setText("Postfix Notation : " + Postfix);
            }
        });

        degrees.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                degreeFlag = !degreeFlag;
            }
        });


        pasteExp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                expression.paste();
            }
        });

        copyAns.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                result.copy();
            }
        });

        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                double answer;

                try {
                    ExpS = new ExpressionSolver(expression.getText(), degreeFlag);
                    ExpS.toPostfix();
                    answer = ExpS.Evaluator();
                    Postfix = ExpS.Postfix();
                    result.setText(Double.toString(answer));
                } catch (ExpressionSolverException esE) {
                    JOptionPane.showMessageDialog(Mathematica.MainTab, esE.getException() + "...\n Check the typed expression", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (Exception E) {
                    JOptionPane.showMessageDialog(Mathematica.MainTab, "Unknown error ...\n Check the typed expression", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        // Setting Components

        m7.add(copyAns);
        m7.add(getPostfix);

        m6.add(pasteExp);
        m6.add(degrees);

        m5.add(result);
        m5.add(m7);

        m4.add(literalsBox);
        m3.add(expression);

        m2.add(m6, BorderLayout.NORTH);
        m2.add(go, BorderLayout.CENTER);
        m2.add(m5, BorderLayout.SOUTH);

        m1.add(m3, BorderLayout.NORTH);
        m1.add(m2, BorderLayout.CENTER);
        m1.add(m4, BorderLayout.WEST);

        mp.add(m1, BorderLayout.CENTER);
        mp.add(title, BorderLayout.EAST);

        Mathematica.MainTab.addTab("Expression Solver", mp);
    }

}
