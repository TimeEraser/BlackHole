package guard.guardshow;

import ecg.realtime.RealTime;
import util.JTableRenderUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/5/29.
 */
public class AlarmShow extends JPanel{
    private AlarmTablePanel temperatureTablePanel;
    private AlarmTablePanel bloodTablePanel;
    private AlarmTablePanel bubbleTablePanel;
    private Observable observable;

    public AlarmShow(Observable observable){
        this.observable=observable;
        temperatureTablePanel=new AlarmTablePanel("                               温度信息","血温","Temperature");
        bloodTablePanel=new AlarmTablePanel("                               漏血信息","漏血透光值","Blood");
        bubbleTablePanel=new AlarmTablePanel("                               气泡信息","气泡大小","Bubble");
        beginObserve();
        BoxLayout layout = new BoxLayout(this,BoxLayout.Y_AXIS);     //BoxLayout布局
        this.setLayout(layout);
        this.add(temperatureTablePanel);
        this.add(bloodTablePanel);
        this.add(bubbleTablePanel);
    }
    private void beginObserve(){
        observable.addObserver(temperatureTablePanel);
        observable.addObserver(bloodTablePanel);
        observable.addObserver(bubbleTablePanel);
    }
}
