package org.ksiddiqui.bscs.mathematica.app;

import org.ksiddiqui.bscs.mathematica.maths.FunctionGrapher;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Mathematica {
    static JFrame frame;
    static JTabbedPane MainTab;

    static JToggleButton fgButton;
    static FunctionGrapher fg;

    public static void start(String s[]) {
        frame = new JFrame("Mathematica 2000");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Container mC = frame.getContentPane();
        frame.setSize(620, 500);
        frame.setResizable(false);

//        ImageIcon animation = new ImageIcon("Mathematica.jpg");
//        JLabel TitleLabel = new JLabel(animation);
//        TitleLabel.setBorder((Border) new BevelBorder(BevelBorder.RAISED));
        JLabel mainLabel = new JLabel(" MATHEMATICA 2000");
        mainLabel.setOpaque(true);
        mainLabel.setBackground(Color.black);
        mainLabel.setForeground(Color.white);
        mainLabel.setFont(new Font("Serif", Font.BOLD, 42));
        JButton aboutButton = new JButton("About");
        aboutButton.setBackground(Color.black);
        aboutButton.setForeground(Color.white);
        aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                about();
            }
        });
        JPanel m1 = new JPanel(new BorderLayout());
        m1.add(mainLabel, BorderLayout.CENTER);
        m1.add(aboutButton, BorderLayout.EAST);
        mC.add(m1, BorderLayout.NORTH);

        fg = null;
        fgButton = new JToggleButton("Show Function Grapher");
        fgButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fgButton_command();
            }
        });
        JPanel fgPanel = new JPanel(new BorderLayout());
        fgPanel.add(fgButton, BorderLayout.EAST);
        mC.add(fgPanel, BorderLayout.SOUTH);


        MainTab = new JTabbedPane(SwingConstants.BOTTOM);
        Introduction intro = new Introduction();
        new Calculator();
        new RootSolverAgent();
        new LinearSystemSolverAgent();
        new InterpolationSolverAgent();
        MainTab.setSelectedIndex(0);

        MainTab.setBorder((Border) new EmptyBorder(5, 5, 5, 5));
        mC.add(MainTab, BorderLayout.CENTER);

        frame.setVisible(true);
        try {
            intro.styleTextList.start();
        } catch (Exception e) {
            System.out.println("Error");
        }
        MainTab.repaint();
    }

    private static void fgButton_command() {
        if (fg == null) fg = new FunctionGrapher(null);
        else {
            fg.exit();
            fg = null;
        }
    }

    private static void about() {
        ImageIcon animation = new ImageIcon("k.class");
        JLabel TitleLabel = new JLabel(animation);
        TitleLabel.setBorder((Border) new BevelBorder(BevelBorder.LOWERED));
        //animation.setImageObserver(TitleLabel);

        String data = "A project of\n" +
                "      NUMERICAL COMPUTNG\n" +
                "     ====================\n\n" +
                "Supervised by : Mr. Shahid Qureshi\n" +
                "in year 2000, semester II.\n\n" +
                "Programmed and designed by : Kashif Ali Siddiqui\n" +
                "Roll # 20, section B, BS-2\n" +
                "Department Of Computer Science\n" +
                "University Of Karachi";
        JTextPane aboutText = new JTextPane();
        aboutText.setText(data);
        aboutText.setEditable(false);
        JScrollPane dataPane = new JScrollPane(aboutText);// dataPane.setSize();

        Object[] obj = new Object[2];
        obj[0] = TitleLabel;
        obj[1] = dataPane;

        JOptionPane.showOptionDialog(
                null, obj, "About", JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, null, null);
    }

}


