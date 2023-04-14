package org.ksiddiqui.bscs.mathematica.app.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;

public class FloatingPanel extends JPanel implements Runnable {

    // ========== Parent Panel Properties ======================
    private JPanel panel;
    private int width, height;

    // ========== Text Array Properties ======================
    private String st_subText[];
    private Color st_fgColor;
    private Font st_font;

    // ========== Flaoting Panel Properties ======================
    private int stX, stY;
    private String direction;
    private int incX, incY;
    private int w, h;

    // ========== Animation Properties =====================
    private Thread thread;
    private int noOfTimes;
    private int timeDelay;
    private boolean isInProcess;
    private boolean enableAnimation;

    /******************************************************************************
     *     CONSTRUCTORS & PROPERTIES SETTING METHODS                               *
     ******************************************************************************/


    FloatingPanel(JPanel p, Color c, Border b) {
        super();

        if (p == null) panel = new JPanel();
        else panel = p;
//           panel.setLayout(new FlowLayout());
        panel.add(this);

        st_subText = null;
        st_fgColor = Color.red;
        st_font = null;

        stX = stY = 0;
        incX = 0;
        incY = 1;

        setBorder(new BevelBorder(BevelBorder.RAISED));

        if (c != null) setBackground(c);
        if (b != null) setBorder(b);

        isInProcess = false;
        enableAnimation = true;
        timeDelay = 1000;
        noOfTimes = 5;
    }


    public void setSubText(String[] text, Color color, Font font) {
        while (isInProcess) ;
        isInProcess = true;

        if (text != null) st_subText = text;
        if (color != null) st_fgColor = color;
        if (font != null) st_font = font;

        removeAll();
        setLayout(new GridLayout(st_subText.length, 1));
        if (st_font != null) setFont(st_font);
        if (st_fgColor != null) setForeground(st_fgColor);

        for (int i = 0; i < st_subText.length; i++) add(new JLabel(st_subText[i]));

        isInProcess = false;
    }

    public void setFloatingPanel(int x, int y, int incx, int incy) {
        while (isInProcess) ;
        isInProcess = true;

        stX = x;
        stY = y;
        if (incx != 0) incX = incx;
        if (incy != 0) incY = incy;
        direction = null;

        isInProcess = false;
    }

    public void setFloatingPanel(Color c1, Border border) {
        while (isInProcess) ;
        isInProcess = true;

        if (c1 != null) setBackground(c1);
        if (border != null) setBorder(border);

        isInProcess = false;
    }

    public void setFloatingPanelDirection(String str) {
        while (isInProcess) ;
        isInProcess = true;

        String s = str.trim();
        direction = s.toLowerCase();
        if (direction != null) {
            if (direction.compareTo("top") == 0) {
                incY = 1;
                incX = 0;
            } else if (direction.compareTo("right") == 0) {
                incX = 1;
                incY = 0;
            } else if (direction.compareTo("bottom") == 0) {
                incY = -1;
                incX = 0;
            } else if (direction.compareTo("left") == 0) {
                incX = -1;
                incY = 0;
            }
        }

        isInProcess = false;
    }

    public void setFloatingPanelIncreament(int x, int y) {
        while (isInProcess) ;
        isInProcess = true;

        if (x != 0) incX = x;
        if (y != 0) incY = y;

        isInProcess = false;
    }


    public boolean setPanel(JPanel p) {
        if (thread != null) return false;

        if (p == null) panel = new JPanel();
        else panel = p;
        panel.setLayout(new FlowLayout());
        panel.add(this);

        return true;
    }

    /******************************************************************************
     *     PROPERTIES QUERYING METHODS                                             *
     ******************************************************************************/


    // ================ Sub Text Properties Querying Mathods ===================
    public String[] getST() {
        return st_subText;
    }

    public Color getSTColor() {
        return st_fgColor;
    }

    public Font getSTFont() {
        return st_font;
    }

    public int getIncX() {
        return incX;
    }

    public int getIncY() {
        return incY;
    }


    // ===================== Animation Properties Querying Mathods ==============

    public int getX() {
        return stX;
    }

    public int getY() {
        return stY;
    }

    public boolean isAnimationEnabled() {
        return enableAnimation;
    }

    public boolean isBuzy() {
        return isInProcess;
    }

    public JPanel getPanel() {
        return panel;
    }

    public int getNoOfTimes() {
        return noOfTimes;
    }

    public void setNoOfTimes(int i) {
        noOfTimes = i;
    }

    public int getTimeDelay() {
        return timeDelay;
    }

    public void setTimeDelay(int t) {
        timeDelay = t;
    }

    /*******************************************************************************************
     *            THREAD OVERRIDE && PROCESS METHODS                                            *
     *******************************************************************************************/

    public void start(int n) {
        noOfTimes = n;
        thread = new Thread(this);
        thread.start();
    }


    public void run() {
        while (enableAnimation && noOfTimes != 0) {
            while (isInProcess) ;
            isInProcess = true;

            doCycle();
            noOfTimes--;

            isInProcess = false;
        }
        panel.remove(this);
        thread = null;
    }

    public void stop() {
        enableAnimation = false;
    }


    /******************************************************************************
     *     PAINTING AND GRAPHICS METHODS                                           *
     ******************************************************************************/


    private void doCycle() {
        int x = stX, y = stY;
        width = panel.getWidth();
        height = panel.getHeight();
        w = getWidth();
        h = getHeight();

        if (direction != null) {
            if (direction.compareTo("top") == 0) {
                stY = -y;
            } else if (direction.compareTo("right") == 0) {
                stX = -w;
            } else if (direction.compareTo("bottom") == 0) {
                stY = height;
            } else if (direction.compareTo("left") == 0) {
                stX = width;
            }
        }

        while (enableAnimation) {
            setLocation(x, y);
            if (x < -w || y < -h || x > width || y > height) break;
            x += incX;
            y += incY;
//              repaint();
            paintImmediately(0, 0, w, h);

            try {
                thread.sleep(timeDelay);
            } catch (InterruptedException e) {
            }
        }
    }

}
