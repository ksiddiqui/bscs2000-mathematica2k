package org.ksiddiqui.bscs.mathematica.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class STPanel extends JPanel implements MouseListener, MouseMotionListener 
{

   // ========== Main Text Properties =====================
        private String  mt_mainText;
        private Color   mt_fgColor;
        private int     mt_x, mt_y;
        private Font    mt_font;
        private int     mt_shadowDist;
        
   // ========== Sub Text Properties ======================
        private String  st_subText[];
        private Color   st_fgColor;
        private int     st_x, st_y;
        private Font    st_font;
        private int     st_shadowDist;
        
   // ========== Animation Properties =====================
        private int     shadowX, shadowY;
        private Color   shadowColor;
        private int     colorDepth;
        private int     state;
        private boolean isInProcess;
        private boolean enableAnimation;

  /******************************************************************************
  *     CONSTRUCTORS & PROPERTIES SETTING METHODS                               *
  ******************************************************************************/      


        STPanel(Color c, Border b, LayoutManager lm)
          {
           if (c!=null) setBackground(c);
           if (b!=null) setBorder(b);
           if (lm!=null) setLayout(lm);
           
           mt_mainText="";
           mt_fgColor=Color.pink;
           mt_x = mt_y = 0;
           mt_font = new Font("Serif", Font.BOLD, 20);
           mt_shadowDist = 5;
           
           st_subText=null;
           st_fgColor=Color.red;
           st_x = st_y = 0;
           st_font = new Font("Serif", Font.PLAIN, 10);
           st_shadowDist = 0;
           
           shadowX = shadowY = 1;
           shadowColor = getBackground();
           colorDepth = 5;
           isInProcess = false;
           enableAnimation = true;
           state = 0;
           
           addMouseListener(this);
           addMouseMotionListener(this);
          }
        

        public void setMainText(String text, Color color, int x, int y, Font font, int d)
          {
           while(isInProcess);
           isInProcess=true;
          
           if (text!=null)  mt_mainText = text; 
           if (color!=null) mt_fgColor = color;
           if (x!=-1)       mt_x = x;    
           if (y!=-1)       mt_y = y;               
           if (font!=null)  mt_font = font;
           if (d!=-1)       mt_shadowDist = d;
           
           isInProcess=false;
          }

        public void setSubText(String[] text, Color color, int x, int y, Font font, int d)
          {
           while(isInProcess);
           isInProcess=true;
          
           if (text!=null)  st_subText = text;
           if (color!=null) st_fgColor = color;
           if (x!=-1)       st_x = x;    
           if (y!=-1)       st_y = y;               
           if (font!=null)  st_font = font;
           if (d!=-1)       st_shadowDist = d;
           
           isInProcess=false;           
          }

        public void setShadow(int x, int y, Color c)
          {
           while(isInProcess);
           isInProcess=true;
          
          if (x!=0)    shadowX = x;
          if (y!=0)    shadowY = y;
          if (c!=null) shadowColor =c;
           
           isInProcess=false;          
          }
        
        public void setColorDepth(int d)
          {
           while(isInProcess);
           isInProcess=true;
          
           if (d!=-1) colorDepth = d;
           
           isInProcess=false;
          }  

        public void enableAnimation(boolean e)
          {
           while(isInProcess);
           isInProcess=true;
          
           enableAnimation=e;
           
           isInProcess=false;
          }


  /******************************************************************************
  *     PROPERTIES QUERYING METHODS                                             *
  ******************************************************************************/      


    // ================ Main Text Properties Querying Mathods ===================

        public String getMT()
          {
           return mt_mainText;     
          }
          
        public Color getMTColor()
          {
           return mt_fgColor;
          }
          
        public int getMTX()
          {
           return mt_x;
          }
          
        public int getMTY()
          {
           return mt_y;          
          }
          
        public Font getMTFont()
          {
           return mt_font;          
          }
          
        public int getMTShadowDist()
          {
           return mt_shadowDist;          
          }

    // ================ Sub Text Properties Querying Mathods ===================

        public String[] getST()
          {
           return st_subText;     
          }
          
        public Color getSTColor()
          {
           return st_fgColor;
          }
          
        public int getSTX()
          {
           return st_x;
          }
          
        public int getSTY()
          {
           return st_y;          
          }
          
        public Font getSTFont()
          {
           return st_font;          
          }
          
        public int getSTShadowDist()
          {
           return st_shadowDist;          
          }        
          
   // ===================== Animation Properties Querying Mathods ==============
   
        public boolean isAnimationEnabled()
          {
           return enableAnimation;
          }
          
        public int getColorDepth()
          {
           return colorDepth;
          }
        
        public Color getShadowColor()
          {
           return shadowColor;
          }
             
  /******************************************************************************
  *     PAINTING AND GRAPHICS METHODS                                           *
  ******************************************************************************/      
  
        public void paint(Graphics g)
          {
           if (!enableAnimation) return;
           
           while(isInProcess);
           isInProcess=true;
          
           Color bgColor = getBackground();
          
           if (state==0)                // Panel drawing state
              {
               doDrawing(g);
              }
           else if (state==9)           // Panel animation initiator, active on mouse enter
              {               
               for (int i=0; i<colorDepth; i++)
                   {
                    bgColor = bgColor.brighter();
                    this.setBackground(bgColor);
                    mt_fgColor =  mt_fgColor.brighter();
                    shadowColor = shadowColor.darker();
                    doDrawing(g);
                    for (int j=0; j<100000; j++){}
                   }
               state=0;
              }
           else if (state==10)          // Panel animation stopper, active on mouse exit
              {
               for (int i=0; i<colorDepth; i++)
                   {
                    bgColor = bgColor.darker();
                    this.setBackground(bgColor);
                    mt_fgColor =  mt_fgColor.darker();
                    shadowColor = shadowColor.brighter();
                    doDrawing(g);
                    for (int j=0; j<100000; j++){}
                   }
               state=0;
              }
              
           isInProcess=false;              
          }
          
          
        private void doDrawing(Graphics g)
          {
           if (mt_mainText==null) return;
           
           g.setFont(mt_font);
           if (mt_shadowDist>0)
              {
               g.setColor(shadowColor);
               int x = mt_x+(mt_shadowDist*shadowX);
               int y = mt_y+(mt_shadowDist*shadowY);
               g.drawString(mt_mainText, x, y);     
              }
           g.setColor(mt_fgColor);
           g.drawString(mt_mainText, mt_x, mt_y);     
          }
  
  
  /******************************************************************************
  *     MOUSE EVENTS METHODS                                                    *
  ******************************************************************************/      

        // ============= MOUSE LISTENER ===============

        public void mousePressed(MouseEvent e) { }
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) { state=9; repaint(); }
        public void mouseExited(MouseEvent e) { state=10; repaint(); }
        public void mouseClicked(MouseEvent e) { }
        
        // =========== MOUSE MOTION LISTENER ==========
        
        public void mouseDragged(MouseEvent e) { }
        public void mouseMoved(MouseEvent e) { }

}
