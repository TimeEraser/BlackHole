package actor;

import command.*;
import actor.config.MonitorActorConfig;
import ecg.ecgshow.ECGDataRefresher;
import ecg.ecgshow.ECGOtherData;
import ecg.ecgshow.ECGShowUI;
import ecg.tcp.*;

import java.io.FileOutputStream;
import java.util.Map;

/**
 * Created by zzq on 16/5/16.
 */
public class MonitorActor extends BaseActor{

    private static FileOutputStream fos;
    Map connectInfo;
    private ECGShowUI ecgShowUI;

    public ECGShowUI getMyECGShowUI() {
        return ecgShowUI;
    }

    private ECGDataRefresher ecgDataRefresher;
    private TCPClient client;

    public MonitorActor(MonitorActorConfig monitorActorConfig){
        //TO DO Initialize the MonitorActor
    }

    @Override
    protected boolean processActorRequest(Request request) {
        if(request==MonitorRequest.MONITOR_SHUTDOWM){
            client.stopFlag =true;
            ecgDataRefresher.setStopFlag();
            System.out.println("client.stopFlag =true");
        }
        if(request==MonitorRequest.MONITOR_ECG_DATA) {
            connectInfo= (Map) request.getConfig().getData();
            start();
        }
        if(request==MonitorRequest.ECG_UI_CONFIG){
            ecgShowUI = (ECGShowUI)request.getConfig().getData();
            ecgDataRefresher=new ECGDataRefresher(ecgShowUI.getECGSeries(),ecgShowUI.getDateAxises(),ecgShowUI.getECGOtherData());
        }
        if(request==MonitorRequest.MONITOR_ECG_START){
            client.stopFlag = false;
            ecgDataRefresher.setStartFlag();
        }
        if(request==MonitorRequest.MONITOR_ECG_STOP)
            ecgDataRefresher.setStopFlag();
        return false;
    }

    @Override
    protected boolean processActorResponse(Response responses) {
        return false;
    }

    @Override
    public boolean start() {
        String host= (String) connectInfo.get("host");//主机域名
        String port=(String) connectInfo.get("port");//主机端口号
        String Id=(String) connectInfo.get("Id"); //档案ID
        String Name = (String) connectInfo.get("Name");//姓名
        String Sex = (String) connectInfo.get("Sex");
        System.out.println("MonitorActor: host ="+host);
        System.out.println("MonitorActor: port ="+port);
        System.out.println("MonitorActor: id ="+Id);
        System.out.println("MonitorActor: name ="+Name);
        System.out.println("MonitorActor: sex ="+Sex);
        System.out.println("MonitorRequest.MONITOR_ECG_DATA");
        if((host!=null)&&(port!=null)) {
            client = new TCPClient(ecgDataRefresher);        //新建一个TCPClient()方法的实例client
            client.setHost(host);    //设置主机
            client.setPort(Integer.parseInt(port));    //设置端口
            client.setID(Id);
            client.setNAME(Name);
            client.setSEX(Sex);
            client.stopFlag = false;
            //client.setMainUiActor((MainUiActor) request.getConfig().getSendActor());
            client.start();        //客户端线程开始运行
        }
        else{
            return false;
        }
        ecgDataRefresher.start();
        return false;
    }

    @Override
    public boolean shutdown() {
        return false;
    }

    public  static FileOutputStream getFos() {
        return fos;
    }

    // public static void setFos(FileOutputStream fos) {this.fos = fos;}
}
