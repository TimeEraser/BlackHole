package actor;

import command.MonitorRequest;
import command.MonitorResponse;
import command.Request;
import command.Response;
import actor.config.MonitorActorConfig;
import command.config.CommandConfig;
import ecg.myals.MyMainWindow;
import ecg.tcp.TCPConfig;

/**
 * Created by zzq on 16/5/16.
 */
public class MonitorActor extends BaseActor{
    public MonitorActor(MonitorActorConfig monitorActorConfig){
        //TO DO Initialize the MonitorActor
    }
    @Override
    protected boolean processActorRequest(Request request) {
        if(request== MonitorRequest.MONITOR_ECG_DATA){
            TCPConfig  TCPC =  new TCPConfig();
            TCPC.setVisible(true);
            System.out.println("MonitorRequest.MONITOR_ECG_DATA");
        }
        //.....
        sendResponse(request,MonitorResponse.MONITOR_DATA,"NI_MA_HIGH");
        return false;
    }

    @Override
    protected boolean processActorResponse(Response responses) {
        return false;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean shutdown() {
        return false;
    }
}
