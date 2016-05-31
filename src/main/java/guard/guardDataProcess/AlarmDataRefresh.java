package guard.guardDataProcess;

import org.jfree.chart.axis.DateAxis;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/5/26.
 */
public class AlarmDataRefresh extends  Observable implements Observer {
    private String result;
    private void dataRefresh(){
        if(result.contains("_")) {
            String alarmData = result.substring(result.indexOf("\r\n") +2);
            setChanged();
            notifyObservers(alarmData);
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        result=(String)arg;
//        if(result.contains("_")) {
//            System.out.println("begin" + result + "end");
//        }
        dataRefresh();
    }
}