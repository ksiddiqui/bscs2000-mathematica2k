package org.ksiddiqui.bscs.mathematica.maths;

import java.io.Serializable;
import java.util.Vector;


/******************************************************************************************
 *                                                                                         *
 *       Divide Difference  Class ..........                                               *
 *                                                                                         *
 *******************************************************************************************/


public class DivideDifference implements Serializable {
    private final String DDDataError = "Invalid data Error";
    private final String DDElementError = "Wrong element pointing Error";
    private final String DDError = "Unknown Error";


    private boolean isEquiSpacedData;
    private double[] x;
    private double[] Fx;
    private Vector difference = null;


    /*****************************************************************************
     *            Constructors ...........                                        *
     *****************************************************************************/

    DivideDifference(double[] x, double[] y) throws Exception {
        if (x.length != y.length) throw new DivideDifferenceException(DDDataError);

        this.x = x;
        this.Fx = y;

        chkForEquiSpace();
    }

    DivideDifference(int data, double[][] values) throws Exception {
        x = new double[data];
        Fx = new double[data];

        for (int i = 0; i < data; i++) {
            x[i] = values[0][i];
            Fx[i] = values[1][i];
        }

        chkForEquiSpace();
    }


    public void chkForEquiSpace() throws Exception {
        double diff = x[1] - x[0];

        isEquiSpacedData = true;
        for (int i = 1; i < (x.length - 1); i++) {
            if (x[i + 1] - x[i] != diff) {
                isEquiSpacedData = false;
                break;
            }
        }
    }

    /*****************************************************************************
     *            Properties Methods ...........                                  *
     *****************************************************************************/

    public int getDataFrequency() throws Exception {
        return x.length;
    }

    public double[] getx() throws Exception {
        return x;
    }

    public double[] getFx() throws Exception {
        return Fx;
    }


    public boolean isEquiSpacedData() throws Exception {
        return isEquiSpacedData;
    }


    /*****************************************************************************
     *            Operations Methods ...........                                  *
     *****************************************************************************/

    private void addToDifference(double[] values) {
        Double[] obj = new Double[values.length];

        for (int i = 0; i < values.length; i++)
            obj[i] = new Double(values[i]);
        difference.addElement(obj);
    }


    public void doDivideDifference() throws Exception {
        difference = new Vector();
        double[] refCol, tarCol;
        double tmp;
        int dd = Fx.length, column = 1;

        // ========= FOR zero ORDER DD ===================
        refCol = Fx;
        addToDifference(refCol);
        dd--;

        // ========= FOR nth ORDER DD ====================
        for (int i = 0; i < dd; dd--, column++) {
            tarCol = new double[dd];
            for (int j = 0; j < dd; j++) {
                tarCol[j] = ((refCol[j + 1] - refCol[j]) / (x[j + column] - x[j]));
            }
            refCol = tarCol;
            addToDifference(refCol);
        }
    }

    public void doForwardDifference() throws Exception {
        difference = new Vector();
        double[] refCol, tarCol;
        double tmp;
        int dd = Fx.length, column = 1;

        // ========= FOR zero ORDER DD ===================
        refCol = Fx;
        addToDifference(refCol);
        dd--;

        // ========= FOR nth ORDER DD ====================
        for (int i = 0; i < dd; dd--, column++) {
            tarCol = new double[dd];
            for (int j = 0; j < dd; j++) {
                tarCol[j] = (refCol[j + 1] - refCol[j]);
            }
            refCol = tarCol;
            addToDifference(refCol);
        }
    }


    public void doFDivideDifference() throws Exception {
        doDivideDifference();
    }

    public void doBDivideDifference() throws Exception {
        difference = new Vector();
        double[] refCol, tarCol;
        double tmp;
        int dd = Fx.length, column = 1;

        // ========= FOR zero ORDER DD ===================
        refCol = Fx;
        addToDifference(refCol);
        dd--;

        // ========= FOR nth ORDER DD ====================
        for (int i = 0; i < dd; dd--, column++) {
            tarCol = new double[dd];
            for (int j = dd - 1; j >= 0; j++) {
                tarCol[j] = ((refCol[j + 1] - refCol[j]) / (x[j + column] - x[j]));
            }
            refCol = tarCol;
            addToDifference(refCol);
        }
    }


    public void doFDifference() throws Exception {
        doForwardDifference();
    }

    public void doBDifference() throws Exception {
        difference = new Vector();
        double[] refCol, tarCol;
        double tmp;
        int dd = Fx.length, column = 1;

        // ========= FOR zero ORDER DD ===================
        refCol = Fx;
        addToDifference(refCol);
        dd--;

        // ========= FOR nth ORDER DD ====================
        for (int i = 0; i < dd; dd--, column++) {
            tarCol = new double[dd];
            for (int j = dd - 1; j >= 0; j++) {
                tarCol[j] = (refCol[j + 1] - refCol[j]);
            }
            refCol = tarCol;
            addToDifference(refCol);
        }
    }


    /*****************************************************************************
     *            Difference Query Methods ...........                            *
     *****************************************************************************/

    public Vector getDDTable() {
        return difference;
    }

    public double getDD(int i, int j) throws Exception {
        int num = Fx.length - 1;

        if (i > num || j > (num - i) || i < 0 || j < 0) throw new DivideDifferenceException(DDElementError);

        Double[] obj = (Double[]) difference.get(j);

        return obj[i].doubleValue();
    }

}


class DivideDifferenceException extends Exception {
    DivideDifferenceException(String exceptionNo) {
        super(exceptionNo);
    }
}


