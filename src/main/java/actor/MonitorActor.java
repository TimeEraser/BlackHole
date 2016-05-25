package actor;

import command.*;
import actor.config.MonitorActorConfig;
import ecg.tcp.*;

import javax.swing.*;
import java.io.FileOutputStream;

/**
 * Created by zzq on 16/5/16.
 */
public class MonitorActor extends BaseActor{

    private static FileOutputStream fos;
    private  static TCPConfig TCPC;
    private TCPClient client;

    private String host=null;   //主机域名
    private String port=null;   //主机端口号
    private String Id=null;	    //档案ID
    private String Name=null;   //姓名
    private String Sex=null;	//性别
    private Object data;


    public MonitorActor(MonitorActorConfig monitorActorConfig){
        //TO DO Initialize the MonitorActor
    }

    @Override
    protected boolean processActorRequest(Request request) {
        if(request== MainUiRequest.MAIN_UI_ECG_CONFIG){
            data=request.getConfig().getData();
            TCPC = new TCPConfig( (JFrame)data,true);
            TCPC.setVisible(true);
            host=TCPC.getjTextField1().getText();
            port=TCPC.getjTextField2().getText();
            Id=TCPC.getjTextField3().getText();
            Name=TCPC.getjTextField4().getText();
            Sex=TCPC.getJRadioButtonName();

            if((host!=null)&&(port!=null)) {
                System.out.println("MonitorActor: "+host);
                System.out.println("MonitorActor: "+port);
                System.out.println("MonitorActor: "+Id);
                System.out.println("MonitorActor: "+Name);
                System.out.println("MonitorActor: "+Sex);
                System.out.println("MonitorRequest.MONITOR_ECG_DATA");

                client = new TCPClient();        //新建一个TCPClient()方法的实例client
                client.setHost(host);    //设置主机
                client.setPort(Integer.parseInt(port));    //设置端口
                client.setID(Id);
                client.setNAME(Name);
                client.setSEX(Sex);

                client.stopFlag = false;
                client.setMainUiActor((MainUiActor) request.getConfig().getSendActor());
                client.start();        //客户端线程开始运行
            }
        }

        if(request==MainUiRequest.MAIN_UI_ECG_STOP){
            client.stopFlag =true;
            sendResponse(request,MonitorResponse.MONITOR_SHUTDOWM);
            System.out.println("client.stopFlag =true");
        }

        if(request==MainUiRequest.MAIN_UI_ECG_START){
            client.getMyECGShowUI().getDataReFresher().setstopFlag();
        }

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

    public  static TCPConfig getTCPC() {
        return TCPC;
    }
    public  static FileOutputStream getFos() {
        return fos;
    }
    // public static void setFos(FileOutputStream fos) {this.fos = fos;}
}
