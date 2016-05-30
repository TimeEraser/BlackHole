package actor;

import actor.config.MobileActorConfig;
import command.MobileRequest;
import command.Request;
import command.Response;
import command.config.CommandConfig;
import command.MobileResponse;
import java.io.*;
import java.net.*;
public class MobileActor extends BaseActor  {
    protected ServerSocket serverSocket;
    protected Socket socket;
    private Request startrequest;
    private TransData transData;
    protected MobileActorConfig mobileActorConfig;
    public MobileActor(MobileActorConfig mobileActorConfig){
        //建立服务器端Socket端口，端口号为4700
        this.mobileActorConfig=mobileActorConfig;
        try {
            serverSocket=new ServerSocket(4700);
        }catch (Exception e) {
            System.out.println("can not listen to:" + e);
        }
        //TO DO Initialize the GuardActor
    }
    @Override
    public boolean processActorRequest(Request request) {
        //接收到连接指令后开始查找手机端
        if(request == MobileRequest.MOBILE_CONNECT){
            System.out.println("MobileRequest.MOBILE_CONNECT");
            startrequest=request;
            start();
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
        //尝试与手机端连接，如果发生异常则回应
        try {
            socket=serverSocket.accept();
        }catch (Exception e){
            sendResponse(startrequest,MobileResponse.MOBILE_CONNECT_FAILED);
            return false;
        }
        if (socket!=null){
            transData = new TransData(serverSocket,mobileActorConfig);
        }
        return true;
    }

    @Override
    public boolean shutdown() {
        return false;
    }
}

class TransData implements Runnable{
    protected ServerSocket serverSocket;
    protected Socket socket;
    protected BufferedReader mobileInput;
    protected PrintWriter mobileOutput;
    protected BufferedReader dataToSend;
    protected MobileActorConfig mobileActorConfig;
    public TransData(ServerSocket serverSocket,MobileActorConfig mobileActorConfig){
        this.mobileActorConfig=mobileActorConfig;
        this.serverSocket=serverSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader mobileInput=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter mobileOutput=new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

