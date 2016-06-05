package ct.ctshow;

import actor.BaseActor;
import actor.Listener.NoticeListener;
import command.CtRequest;
import config.ConfigCenter;
import util.ImageUtil;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Vector;

/**
 * Created by zzq on 16/5/29.
 */
public class CTHistoryData extends JScrollPane{
    private final String ROOT= ConfigCenter.getString("ct.analyse.result.save.root");
    private final String filterFormat=ConfigCenter.getString("ct.analyse.result.save.format");
    private final String[] names={"历史信息"};
    private Integer imageWidth = 200;
    private Integer imageHeight = 200;
    private BaseActor boss;
    private DefaultTableModel imageTableModel;
    private JTable imageTable;
    public CTHistoryData(BaseActor boss){
        this.boss=boss;
        getVerticalScrollBar().setPreferredSize(new Dimension(0, 0));
        getViewport().setBorder(null);
        setViewportBorder(null);
        setBorder(null);
    }
    public void refresh(String result){
        imageTableModel=new JTableButtonModel(null,names);
        imageTable=new JTable(imageTableModel);
        imageTable.getColumn("历史信息").setCellRenderer(new JTableButtonRenderer());
        imageTable.addMouseListener(new JTableButtonMouseListener(imageTable));
        imageTable.setRowHeight(imageHeight);
        File folder = new File(ROOT+"/"+result);
        if(!folder.exists()||!folder.isDirectory()) {
            setViewportView(imageTable);
            return;
        }
        File[] files=folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(filterFormat);
            }
        });
        for (File f:files) {
            try {
                BufferedImage image = ImageIO.read(f);
//<<<<<<< HEAD
//                BufferedImage zoomImage = ImageUtil.zoom(image,imageWidth,imageHeight);
//=======
                BufferedImage zoomImage = ImageUtil.zoom(image,imageWidth,imageHeight,Color.BLACK);
//>>>>>>> dd2361f9a71eefb298ae36e051e3b241501c6553
                ImageIcon show = new ImageIcon(zoomImage);
                JButton showButton = new JButton(show);
                showButton.addActionListener(new NoticeListener(boss, CtRequest.CT_SHOW_HISTORY,f.getAbsolutePath()));
                Object[] rowData = new Object[1];
                rowData[0]=showButton;
                imageTableModel.addRow(rowData);
            } catch (IOException e) {
                System.out.println(e);
            }
        }
        setViewportView(imageTable);
        setVisible(true);
    }
    public void addHistory(String path){
        File f = new File(path);
        BufferedImage image = null;
        try {
            image = ImageIO.read(f);
//<<<<<<< HEAD
//            BufferedImage zoomImage = ImageUtil.zoom(image,imageWidth,imageHeight);
//=======
            BufferedImage zoomImage = ImageUtil.zoom(image,imageWidth,imageHeight,Color.BLACK);
//>>>>>>> dd2361f9a71eefb298ae36e051e3b241501c6553
            ImageIcon show = new ImageIcon(zoomImage);
            JButton showButton = new JButton(show);
            showButton.addActionListener(new NoticeListener(boss, CtRequest.CT_SHOW_HISTORY,f.getAbsolutePath()));
            Object[] rowData = new Object[1];
            rowData[0]=showButton;
            imageTableModel.insertRow(0,rowData);
        } catch (IOException e) {
            System.out.println(e);
        }

    }
    private  class JTableButtonMouseListener extends MouseAdapter {
        private final JTable table;

        public JTableButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX());
            int row    = e.getY()/table.getRowHeight();

            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {
                    ((JButton)value).doClick();
                }
            }
        }
    }
    private  class JTableButtonRenderer implements TableCellRenderer {

        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton)value;
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            return button;
        }
    }
    private  class JTableButtonModel extends DefaultTableModel {
        public JTableButtonModel(Object[][] data, Object[] columnNames)
        {
            super(data,columnNames);
        }
        @Override
        public Object getValueAt(final int rowIndex, final int columnIndex){
            return ((Vector)dataVector.elementAt(rowIndex)).elementAt(columnIndex);
        }
        @Override
        public Class getColumnClass(int col)
        {
            // dataVector is a protected member of DefaultTableModel
            Vector v = (Vector)dataVector.elementAt(0);
            return v.elementAt(col).getClass();
        }
        @Override
        public boolean isCellEditable(int row,int col)
        {
            return false;
        }
    }
}