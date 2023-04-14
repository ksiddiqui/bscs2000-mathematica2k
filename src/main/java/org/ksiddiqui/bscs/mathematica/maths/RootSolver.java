package org.ksiddiqui.bscs.mathematica.maths;

import org.ksiddiqui.bscs.mathematica.maths.exceptions.RootSolverException;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.RootSolverIterationsEndException;
import org.ksiddiqui.bscs.mathematica.maths.exceptions.RootSolverWrongValueException;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.io.*;


/******************************************************************************************
*                                                                                         *
*       Root Solver class ..........                                                      *
*                                                                                         *
*******************************************************************************************/


public class RootSolver implements Serializable
{
        final String invalidVariableError = "Invalid variable Found Error";
        final String noVariableError = "No variable Found Error";
        final String unknownError = "Unknown Error";


        private Function function=null;
        private char[] variables;
        private char variable;
        private double[] values;
        private double tolerence;
        private double stepping;
        private long iterationsLimit;

        private RootSeqListener listener;

        private boolean isRendering;
        private JPanel panel=null;        
        private JFrame frame=null;
        private JLabel label=null;
        private JTable seqTable=null;
        private DefaultTableModel seqTableModel=null;

   /*****************************************************************************
   *            Constructors ...........                                        *
   *****************************************************************************/

        RootSolver(String str, char[] vars, double[] vals, char var) throws Exception
          {
           this(new Function(str), vars, vals, var);           
          }

        public RootSolver(Function function, char[] vars, double[] vals, char var) throws Exception
          {
           this.function = function;
           variables = vars;
           values = vals;
           variable = var;           
           tolerence = 0.0000000001;
           stepping = 0.00001;
           iterationsLimit = -1;
           
           listener = null;
           isRendering=false;
           
           function.toPostfix();
          }

   /*****************************************************************************
   *            Setting & Queury Methods .......                                *
   *****************************************************************************/

        public void setValues(double[] values) throws Exception
          {
           this.values=values;
          }    

        public void setVariables(char[] variables) throws Exception
          {
           this.variables = variables;
          }
          
        public void setVariable(char var) throws Exception
          {
           variable = var;
          }  
          
        public void setStepping(double h) throws Exception
          {
           stepping=h;
          }
          
        public void setTolerence(double tol) throws Exception
          {
           tolerence = tol;
          }  
          
        public void setIterationsLimit(long limit)
          {
           iterationsLimit=limit;
          }  
          
        public double getStepping() throws Exception
          {
           return stepping;
          }
          
        public double getTolerence() throws Exception
          {
           return tolerence;
          }  

        public long getIterationsLimit() throws Exception
          {
           return iterationsLimit;
          }
          
        public char getVariable() throws Exception
          {
           return variable;
          }  

        public char[] getVariables() throws Exception
          {
           return variables;
          }    
          
        public double[] getValues() throws Exception
          {
           return values;
          }    
          

   /*****************************************************************************
   *            Primary Methods .......                                         *
   *****************************************************************************/

          
        public void setListener(RootSeqListener listener) throws Exception
          {
           this.listener = listener;
          }  

        private int foundVariableIndex()
          {      
           int index;
           
           for (index=0; index<variables.length; index++)
               {
                if (variables[index]==variable) break;
               }
           if (index>=variables.length) index=-1;    
           return index;    
          }

   /*****************************************************************************
   *            Secondary Methods .......                                       *
   *****************************************************************************/

