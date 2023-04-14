package org.ksiddiqui.bscs.mathematica.app;

import org.ksiddiqui.bscs.mathematica.maths.DifferenceViewer;
import org.ksiddiqui.bscs.mathematica.maths.InterpolationSolver;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.InterpolationSolverException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;


public class InterpolationSolverAgent {
    private boolean inputTableBuzy;
    private int rows;
    private JTable inputTable;
    private DefaultTableModel inputTableModel;
    private TableColumnModel inputTableColumnModel;
    private JButton plus, minus;

    private JTextField valueOfX, degreeOfPoly;
    //      private JButton fgButton;
    private JCheckBox fgEnable;

//      private FunctionGrapher fg;

    private JTabbedPane methodsPane;

    private JTextField[] valueAtX = new JTextField[3];
    private JTextArea[] info = new JTextArea[3];
    private JScrollPane nevilleTablePane;
    private JPanel mD1;

    InterpolationSolverAgent() {
        JPanel m = new JPanel(new BorderLayout());
        JPanel m1 = new JPanel(new BorderLayout());
        JPanel m11 = new JPanel(new BorderLayout());
        JPanel m12 = new JPanel(new BorderLayout());
        JPanel m2 = new JPanel(new BorderLayout());
        JPanel m21 = new JPanel(new GridLayout(1, 2));
        JPanel m211 = new JPanel(new BorderLayout());
        JPanel m212 = new JPanel(new BorderLayout());
        JPanel m22 = new JPanel(new BorderLayout());
        JPanel m23 = new JPanel(new BorderLayout());
        m.setBorder(new EmptyBorder(5, 5, 5, 5));
        m1.setBorder(new TitledBorder("Data Values"));
        m211.setBorder(new TitledBorder("Find Fx at X"));
        m212.setBorder(new TitledBorder("Degree"));
        m22.setBorder(new TitledBorder("Solution Finding Methods"));


        inputTableBuzy = false;
        rows = 2;
        inputTableModel = new DefaultTableModel(new String[]{"#", "x", "Fx"}, 0);
        inputTable = new JTable(inputTableModel);
        inputTable.setShowGrid(true);
        inputTable.setPreferredScrollableViewportSize(new Dimension(100, 300));
        inputTableColumnModel = inputTable.getColumnModel();
        inputTableModel.addRow(new String[]{"0", " ", " "});
        inputTableModel.addRow(new String[]{"1", " ", " "});
        plus = new JButton("Add");
        minus = new JButton("Remove");
        JButton diff = new JButton("Difference Table");
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
        diff.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDTable();
            }
        });
        m11.add(new JScrollPane(inputTable), BorderLayout.CENTER);
        m12.add(plus, BorderLayout.WEST);
        m12.add(minus, BorderLayout.EAST);
        m1.add(m11, BorderLayout.CENTER);
        m1.add(m12, BorderLayout.NORTH);
        m1.add(diff, BorderLayout.SOUTH);


        valueOfX = new JTextField();
        degreeOfPoly = new JTextField();
        m211.add(valueOfX, BorderLayout.CENTER);
        m212.add(degreeOfPoly, BorderLayout.CENTER);
        m21.add(m211);
        m21.add(m212);

        methodsPane = new JTabbedPane();
        createTabs();
        methodsPane.setSelectedIndex(0);
        m22.add(methodsPane);

//         fgButton = new JButton("Show Function Grapher");
        fgEnable = new JCheckBox("Generate graph");
