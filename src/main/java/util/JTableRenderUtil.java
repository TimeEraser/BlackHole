package util;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by adminstrator on 2016/6/1.
 */
public class JTableRenderUtil extends JLabel implements TableCellRenderer{
    public JTableRenderUtil(){
        super();
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if(value!=null) {
            if (((JLabel) value).getText() != null)
                setText(((JLabel) value).getText());
            if (((JLabel) value).getIcon() != null) {
                setIcon(((JLabel) value).getIcon());
                setHorizontalTextPosition(JLabel.RIGHT);
            } else {
                setHorizontalTextPosition(JLabel.CENTER);
            }
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                super.setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }
            return this;
        }
        return null;
    }
}
