package actor;


import actor.config.GuardActorConfig;
import com.sun.corba.se.spi.orbutil.fsm.Guard;
import config.ConfigCenter;
import guard.guardDataProcess.AlarmDataRefresh;
import guard.guardDataProcess.TemperatureDataRefresh;
import guard.guardshow.TemperatureShow;
import guard.guardDataProcess.GuardSerialDataProcess;
import guard.guardDataProcess.SerialComm;
import command.*;

import ecg.realtime.RealTime;
import gnu.io.*;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Created by zzq on 16/5/16.
 */
public class GuardActor extends BaseActor {
    private GuardActorConfig guardActorConfig;
    private TemperatureDataRefresh temperatureDataRefresh;
    private AlarmDataRefresh alarmDataRefresh;
    private byte readFlag=0;
    private byte[] data = new byte[2];
    private GuardSerialDataProcess guardSerialDataProcess;
    private SerialComm serialComm;

    public GuardActor(GuardActorConfig guardActorConfig){
        RealTime realTime=new RealTime();
        String time=realTime.getRealTime().substring(0,realTime.getRealTime().indexOf(' '));
        File cataloguePath= new File(ConfigCenter.getString("guard.data.root"));
        File temperaturePath=new File(ConfigCenter.getString("guard.data.temperature.root"));
        File alarmMessagePath=new File(ConfigCenter.getString("guard.data.alarm.root"));
        File temperatureDataFile=new File(temperaturePath.getAbsolutePath()+time+".txt");
        File alarmMessageDataFile=new File(alarmMessagePath.getAbsolutePath()+time+".txt");
        this.guardActorConfig=guardActorConfig;
        time=realTime.getRealTime();
        if (!cataloguePath.exists()){
            cataloguePath.mkdirs();
        }
        if (!temperaturePath.exists()){
            temperaturePath.mkdirs();
        }
        if(!alarmMessagePath.exists()){
            alarmMessagePath.mkdirs();
        }
        try {
            temperatureDataFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            alarmMessageDataFile.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            guardSerialDataProcess=new GuardSerialDataProcess(temperatureDataFile,alarmMessageDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        temperatureDataRefresh=new TemperatureDataRefresh();
        alarmDataRefresh=new AlarmDataRefresh();
        guardSerialDataProcess.addObserver(temperatureDataRefresh);
        guardSerialDataProcess.addObserver(alarmDataRefresh);
        //TO DO Initialize the GuardActor
    }
    @Override
    protected boolean processActorRequest(Request requests) {
        if (requests == GuardRequest.GUARD_START) {
            System.out.println("GuardRequest.GUARD_START");
            if(!start()){
                sendResponse(requests,MainUiResponse.MAIN_UI_GUARD_START_ERROR);
            }
        }
        if(requests==GuardRequest.GUARD_SHUT_DOWN){
            shutdown();
        }
        if(requests==GuardRequest.GUARD_SERIAL_NUM){
            guardActorConfig.setSerialPortNum((Integer)requests.getConfig().getData());
        }
        if (requests == GuardRequest.GUARD_DATA) {
            byte temp;
            temp = (Byte) (requests.getConfig().getData());
            if (temp > 0) {
                data[0] = temp;
                readFlag += 1;
            } else {
                data[1] = temp;
                readFlag += 1;
            }
        }
        //完整读取2字节后处理
        if (readFlag >= 2) {
            //返回值为1时漏血，返回值为2时气泡
            try {
                switch (guardSerialDataProcess.process(data)){
                   case 1:{
                        sendRequest(getGuardActorConfig().getBlackHoleActor(), MainUiRequest.MAIN_UI_GUARD_BLOOD_LEAK);
                        break;
                    }
                    case 2:{
                        sendRequest(getGuardActorConfig().getBlackHoleActor(),MainUiRequest.MAIN_UI_GUARD_BUBBLE);
                        break;
                    }
                    default:;
                }
                readFlag = 0;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    protected boolean processActorResponse(Response responses) {
        return false;
    }

    @Override
    public boolean start() {
        Enumeration portList;
        portList=CommPortIdentifier.getPortIdentifiers();//读出串口列表
        CommPortIdentifier portId;
        boolean successFlag=false;
        System.out.print("serialport:");
        System.out.println(guardActorConfig.getSerialPortNum());
        String winSerialPort="COM"+String.valueOf(guardActorConfig.getSerialPortNum());
        String unixSerialPort="/dev/term/"+String.valueOf((char)(guardActorConfig.getSerialPortNum()+64));
        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            /*getPortType方法返回端口类型*/
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            /* 默认找Windows下的第6个串口,即小板子连上我的电脑后的串口编号*/
                if ((portId.getName().equals(winSerialPort))||(portId.getName().equals(unixSerialPort))){
                    serialComm = new SerialComm(this,portId,GuardRequest.GUARD_DATA);
                    successFlag=true;
                }
            }
        }
        return successFlag;
    }

    @Override
    public boolean shutdown() {
        serialComm.stopRun();
        return false;
    }
    public GuardActorConfig getGuardActorConfig(){
        return guardActorConfig;
    }
    public TemperatureDataRefresh getTemperatureDataRefresh(){
        return temperatureDataRefresh;
    }
    public AlarmDataRefresh getAlarmDataRefresh(){
        return alarmDataRefresh;
    }
}





