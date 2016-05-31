package guard.guardshow;

import ecg.realtime.RealTime;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/5/29.
 */
public class AlarmShow extends JPanel implements Observer{
    private DefaultTableModel temperatureTableModel;
    private DefaultTableModel bloodAlarmModel;
    private DefaultTableModel bubbleAlarmModel;
    private JTable temperatureAlarmTable;
    private JTable bloodAlarmTable;
    private JTable bubbleAlarmTable;
    public AlarmShow(){
        JScrollPane temperatureScrollPane;
        JScrollPane bloodScrollPane;
        JScrollPane bubbleScrollPane;
        RealTime realTime=new RealTime();
        String time=realTime.getHMS();
        String[][] firstRowValue={{time,"开始"}};
        JLabel temperatureTableTitle=new JLabel("温度报警信息",JLabel.CENTER);
        JLabel bloodTableTitle=new JLabel("漏血报警信息",JLabel.CENTER);
        JLabel bubbleTableTitle=new JLabel("气泡报警信息",JLabel.CENTER);
        String[] temperatureColumnNames={"时间","报警信息"};
        String[] bloodColumnNames={"时间","报警信息"};
        String[] bubbleColumnNames={"时间","报警信息"};
        temperatureTableModel=new DefaultTableModel(firstRowValue,temperatureColumnNames);
        bloodAlarmModel=new DefaultTableModel(firstRowValue,bloodColumnNames);
        bubbleAlarmModel=new DefaultTableModel(firstRowValue,bubbleColumnNames);

        temperatureAlarmTable=new JTable(temperatureTableModel);
        temperatureAlarmTable.setBackground(Color.WHITE);

        bloodAlarmTable=new JTable(bloodAlarmModel);
        bloodAlarmTable.setBackground(Color.WHITE);

        bubbleAlarmTable=new JTable(bubbleAlarmModel);
        bubbleAlarmTable.setBackground(Color.WHITE);
        temperatureScrollPane=new JScrollPane(temperatureAlarmTable);
        bloodScrollPane=new JScrollPane(bloodAlarmTable);
        bubbleScrollPane=new JScrollPane(bubbleAlarmTable);

        BoxLayout layout = new BoxLayout(this,BoxLayout.Y_AXIS);     //BoxLayout布局
        this.setLayout(layout);
        this.add(temperatureTableTitle);
        this.add(temperatureScrollPane);
        this.add(bloodTableTitle);
        this.add(bloodScrollPane);
        this.add(bubbleTableTitle);
        this.add(bubbleScrollPane);
        this.setVisible(true);
    }
    @Override
    public void update(Observable o, Object arg) {
        String temp=(String)arg;
        if(temp.contains("_")){
            String time = temp.substring(0, temp.indexOf('_'));
            String [] rowValue=new String[2];
            rowValue[0]=time;
            if (temp.contains("Temperature")) {
                if(temp.contains("Solved"))
                   rowValue[1]="温度恢复正常";
                else
                    rowValue[1]="温度报警";
                temperatureTableModel.addRow(rowValue);

            }
            else if (temp.contains("Blood")) {
                if(temp.contains("Solved"))
                    rowValue[1]="不再漏血";
                else
                    rowValue[1]="发生漏血";
                bloodAlarmModel.addRow(rowValue);
            }
            else if (temp.contains("Bubble")) {
                if(temp.contains("Solved"))
                    rowValue[1]="气泡报警消失";
                else
                    rowValue[1]="出现气泡";
                bubbleAlarmModel.insertRow(0,rowValue);
            }
        }
    }
}