/*         fgButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { showFGCommand(); }
                });     
*/
        m23.add(fgEnable, BorderLayout.CENTER);  // m23.add(fgButton, BorderLayout.EAST);

        m2.add(m21, BorderLayout.NORTH);
        m2.add(m22, BorderLayout.CENTER);
        m2.add(m23, BorderLayout.SOUTH);

        m.add(m2, BorderLayout.CENTER);
        m.add(m1, BorderLayout.WEST);

        Mathematica.MainTab.addTab("Interpolation", m);
    }

    private void createTabs() {
        for (int i = 0; i < 3; i++) {
            valueAtX[i] = new JTextField();
            valueAtX[i].setEditable(false);
            info[i] = new JTextArea();
            info[i].setEditable(false);
        }

        // ======== FOR LAGRANGE ============================
        JPanel mA = new JPanel(new BorderLayout());
        JPanel mA1 = new JPanel(new BorderLayout());
        JPanel mA2 = new JPanel(new BorderLayout());
        mA2.setBorder(new TitledBorder("Process Execution"));
        JButton bA = new JButton("Execute");
        bA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(1);
            }
        });
        mA1.add(new JScrollPane(info[0]), BorderLayout.CENTER);
        mA2.add(bA, BorderLayout.WEST);
        mA2.add(valueAtX[0], BorderLayout.CENTER);
        mA.add(mA1, BorderLayout.CENTER);
        mA.add(mA2, BorderLayout.SOUTH);

        methodsPane.addTab("Lagrangian", mA);

        // ======== FOR NEVILLE ============================
        JPanel mD = new JPanel(new BorderLayout());
        mD1 = new JPanel(new BorderLayout());
        JButton bD = new JButton("Execute");
        bD.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                executeNeville();
            }
        });
        mD.add(bD, BorderLayout.NORTH);
        mD.add(mD1, BorderLayout.CENTER);

        methodsPane.addTab("Neville", mD);

        // ======== FOR NEWTONS DD ==========================
        JPanel mB = new JPanel(new BorderLayout());
        JPanel mB1 = new JPanel(new BorderLayout());
        JPanel mB2 = new JPanel(new BorderLayout());
        mB2.setBorder(new TitledBorder("Process Execution"));
        JButton bB = new JButton("Execute");
        bB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(2);
            }
        });
        mB1.add(new JScrollPane(info[1]), BorderLayout.CENTER);
        mB2.add(bB, BorderLayout.WEST);
        mB2.add(valueAtX[1], BorderLayout.CENTER);
        mB.add(mB1, BorderLayout.CENTER);
        mB.add(mB2, BorderLayout.SOUTH);

        methodsPane.addTab("Newton", mB);

        // ======== FOR CUBICS SPLINES ======================
        JPanel mC = new JPanel(new BorderLayout());
        JPanel mC1 = new JPanel(new BorderLayout());
        JPanel mC2 = new JPanel(new BorderLayout());
        mC2.setBorder(new TitledBorder("Process Execution"));
        JButton bC = new JButton("Execute");
        bC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                execute(3);
            }
        });
        mC1.add(new JScrollPane(info[2]), BorderLayout.CENTER);
        mC2.add(bC, BorderLayout.WEST);
        mC2.add(valueAtX[2], BorderLayout.CENTER);
        mC.add(mC1, BorderLayout.CENTER);
        mC.add(mC2, BorderLayout.SOUTH);

        methodsPane.addTab("Natural Cubic Spline", mC);
    }

    private double[] getX() {
        double[] x = new double[rows];

        for (int i = 0; i < rows; i++)
            x[i] = Double.parseDouble((String) inputTable.getValueAt(i, 1));

        return x;
    }

    private double[] getFx() {
        double[] x = new double[rows];

        for (int i = 0; i < rows; i++)
            x[i] = Double.parseDouble((String) inputTable.getValueAt(i, 2));

        return x;
    }


    private void showDTable() {
        try {
            new DifferenceViewer(null, getX(), getFx());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, "Unknown Error...\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void plusCommand() {
        while (inputTableBuzy) ;
        inputTableBuzy = true;

        String[] vals = new String[3];
        for (int i = 1; i < 3; i++) vals[i] = new String("");
        vals[0] = new String(String.valueOf(rows));
        inputTableModel.addRow(vals);
        rows++;

        inputTableBuzy = false;
    }

    private void minusCommand() {
        while (inputTableBuzy) ;
        inputTableBuzy = true;

        if (rows > 2) {
            rows--;
            inputTableModel.removeRow(rows);
        }
        inputTableBuzy = false;
    }
/*          
        private void showFGCommand()
          {           
           fg = new FunctionGrapher(null);              
          }
*/

    private void validateInputs() throws Exception {
        int i;
        double d;
        long l;
        String s1, s2;

        // ======== CHECK FOR VALUE AT X FIELD && DEGREE =====
        s1 = valueOfX.getText();
        s2 = degreeOfPoly.getText();
        d = Double.parseDouble(s1);
        i = Integer.parseInt(s2);

        // ======== CHECK FOR DATA TABLE =====================
        getX();
        getFx();
        if (inputTable.getSelectedRows().length < 2)
            throw new Exception("No interval is selected.");
    }

    private void execute(int m) {
        try {
            validateInputs();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage() + "...\n Check inputs ...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        info[m - 1].setText("");
        valueAtX[m - 1].setText("");

        InterpolationSolver is;
        double xValue = Double.parseDouble(valueOfX.getText());
        int degree = Integer.parseInt(degreeOfPoly.getText());
        int[] xs = inputTable.getSelectedRows();
        double result = 0;
        String str = new String();

        try {
            is = new InterpolationSolver(getX(), getFx());
            if (fgEnable.isSelected() && Mathematica.fg != null) is.setFunctionGrapher(Mathematica.fg);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, "Unknown Error...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (m == 1) result = is.lagrangeInterpolation(xs[0], xs[xs.length - 1], xValue, degree);
            else if (m == 2) result = is.newtonDDInterpolation(xs[0], xs[xs.length - 1], xValue, degree);
            else if (m == 3) result = is.cubicSplineInterpolation(xs[0], xs[xs.length - 1], xValue);

            valueAtX[m - 1].setText(String.valueOf(result));
            str = "Starting at   x" + String.valueOf(xs[0]) + "\nEnding at   x" + String.valueOf(xs[xs.length - 1]) + "\n\nApprox. Polynomial formed is:\n" + is.getFunction();
            info[m - 1].setText(str);
        } catch (InterpolationSolverException e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage() + ".\nTry again...", "Calculation Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executeNeville() {
        try {
            validateInputs();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage() + "...\n Check inputs ...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int[] xs = inputTable.getSelectedRows();
        InterpolationSolver is;
        double xValue = Double.parseDouble(valueOfX.getText());
        Vector nevilleTable = null;
        double[] x = getX(), Fx = getFx();

        try {
            is = new InterpolationSolver(x, Fx);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, "Unknown Error...", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            nevilleTable = is.nevilleInterpolation(xs[0], xs[xs.length - 1], xValue);
        } catch (InterpolationSolverException e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage() + ".\nTry again...", "Calculation Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(Mathematica.MainTab, e.getMessage(), "Calculation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nevilleTablePane != null) mD1.remove(nevilleTablePane);
        String[] columns = new String[xs.length + 1];
        columns[0] = "x";
        for (int i = 1; i < xs.length + 1; i++) columns[i] = "P(" + String.valueOf(i - 1) + ")";

        DefaultTableModel dTableModel = new DefaultTableModel(columns, 0);
        JTable dTable = new JTable(dTableModel);
        dTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dTable.setShowGrid(true);
        nevilleTablePane = new JScrollPane(dTable);
        mD1.add(nevilleTablePane, BorderLayout.CENTER);

        for (int i = 0; i < xs.length; i++) dTableModel.addRow(new String[xs.length + 1]);

        Double[] values;
        for (int i = xs[0]; i <= xs[xs.length - 1]; i++) dTableModel.setValueAt(x[i], i - xs[0], 0);
        for (int i = 0; i < nevilleTable.size(); i++) {
            values = (Double[]) nevilleTable.get(i);
            for (int j = 0; j < values.length; j++) {
                dTableModel.setValueAt(values[j], j, i + 1);
            }
        }

        Mathematica.MainTab.repaint();
    }


}
