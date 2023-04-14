package org.ksiddiqui.bscs.mathematica.maths;

import org.ksiddiqui.bscs.mathematica.maths.exceptions.InterpolationSolverException;

import java.util.*;
import java.io.*;

//import FunctionGrapher;
//import LinearSystemSolver;
//import Function;
//import DivideDifference;

/******************************************************************************************
*                                                                                         *
*       Interpolation Solver class ..........                                             *
*                                                                                         *
*******************************************************************************************/


public class InterpolationSolver implements Serializable
{
        final String incompleteDataError = "Incomplete data Error";
        final String invalidItemError = "Invalid data item Error";
        final String insufficientDataError = "Insufficient data Error";        
        final String unknownError = "Unknown Error";
        final String deadLockError = "Calculation DEADLOCK .... Either\n -Infinity approached\n -Complex number evaluated\nTry another interval...";

        private double[] x,Fx;
        private String function;
        private DivideDifference divideDifference;
        
        private boolean isRendering;
        private FunctionGrapher fg;



   /*****************************************************************************
   *            Constructors ...........                                        *
   *****************************************************************************/

   public InterpolationSolver(double[] x, double[] Fx) throws Exception
          {
           if (x.length!=Fx.length) throw new InterpolationSolverException(incompleteDataError);
          
           this.x=x;
           this.Fx=Fx;
           
           divideDifference = new DivideDifference(x, Fx);           
           divideDifference.doDivideDifference();
           isRendering = false;
          }


   /*****************************************************************************
   *            Setting & Queury Methods .......                                *
   *****************************************************************************/

        public DivideDifference getDivideDifference()
          {
           return divideDifference;
          }
          
        public Vector getDivideDifferenceTable()
          {
           return divideDifference.getDDTable();
          }
          
        public int getxLength()
          {
           return x.length;
          }
          
        public int getFxLength()
          {
           return Fx.length;
          }  
          
        public double[] getx()
          {
           return x;  
          }
          
        public double[] getFx()
          {
           return Fx;
          }
          
        public double getx(int i) throws Exception    
          {
           return x[i];
          }
          
        public double getFx(int i) throws Exception
          {
           return Fx[i];
          }  
        
        public double getFx(double d) throws Exception
          {
           int i;
           for (i=0; i<x.length; i++)
               { if (x[i]==d) break; }
           
           if (i==x.length) throw new InterpolationSolverException(invalidItemError);    
           return Fx[i];     
          }
          
        public double getx(double d) throws Exception
          {
           int i;
           for (i=0; i<Fx.length; i++)
               { if (Fx[i]==d) break; }
               
           if (i==Fx.length) throw new InterpolationSolverException(invalidItemError);
           return x[i];     
          }
          
        
        public double setFx(double d, double n) throws Exception
          {
           int i;
           for (i=0; i<x.length; i++)
               { if (x[i]==d) break; }
           
           if (i==x.length) throw new InterpolationSolverException(invalidItemError);    
           
           double fx=Fx[i];
           Fx[i]=n;
           
           return fx;
          }
          
        public double setFx(int i, double n) throws Exception
          {
           if (i<0 || i>=x.length) throw new InterpolationSolverException(invalidItemError);    
           
           double fx=Fx[i];
           Fx[i]=n;
           
           return fx;
          }


   /*****************************************************************************
   *            Primary Methods .......                                         *
   *****************************************************************************/

        public boolean validateInterval(int x1, int x2) 
          {
           boolean cond=true;
           
           if (x1>x2) cond=false;
           try { getFx(x1); getFx(x2); }
           catch(Exception e){ cond = false; }
           return cond;
          }
          
        public int getDegree(int x1, int x2)
          {
           return (x2-x1);
          }  
          
        public String getFunction()
          {
           return function;
          }
          
   /*****************************************************************************
   *            Secondary Methods .......                                       *
   *****************************************************************************/

        public double lagrangeInterpolation(int x1, int x2, double valx, int degree) throws Exception
          {
           String func = new String("");
           String xi, xj, fxi;
           char[] var = new char[1];
           double[] val = new double[1];
           double result=0;
           var[0]='X'; val[0]=valx;           
           int degreeX=0;
           
           for (int i=x1; i<=x2; i++)
               {
                xi = String.valueOf(x[i]); 
                fxi = String.valueOf(Fx[i]); 
                
                for (int j=x1; j<=x2; j++)
                    {
                     if (i==j) continue;
                     xj = String.valueOf(x[j]); 
                     
                     func = func+"(X-"+xj+")/("+xi+"-"+xj+")*";
                    }
                func = func+fxi+"+";    
                
                degreeX++;
                if (degreeX>degree) break;
               }
           func = func+"0";        
           function =" "+func+"\n";
           
           Function tmp = new Function(func);
           result=tmp.evaluator(var, val);
           if (Double.isInfinite(result) || Double.isNaN(result)) throw new Exception(deadLockError);
           
           if (isRendering && fg!=null) fg.drawGraph(func, x[x1], x[x2]);
           
           return result;
          }

