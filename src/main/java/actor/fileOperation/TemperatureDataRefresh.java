package actor.fileOperation;

import java.io.File;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/5/26.
 */
public class TemperatureDataRefresh extends  Observable implements Observer {
    private String result;
    private void dataRefresh(){
        setChanged();
        notifyObservers(result.substring(0,result.indexOf("\r\n")));
    }
    @Override
    public void update(Observable o, Object arg) {
        result=(String)arg;
        dataRefresh();
    }
}
