package org.ksiddiqui.bscs.mathematica.maths;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;

//import Function;

/******************************************************************************************
*                                                                                         *
*       Function Grapher class ..........                                                 *
*                                                                                         *
*******************************************************************************************/


public class FunctionGrapher implements Serializable
{
        private JFrame frame=null;
        private JPanel mainPanel=null;

        private FunctionGrapherPanel graphPanel=null;
                BufferedImage gridImage;
                Graphics gridImageG;
        
        private JLabel colorLabel=null;
        private JTextField scaleXField=null, scaleYField=null;
                int incXY;
        private JToggleButton gridToggle;
                JToggleButton markToggle;        
        
        private JTextField functionField=null;
        private JTextField startX=null, endX=null;
                JTextField currX=null, currY=null;                

        private Color graphColor;
                double scaleX, scaleY;

        private Color gridColor;
        private int centerX, centerY;        
                int _centerX, _centerY;                
        private int graphW, graphH;

   /*****************************************************************************
   *            Constructors ...........                                        *
   *****************************************************************************/

   public FunctionGrapher(JPanel p)
          {
           this(p, 1,1, 1000,1000, 350,600);
          }
          
        FunctionGrapher(JPanel p, double s1, double s2)
          {
           this(p, s1,s2, 1000,1000, 350,600);
          }


        FunctionGrapher(JPanel p, double s1, double s2, int w, int h, int cx, int cy)
          {
           if (p==null)
              {
               frame = new JFrame("Function Grapher");
           
               frame.addWindowListener(new WindowAdapter() {
                  public void windowClosing(WindowEvent e) { /* exit(); */ }
                  });
               frame.setSize(800,600);               
           
               Container mC = frame.getContentPane();               
               p = new JPanel();
               mC.add(p);
              }
           mainPanel=p;
           mainPanel.setLayout(new BorderLayout());
           
           graphW=w;
           graphH=h;
           graphColor=Color.black;
           scaleX=s1;
           scaleY=s2;
           gridColor=Color.gray;
           centerX=cx;
           centerY=cy;
           incXY=5;
           
           setMainPanel();
           clearGraph();
          }

   /*****************************************************************************
   *            Setting & Queury Methods .......                                *
   *****************************************************************************/

        public void setCenter(int x, int y)
          {
           centerX = x; centerY = y;
          }
          
        public void setGraphDimension(int w, int h)
          {
           graphW = w; graphH = h;
          }  

        public int getCenterX()
          {
           return centerX;
          }

        public int getCenterY()
          {
           return centerY;
          }

        public int getGraphWidth()
          {
           return graphW;
          }

        public int getGraphHeight()
          {
           return graphH;
          }

        
   /*****************************************************************************
   *            Primary Methods .......                                         *
   *****************************************************************************/

