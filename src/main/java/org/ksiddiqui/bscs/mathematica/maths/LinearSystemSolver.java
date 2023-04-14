package org.ksiddiqui.bscs.mathematica.maths;

import org.ksiddiqui.bscs.mathematica.maths.exceptions.LinearSystemSolverIterationsEndException;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.LinearSystemSolverSingularException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;

//import Matrix;

/******************************************************************************************
 *                                                                                         *
 *       Linear Equation System Solver class ..........                                    *
 *                                                                                         *
 *******************************************************************************************/


public class LinearSystemSolver implements Serializable {
    final String invalidVariableError = "Invalid variable Found Error";
    final String noVariableError = "No variable Found Error";
    final String unknownError = "Unknown Error";


    private double[][] coefficients;
    private double[] constants;
    private int order;
    private Matrix coefficientMatrix;
    private Matrix constantMatrix;

    private double tolerence;
    private long iterationsLimit;


    private boolean isRendering;
    private JPanel panel = null;
    private JFrame frame = null;
    private JLabel label = null;
    private JTable seqTable = null;
    private DefaultTableModel seqTableModel = null;


    /*****************************************************************************
     *            Constructors ...........                                        *
     *****************************************************************************/

    public LinearSystemSolver(double[][] dd, double[] d, int o) throws Exception {
        coefficients = dd;
        constants = d;
        order = o;

        coefficientMatrix = new Matrix(o, o, dd);
        constantMatrix = new Matrix(o, 1, d);

        tolerence = 0.0000001;
        iterationsLimit = 10000;
    }

    public double getTolerence() {
        return tolerence;
    }

    /*****************************************************************************
     *            Setting & Queury Methods .......                                *
     *****************************************************************************/

    public void setTolerence(double d) {
        tolerence = d;
    }

    public long getIterationsLimit() {
        return iterationsLimit;
    }

    public void setIterationsLimit(long l) {
        iterationsLimit = l;
    }

    public double[][] getCoefficients() {
        return coefficients;
    }

    public double[] getConstants() {
        return constants;
    }

    public Matrix getCoefficientMatrix() {
        return coefficientMatrix;
    }

    public Matrix getConstantMatrix() {
        return constantMatrix;
    }

    public int getOrder() {
        return order;
    }

    /*****************************************************************************
     *            Primary Methods .......                                         *
     *****************************************************************************/

    public boolean isSingular() throws Exception {
        double d = coefficientMatrix.getDeterminantValue();

        if (d == 0) return true;
        return false;
    }


    /*****************************************************************************
     *            Secondary Methods .......                                       *
     *****************************************************************************/


    public double[] eliminationMethod() throws Exception {
        if (isSingular()) throw new LinearSystemSolverSingularException();

        Matrix matrix = coefficientMatrix.augment(constantMatrix, 1);
        matrix = matrix.toDiagonalMatrix();
        double[][] data = matrix.getElements();
        double[] values = new double[order];

        for (int i = 0; i < order; i++) {
            values[i] = data[i][order] / data[i][i];
        }

        return values;
    }

    public double[] jacobiMethod() throws Exception {
        if (isSingular()) throw new LinearSystemSolverSingularException();

        if (isRendering) setPanel("Jacobi Method", order);

        Matrix matrix = coefficientMatrix.augment(constantMatrix, 1);
        double[][] data = matrix.getElements();
        double[] values = new double[order];
        double[] valuesX = new double[order];
        double tmp = 0;
        int iterations = 0;
        boolean isSolution = false;

        // ======== ORDERING THE SYSTEM (PIVOTING) ============
        for (int i = 0; i < order - 1; i++)
            data = matrix.pivoting(data, i);

        // ======== INITIAL VALUES SETTING ====================
        for (int i = 0; i < order; i++) {
            valuesX[i] = 0;
            tmp = data[i][i];
            for (int j = 0; j < order + 1; j++)
                data[i][j] = data[i][j] / tmp;
        }

        // ======== PROCESS ===================================
        while (true) {
            for (int i = 0; i < order; i++) {
                tmp = 0;
                for (int j = 0; j < order; j++) {
                    if (j == i) continue;
                    tmp += data[i][j] * valuesX[j];
                }
                values[i] = data[i][order] - tmp;
            }

            if (isRendering) renderIteration(iterations, values);

            isSolution = true;
            for (int i = 0; i < order; i++) {
                if (Math.abs(valuesX[i] - values[i]) >= tolerence) {
                    isSolution = false;
                    break;
                }
            }
            if (isSolution) break;

            for (int i = 0; i < order; i++) valuesX[i] = values[i];
            iterations++;
            if (iterations > iterationsLimit) throw new LinearSystemSolverIterationsEndException();
        }

        return values;
    }


