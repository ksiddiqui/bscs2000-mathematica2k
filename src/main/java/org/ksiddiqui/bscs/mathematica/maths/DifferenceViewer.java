package org.ksiddiqui.bscs.mathematica.maths;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.Vector;

//import DivideDifference;

/******************************************************************************************
 *                                                                                         *
 *       Difference Viewer class ..........                                                *
 *                                                                                         *
 *******************************************************************************************/


public class DifferenceViewer implements Serializable {
    private JFrame frame = null;
    private JPanel mainPanel = null;

    private double[] x;
    private double[] Fx;
    private JLabel dTableLabel;
    private JTable dTable;
    private DefaultTableModel dTableModel;


    /*****************************************************************************
     *            Constructors ...........                                        *
     *****************************************************************************/

    public DifferenceViewer(JPanel p, double x[], double Fx[]) throws Exception {
        this.x = x;
        this.Fx = Fx;
        DivideDifference dd = new DivideDifference(x, Fx);

        if (p == null) {
            frame = new JFrame("Difference Viewer");

            frame.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    exit();
                }
            });
            frame.setSize(400, 300);
            frame.setResizable(false);

            Container mC = frame.getContentPane();
            p = new JPanel();
            mC.add(p);
        }
        mainPanel = p;
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        setMainPanel(x.length);
    }

    /*****************************************************************************
     *            Setting & Queury Methods .......                                *
     *****************************************************************************/


    /*****************************************************************************
     *            Primary Methods .......                                         *
     *****************************************************************************/

    private void setMainPanel(int rows) {
        JPanel m1 = new JPanel(new GridLayout(1, 2));
        JPanel m2 = new JPanel(new BorderLayout());
//         JPanel m3 = new JPanel(new BorderLayout());
        m1.setBorder(new TitledBorder("Difference Types"));
        m2.setBorder(new TitledBorder("Difference Table"));

        JButton DDButton = new JButton("Divide Difference");
        JButton DButton = new JButton("Difference");
        m1.add(DDButton);
        m1.add(DButton);
        DDButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                D_command(0);
            }
        });
        DButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                D_command(1);
            }
        });

        setDTable(rows);
        dTableLabel = new JLabel();
        m2.add(dTableLabel, BorderLayout.NORTH);
        m2.add(new JScrollPane(dTable), BorderLayout.CENTER);

        mainPanel.add(m1, BorderLayout.NORTH);
        mainPanel.add(m2, BorderLayout.CENTER);
//         mainPanel.add(m3, BorderLayout.SOUTH);

        if (frame != null)
            frame.setVisible(true);
    }

    private void setDTable(int rows) {
        String[] columns = new String[rows + 1];
        columns[0] = "x";
        columns[1] = "Fx";
        for (int i = 2; i < rows + 1; i++) columns[i] = String.valueOf(i - 1);

        dTableModel = new DefaultTableModel(columns, 0);
        dTable = new JTable(dTableModel);
        dTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dTable.setShowGrid(true);
        TableColumnModel dTableColumnModel = dTable.getColumnModel();

        int cols = rows + 1;
        for (int i = rows + 1, j = 0; i >= 2; i--, j++) {
            columns = new String[i];
            columns[0] = String.valueOf(x[j]);
            columns[1] = String.valueOf(Fx[j]);
            dTableModel.addRow(columns);
        }
/*               
           DifferenceViewerTableCellRenderer renderer = new DifferenceViewerTableCellRenderer();
           for (int i=0; i<rows+1; i++)
               {
                TableColumn tc = dTableColumnModel.getColumn(i);
                tc.setCellRenderer(renderer);
               }    
*/
    }


    public void exit() {
        if (frame != null)
            frame.dispose();
    }


    private void D_command(int command) {
        Double[] values;
        Vector table = null;
        try {
            DivideDifference dd = new DivideDifference(x, Fx);
            if (command == 0) dd.doFDivideDifference();
            else if (command == 1) dd.doFDifference();
            table = dd.getDDTable();
        } catch (Exception e) {
        }
        if (table == null) return;

        for (int i = 1; i < x.length; i++) {
            values = (Double[]) table.get(i);
            for (int j = 0; j < values.length; j++) {
                dTableModel.setValueAt(values[j], j, i + 1);
            }
        }
    }


}


/*****************************************************************************
 *            Table Cell Rendering Class ....                                 *
 *****************************************************************************/

/*
class DifferenceViewerTableCellRenderer extends JLabel implements TableCellRenderer
{
        DifferenceViewerTableCellRenderer() {}
                
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                boolean hasFocus, int row, int col)
               {       
                if (value!=null) setText(value.toString());      
                if     (col==0) { setBackground(Color.pink); setBorder(new BevelBorder(BevelBorder.RAISED)); }
                else if(col==1) { setBackground(Color.yellow); setBorder(new BevelBorder(BevelBorder.RAISED)); }
                else setBackground(Color.white);
                return this;
               }

}


*/