        public double bisectionMethod(double a, double b) throws Exception
          {
           int iterations = 0;
           double fa, fb, fc, c;
           int index = foundVariableIndex();
           
           if (variables.length == 0) throw new RootSolverException(noVariableError);
           if (index == -1) throw new RootSolverException(invalidVariableError); 
           
           if (isRendering) setPanel("Bisection Method | f: "+function.toString());

           values[index]=a;
           fa = function.evaluator(variables, values);
           values[index]=b;
           fb = function.evaluator(variables, values);
           if (fa*fb>0) throw new RootSolverWrongValueException(null);
           
           c = (a + b)/2;
           values[index]=c;
           fc = function.evaluator(variables, values);
           
           while (Math.abs(fc) > tolerence)
             {
              if (listener != null) listener.newIteration(c, fc);
              if (isRendering) renderIteration(iterations, c, fc);
             
              if (fa*fc > 0) { a=c; fa=fc; }
              else           { b=c; fb=fc; }
              c = (a + b)/2;
              values[index]=c;
              fc = function.evaluator(variables, values);
              
              iterations++;
              if (iterations > iterationsLimit && iterationsLimit>0)
                 throw new RootSolverIterationsEndException(null);
             }
                           
           return c;
          }

        public double secantMethod(double a, double b) throws Exception
          {
           int iterations = 0;
           double fa, fb, fc, c;
           int index = foundVariableIndex();
           
           if (variables.length == 0) throw new RootSolverException(noVariableError);
           if (index == -1) throw new RootSolverException(invalidVariableError); 
           
           if (isRendering) setPanel("Secant Method | f: "+function.toString());

           values[index]=a;
           fa = function.evaluator(variables, values);
           values[index]=b;
           fb = function.evaluator(variables, values);
           c = b - (fb*(b - a))/(fb-fa);
           values[index]=c;
           fc = function.evaluator(variables, values);
           
           while (Math.abs(fc) > tolerence)
             {
              if (listener != null) listener.newIteration(c, fc);                              
              if (isRendering) renderIteration(iterations, c, fc);              
             
              a=b; b=c; fa=fb; fb=fc;
              c = b - (fb*(b - a))/(fb-fa);
              values[index]=c;
              fc = function.evaluator(variables, values);
              
              iterations++;
              if (iterations > iterationsLimit && iterationsLimit>0)
                 throw new RootSolverIterationsEndException(null); 
             }
                           
           return c;          
          }
        
        public double falsePositionMethod(double a, double b) throws Exception
          {
           int iterations = 0;
           double fa, fb, fc, c;
           int index = foundVariableIndex();
           
           if (variables.length == 0) throw new RootSolverException(noVariableError);
           if (index == -1) throw new RootSolverException(invalidVariableError); 

           if (isRendering) setPanel("False Position Method | f: "+function.toString());

           values[index]=a;
           fa = function.evaluator(variables, values);
           values[index]=b;
           fb = function.evaluator(variables, values);
           if (fa*fb>0) throw new RootSolverWrongValueException(null);
           
           c = b - (fb*(b - a))/(fb-fa);
           values[index]=c;
           fc = function.evaluator(variables, values);
           
           while (Math.abs(fc) > tolerence)
             {
              if (listener != null) listener.newIteration(c, fc);                              
              if (isRendering) renderIteration(iterations, c, fc);              
             
              if (fb*fc < 0) { a=b; fa=fb; }
              b=c; fb=fc;  
              
              c = b - (fb*(b - a))/(fb-fa);
              values[index]=c;
              fc = function.evaluator(variables, values);
              
              iterations++;
              if (iterations > iterationsLimit && iterationsLimit>0)
                 throw new RootSolverIterationsEndException(null); 
             }
                           
           return c;          
          }  
          
          
        public double iterativeMethod(double a) throws Exception
          {
           int iterations = 0;
           double fa=a+1, b;
           int index = foundVariableIndex();
           
           if (variables.length == 0) throw new RootSolverException(noVariableError);
           if (index == -1) throw new RootSolverException(invalidVariableError); 
           
           if (isRendering) setPanel("Fixed Point Iterative Method | f: "+function.toString());           

           values[index]=a;
           fa = function.evaluator(variables, values);

           while (Math.abs(fa-a) > tolerence)
             {
              a=fa;             
              values[index]=a;
              fa = function.evaluator(variables, values);
              
              if (listener != null) listener.newIteration(a, fa);
              if (isRendering) renderIteration(iterations, a, fa);              
              
              iterations++;
              if (iterations > iterationsLimit && iterationsLimit>0)
                 throw new RootSolverIterationsEndException(null); 
             }
                           
           return a;          
          }
          
