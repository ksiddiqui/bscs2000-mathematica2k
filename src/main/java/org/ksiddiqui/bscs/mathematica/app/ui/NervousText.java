package org.ksiddiqui.bscs.mathematica.app.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class NervousText extends JLabel implements Runnable, MouseListener {
    static final int REGULAR_WD = 15;
    static final int REGULAR_HT = 36;
    static final int SMALL_WD = 12;
    static final int SMALL_HT = 24;
    String banner;              // The text to be displayed
    char bannerChars[];         // The same text as an array of characters
    char attributes[];          // Character attributes ('^' for superscript)
    Thread runner = null;       // The thread that is displaying the text
    boolean threadSuspended;    // True when thread suspended (via mouse click)
    Font regularFont = new Font("TimesRoman", Font.BOLD, REGULAR_HT);
    Font smallFont = new Font("TimesRoman", Font.BOLD, SMALL_HT);

    private Color backgroundColor = null;
    private Color foregroundColor = null;
    private int width, height;

    NervousText() {
        this("KASSOFT", null, null);
    }


    NervousText(String text, Color fore, Color back) {
        banner = text;
        foregroundColor = fore;
        backgroundColor = back;

        int bannerLength = banner.length();
        StringBuffer bc = new StringBuffer(bannerLength);
        StringBuffer attrs = new StringBuffer(bannerLength);
        int wd = 0;
        for (int i = 0; i < bannerLength; i++) {
            char c = banner.charAt(i);
            char a = 0;
            if (c == '^') {
                i++;
                if (i < bannerLength) {
                    c = banner.charAt(i);
                    a = '^';
                    wd += SMALL_WD - REGULAR_WD;
                } else {
                    break;
                }
            }
            bc.append(c);
            attrs.append(a);
            wd += REGULAR_WD;
        }

        bannerLength = bc.length();
        bannerChars = new char[bannerLength];
        attributes = new char[bannerLength];
        bc.getChars(0, bannerLength, bannerChars, 0);
        attrs.getChars(0, bannerLength, attributes, 0);

        threadSuspended = false;
        setMinimumSize(new Dimension(wd + 10, 50));
        setPreferredSize(new Dimension(wd + 10, 50));
        width = wd + 10;
        height = 50;
        addMouseListener(this);
    }

    public JPanel getJPanel(Border border) {
        JPanel panel = new JPanel();
        panel.add(this);
        panel.setBackground(backgroundColor);
        if (panel != null) panel.setBorder(border);
        return panel;
    }


    public void start() {
        runner = new Thread(this);
        runner.start();
    }

    public synchronized void stop() {
        runner = null;
        if (threadSuspended) {
            threadSuspended = false;
            notify();
        }
    }

    public void run() {
        while (runner != null) {
            try {
                Thread.sleep(100);
                synchronized (this) {
                    while (threadSuspended) {
                        wait();
                    }
                }
            } catch (InterruptedException e) {
            }
            repaint();
        }
    }


    public void paint(Graphics g) {
        int length = bannerChars.length;

        if (backgroundColor != null) g.setColor(backgroundColor);
        g.fillRect(0, 0, width, height);
        if (foregroundColor != null) g.setColor(foregroundColor);

        for (int i = 0, x = 0; i < length; i++) {
            int wd, ht;
            if (attributes[i] == '^') {
                wd = SMALL_WD;
                ht = SMALL_HT;
                g.setFont(smallFont);
            } else {
                wd = REGULAR_WD;
                ht = REGULAR_HT;
                g.setFont(regularFont);
            }
            int px = (int) (10 * Math.random() + x);
            int py = (int) (10 * Math.random() + ht);
            g.drawChars(bannerChars, i, 1, px, py);
            x += wd;
        }
    }


    public synchronized void mousePressed(MouseEvent e) {
        e.consume();
        threadSuspended = !threadSuspended;
        if (!threadSuspended)
            notify();
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }


}
