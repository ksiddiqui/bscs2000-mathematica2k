package org.ksiddiqui.bscs.mathematica;

import org.ksiddiqui.bscs.mathematica.maths.Function;
import org.ksiddiqui.bscs.mathematica.maths.RootSolver;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.RootSolverDivergenceException;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.RootSolverException;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.RootSolverIterationsEndException;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.RootSolverWrongValueException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RootSolverAgent {

    private JTextField funcField = null;
    private JButton funcPasteButton = null;

    private JTextField tolField = null;
    private JLabel tolLabel = null;
    private JTextField iterationField = null;
    private JLabel iterationLabel = null;
    private JTextField steppingField = null;
    private JLabel steppingLabel = null;
    private JCheckBox showSeq = null;

    private JTabbedPane methodsPane;
    private JTextField[] tf1 = new JTextField[6];
    private JTextField[] tf2 = new JTextField[6];
    private JTextField[] tf3 = new JTextField[6];

    RootSolverAgent() {
        JPanel m = new JPanel(new BorderLayout());
        JPanel m1 = new JPanel(new BorderLayout());  // north
        JPanel m2 = new JPanel(new BorderLayout());  // center
        JPanel m3 = new JPanel(new GridLayout(7, 1)); // east
        m.setBorder(new EmptyBorder(5, 5, 5, 5));
        m1.setBorder(new TitledBorder("Function"));
        m2.setBorder(new TitledBorder("Root Finding Methods"));
        m3.setBorder(new TitledBorder("Settings"));

        funcField = new JTextField();
        funcPasteButton = new JButton("Paste");
        funcPasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                funcField.paste();
            }
        });

        tolField = new JTextField("0.000000001");
        tolLabel = new JLabel("Tolerence :");
        iterationField = new JTextField("10000");
        iterationLabel = new JLabel("Max. iteration limit :");
        steppingField = new JTextField("0.00001");
        steppingLabel = new JLabel("Stepping(Only in Newton's method) :");
        showSeq = new JCheckBox("Show sequence generator ...");

        methodsPane = new JTabbedPane();
        createTabs();
        methodsPane.setSelectedIndex(0);

        m1.add(funcField, BorderLayout.CENTER);
        m1.add(funcPasteButton, BorderLayout.EAST);

        m2.add(methodsPane, BorderLayout.CENTER);

        m3.add(tolLabel);
        m3.add(tolField);
        m3.add(iterationLabel);
        m3.add(iterationField);
        m3.add(steppingLabel);
        m3.add(steppingField);
        m3.add(showSeq);

        m.add(m1, BorderLayout.NORTH);
        m.add(m2, BorderLayout.CENTER);
        m.add(m3, BorderLayout.EAST);

        Mathematica.MainTab.addTab("Root Solver", m);
    }

    private void createTabs() {
        for (int i = 0; i < 6; i++) {
            tf1[i] = new JTextField();
            tf2[i] = new JTextField();
            tf3[i] = new JTextField();
            tf3[i].setEditable(false);
        }

        // ========= Bisection Mathod Tab ============
        JPanel mA = new JPanel(new GridLayout(3, 1));
        JPanel mA1 = new JPanel(new BorderLayout());
        JPanel mA2 = new JPanel(new BorderLayout());
        JPanel mA3 = new JPanel(new BorderLayout());
        JButton bA = new JButton("Execute");
        mA.setBorder(new EmptyBorder(2, 2, 2, 2));
        mA1.setBorder(new TitledBorder("First initial value"));
        mA2.setBorder(new TitledBorder("Second initial value"));
        mA3.setBorder(new TitledBorder("Approx. root"));
        bA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(1);
            }
        });
        mA1.add(tf1[0], BorderLayout.CENTER);
        mA2.add(tf2[0], BorderLayout.CENTER);
        mA3.add(tf3[0], BorderLayout.CENTER);
        mA3.add(bA, BorderLayout.EAST);
        mA.add(mA1);
        mA.add(mA2);
        mA.add(mA3);
        methodsPane.addTab("Bisection", mA);

        // ========= Secant Mathod Tab ============
        JPanel mB = new JPanel(new GridLayout(3, 1));
        JPanel mB1 = new JPanel(new BorderLayout());
        JPanel mB2 = new JPanel(new BorderLayout());
        JPanel mB3 = new JPanel(new BorderLayout());
        JButton bB = new JButton("Execute");
        mB.setBorder(new EmptyBorder(2, 2, 2, 2));
        mB1.setBorder(new TitledBorder("First initial value"));
        mB2.setBorder(new TitledBorder("Second initial value"));
        mB3.setBorder(new TitledBorder("Approx. root"));
        bB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(2);
            }
        });
        mB1.add(tf1[1], BorderLayout.CENTER);
        mB2.add(tf2[1], BorderLayout.CENTER);
        mB3.add(tf3[1], BorderLayout.CENTER);
        mB3.add(bB, BorderLayout.EAST);
        mB.add(mB1);
        mB.add(mB2);
        mB.add(mB3);
        methodsPane.addTab("Secant", mB);

        // ========= False Position Mathod Tab ============
        JPanel mC = new JPanel(new GridLayout(3, 1));
        JPanel mC1 = new JPanel(new BorderLayout());
        JPanel mC2 = new JPanel(new BorderLayout());
        JPanel mC3 = new JPanel(new BorderLayout());
        JButton bC = new JButton("Execute");
        mC.setBorder(new EmptyBorder(2, 2, 2, 2));
        mC1.setBorder(new TitledBorder("First initial value"));
        mC2.setBorder(new TitledBorder("Second initial value"));
        mC3.setBorder(new TitledBorder("Approx. root"));
        bC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(3);
            }
        });
        mC1.add(tf1[2], BorderLayout.CENTER);
        mC2.add(tf2[2], BorderLayout.CENTER);
        mC3.add(tf3[2], BorderLayout.CENTER);
        mC3.add(bC, BorderLayout.EAST);
        mC.add(mC1);
        mC.add(mC2);
        mC.add(mC3);
        methodsPane.addTab("False Position", mC);

        // ========= Iterative Mathod Tab ============
        JPanel mD = new JPanel(new GridLayout(3, 1));
        JPanel mD1 = new JPanel(new BorderLayout());
        JPanel mD2 = new JPanel(new BorderLayout());
        JPanel mD3 = new JPanel(new BorderLayout());
        JButton bD = new JButton("Execute");
        tf1[3].setText("Enter valid 'g(x)' by breaking 'f(x)' in the form 'x=g(x)'.");
        tf1[3].setEditable(false);
        mD.setBorder(new EmptyBorder(2, 2, 2, 2));
        mD2.setBorder(new TitledBorder("First initial value"));
        mD3.setBorder(new TitledBorder("Approx. root"));
        bD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(4);
            }
        });
        mD1.add(tf1[3], BorderLayout.CENTER);
        mD2.add(tf2[3], BorderLayout.CENTER);
        mD3.add(tf3[3], BorderLayout.CENTER);
        mD3.add(bD, BorderLayout.EAST);
        mD.add(mD1);
        mD.add(mD2);
        mD.add(mD3);
        methodsPane.addTab("Iterative", mD);

        // ========= Newton's Mathod Tab ============
        JPanel mE = new JPanel(new GridLayout(3, 1));
        JPanel mE1 = new JPanel(new BorderLayout());
        JPanel mE2 = new JPanel(new BorderLayout());
        JPanel mE3 = new JPanel(new BorderLayout());
        JButton bE = new JButton("Execute");
        mE.setBorder(new EmptyBorder(2, 2, 2, 2));
        mE2.setBorder(new TitledBorder("First initial value"));
        mE3.setBorder(new TitledBorder("Approx. root"));
        bE.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(5);
            }
        });
        mE2.add(tf2[4], BorderLayout.CENTER);
        mE3.add(tf3[4], BorderLayout.CENTER);
        mE3.add(bE, BorderLayout.EAST);
        mE.add(mE1);
        mE.add(mE2);
        mE.add(mE3);
        methodsPane.addTab("Newton", mE);

        // ========= Steffensen's Method Tab ============
        JPanel mF = new JPanel(new GridLayout(3, 1));
        JPanel mF1 = new JPanel(new BorderLayout());
        JPanel mF2 = new JPanel(new BorderLayout());
        JPanel mF3 = new JPanel(new BorderLayout());
        JButton bF = new JButton("Execute");
        tf1[5].setText("Enter valid 'g(x)' by breaking 'f(x)' in the form 'x=g(x)'.");
        tf1[5].setEditable(false);
        mF.setBorder(new EmptyBorder(2, 2, 2, 2));
        mF2.setBorder(new TitledBorder("First initial value"));
        mF3.setBorder(new TitledBorder("Approx. root"));
        bF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(6);
            }
        });
        mF1.add(tf1[5], BorderLayout.CENTER);
        mF2.add(tf2[5], BorderLayout.CENTER);
        mF3.add(tf3[5], BorderLayout.CENTER);
        mF3.add(bF, BorderLayout.EAST);
        mF.add(mF1);
        mF.add(mF2);
        mF.add(mF3);
        methodsPane.addTab("Steffensen", mF);
    }

    private void validateInputs(int i) throws Exception {
        double d;
        long l;
        String s1, s2, s3;
        // ===== CHECKING OF FUNCTION STRING =================
        s1 = funcField.getText();
        if (s1 == null || s1.length() == 0) throw new Exception("No function specified.");
        Function f = new Function(s1);
        if (!f.isAnyVariable() || f.getVariables().length > 1)
            throw new Exception("No variable specified.\nOR variables are more than one.");

        // ===== CHECKING OF SETTING VALUES ==================
        s1 = tolField.getText();
        s2 = iterationField.getText();
        s3 = steppingField.getText();
        if (s1 == null || s2 == null || s3 == null || s1.length() < 2 || s2.length() < 2 || s3.length() < 2)
            throw new Exception("Setting values are invalid.");
        d = Double.parseDouble(s1);
        l = Long.parseLong(s2);
        d = Double.parseDouble(s3);

        // ===== CHECKING OF INITIAL VALUES ==================
        s1 = tf1[i - 1].getText();
        s2 = tf2[i - 1].getText();
        if (i < 4) {
            if (s1 == null || s2 == null || s1.length() == 0 || s2.length() == 0)
                throw new Exception("Initial values are invalid.");
            d = Double.parseDouble(s1);
            d = Double.parseDouble(s2);
        } else {
            if (s2 == null || s2.length() == 0) throw new Exception("Initial value are invalid.");
            d = Double.parseDouble(s2);
        }
    }

    private void execute(int m) {
        try {
            validateInputs(m);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage() + "...\n Check inputs ...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        char[] vars;
        char var;
        double[] vals;
        Function function;
        RootSolver rs;

        double tol = Double.parseDouble(tolField.getText());
        long iterations = Long.parseLong(iterationField.getText());
        double stepping = Double.parseDouble(steppingField.getText());

        try {
            function = new Function(funcField.getText());
            vars = function.getVariables();
            var = vars[0];
            vals = new double[vars.length];

            rs = new RootSolver(function, vars, vals, var);
            rs.setTolerence(tol);
            rs.setIterationsLimit(iterations);
            rs.setStepping(stepping);
            if (showSeq.isSelected()) rs.setSeqRenderingPanel(null, true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, "Unknown Error...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double a = 0, b = 0, result = 0;
        if (m < 4) a = Double.parseDouble(tf1[m - 1].getText());
        b = Double.parseDouble(tf2[m - 1].getText());
        try {
            if (m == 1) result = rs.bisectionMethod(a, b);
            else if (m == 2) result = rs.secantMethod(a, b);
            else if (m == 3) result = rs.falsePositionMethod(a, b);
            else if (m == 4) result = rs.iterativeMethod(b);
            else if (m == 5) result = rs.newtonMethod(b);
            else if (m == 6) result = rs.steffensenMethod(b);

            tf3[m - 1].setText(Double.toString(result));
        } catch (RootSolverWrongValueException e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, "Initial values are invalid./nTry again...", "Calculation Error", JOptionPane.ERROR_MESSAGE);
        } catch (RootSolverDivergenceException e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, "Initial values causes divergence./nTry again...", "Calculation Error", JOptionPane.ERROR_MESSAGE);
        } catch (RootSolverIterationsEndException e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, "Iteration limit is reached./nTry again...", "Calculation Error", JOptionPane.ERROR_MESSAGE);
        } catch (RootSolverException e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage() + ".\nTry again...", "Calculation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