        public void setMainPanel()
          {
           JPanel m1 = new JPanel(new BorderLayout());
           JPanel m11 = new JPanel(new GridLayout(2,1));
           JPanel m12 = new JPanel(new GridLayout(2,2));
           JPanel m13 = new JPanel(new GridLayout(1,4));
           JPanel m2 = new JPanel(new BorderLayout());
           JPanel m3 = new JPanel(new GridLayout(1,4));
           JPanel m31 = new JPanel(new BorderLayout());           
           JPanel m32 = new JPanel(new GridLayout(2,2));
           JPanel m33 = new JPanel(new BorderLayout());
           JPanel m34 = new JPanel(new GridLayout(2,2));
           mainPanel.setBorder(new EmptyBorder(5,5,5,5));
           m1.setBorder(new TitledBorder("Graph Settings"));
           m11.setBorder(new TitledBorder("Graph color"));
           m12.setBorder(new TitledBorder("Graph scale"));
           m31.setBorder(new TitledBorder("Function"));
           m32.setBorder(new TitledBorder("Graphing interval"));
           m34.setBorder(new TitledBorder("current coordinates"));
           
           //=========== SETTING CONTROLS FOR GRAPHS ==========================
           JButton colorButton = new JButton("Choose Color");
           colorLabel=new JLabel(); colorLabel.setBackground(graphColor); colorLabel.setOpaque(true);
           scaleXField=new JTextField(String.valueOf(scaleX)); scaleYField=new JTextField(String.valueOf(scaleY));
           JButton clearButton = new JButton("Clear");
           JButton refreshButton = new JButton("Refresh");           
           gridToggle = new JToggleButton("Grids");  gridToggle.setSelected(true);
           markToggle = new JToggleButton("Marker");  markToggle.setSelected(false);
           colorButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e)
                  {
                   Color c=JColorChooser.showDialog(frame, "Color Chooser", graphColor);
                   if (c!=null) { graphColor=c; gridImageG.setColor(c); }
                   colorLabel.setBackground(graphColor);
                   colorLabel.repaint();
                  }
               });
           clearButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { clearGraph(); }
               });
           refreshButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { refreshGraph(); }
               });               
           m11.add(colorButton); m11.add(colorLabel);
           m12.add(new JLabel("On x axis- 1 square =")); m12.add(scaleXField);
           m12.add(new JLabel("On y axis- 1 square =")); m12.add(scaleYField);
           m13.add(gridToggle);m13.add(markToggle); m13.add(clearButton); m13.add(refreshButton);
           m1.add(m11, BorderLayout.WEST); m1.add(m12, BorderLayout.EAST); m1.add(m13, BorderLayout.CENTER);
           
           //=========== SETTING GRAPH PANEL ==========================
           graphPanel = new FunctionGrapherPanel(this);
           m2.add(new JScrollPane(graphPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS ), BorderLayout.CENTER);
           graphPanel.setSize(graphW, graphH);                                
           
           //=========== SETTING FUNCTION PANEL =======================
           functionField = new JTextField();
           startX = new JTextField();
           endX = new JTextField();
           JButton drawGraph = new JButton("Draw Graph");
           currX = new JTextField(); currX.setEditable(false); currX.setAutoscrolls(false);
           currY = new JTextField(); currY.setEditable(false); currY.setAutoscrolls(false);
           drawGraph.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) { execute(); }
               });
           m31.add(functionField, BorderLayout.CENTER);
           m32.add(new JLabel("starting X")); m32.add(startX);
           m32.add(new JLabel("ending X")); m32.add(endX);
           m33.add(drawGraph, BorderLayout.CENTER);
           m34.add(new JLabel(" X :")); m34.add(currX);
           m34.add(new JLabel(" Y :")); m34.add(currY);
           m3.add(m31); m3.add(m32); m3.add(m33); m3.add(m34);
           
           //=========== GENERAL SETTINGS==============================
           
           mainPanel.add(m1, BorderLayout.NORTH);
           mainPanel.add(m2, BorderLayout.CENTER);
           mainPanel.add(m3, BorderLayout.SOUTH);
           
           if (frame!=null)
              frame.setVisible(true);
          }

        public void exit()
          {
           if (frame!=null)
              frame.dispose();
          }
          
   /*****************************************************************************
   *            Secondary Methods .......                                       *
   *****************************************************************************/

        private void validateInputs() throws Exception
          {
           double d;
           long l;
           String s1,s2,s3;
           // ===== CHECKING OF FUNCTION STRING =================
           s1 = functionField.getText();
           if (s1==null || s1.length()==0) throw new Exception("No function specified.");
           Function f = new Function(s1);
           if (!f.isAnyVariable() || f.getVariables().length>1) throw new Exception("No variable specified.\nOR variables are more than one.");

           // ===== CHECKING OTHER SETTING VALUES ===============
           d=Double.parseDouble(startX.getText());
           d=Double.parseDouble(endX.getText());
          }


        private void execute()
          {
           try { validateInputs(); }
           catch(Exception e) 
             { 
              JOptionPane.showMessageDialog(frame,e.getMessage()+"...\n Check inputs ...","Error",JOptionPane.ERROR_MESSAGE); 
              return;
             }
                
           String funcStr = functionField.getText();
           double x = Double.parseDouble(startX.getText());
           double y = Double.parseDouble(endX.getText());
           try
              {
               Function f = new Function(funcStr);
               drawGraph(f, x, y);
              }
           catch(Exception e) 
             { 
              JOptionPane.showMessageDialog(frame,e.getMessage(),"Unknown Error",JOptionPane.ERROR_MESSAGE); 
              return;
             }               
          }


        synchronized public void drawGraph(String funcStr, double x1, double x2) throws Exception
          {
           Function f = new Function(funcStr);
           drawGraph(f, x1, x2);
          }
          
        synchronized public void drawGraph(Function func, double x1, double x2) throws Exception   
          {
           String errStr = "Calculation DEADLOCK .... Either\n -Infinity approached\n -Complex number evaluated\nTry another interval...";
              
           char[] var = func.getVariables();
           double[] val = new double[1];
           double ans;
           double x,y;
           int ix,iy, fx,fy;
           
           gridImageG.setColor(graphColor);
           x=x1;
           val[0] = x;
           y=func.evaluator(var, val);
           if (Double.isInfinite(y) || Double.isNaN(y)) 
              {
               JOptionPane.showMessageDialog(frame,errStr,"Calculation Error",JOptionPane.ERROR_MESSAGE); 
               return;
              } 
           ix = getX(x); 
           iy = getY(y);            
           for (x=x1; x<=x2; x+=scaleX)
               {
                val[0] = x;
                y = func.evaluator(var, val);
                if (Double.isInfinite(y) || Double.isNaN(y))
                   {
                    JOptionPane.showMessageDialog(frame,errStr,"Calculation Error",JOptionPane.ERROR_MESSAGE); 
                    return;
                   } 
                fx = getX(x); 
                fy = getY(y); 
                gridImageG.drawLine(ix,iy, fx,fy);
                ix=fx; iy=fy;
               } 
               
           graphPanel.repaint();    
          }

        private int getX(double n)
          {
           int i;
           i = (int)(_centerX+(n*incXY/scaleX) );
           return i;           
          }

        private int getY(double n)
          {
           int i;
           i = (int)(_centerY-(n*incXY/scaleY) ); 
           return i;           
          }
        
   /*****************************************************************************
   *            Graph Rendering Methods .......                                 *
   *****************************************************************************/

        public void refreshGraph()
          {                
           graphPanel.setSize(graphW, graphH);                    
           graphPanel.refreshGraph();
          }

        public void clearGraph()
          {
           //===== ADAPTING NEW SETTINGS ==========================
           _centerX = centerX;
           _centerY = centerY;           
           try
              {
               double q,w;
               q = Double.parseDouble(scaleXField.getText());
               w = Double.parseDouble(scaleYField.getText());
               scaleX=q; scaleY=w;
              }
           catch(Exception e) 
              {
               scaleXField.setText(String.valueOf(scaleX));
               scaleYField.setText(String.valueOf(scaleY));
              }    
           
           graphPanel.setSize(graphW, graphH);          
           gridImage = new BufferedImage(graphW, graphH, BufferedImage.TYPE_INT_RGB);
           Graphics g = gridImage.getGraphics();
           gridImageG = g;

           g.setColor(Color.white);
           g.fillRect(0,0, graphW, graphH);
           if (gridToggle.isSelected())
              {
               g.setColor(Color.gray);
               for (int i=0; i<graphW; i+=incXY) g.drawLine(i,0, i,graphH);
               for (int i=0; i<graphW; i+=incXY) g.drawLine(0,i, graphW,i);                   
              } 
           g.setColor(Color.red);
           g.drawLine( _centerX,0, centerX,graphH );
           g.drawLine( 0,_centerY, graphW,centerY );
           g.drawLine( _centerX+1,0, centerX+1,graphH );
           g.drawLine( 0,_centerY+1, graphW,centerY+1 );
           
           refreshGraph();
          }


}


