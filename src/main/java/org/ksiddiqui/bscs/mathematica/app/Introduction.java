package org.ksiddiqui.bscs.mathematica.app;

import org.ksiddiqui.bscs.mathematica.app.ui.StylisPanel;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Introduction {

    StylisPanel styleTextList;

    public Introduction() {
        JPanel mp = new JPanel(new GridLayout(4, 1));
        JPanel m1 = new JPanel(new BorderLayout());
        JPanel m2 = new JPanel(new GridLayout(3, 1));
        m1.setBorder((Border) new EmptyBorder(2, 2, 2, 1));
        m2.setBorder((Border) new BevelBorder(BevelBorder.RAISED));

        String[] list = new String[17];
        list[0] = " A Project Of";
        list[1] = " NUMERICAL COMPUTING";
        list[2] = " =======================";
        list[3] = " ";
        list[4] = " To";
        list[5] = " Mr.Shahid Qureshi";
        list[6] = " ----------------------";
        list[7] = " course supervisor of";
        list[8] = " Numerical Computing ";
        list[9] = " in year 2000.";
        list[10] = " ";
        list[11] = " programmed by: ";
        list[12] = " Kashif Ali Siddiqui";
        list[13] = " roll # 20";
        list[14] = " BS-2(B), 2000";
        list[15] = " ";
        list[16] = " ";
        try {
            styleTextList = new StylisPanel();
            styleTextList.setBackground(Color.black);
            styleTextList.setMainText("MATHEMATICA 2000", Color.darkGray, 10, 50, new Font("New Times Roman", Font.BOLD, 45));
            styleTextList.setTextList(list, Color.yellow, 220, 20, new Font("Serif", Font.BOLD, 20));
            styleTextList.setShadow(5, 15, 0, 0, Color.gray);
            styleTextList.setAnimation(50, false);
        } catch (Exception e) {
        }

        ImageIcon closeicon = new ImageIcon("car.gif");

        JLabel about = new JLabel("Programmed by KASHIF ALI SIDDIQUI", SwingConstants.CENTER);
        about.setBorder((Border) new EtchedBorder());

        JButton Win = new JButton("Selecting Windows GUI");
        JButton Met = new JButton("Selecting Java Metal GUI");
        JButton Mot = new JButton("Selecting Motif GUI");
        JButton Close = new JButton("Close", closeicon);

        m2.add(Win);
        m2.add(Met);
        m2.add(Mot);
        m1.add(Close, BorderLayout.EAST);

        mp.setBorder((Border) new EmptyBorder(5, 5, 5, 5));
        mp.add(styleTextList);
        mp.add(about);
        mp.add(m2);
        mp.add(m1);

        Win.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(Mathematica.frame);
                } catch (Exception ex) {
                    System.out.println("Error in Look and Feel !!");
                }
            }
        });
        Met.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(Mathematica.frame);
                } catch (Exception ex) {
                    System.out.println("Error in Look and Feel !!");
                }
            }
        });
        Mot.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
                    SwingUtilities.updateComponentTreeUI(Mathematica.frame);
                } catch (Exception ex) {
                    System.out.println("Error in Look and Feel !!");
                }
            }
        });
        Close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        Mathematica.MainTab.addTab("Mathematica", mp);
    }

}
