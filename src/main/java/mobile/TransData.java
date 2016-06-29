package mobile;

import actor.config.MobileActorConfig;
import ecg.ecgshow.ECGDataRefresher;
import guard.guardDataProcess.GuardData;
import guard.guardDataProcess.GuardSerialDataProcess;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/6/22.
 */
public class TransData extends Observable implements Runnable,Observer {
    private ServerSocket serverSocket;
    private MobileActorConfig mobileActorConfig;
    private Map<String,String> connectInfo=new HashMap<>();
    private static boolean enableFlag=true;
    public TransData(ServerSocket serverSocket,MobileActorConfig mobileActorConfig){
        this.mobileActorConfig=mobileActorConfig;
        this.serverSocket=serverSocket;
    }
    public void setEnableFlag(boolean enableFlag){
        this.enableFlag=enableFlag;
    }
    @Override
    public void run() {
        try {
            while (enableFlag) {
                // 一旦有堵塞, 则表示服务器与客户端获得了连接
                Socket client = serverSocket.accept();
                // 处理这次连接
                new HandlerThread(client);
                Thread.sleep(200);
            }
        } catch (Exception e) {
            System.out.println("服务器异常: " + e.getMessage());
        }
    }
    public void start(){
        new Thread(this).start();

    }

    @Override
    public void update(Observable o, Object arg) {
//        System.out.println("ReadOne");
        if(o instanceof GuardSerialDataProcess){
            GuardData temp=(GuardData) arg;
            connectInfo.put("temperature",temp.getTemperature());
            connectInfo.put("time",temp.getTime());
            connectInfo.put("temperatureMessage",temp.getTemperatureMessage());
            connectInfo.put("bloodMessage",temp.getBloodMessage());
            connectInfo.put("bubbleMessage",temp.getBubbleMessage());
        }
        if(o instanceof ECGDataRefresher){
            String temp=(String)arg;
            if(temp.contains("HeartRate")){
                connectInfo.put("heartRateData",temp.substring(temp.indexOf(':')+1));
            }
            if(temp.contains("bloodOxygendata")){
                connectInfo.put("bloodOxygenData",temp.substring(temp.indexOf(':')+1));
            }
        }
    }

    private class HandlerThread implements Runnable{
        private  Socket client;
        private boolean confirmedFlag=false;
        private DataInputStream input;
        private DataOutputStream out;
        HandlerThread(Socket client){
            this.client=client;
            try {
                input = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
//                System.out.println(client==null);
//                System.out.println("sendOne"+confirmedFlag);
                try {
                    // 读取客户端数据
                    if (!confirmedFlag&&input.available() > 0) {
                        String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                        if (!confirmedFlag) {
                            if (clientInputStr.equals("zjuisee")) {
                                out.writeUTF("success");
                                out.flush();
//                                out.close();
                                confirmedFlag = true;
                            }
                            else {
//                                System.out.println("failed");
                                out.writeUTF("fail");
                                out.flush();
                                input.close();
                                out.close();
                                client.close();
                                break;
                            }
                        }
                    }
                    else if(confirmedFlag) {
                        DataOutputStream out = new DataOutputStream(client.getOutputStream());
                        if (!connectInfo.isEmpty()) {
                            if(connectInfo.get("temperature")!=null) {
                                out.writeUTF("temperature:" + connectInfo.get("temperature") + "\r\n");
                                out.flush();
                                Thread.sleep(10);
                            }
                            if(connectInfo.get("time")!=null) {
                                out.writeUTF("time:" + connectInfo.get("time") + "\r\n");
                                out.flush();
                                Thread.sleep(10);
                            }
                            if (connectInfo.get("temperatureMessage") != null) {
                                out.writeUTF("temperatureMessage:" + connectInfo.get("temperatureMessage") + "\r\n");
                                out.flush();
                                Thread.sleep(10);
                            }
                            if (connectInfo.get("bloodMessage") != null) {
                                out.writeUTF("bloodMessage:" + connectInfo.get("bloodMessage") + "\r\n");
                                out.flush();
                                Thread.sleep(10);
                            }
                            if (connectInfo.get("bubbleMessage") != null) {
                                out.writeUTF("bubbleMessage:" + connectInfo.get("bubbleMessage") + "\r\n");
                                out.flush();
                                Thread.sleep(10);
                            }
                            if(connectInfo.get("heartRateData")!=null) {
                                out.writeUTF("heartRateData:" + connectInfo.get("heartRateData") + "\r\n");
                                out.flush();
                                Thread.sleep(10);
                            }
                            if(connectInfo.get("bloodOxygenData")!=null) {
                                out.writeUTF("bloodOxygenData:" + connectInfo.get("bloodOxygenData") + "\r\n");
                                out.flush();
                                Thread.sleep(10);
                            }
                            connectInfo.clear();
//                            out.flush();
//                            out.close();
                            setChanged();
                            notifyObservers();
//                            System.out.println("SendOne");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("服务器 run 异常: " + e.getMessage());
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