        private Vector addToTable(Vector table, double[] values)
          {
           Double[] obj = new Double[values.length];
           
           for (int i=0; i<values.length; i++)  obj[i] = new Double(values[i]);
           table.addElement(obj);    
           return table;
          }
        public Vector nevilleInterpolation(int x1, int x2, double valx) throws Exception
          {
           Vector nevilleTable = new Vector();
           int columns = x2-x1+1, rows;
           rows = columns;
           double[] refCol, tarCol;
           
           // ========= FOR zero ORDER P(i,0) ===================
           refCol = new double[rows];
           for (int i=x1; i<=x2; i++) refCol[i-x1]=Fx[i];
           nevilleTable=addToTable(nevilleTable, refCol);
           rows--; 
           
           // ========= FOR nth ORDER P(i,j) ====================
           for (int j=1; j<columns; j++, rows--)
               {
                tarCol = new double[rows];                
                for (int i=0; i<rows; i++) 
                    tarCol[i] = ( (valx-x[x1+i])*refCol[i+1] + (x[x1+i+j]-valx)*refCol[i] ) / ( x[x1+i+j]-x[x1+i] );                
                refCol = tarCol;    
                nevilleTable=addToTable(nevilleTable, refCol);                
               }

           return nevilleTable;           
          }


        public double newtonDDInterpolation(int x1, int x2, double valx, int degree) throws Exception
          {
           String func = new String("");
           String xj, fxi;
           char[] var = new char[1];
           double[] val = new double[1];
           double result=0;
           int degreeX=0;
           var[0]='X'; val[0]=valx;
           
           for (int i=x1; i<=x2; i++)
               {     
                fxi = String.valueOf(divideDifference.getDD(x1,degreeX)); 
                
                for (int j=x1; j<i; j++)
                    {
                     xj = String.valueOf(x[j]); 
                     
                     func = func+"(X-"+xj+")*";
                    }
                func = func+fxi+"+";
                degreeX++;    
                if (degreeX>degree) break;
               }
           func = func+"0";        
           function =" "+func+"\n";
           
           Function tmp = new Function(func);
           result=tmp.evaluator(var, val);
           if (Double.isInfinite(result) || Double.isNaN(result)) throw new Exception(deadLockError);
           
           if (isRendering && fg!=null) fg.drawGraph(func, x[x1], x[x2]);           
           
           return result;
          }

        public double cubicSplineInterpolation(int x1, int x2, double valx) throws Exception
          {
           if (x1==x2) throw new InterpolationSolverException(insufficientDataError);
           
           int pts = x2-x1+1;
           String func = new String();
           char[] var = new char[1]; var[0]='X';
           double[] val = new double[1]; val[0]=valx;
           
           double[] A = new double[pts-1]; 
           double[] B = new double[pts-1]; 
           double[] C = new double[pts-1]; 
           double[] D = new double[pts-1];
           double[] S;
           double[][] matrixA = new double[pts][pts];
           double[] matrixB = new double[pts];           
           double hi, result=0;
           String sA,sB,sC,sD,sX,sY;
           LinearSystemSolver lss;           
           
           // ==== FINDING 2ND DERIVATIVES ==============================
           for (int i=0; i<pts; i++) { for (int j=0; j<pts; j++) matrixA[i][j]=0; }
           matrixA[0][0]=matrixA[pts-1][pts-1]=1;
           for (int i=1; i<(pts-1); i++)
               {
                matrixA[i][i-1]=x[i]-x[i-1];
                matrixA[i][i+0]=2*(x[i+1]-x[i-1]);
                matrixA[i][i+1]=x[i+1]-x[i];
               }
           matrixB[0]=matrixB[pts-1]=0;    
           for (int i=1; i<(pts-1); i++)
               {
                matrixB[i]=6*( divideDifference.getDD(i,1)-divideDifference.getDD(i-1,1) );
               }

           lss = new LinearSystemSolver(matrixA, matrixB, pts);
           S = lss.eliminationMethod();
           
           // ==== FINDING THE VALUES OF 'A', 'B', 'C', 'D' =============
           for (int i=0; i<(pts-1); i++)
               {
                hi = x[i+1]-x[i];
                A[i] = (S[i+1]-S[i])/(6*hi);
                B[i] = S[i]/2;
                C[i] = divideDifference.getDD(i,1)-(hi/6)*(2*S[i]+S[i+1]);
                D[i] = Fx[i];
               }
           
           // ==== EVALUATING THE CUBIC EQUATIONS =======================
           function="";
           for (int i=0; i<(pts-1); i++)
               {
                sA = String.valueOf(A[i]);
                sB = String.valueOf(B[i]);
                sC = String.valueOf(C[i]);
                sD = String.valueOf(D[i]);
                sX = String.valueOf(x[i]);
                sY = String.valueOf(Fx[i]);
                func = sA+ "*(X-" +sX+ ")^3+" +sB+ "*(X-" +sX+ ")^2+" +sC+ "*(X-" +sX+ ")+" +sY;
                function +=" P["+String.valueOf(i)+"]="+func+"\n";
           
                Function tmp = new Function(func);
                if (valx>x[i] && valx<x[i+1]) 
                   {
                    result=tmp.evaluator(var, val);
                    if (Double.isInfinite(result) || Double.isNaN(result)) throw new Exception(deadLockError);
                   } 
           
                if (isRendering && fg!=null) fg.drawGraph(func, x[i], x[i+1]);                           
               }
               
           return result;    
          }

   /*****************************************************************************
   *            Graph Rendering Methods .......                                 *
   *****************************************************************************/

        public void setFunctionGrapher(FunctionGrapher fg)
          {
           if (fg==null) fg=new FunctionGrapher(null, 0.1, 0.1);
           this.fg = fg;   

           isRendering = true;                   
          }        

// END OF CLASS
}




