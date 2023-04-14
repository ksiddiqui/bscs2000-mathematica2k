package org.ksiddiqui.bscs.mathematica.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.Vector;


public class TextPanel extends JPanel {

    // ======= PROPERTIES OF TEXT(s) ========
    private Vector strList;
    private int strListSize;


    // ======= PROPERTIES OF SHADOW =========
    private Color shadowLColor;
    private Color shadowDColor;
    private int shadowX, shadowY;

    // ======= PROPERTIES TO COMMON =========


    /*******************************************************************************************
     *            CONSTRUCTOR(s)                                                                *
     *******************************************************************************************/

    TextPanel() {
        super();
        initialize();
    }


    TextPanel(LayoutManager lm) {
        super(lm);
        initialize();
    }

    TextPanel(Color background, Border border) {
        if (background != null) setBackground(background);
        if (border != null) setBorder(border);
        initialize();
    }

    TextPanel(LayoutManager lm, Color background, Border border) {
        if (background != null) setBackground(background);
        if (border != null) setBorder(border);
        if (lm != null) setLayout(lm);
        initialize();
    }


    private void initialize() {
        strList = new Vector();
        strListSize = 0;

        shadowLColor = Color.gray;
        shadowDColor = Color.black;
        shadowX = 1;
        shadowY = 1;
    }

    /*******************************************************************************************
     *            PROPERTIES QUERY METHODS                                                      *
     *******************************************************************************************/

    public Vector getTextProperties() {
        return strList;
    }

    public int getCount() {
        return strListSize;
    }


    /*******************************************************************************************
     *            PRIVATE METHODS                                                               *
     *******************************************************************************************/

    private void addTextToList(String str, int x, int y, Color c, Font f, int dist) {
        Object[] obj = new Object[6];
        obj[0] = str;
        obj[1] = new Integer(x);
        obj[2] = new Integer(y);
        obj[3] = c;
        obj[4] = f;
        obj[5] = new Integer(dist);

        if (strListSize == 0) strList.addElement(obj);
        else {
            Object[] object;
            int i;
            for (i = 0; i < strListSize; i++) {
                object = (Object[]) strList.elementAt(i);
                if (((Integer) object[5]).intValue() > dist) break;
            }
            strList.insertElementAt(obj, i);
        }

        strListSize++;
    }


    private int delTextFromList(String str) {
        Object[] object;
        int delItems = 0;

        for (int i = 0; i < strListSize; i++) {
            object = (Object[]) strList.elementAt(i);
            if (((String) object[0]).compareTo(str) == 0) {
                strList.remove(i);
                strListSize--;
                delItems++;
            }
        }

        return delItems;
    }


    /*******************************************************************************************
     *            PRIMARY METHODS                                                               *
     *******************************************************************************************/


    public void addText(String str, int x, int y, Color c, Font f, int dist) {
        addTextToList(str, x, y, c, f, dist);
    }


    public void addText(String[] str, int[] x, int[] y, Color c, Font f, int dist) throws Exception {
        if (str.length != x.length || str.length != y.length || x.length != y.length)
            throw new Exception("Worng no. of strings, and their respective x & y-axises...");

        for (int i = 0; i < str.length; i++)
            addTextToList(str[i], x[i], y[i], c, f, dist);
    }


    public void delText(String str) throws Exception {
        if (strListSize == 0) throw new Exception("Cannot delete in empty list...");

        if (str == null) {
            strList.removeAllElements();
            strListSize = 0;
        } else {
            delTextFromList(str);
        }

        return;
    }


    public void delText(String[] str) throws Exception {
        if (strListSize == 0) throw new Exception("Cannot delete in empty list...");

        if (str == null) {
            strList.removeAllElements();
            strListSize = 0;
        } else {
            for (int i = 0; i < str.length; i++) delTextFromList(str[i]);
        }

        return;
    }


    public void chgProperties(String str, int x, int y, Color c, Font f, int dist) {
    }

    public void chgProperties(String[] str, int[] x, int[] y, Color c, Font f, int dist) {
    }


    public void chgShadow(Color c1, Color c2, int x, int y) {
        if (c1 != null) shadowLColor = c1;
        if (c2 != null) shadowDColor = c2;
        shadowX = x;
        shadowY = y;
    }

    /*******************************************************************************************
     *            GRAPHICS & PAINTING METHODS                                                   *
     *******************************************************************************************/

    public void paint(Graphics g) {
        Object[] obj;
        int x, y, d;
        Color defaultColor = g.getColor();
        Font defaultFont = g.getFont();

        for (int i = 0; i < strListSize; i++) {
            obj = (Object[]) strList.elementAt(i);

            x = ((Integer) obj[1]).intValue();
            y = ((Integer) obj[2]).intValue();
            d = ((Integer) obj[5]).intValue();

            if (obj[4] != null) g.setFont((Font) obj[4]);
            else g.setFont(defaultFont);

            g.setColor(shadowLColor);
//                g.setXORMode(shadowDColor);                
            g.drawString((String) obj[0], x + (d * shadowX), y + (d * shadowY));

//                g.setPaintMode();
            if (obj[3] != null) g.setColor((Color) obj[3]);
            else g.setColor(defaultColor);
            g.drawString((String) obj[0], x, y);
        }
    }


}
