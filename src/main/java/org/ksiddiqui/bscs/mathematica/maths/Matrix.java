package org.ksiddiqui.bscs.mathematica.maths;

import java.io.Serializable;


/******************************************************************************************
 *                                                                                         *
 *       Function Class ..........                                                         *
 *                                                                                         *
 *******************************************************************************************/


public class Matrix implements Serializable {
    private final String matrixElementError = "Invalid numbers of elements Error";
    private final String matrixOrderError = "Invalid order number Error";
    private final String matrixOperationError = "Invalid matrix in operation Error";
    private final String matrixError = "Unknown Error";


    private int rows, columns;
    private double[][] matrix;

    /*****************************************************************************
     *            Constructors ...........                                        *
     *****************************************************************************/


    Matrix(int rows, int columns) throws Exception {
        this.rows = rows;
        this.columns = columns;
    }

    public Matrix(int r, int c, double[][] matrix) throws Exception {
        this.matrix = matrix;
        rows = r;
        columns = c;

    }

    Matrix(int row, int col, double[] values) throws Exception {
        matrix = new double[row][col];
        int index = 0;

        if (values.length != (row * col)) throw new MatrixException(matrixElementError);

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++)
                matrix[i][j] = values[index++];
        }

    }

    /*****************************************************************************
     *            Properties Methods ...........                                  *
     *****************************************************************************/


    public int getRows() throws Exception {
        return rows;
    }

    public void setRows(int rows) throws Exception {
        this.rows = rows;
    }

    public int getColumns() throws Exception {
        return columns;
    }

    public void setColumns(int columns) throws Exception {
        this.columns = columns;
    }

    public double getElement(int r, int c) throws Exception {
        return matrix[r][c];
    }

    public double[][] getElements() throws Exception {
        return matrix;
    }

    public void setElement(int r, int c, double value) throws Exception {
        matrix[r][c] = value;
    }

    public void setUnitMatrix() throws Exception {
        if (rows != columns) throw new MatrixException(matrixOrderError);
        if (matrix == null) matrix = new double[rows][columns];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                if (i == j) matrix[i][j] = 1;
                else matrix[i][j] = 0;
            }
    }

    public void setDiagonalMatrix(double num) throws Exception {
        if (rows != columns) throw new MatrixException(matrixOrderError);
        if (matrix == null) matrix = new double[rows][columns];

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++) {
                if (i == j) matrix[i][j] = num;
                else matrix[i][j] = 0;
            }
    }


    /*****************************************************************************
     *            Arithematic Operations Methods ...........                      *
     *****************************************************************************/


    public Matrix add(Matrix otherMatrix) throws Exception {
        if (rows != otherMatrix.getRows() || columns != otherMatrix.getColumns())
            throw new MatrixException(matrixOperationError);

        double[][] result = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                result[i][j] = matrix[i][j] + otherMatrix.getElement(i, j);
        }

        Matrix newMatrix = new Matrix(rows, columns, result);

        return newMatrix;
    }

    public Matrix sub(Matrix otherMatrix) throws Exception {
        if (rows != otherMatrix.getRows() || columns != otherMatrix.getColumns())
            throw new MatrixException(matrixOperationError);

        double[][] result = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                result[i][j] = matrix[i][j] - otherMatrix.getElement(i, j);
        }

        Matrix newMatrix = new Matrix(rows, columns, result);

        return newMatrix;
    }

    public Matrix mul(Matrix otherMatrix) throws Exception {
        if (columns != otherMatrix.getRows())
            throw new MatrixException(matrixOperationError);

        int r = otherMatrix.getRows();
        int c = otherMatrix.getColumns();
        double[][] secondMatrix = otherMatrix.getElements();

        double element = 0;
        double[][] result = new double[rows][c];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < c; j++) {
                element = 0;
                for (int k = 0; k < c; k++) {
                    element = element + (matrix[i][k] * secondMatrix[k][j]);
                }
                result[i][j] = element;
            }
        }

        Matrix newMatrix = new Matrix(rows, c, result);

        return newMatrix;
    }

    public Matrix mul(double value) throws Exception {
        double[][] result = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                result[i][j] = matrix[i][j] * value;
        }

        Matrix newMatrix = new Matrix(rows, columns, result);

        return newMatrix;
    }


    /*****************************************************************************
     *            Primary Operations Methods ...........                          *
     *****************************************************************************/


    public Matrix augment(Matrix secondMatrix, int c) throws Exception {
        double[][] newMatrix = new double[rows][columns + c];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                newMatrix[i][j] = matrix[i][j];
            for (int j = columns; j < columns + c; j++)
                newMatrix[i][j] = secondMatrix.getElement(i, j - columns);
        }

        return new Matrix(rows, columns + c, newMatrix);
    }


    public double[][] pivoting(double[][] matrixValues, int refRow) throws Exception {
        double maxElement = Math.abs(matrixValues[refRow][refRow]);
        double tmpElement = 0;
        int targetRow = refRow;

        for (int i = refRow + 1; i < rows; i++) {
            if (Math.abs(matrixValues[i][refRow]) > maxElement) {
                maxElement = Math.abs(matrixValues[i][refRow]);
                targetRow = i;
            }
        }
        if (refRow < targetRow) {
            for (int j = 0; j < columns; j++) {
                tmpElement = matrixValues[refRow][j];
                matrixValues[refRow][j] = matrixValues[targetRow][j];
                matrixValues[targetRow][j] = tmpElement;
            }
        }

        return matrixValues;
    }


    /*****************************************************************************
     *            Secondary Operations Methods ...........                        *
     *****************************************************************************/


    public Matrix toUpperMatrix() throws Exception {
        double[][] newMatrix = new double[rows][columns];
        double tmp, ratio;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                newMatrix[i][j] = matrix[i][j];

        // ======== ELIMINATION METHOD TO CHANGE INTO UPPER TRIANGULAR FORM ==========

        for (int refR = 0, refC = 0; refR < rows - 1; refR++, refC++) {
            newMatrix = pivoting(newMatrix, refR);
            for (int tarRow = refR + 1; tarRow < rows; tarRow++) {
                ratio = (newMatrix[tarRow][refC] / newMatrix[refR][refC]);
                for (int tarCol = 0; tarCol < columns; tarCol++) {
                    tmp = newMatrix[refR][tarCol] * ratio;
                    newMatrix[tarRow][tarCol] -= tmp;
                }
            }
        }

        // ======== END ==============================================================

        return new Matrix(rows, columns, newMatrix);
    }


    public Matrix toDiagonalMatrix() throws Exception {
        double[][] newMatrix = new double[rows][columns];
        double tmp, ratio;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                newMatrix[i][j] = matrix[i][j];

        // ======== GAUSS JORDON METHOD TO CHANGE INTO DIAGONAL FORM ================

        for (int refR = 0, refC = 0; refR < rows; refR++, refC++) {
            newMatrix = pivoting(newMatrix, refR);
            for (int tarRow = 0; tarRow < rows; tarRow++) {
                if (tarRow == refR) continue;
                ratio = (newMatrix[tarRow][refC] / newMatrix[refR][refC]);
                for (int tarCol = 0; tarCol < columns; tarCol++) {
                    tmp = newMatrix[refR][tarCol] * ratio;
                    newMatrix[tarRow][tarCol] -= tmp;
                }
            }
        }

        // ======== END ==============================================================

        return new Matrix(rows, columns, newMatrix);
    }


    public Matrix toInverseMatrix() throws Exception {
        if (rows != columns) throw new MatrixException(matrixOrderError);

        double[][] newMatrix = new double[rows][columns];
        double[][] tmpMatrix = new double[rows][columns];
        double tmp;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                newMatrix[i][j] = matrix[i][j];

        Matrix m1, m2;
        m1 = new Matrix(rows, columns, newMatrix);
        m2 = new Matrix(rows, columns);
        m2.setUnitMatrix();
        m1 = m1.augment(m2, columns);

        m2 = m1.toDiagonalMatrix();

        newMatrix = m2.getElements();
        for (int i = 0; i < rows; i++) {
            tmp = newMatrix[i][i];
            for (int j = columns; j < (columns + columns); j++) {
                tmpMatrix[i][j - columns] = newMatrix[i][j] / tmp;
            }
        }

        return new Matrix(rows, columns, tmpMatrix);
    }


    public double getDeterminantValue() throws Exception {
        if (rows != columns) throw new MatrixException(matrixOrderError);

        double[][] newMatrix = new double[rows][columns];
        double tmp = 1;

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < columns; j++)
                newMatrix[i][j] = matrix[i][j];

        Matrix m1 = new Matrix(rows, columns, newMatrix);
        m1 = m1.toUpperMatrix();
        newMatrix = m1.getElements();

        for (int i = 0; i < rows; i++)
            tmp *= newMatrix[i][i];

        return tmp;
    }


}


class MatrixException extends Exception {
    MatrixException(String exceptionNo) {
        super(exceptionNo);
    }
}


