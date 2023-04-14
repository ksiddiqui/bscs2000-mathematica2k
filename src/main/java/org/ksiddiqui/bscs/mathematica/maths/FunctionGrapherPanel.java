package org.ksiddiqui.bscs.mathematica.maths;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class FunctionGrapherPanel extends JPanel implements MouseListener, MouseMotionListener {
    private FunctionGrapher fg;

    private int x1, y1, dx, dy;
    private boolean isFirst = true;
    private boolean isDragged = false;

    private JLabel markerSheet;
    private Graphics graph;

    FunctionGrapherPanel(FunctionGrapher fg) {
        this.fg = fg;
        addMouseListener(this);
        addMouseMotionListener(this);
        setLayout(new BorderLayout());

        markerSheet = null;
    }

    private void initMarkerSheet() {
        markerSheet = new JLabel();
        markerSheet.setSize(getWidth(), getHeight());
        add(markerSheet, BorderLayout.CENTER);
        markerSheet.setOpaque(false);
        graph = markerSheet.getGraphics();
        graph.setColor(Color.black);
    }


    public void refreshGraph() {
        repaint();
        if (markerSheet != null) {
            remove(markerSheet);
            initMarkerSheet();
        }
    }


    public void paint(Graphics g) {
        try {
            g.drawImage(fg.gridImage, 0, 0, null);
        } catch (Exception e) {
        }

    }


    public void mouseClicked(MouseEvent e) {
        double x = e.getX(), y = e.getY();
        String s1, s2;

        x = x - fg._centerX;
        y = fg._centerY - y;
        x = x * (fg.scaleX / fg.incXY);
        y = y * (fg.scaleY / fg.incXY);
        s1 = " " + String.valueOf(x);
        s2 = " " + String.valueOf(y);
        fg.currX.setText(s1);
        fg.currY.setText(s2);

        if (fg.markToggle.isSelected() && markerSheet == null) initMarkerSheet();
        if (fg.markToggle.isSelected() && (e.getModifiers() == InputEvent.BUTTON1_MASK)) {
            int x2 = e.getX(), y2 = e.getY();
            if (isFirst) {
                x1 = x2;
                y1 = y2;
            } else {
                graph.drawLine(x1, y1, x2, y2);
            }
            isFirst = !isFirst;
        } else if (fg.markToggle.isSelected() && (e.getModifiers() == InputEvent.BUTTON3_MASK)) {
            int x2 = e.getX(), y2 = e.getY();
            if (isFirst) {
                x1 = x2;
                y1 = y2;
            } else {
                int tx = x1, ty = y1;
                if (x2 < x1) tx = x2;
                if (y2 < y1) ty = y2;
                x2 = Math.abs(x1 - x2);
                y2 = Math.abs(y1 - y2);
                graph.drawOval(tx, ty, x2, y2);
            }
            isFirst = !isFirst;
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        isDragged = false;
    }

    public void mouseDragged(MouseEvent e) {
        double x = e.getX(), y = e.getY();
        String s1, s2;

        x = x - fg._centerX;
        y = fg._centerY - y;
        x = x * (fg.scaleX / fg.incXY);
        y = y * (fg.scaleY / fg.incXY);
        s1 = " " + String.valueOf(x);
        s2 = " " + String.valueOf(y);
        fg.currX.setText(s1);
        fg.currY.setText(s2);

        if (fg.markToggle.isSelected() && markerSheet == null) initMarkerSheet();
        if (fg.markToggle.isSelected() && (e.getModifiers() == InputEvent.BUTTON3_MASK)) {
            int x2 = e.getX(), y2 = e.getY();
            if (!isDragged) {
                dx = x2;
                dy = y2;
                isDragged = true;
            }
            graph.drawLine(dx, dy, x2, y2);
            dx = x2;
            dy = y2;
        }
    }

    public void mouseMoved(MouseEvent e) {
    }
}
