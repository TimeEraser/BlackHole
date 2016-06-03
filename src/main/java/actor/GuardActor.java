package actor;


import actor.config.GuardActorConfig;
import config.ConfigCenter;
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
    private byte readFlag=0;
    private byte[] data = new byte[4];
    private GuardSerialDataProcess guardSerialDataProcess;
    private SerialComm serialComm;

    public GuardActor(GuardActorConfig guardActorConfig){
        RealTime realTime=new RealTime();
        String time=realTime.getRealTime().substring(0,realTime.getRealTime().indexOf(' '));
        File cataloguePath= new File(ConfigCenter.getString("guard.data.root"));
        File temperaturePath=new File(ConfigCenter.getString("guard.data.temperature.root"));
        File alarmMessagePath=new File(ConfigCenter.getString("guard.data.alarm.root"));
        File temperatureDataFile=new File(temperaturePath.getAbsolutePath()+"/"+time+".txt");
        File alarmMessageDataFile=new File(alarmMessagePath.getAbsolutePath()+"/"+time+".txt");
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
            guardSerialDataProcess=new GuardSerialDataProcess(temperatureDataFile,alarmMessageDataFile,guardActorConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //TO DO Initialize the GuardActor
    }
    @Override
    protected boolean processActorRequest(Request requests) {
        if (requests == GuardRequest.GUARD_START) {
            System.out.println("GuardRequest.GUARD_START");
            if(!start()){
                sendResponse(requests,SystemResponse.SYSTEM_FAILURE,"请检查报警模块及配置");
            }
            else {
                sendResponse(requests,SystemResponse.SYSTEM_SUCCESS);
            }
        }
        if(requests==GuardRequest.GUARD_SHUT_DOWN){
            shutdown();
            sendResponse(requests,SystemResponse.SYSTEM_SUCCESS);
        }
        if(requests==GuardRequest.GUARD_SERIAL_NUM){
            guardActorConfig.setSerialPortNum((Integer)requests.getConfig().getData());
        }
        if (requests == GuardRequest.GUARD_DATA) {
            byte temp;
            temp = (Byte) (requests.getConfig().getData());
            if ((temp > 0) && ((temp & 0x40) == 0)) {
                data[0] = temp;
                readFlag += 1;
            } else if ((temp > 0) && ((temp & 0x40) != 0)) {
                data[1] = temp;
                readFlag += 1;
            } else if ((temp < 0) && ((temp & 0x40) == 0)) {
                data[2] = temp;
                readFlag += 1;
            } else if ((temp < 0) && ((temp & 0x40) != 0)) {
                data[3] = temp;
                readFlag += 1;
            }

            //完整读取4字节后处理
            if (readFlag >= 4) {
                //返回值为1时漏血，返回值为2时气泡
                try {
                    short tempData = guardSerialDataProcess.process(data);
                    if (tempData % 2 == 1) {
                        sendRequest(guardActorConfig.getBlackHoleActor(), MainUiRequest.MAIN_UI_GUARD_BLOOD_LEAK);
                    }
                    if (tempData % 4 / 2 == 1) {
                        sendRequest(guardActorConfig.getBlackHoleActor(), MainUiRequest.MAIN_UI_GUARD_BUBBLE);
                    }
                    if (tempData % 8 / 4 == 1) {
                        sendRequest(guardActorConfig.getBlackHoleActor(), MainUiRequest.MAIN_UI_GUARD_TEMPERATURE_LOW);
                    }
                    if (tempData % 16 / 8 == 1) {
                        sendRequest(guardActorConfig.getBlackHoleActor(), MainUiRequest.MAIN_UI_GUARD_TEMPERATURE_HIGH);
                    }
                    readFlag = 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        String winSerialPort="COM"+String.valueOf(guardActorConfig.getSerialPortNum());
        String unixSerialPort="/dev/term/"+String.valueOf((char)(guardActorConfig.getSerialPortNum()+64));
        System.out.println(winSerialPort);
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
        if (successFlag&&(!serialComm.getSuccessFlag())){
            successFlag=false;
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

    public GuardSerialDataProcess getGuardSerialDataProcess(){
        return guardSerialDataProcess;
    }
}