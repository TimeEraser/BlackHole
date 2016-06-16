package mobile;

import actor.config.MobileActorConfig;
import ecg.ecgshow.ECGDataRefresher;
import guard.guardDataProcess.GuardData;
import guard.guardDataProcess.GuardSerialDataProcess;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/6/16.
 */
public class TransData extends Observable implements Runnable,Observer{
    private ServerSocket serverSocket;
    private MobileActorConfig mobileActorConfig;
    private Map<String,String> connectInfo=new HashMap<>();
    private static boolean enableFlag=true;
    public TransData(ServerSocket serverSocket,MobileActorConfig mobileActorConfig){
        this.mobileActorConfig=mobileActorConfig;
        this.serverSocket=serverSocket;
        new Thread(this).start();
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

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof GuardSerialDataProcess){
            GuardData temp=(GuardData) arg;
            connectInfo.put("temperature",temp.getTemperature());
            connectInfo.put("lightValue",temp.getLightValue());
            connectInfo.put("time",temp.getTime());
            connectInfo.put("temperatureMessage",temp.getTemperatureMessage());
            connectInfo.put("bloodMessage",temp.getBloodMessage());
            connectInfo.put("bubbleMessage",temp.getBubbleMessage());
        }
        if(o instanceof ECGDataRefresher){

        }
    }

    private class HandlerThread implements Runnable{
        private Socket client;
        private boolean confirmedFlag=false;
        private DataInputStream input;
        HandlerThread(Socket client){
            this.client=client;
            try {
                input = new DataInputStream(client.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 读取客户端数据
                    if (input.available() > 0) {
                        String clientInputStr = input.readUTF();//这里要注意和客户端输出流的写方法对应,否则会抛 EOFException
                        if (!confirmedFlag) {
                            DataOutputStream out = new DataOutputStream(client.getOutputStream());
                            if (clientInputStr.equals("zjuisee")) {
                                out.writeUTF("success");
                                confirmedFlag=true;
                            }
                            else {
                                out.writeUTF("fail");
                                input.close();
                                out.close();
                                client.close();
                                break;
                            }
                            out.close();
                        } else {
                            if(clientInputStr.equals("bye")){
                                input.close();
                                client.close();
                                break;
                            }
                            else {
                                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                                if (connectInfo != null) {
                                    out.writeUTF("temperature:" + connectInfo.get("temperature") + "\r\n");
                                    out.writeUTF("lightValue:" + connectInfo.get("lightValue") + "\r\n");
                                    out.writeUTF("time:" + connectInfo.get("time") + "\r\n");
                                    out.writeUTF("temperatureMessage:" + connectInfo.get("temperatureMessage") + "\r\n");
                                    out.writeUTF("bloodMessage:" + connectInfo.get("bloodMessage") + "\r\n");
                                    out.writeUTF("bubbleMessage:" + connectInfo.get("bubbleMessage") + "\r\n");
                                    out.writeUTF("heartRateData:" + connectInfo.get("heartRateData") + "\r\n");
                                    out.writeUTF("bloodOxygenData:" + connectInfo.get("bloodOxygenData") + "\r\n");
                                    connectInfo.clear();
                                    out.flush();
                                    out.close();
                                    setChanged();
                                    notifyObservers();
                                }
                            }
                        }
                        // 向客户端回复信息
                    }
                } catch (Exception e) {
                    System.out.println("服务器 run 异常: " + e.getMessage());
                } finally {
                    if (client != null) {
                        try {
                            client.close();
                        } catch (Exception e) {
                            client = null;
                            System.out.println("服务端 finally 异常:" + e.getMessage());
                        }
                    }
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