        public double newtonMethod(double a) throws Exception
          {
           int iterations = 0;
           double fa, _fa, fc, c;
           int index = foundVariableIndex();
           
           if (variables.length == 0) throw new RootSolverException(noVariableError);
           if (index == -1) throw new RootSolverException(invalidVariableError); 
           
           if (isRendering) setPanel("Newton's Method | f: "+function.toString());           

           values[index]=a;
           fa = function.evaluator(variables, values);
           _fa = function.firstDerivative(variables, values, variable, stepping);
           
           c=a-(fa/_fa);
           values[index]=c;
           fc = function.evaluator(variables, values);
           
           while (Math.abs(fc) > tolerence)
             {
              if (listener != null) listener.newIteration(c, fc);             
              if (isRendering) renderIteration(iterations, c, fc);              
             
              a=c; fa=fc;
              values[index]=a;              
              _fa = function.firstDerivative(variables, values, variable, stepping);
              
              c=a-(fa/_fa);
              values[index]=c;
              fc = function.evaluator(variables, values);

              iterations++;
              if (iterations > iterationsLimit && iterationsLimit>0)
                 throw new RootSolverIterationsEndException(null); 
             }
                           
           return c;          
          }

        public double steffensenMethod(double a) throws Exception
          {
           int iterations = 0;
           double b, c, d=a, fd=d+1;
           int index = foundVariableIndex();
           
           if (variables.length == 0) throw new RootSolverException(noVariableError);
           if (index == -1) throw new RootSolverException(invalidVariableError); 

           if (isRendering) setPanel("Steffensen's Method | f: "+function.toString());           

           while (Math.abs(fd-d) > tolerence)
             {
              a=d;             
              values[index]=a;
              b = function.evaluator(variables, values);
              values[index]=b;
              c = function.evaluator(variables, values);              
              
              d = a - ( Math.pow((b-c),2) )/(c-2*b+a);
              values[index]=d;
              fd = function.evaluator(variables, values);
              
              if (listener != null) listener.newIteration(d, fd);   
              if (isRendering) renderIteration(iterations, d, fd);              
              
              iterations++;
              if (iterations > iterationsLimit && iterationsLimit>0)
                 throw new RootSolverIterationsEndException(null); 
             }
                           
           return d;
          }
/*
        public double mullerMethod(double a, double b, double c) throws Exception
          {
          }
*/

   /*****************************************************************************
   *            Sequence Rendering Methods .......                              *
   *****************************************************************************/

        public void setRendering(boolean b)
          {
           isRendering = b;
          }

        public boolean getRendering()
          {
           return isRendering;
          }

        public void setSeqRenderingPanel(JPanel p, boolean b)
          {
           if (p==null)
              {
               frame = new JFrame("Sequence Generator");
           
               frame.addWindowListener(new WindowAdapter() {
                  public void windowClosing(WindowEvent e) {endSeqRendering();}
                  });
               frame.setSize(200,500);               
           
               Container mC = frame.getContentPane();               
               p = new JPanel();
               mC.add(p);
              }
           panel=p;
           panel.setLayout(new BorderLayout());
           
           isRendering = b;   
          }
          
        public void setPanel(String str)
          {
           seqTableModel = new DefaultTableModel(new String[] { "#", "Approx. Root", "Value"}, 0);
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
        
        private void renderIteration(int i, double x, double Fx)
          {
           Object[] rowObjects = new Object[3];
           rowObjects[0]=new Integer(i);
           rowObjects[1]=new Double(x);
           rowObjects[2]=new Double(Fx);           
          
           seqTableModel.addRow (rowObjects);
          } 
          
        public void endSeqRendering()
          {
           isRendering=false; 
           frame.dispose();
           
           frame=null;
           seqTable=null;
           seqTableModel=null;
           label=null;
           panel=null;
          }  
          

// END OF CLASS
}


