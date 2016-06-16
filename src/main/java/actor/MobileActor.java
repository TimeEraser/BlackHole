package actor;

import actor.config.MobileActorConfig;
import command.MobileRequest;
import command.Request;
import command.Response;
import command.config.CommandConfig;
import command.MobileResponse;
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
    }
    @Override
    public boolean processActorRequest(Request request) {
        //接收到连接指令后开始查找手机端
        if(request == MobileRequest.MOBILE_CONNECT){
            System.out.println("MobileRequest.MOBILE_CONNECT");
            if(!start()){
                sendResponse(request,MobileResponse.MOBILE_CONNECT_FAILED);
            }
        }
        if(request==MobileRequest.MOBILE_DISCONNECT){
            transData.setEnableFlag(false);
        }
        if(request==MobileRequest.MOBILE_SYNCHRONIZE){
            transData.addObserver((GuardBottomShow)(request.getConfig().getData()));
        }
        return false;
    }

    @Override
    public boolean processActorResponse(Response  responses) {
        return false;
    }

    @Override
    public boolean start()
    {
        try {
            serverSocket=new ServerSocket(4700);
        }catch (Exception e) {
            System.out.println("can not listen to:" + e);
        }
        if(serverSocket!=null) {
            transData = new TransData(serverSocket, mobileActorConfig);
            transData.run();
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public boolean shutdown() {
        return false;
    }
}
