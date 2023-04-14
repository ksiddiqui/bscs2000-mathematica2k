package org.ksiddiqui.bscs.mathematica.app.ui;

import javax.swing.*;
import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;


public class StylisPanel extends JPanel implements Runnable {

    // ======= PROPERTIES OF MAIN TEXT ========
    private String mt_mainText;
    private Color mt_fgColor;
    private int mt_x, mt_y;
    private Font mt_font;
    private int mt_shadowDist;


    // ======= PROPERTIES OF TEXT LIST ========
    private Vector tl_textList;
    private String tl_textListX[];
    private int tl_count;
    private Color tl_fgColor;
    private int tl_x;
    private Font tl_font;
    private int tl_shadowDist;
    private int tl_lineDist;


    // ======= PROPERTIES OF ANIMATION ========
    private int width, height;
    private int timeDelay;
    private int shadowX, shadowY;
    private Color shadowColor;
    private int refY;
    private boolean isInProcess;
    private Thread thread;
    private boolean isAnimate;
    private boolean isStopInEnd;

    /*******************************************************************************************
     *            CONSTRUCTOR & PROPERTIES SETTING MATHODS                                      *
     *******************************************************************************************/

    public StylisPanel() throws Exception {
        mt_mainText = "";
        mt_fgColor = Color.white;
        mt_x = mt_y = 0;
        mt_font = new Font("Serif", Font.BOLD, 30);
        mt_shadowDist = 3;

        tl_textList = new Vector();
        tl_count = 0;
        tl_fgColor = Color.blue;
        tl_x = 0;
        tl_font = new Font("Serif", Font.PLAIN, 20);
        mt_shadowDist = 5;
        tl_lineDist = 15;

        timeDelay = 250;     // 1/4 seconds delay
        shadowX = shadowY = 1;
        shadowColor = Color.darkGray;
        isInProcess = false;
        isAnimate = true;
        isStopInEnd = false;
    }


    public void setMainText(String mainText, Color fore, int x, int y, Font font) throws Exception {
        while (isInProcess) ;
        isInProcess = true;

        if (mainText != null) mt_mainText = mainText;
        if (fore != null) mt_fgColor = fore;
        if (x != -1) mt_x = x;
        if (y != -1) mt_y = y;
        if (font != null) mt_font = font;

        isInProcess = false;
    }

    public void setTextList(String[] textList, Color fore, int x, int dist, Font font) throws Exception {
        while (isInProcess) ;
        isInProcess = true;

        if (textList != null && textList.length != 0) {
            tl_count = textList.length;
            tl_textList.clear();
            for (int i = 0; i < tl_count; i++) {
                tl_textList.addElement(textList[i]);
            }
        }
        if (fore != null) tl_fgColor = fore;
        if (x != -1) tl_x = x;
        if (dist != -1) tl_lineDist = dist;
        if (font != null) tl_font = font;

        isInProcess = false;
    }

    public void addTextInList(String[] newList) throws Exception {
        while (isInProcess) ;
        isInProcess = true;

        if (newList == null || newList.length == 0) return;

        for (int i = 0; i < newList.length; i++) {
            tl_textList.addElement(newList[i]);
        }

        tl_count += newList.length;

        isInProcess = false;
    }

    public void addTextInList(Vector newList) throws Exception {
        while (isInProcess) ;
        isInProcess = true;

        if (newList == null) return;
        Enumeration enum1 = newList.elements();

        while (enum1.hasMoreElements()) {
            tl_textList.addElement((String) enum1.nextElement());
            tl_count++;
        }

        isInProcess = false;
    }


    public void setShadow(int d1, int d2, int x, int y, Color c) throws Exception {
        while (isInProcess) ;
        isInProcess = true;

        if (d1 != -1) mt_shadowDist = d1;
        if (d2 != -1) tl_shadowDist = d2;
        if (x != 0) shadowX = x;
        if (y != 0) shadowY = y;
        if (c != null) shadowColor = c;

        isInProcess = false;
    }

    public void setAnimation(int delay, boolean b) throws Exception {
        if (delay != -1) timeDelay = delay;
        isStopInEnd = b;
    }


    /*******************************************************************************************
     *            THREAD OVERRIDE && PROCESS METHODS                                            *
     *******************************************************************************************/

    public void start() {
        thread = new Thread(this);
        thread.start();
    }


    public void run() {
        width = getWidth();
        height = getHeight();

        initListAnimation();
        while (isAnimate) {
            while (isInProcess) ;
            isInProcess = true;

            repaint();

            isInProcess = false;
            try {
                thread.sleep(timeDelay);
            } catch (InterruptedException e) {
            }
        }
    }

    public void stop() {
        isAnimate = false;
    }


    /*******************************************************************************************
     *            GRAPHICS & PAINTING METHODS                                                   *
     *******************************************************************************************/

    public void initListAnimation() {
        refY = height;
        tl_textListX = new String[tl_count];
        for (int i = 0; i < tl_count; i++) {
            try {
                tl_textListX[i] = (String) tl_textList.get(i);
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);

        boolean isLineDraw = false;
        int drawingY, any1, any2;

        if (mt_mainText != null && mt_shadowDist < tl_shadowDist) drawMainText(graphics);

        isLineDraw = false;
        graphics.setFont(tl_font);
        graphics.setColor(tl_fgColor);
        for (int i = 0; i < tl_count; i++) {
            drawingY = refY + (i * tl_lineDist);
            if (drawingY > ((-1) * tl_lineDist) && drawingY < (height + tl_lineDist)) {
                isLineDraw = true;
                if (tl_shadowDist > 0) {
                    any1 = tl_x + (tl_shadowDist * shadowX);
                    any2 = drawingY + (tl_shadowDist * shadowY);
                    graphics.setColor(shadowColor);
                    graphics.drawString(tl_textListX[i], any1, any2);
                    graphics.setColor(tl_fgColor);
                }
                graphics.drawString(tl_textListX[i], tl_x, drawingY);
            }
        }
        if (!isLineDraw) {
            if (isStopInEnd) {
                isAnimate = false;
            }
            initListAnimation();
        } else refY -= 1;

        if (mt_mainText != null && mt_shadowDist > tl_shadowDist) drawMainText(graphics);

    }


    private void drawMainText(Graphics graphics) {
        int any1, any2;

        graphics.setFont(mt_font);
        if (mt_shadowDist > 0) {
            any1 = mt_x + (mt_shadowDist * shadowX);
            any2 = mt_y + (mt_shadowDist * shadowY);
            graphics.setColor(shadowColor);
            graphics.drawString(mt_mainText, any1, any2);
        }
        graphics.setColor(mt_fgColor);
        graphics.drawString(mt_mainText, mt_x, mt_y);
    }

}
