package actor;


import actor.config.GuardActorConfig;
import actor.fileOperation.AlarmDataRefresh;
import actor.fileOperation.TemperatureDataRefresh;
import actor.guard.TemperatureShow;
import actor.serialPort.GuardSerialDataProcess;
import actor.serialPort.SerialComm;
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
    public GuardActorConfig guardActorConfig;
    public TemperatureDataRefresh temperatureDataRefresh;
    public AlarmDataRefresh alarmDataRefresh;
    private byte readFlag=0;
    private byte[] data = new byte[2];
    private static Request startRequest;
    private GuardSerialDataProcess guardSerialDataProcess;
    private TemperatureShow guardShow;

    public GuardActor(GuardActorConfig guardActorConfig){
        RealTime realTime=new RealTime();
        String time=realTime.getRealTime().substring(0,realTime.getRealTime().indexOf(' '));
        File cataloguePath= new File(".\\data");
        File temperaturePath=new File(".\\data\\temperature");
        File alarmMessagePath=new File(".\\data\\alarm");
        File temperatureDataFile=new File(".\\data\\temperature\\"+time+".txt");
        File alarmMessageDataFile=new File(".\\data\\alarm\\"+time+".txt");
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
        if (requests == MainUiRequest.MAIN_UI_GUARD_START) {
            System.out.println("GuardRequest.GUARD_START");
            startRequest=requests;
            if(!start()){
                sendResponse(requests,GuardResponse.GUARD_ERROR);
            }

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
        if (requests == GuardRequest.GUARD_ERROR) {
            System.out.println("GUARD_ERROR");
        }
        //完整读取2字节后处理
        if (readFlag >= 2) {
            //返回值为1时漏血，返回值为2时气泡
            try {
                switch (guardSerialDataProcess.process(data)){
                   case 1:{
                        sendRequest(startRequest.getConfig().getSendActor(), GuardRequest.GUARD_BLOOD_LEAK);
                        System.out.println("BLOOD");
                        break;
                    }
                    case 2:{
                        sendRequest(startRequest.getConfig().getSendActor(),GuardRequest.GUARD_BUBBLE);
                        System.out.println("Bubble");
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
                if (portId.getName().equals(winSerialPort)) {
            /*找Unix-like系统下的第一个串口*/
                    //if (portId.getName().equals(unixSerialPort)) {
                    SerialComm serialComm = new SerialComm(this,portId,portList,GuardRequest.GUARD_DATA);
                    successFlag=true;
                }
            }
        }
        return successFlag;
    }

    @Override
    public boolean shutdown() {
        return false;
    }
}





