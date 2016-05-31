package guard.guardshow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/5/31.
 */
public class ActiveAlarmShow extends JPanel implements Observer{
    private DefaultTableModel activeAlarmModel;
    public ActiveAlarmShow(){
        JLabel titleLabel=new JLabel("活跃报警信息");
        String[] columnNames={"时间","报警信息"};
        activeAlarmModel=new DefaultTableModel(null,columnNames);
        JTable activeAlarmTable=new JTable(activeAlarmModel);
        activeAlarmTable.setBackground(Color.WHITE);

        JScrollPane jScrollPane=new JScrollPane(activeAlarmTable);

        BoxLayout layout = new BoxLayout(this,BoxLayout.Y_AXIS);     //BoxLayout布局
        this.setLayout(layout);
        this.add(titleLabel);
        this.add(jScrollPane);
        this.setVisible(true);
    }
    @Override
    public void update(Observable o, Object arg) {
        String temp=(String) arg;
        if (temp.contains("Solved")){
            int i=0;
            String targetString=new String();
            if(temp.contains("Temperature")){
                targetString="温度";
            }
            else if(temp.contains("Blood")){
                targetString="漏血";
            }
            else if(temp.contains("Bubble")){
                targetString="气泡";
            }
            while (i<activeAlarmModel.getRowCount()) {
                if(((String)activeAlarmModel.getValueAt(i,1)).contains(targetString)) {
                    activeAlarmModel.removeRow(i);
                    break;
                }
                i++;
            }
        }
        else {
            String time = temp.substring(0, temp.indexOf('_'));
            String [] rowValue=new String[2];
            rowValue[0]=time;
            if(temp.contains("Temperature")){
                rowValue[1]="温度报警";
            }
            else if(temp.contains("Blood")){
                rowValue[1]="发生漏血";
            }
            else if(temp.contains("Bubble")){
                rowValue[1]="出现气泡";
            }
            activeAlarmModel.addRow(rowValue);
        }
    }
}
