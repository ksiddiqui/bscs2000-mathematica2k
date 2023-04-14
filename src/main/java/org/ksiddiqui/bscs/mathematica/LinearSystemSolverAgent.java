package org.ksiddiqui.bscs.mathematica;

import org.ksiddiqui.bscs.mathematica.maths.LinearSystemSolver;
import org.ksiddiqui.bscs.mathematica.maths.Matrix;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.LinearSystemSolverException;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.LinearSystemSolverIterationsEndException;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.LinearSystemSolverSingularException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LinearSystemSolverAgent {
    private boolean inputTableBuzy;
    private int rows, cols;
    private JTable inputTable;
    private DefaultTableModel inputTableModel;
    private TableColumnModel inputTableColumnModel;
    private JButton plus, minus;

    private JButton detButton;
    private JTextField detField;

    private JTextField tolField = null;
    private JLabel tolLabel = null;
    private JTextField iterationField = null;
    private JLabel iterationLabel = null;
    private JCheckBox showSeq = null;

    private JTabbedPane methodsPane;
    private JTable[] valuesTable = new JTable[3];
    private DefaultTableModel[] valuesTableModel = new DefaultTableModel[3];

    LinearSystemSolverAgent() {
        JPanel m = new JPanel(new GridLayout(2, 1));
        JPanel m1 = new JPanel(new BorderLayout());
        JPanel m11 = new JPanel(new BorderLayout());
        JPanel m12 = new JPanel(new BorderLayout());
        JPanel m2 = new JPanel(new BorderLayout());
        JPanel m21 = new JPanel(new BorderLayout());
        JPanel m211 = new JPanel(new BorderLayout());
        JPanel m212 = new JPanel(new BorderLayout());
        JPanel m22 = new JPanel(new GridLayout(5, 1));
        m.setBorder(new EmptyBorder(5, 5, 5, 5));
        m1.setBorder(new TitledBorder("Linear System"));
        m211.setBorder(new TitledBorder("Determinant value of coefficient matrix"));
        m212.setBorder(new TitledBorder("Solution Finding Methods"));
        m22.setBorder(new TitledBorder("Settings"));

        inputTableBuzy = false;
        rows = 2;
        cols = 3;
        inputTableModel = new DefaultTableModel(new String[]{"constant", "A x1", "B x2"}, 0);
        inputTable = new JTable(inputTableModel);
        inputTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        inputTable.setShowGrid(true);
        inputTableColumnModel = inputTable.getColumnModel();
        inputTableModel.addRow(new String[]{" ", " ", " "});
        inputTableModel.addRow(new String[]{" ", " ", " "});
        plus = new JButton("Add");
        minus = new JButton("Remove");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plusCommand();
            }
        });
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                minusCommand();
            }
        });


        tolField = new JTextField("0.000000001");
        tolLabel = new JLabel("Tolerence :");
        iterationField = new JTextField("10000");
        iterationLabel = new JLabel("Max. iteration limit :");
        showSeq = new JCheckBox("Show sequence generator ...");

        detButton = new JButton("Execute");
        detField = new JTextField();
        detField.setEditable(false);
        detButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                detCommand();
            }
        });

        methodsPane = new JTabbedPane();
        createTabs();
        methodsPane.setSelectedIndex(0);

        m11.add(new JScrollPane(inputTable), BorderLayout.CENTER);
        m12.add(plus, BorderLayout.WEST);
        m12.add(minus, BorderLayout.EAST);
        m1.add(m11, BorderLayout.CENTER);
        m1.add(m12, BorderLayout.SOUTH);

        m211.add(detButton, BorderLayout.WEST);
        m211.add(detField, BorderLayout.CENTER);
        m212.add(methodsPane);
        m21.add(m211, BorderLayout.NORTH);
        m21.add(m212, BorderLayout.CENTER);
        m22.add(tolLabel);
        m22.add(tolField);
        m22.add(iterationLabel);
        m22.add(iterationField);
        m22.add(showSeq);
        m2.add(m21, BorderLayout.CENTER);
        m2.add(m22, BorderLayout.WEST);

        m.add(m1);
        m.add(m2);

        Mathematica.MainTab.addTab("Linear System", m);
    }

    private void createTabs() {
        for (int i = 0; i < valuesTable.length; i++) {
            valuesTableModel[i] = new DefaultTableModel(new String[]{"Variables", "Values"}, 0);
            valuesTable[i] = new JTable(valuesTableModel[i]);
            valuesTable[i].setShowGrid(true);
        }

        // ========= TAB FOR GAUSS ELIMINATION METHOD =========
        JPanel mA = new JPanel(new BorderLayout());
        JButton bA = new JButton("Execute");
        bA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(1);
            }
        });
        mA.add(bA, BorderLayout.WEST);
        mA.add(new JScrollPane(valuesTable[0]), BorderLayout.CENTER);
        methodsPane.addTab("Elimination", mA);

        // ========= TAB FOR JACOBI METHOD =========
        JPanel mB = new JPanel(new BorderLayout());
        JButton bB = new JButton("Execute");
        bB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(2);
            }
        });
        mB.add(bB, BorderLayout.WEST);
        mB.add(new JScrollPane(valuesTable[1]), BorderLayout.CENTER);
        methodsPane.addTab("Jacobi", mB);

        // ========= TAB FOR GAUSS SEIDEL METHOD =========
        JPanel mC = new JPanel(new BorderLayout());
        JButton bC = new JButton("Execute");
        bC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(3);
            }
        });
        mC.add(bC, BorderLayout.WEST);
        mC.add(new JScrollPane(valuesTable[2]), BorderLayout.CENTER);
        methodsPane.addTab("Gauss Seidel", mC);
    }


    private void plusCommand() {
        while (inputTableBuzy) ;
        inputTableBuzy = true;

        String[] vals = new String[rows];
        for (int i = 0; i < rows; i++) vals[i] = new String("");
        char var = 'A';
        var += (char) rows;
        String name = String.valueOf(var) + " x" + String.valueOf(cols);
        inputTableModel.addColumn(name, vals);
        cols++;

        vals = new String[cols];
        for (int i = 0; i < cols; i++) vals[i] = new String("");
        inputTableModel.addRow(vals);
        rows++;

        inputTableBuzy = false;
    }

    private void minusCommand() {
        while (inputTableBuzy) ;
        inputTableBuzy = true;

        if (cols > 3 && rows > 2) {
            TableColumn tc = inputTableColumnModel.getColumn(cols - 1);
            inputTableColumnModel.removeColumn(tc);
            cols--;
            inputTableModel.removeRow(rows - 1);
            rows--;
        }
        inputTableBuzy = false;
    }

    private void detCommand() {
        try {
            validateInputs();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage() + "...\n Check inputs ...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int constCol = 0;
        try {
            constCol = getConstColPosition();
            Matrix matrix = new Matrix(rows, rows, getCoefficients(constCol));
            double det = matrix.getDeterminantValue();
            detField.setText(String.valueOf(det));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }


    private void validateInputs() throws Exception {
        int i;
        double d;
        long l;
        String s1, s2;

        // ============== VALIDATING LINEAR SYSTEM INPUTS ================
        i = getConstColPosition();
        getCoefficients(i);
        getConstants(i);

        // ===== CHECKING OF SETTING VALUES ==================
        s1 = tolField.getText();
        s2 = iterationField.getText();
        if (s1 == null || s2 == null || s1.length() < 2 || s2.length() < 2)
            throw new Exception("Setting values are invalid.");
        d = Double.parseDouble(s1);
        l = Long.parseLong(s2);
    }

    private void execute(int m) {
        try {
            validateInputs();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage() + "...\n Check inputs ...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            for (int i = 0; i < rows; i++) valuesTableModel[m - 1].removeRow(0);
        } catch (Exception e) {
        }

        LinearSystemSolver lss;

        double tol = Double.parseDouble(tolField.getText());
        long iterations = Long.parseLong(iterationField.getText());
        double result[] = new double[0];
        int constCol = 0;

        try {
            constCol = getConstColPosition();
            lss = new LinearSystemSolver(getCoefficients(constCol), getConstants(constCol), rows);
            lss.setTolerence(tol);
            lss.setIterationsLimit(iterations);
            if (showSeq.isSelected()) lss.setSeqRenderingPanel(null, true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, "Unknown Error...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (m == 1) result = lss.eliminationMethod();
            else if (m == 2) result = lss.jacobiMethod();
            else if (m == 3) result = lss.gaussSeidelMethod();

            String s[] = new String[2];
            for (int i = 0; i < result.length; i++) {
                s[0] = " x" + String.valueOf(i + 1);
                s[1] = String.valueOf(result[i]);
                valuesTableModel[m - 1].addRow(s);
            }
        } catch (LinearSystemSolverSingularException e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, "Linear system is singular in nature./nTry again...", "Calculation Error", JOptionPane.ERROR_MESSAGE);
        } catch (LinearSystemSolverIterationsEndException e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, "Iteration limit is reached./nTry again...", "Calculation Error", JOptionPane.ERROR_MESSAGE);
        } catch (LinearSystemSolverException e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage() + ".\nTry again...", "Calculation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
        }

    }


    private int getConstColPosition() //throws Exception
    {
        int i;
        for (i = 0; i < cols; i++) {
            if (inputTableModel.getColumnName(i).equals("constant")) break;
        }

        if (i < cols) return i;
        else return -1;
    }

    private double[][] getCoefficients(int constCol) // throws Exception
    {
        double[][] values = new double[rows][cols - 1];
        int a = 0, b = 0;
        for (int i = 0; i < cols; i++) {
            if (i == constCol) continue;
            a = 0;
            for (int j = 0; j < rows; j++) {
                values[a][b] = Double.parseDouble((String) inputTableModel.getValueAt(j, i));
                a++;
            }
            b++;
        }

        return values;
    }

    private double[] getConstants(int constCol) // throws Exception
    {
        double[] values = new double[rows];
        for (int i = 0; i < rows; i++)
            values[i] = Double.parseDouble((String) inputTableModel.getValueAt(i, constCol));

        return values;
    }


}