    public double[] gaussSeidelMethod() throws Exception {
        if (isSingular()) throw new LinearSystemSolverSingularException();

        if (isRendering) setPanel("Gauss Seidel Method", order);

        Matrix matrix = coefficientMatrix.augment(constantMatrix, 1);
        double[][] data = matrix.getElements();
        double[] values = new double[order];
        double[] valuesX = new double[order];
        double tmp = 0;
        int iterations = 0;
        boolean isSolution = false;

        // ======== ORDERING THE SYSTEM (PIVOTING) ============
        for (int i = 0; i < order - 1; i++)
            data = matrix.pivoting(data, i);

        // ======== INITIAL VALUES SETTING ====================
        for (int i = 0; i < order; i++) {
            values[i] = 0;
            tmp = data[i][i];
            for (int j = 0; j < order + 1; j++)
                data[i][j] = data[i][j] / tmp;
        }

        // ======== PROCESS ===================================
        while (true) {
            for (int i = 0; i < order; i++) valuesX[i] = values[i];
            for (int i = 0; i < order; i++) {
                tmp = 0;
                for (int j = 0; j < order; j++) {
                    if (j == i) continue;
                    tmp += data[i][j] * values[j];
                }
                values[i] = data[i][order] - tmp;
            }

            if (isRendering) renderIteration(iterations, values);

            isSolution = true;
            for (int i = 0; i < order; i++) {
                if (Math.abs(valuesX[i] - values[i]) >= tolerence) {
                    isSolution = false;
                    break;
                }
            }
            if (isSolution) break;

            iterations++;
            if (iterations > iterationsLimit) throw new LinearSystemSolverIterationsEndException();
        }

        return values;
    }

    public boolean getRendering() {
        return isRendering;
    }

    /*****************************************************************************
     *            Sequence Rendering Methods .......                              *
     *****************************************************************************/

    public void setRendering(boolean b) {
        isRendering = b;
    }

    public void setSeqRenderingPanel(JPanel p, boolean b) {
        if (p == null) {
            frame = new JFrame("Sequence Generator");

            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    endSeqRendering();
                }
            });
            frame.setSize(300, 500);

            Container mC = frame.getContentPane();
            p = new JPanel();
            mC.add(p);
        }
        panel = p;
        panel.setLayout(new BorderLayout());

        isRendering = b;
    }

    public void setPanel(String str, int cols) {
        cols++;
        String[] columns = new String[cols];
        columns[0] = new String("#");
        for (int i = 1; i < cols; i++)
            columns[i] = new String("x" + Integer.toString(i));

        seqTableModel = new DefaultTableModel(columns, 0);
        seqTable = new JTable(seqTableModel);
        seqTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        seqTable.doLayout();
        seqTable.setShowGrid(true);

        label = new JLabel(str);
        label.setBorder(new EtchedBorder());

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(seqTable), BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void renderIteration(int i, double[] x) {
        Object[] rowObjects = new Object[x.length + 1];
        rowObjects[0] = new Integer(i);
        for (int j = 0; j < x.length; j++)
            rowObjects[j + 1] = new Double(x[j]);

        seqTableModel.addRow(rowObjects);
    }

    public void endSeqRendering() {
        isRendering = false;
        frame.dispose();

        frame = null;
        seqTable = null;
        seqTableModel = null;
        label = null;
        panel = null;
    }


// END OF CLASS
}


