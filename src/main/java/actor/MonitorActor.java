package actor;

import command.MonitorRequest;
import command.MonitorResponse;
import command.Request;
import command.Response;
import actor.config.MonitorActorConfig;
import command.config.CommandConfig;
import ecg.model.GuardianData;
import ecg.model.PressureData;
import ecg.model.PumpSpeedData;
import ecg.myals.MyMainWindow;
import ecg.tcp.TCPConfig;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by zzq on 16/5/16.
 */
public class MonitorActor extends BaseActor{
    private static TCPConfig TCPC;
    private String surgeryNo = "unknown";
    private GuardianData guardianData;
    private PressureData pressureData;
    private PumpSpeedData pumpSpeedData;
    private FileOutputStream fos;

    public MonitorActor(MonitorActorConfig monitorActorConfig){
        //TO DO Initialize the MonitorActor

    }
    @Override
    protected boolean processActorRequest(Request request) {
        if(request== MonitorRequest.MONITOR_ECG_DATA){

            if(TCPC == null)
                TCPC = new TCPConfig();
            TCPC.setVisible(true);		//使得框架可见
            try {
                fos = new FileOutputStream(surgeryNo+"_ecg.dat");
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

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

    public static TCPConfig getTCPC() {
        return TCPC;
    }
}