class FunctionGrapherPanel extends JPanel implements MouseListener, MouseMotionListener
{
        private FunctionGrapher fg;
        
        private int x1,y1, dx, dy;
        private boolean isFirst=true;
        private boolean isDragged=false;
        
        private JLabel markerSheet;
        private Graphics graph;
        
        FunctionGrapherPanel(FunctionGrapher fg)
          {
           this.fg=fg;
           addMouseListener(this);
           addMouseMotionListener(this);           
           setLayout(new BorderLayout());
           
           markerSheet = null;
          }
          
        private void initMarkerSheet()
          {
           markerSheet = new JLabel();
           markerSheet.setSize(getWidth(), getHeight());                    
           add(markerSheet, BorderLayout.CENTER);
           markerSheet.setOpaque(false);
           graph = markerSheet.getGraphics();
           graph.setColor(Color.black); 
          }  
          
          
        public void refreshGraph()
          {
           repaint();
           if (markerSheet!=null)
              {
               remove(markerSheet);
               initMarkerSheet();
              }            
          }  
          


        public void paint(Graphics g)
          {
           try{ g.drawImage(fg.gridImage, 0,0, null); }
           catch(Exception e) {}
           
          }       


        
        public void mouseClicked(MouseEvent e)
          {
           double x = e.getX(), y = e.getY();
           String s1,s2;
           
           x = x-fg._centerX;    
           y = fg._centerY-y;           
           x = x*(fg.scaleX/fg.incXY);  
           y = y*(fg.scaleY/fg.incXY);
           s1 = " "+String.valueOf(x);
           s2 = " "+String.valueOf(y);
           fg.currX.setText(s1);
           fg.currY.setText(s2);          
           
           if (fg.markToggle.isSelected() && markerSheet==null) initMarkerSheet();
           if (fg.markToggle.isSelected() && (e.getModifiers()==InputEvent.BUTTON1_MASK))
              {
               int x2=e.getX(), y2 = e.getY();
               if (isFirst) 
                  { x1 = x2; y1 = y2; }
               else
                  {graph.drawLine(x1,y1, x2,y2); }
               isFirst=!isFirst;   
              } 
           else if (fg.markToggle.isSelected() && (e.getModifiers()==InputEvent.BUTTON3_MASK))
              {
               int x2=e.getX(), y2 = e.getY();               
               if (isFirst) 
                  { x1 = x2; y1 = y2; }
               else
                  {
                   int tx=x1, ty=y1;
                   if (x2<x1)  tx=x2; if (y2<y1)  ty=y2;
                   x2=Math.abs(x1-x2); y2=Math.abs(y1-y2); graph.drawOval(tx,ty, x2,y2); 
                  }
               isFirst=!isFirst;   
              }   
          }
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mousePressed(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {isDragged=false;}          
        
        public void mouseDragged(MouseEvent e)
          {
           double x = e.getX(), y = e.getY();
           String s1,s2;
           
           x = x-fg._centerX;    
           y = fg._centerY-y;           
           x = x*(fg.scaleX/fg.incXY);  
           y = y*(fg.scaleY/fg.incXY);
           s1 = " "+String.valueOf(x);
           s2 = " "+String.valueOf(y);
           fg.currX.setText(s1);
           fg.currY.setText(s2);           
           
           if (fg.markToggle.isSelected() && markerSheet==null) initMarkerSheet();
           if (fg.markToggle.isSelected() && (e.getModifiers()==InputEvent.BUTTON3_MASK))
              {
               int x2=e.getX(), y2 = e.getY();               
               if (!isDragged)
                  {
                   dx = x2; dy = y2;
                   isDragged=true;
                  }                  
               graph.drawLine(dx,dy, x2, y2); 
               dx = x2; dy = y2;
              } 
          }
        public void mouseMoved(MouseEvent e) {}
}

