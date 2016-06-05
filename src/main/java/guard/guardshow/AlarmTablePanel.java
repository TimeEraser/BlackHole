package guard.guardshow;

import guard.guardDataProcess.GuardData;
import util.JTableRenderUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/5/31.
 */
public class AlarmTablePanel extends JPanel implements Observer{
    private DefaultTableModel activeAlarmModel;
    private JTable activeAlarmTable;
    private Icon warningSolveIcon;
    private Icon warningIcon;
    private Icon errorIcon;
    private String targetString;
    private boolean connectFlag=false;
    public AlarmTablePanel(String title,String valueName,String targetString){
        this.targetString=targetString;
        JLabel titleLabel=new JLabel(title);
        String[] columnNames={"时间","报警信息",valueName};
        warningIcon=new ImageIcon(getIconImage("Icon/warning.png"));
        errorIcon=new ImageIcon(getIconImage("Icon/error.png"));
        warningSolveIcon=new ImageIcon(getIconImage("Icon/warningsolve.png"));

        activeAlarmModel=new DefaultTableModel(null,columnNames);
        activeAlarmTable=new JTable(activeAlarmModel){
            public boolean isCellEditable(int row, int column)
            {
                return false;}//表格不允许被编辑
        };
        activeAlarmTable.setBackground(Color.WHITE);
        activeAlarmTable.setSelectionBackground(Color.WHITE);
        activeAlarmTable.setSelectionForeground(Color.RED);
        TableColumnModel activeAlarmTableColumnModel=activeAlarmTable.getColumnModel();
        activeAlarmTableColumnModel.getColumn(0).setCellRenderer(new JTableRenderUtil());
        activeAlarmTableColumnModel.getColumn(1).setCellRenderer(new JTableRenderUtil());
        activeAlarmTableColumnModel.getColumn(2).setCellRenderer(new JTableRenderUtil());

        JScrollPane jScrollPane=new JScrollPane(activeAlarmTable);
        jScrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));

        BoxLayout layout = new BoxLayout(this,BoxLayout.Y_AXIS);     //BoxLayout布局
        this.setLayout(layout);
        this.add(titleLabel);
        this.add(jScrollPane);
    }
    private URL getIconImage(String path){
        return this.getClass().getClassLoader().getResource(path);
    }
    @Override
    public void update(Observable o, Object arg) {
        GuardData guardData = (GuardData) arg;
        if(!connectFlag){
            JLabel [] rowValue=new JLabel[3];
            rowValue[0]=new JLabel(guardData.getTime());
            connectFlag=true;
            rowValue[1]=new JLabel("连接成功");
            rowValue[1].setIcon(warningSolveIcon);
            rowValue[2]=new JLabel(" ");
            activeAlarmModel.insertRow(0,rowValue);
            activeAlarmTable.setRowSelectionInterval(0,0);
        }
        if (guardData.getMessage(targetString)!=null) {
            JLabel[] rowValue = new JLabel[3];
            rowValue[0] = new JLabel(guardData.getTime());
            String alarmMessage=guardData.getMessage(targetString);
            System.out.println(alarmMessage);
            rowValue[1]=new JLabel(alarmMessage);
            switch (alarmMessage){
                case "血温正常":case "不再漏血":case"气泡消失":
                    rowValue[1].setIcon(warningSolveIcon);
                    break;
                case "血温过高":case "血温过低":
                    rowValue[1].setIcon(warningIcon);
                    break;
                default:
                    rowValue[1].setIcon(errorIcon);
            }
            rowValue[2]=new JLabel(guardData.getValue(targetString));
            activeAlarmModel.insertRow(0,rowValue);
            activeAlarmTable.setRowSelectionInterval(0,0);
        }
    }
}
