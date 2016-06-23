package actor;

import actor.config.MobileActorConfig;
import command.*;
import command.config.CommandConfig;
import ecg.ecgshow.ECGDataRefresher;
import guard.guardDataProcess.GuardSerialDataProcess;
import guard.guardshow.GuardBottomShow;
import mobile.TransData;

import java.io.*;
import java.net.*;
import java.util.Observable;
import java.util.Observer;

public class MobileActor extends BaseActor  {
    private ServerSocket serverSocket;
    private TransData transData;
    private MobileActorConfig mobileActorConfig;
    public MobileActor(MobileActorConfig mobileActorConfig){
        this.mobileActorConfig=mobileActorConfig;
        try {
            serverSocket=new ServerSocket(4800);
        }catch (Exception e) {
            System.out.println("can not listen to:" + e);
        }
        System.out.println(serverSocket!=null);
        if(serverSocket!=null) {
            System.out.println("tried");
            transData = new TransData(serverSocket, mobileActorConfig);
        }
    }
    @Override
    public boolean processActorRequest(Request request) {
        //接收到连接指令后开始查找手机端
        if(request == MobileRequest.MOBILE_CONNECT){
//            System.out.println("firstAccepted");
           if(start()){
               sendResponse(request,MobileResponse.MOBILE_CONNECT,transData);
//               System.out.println("sent");
           }
        }
        if(request==MobileRequest.MOBILE_DISCONNECT){
            transData.setEnableFlag(false);
        }
        if(request==MobileRequest.MOBILE_GET_ECG_DATA_REFRESH){
            ((ECGDataRefresher)request.getConfig().getData()).addObserver(transData);
        }
        return false;
    }

    @Override
    public boolean processActorResponse(Response  responses) {
        if(responses==GuardResponse.GUARD_SERIAL_DATA_PROCESS){
            ((GuardSerialDataProcess)responses.getConfig().getData()).addObserver(transData);
        }
        return false;
    }

    @Override
    public boolean start()
    {
        sendRequest(mobileActorConfig.getBlackHoleActor(), GuardRequest.GUARD_SERIAL_DATA_PROCESS);
        transData.start();
        return true;
    }

    @Override
    public boolean shutdown() {
        return false;
    }
}
