package actor;

import actor.config.GuardActorConfig;
import actor.serialPort.SerialComm;
import command.Request;
import command.Response;

import command.GuardRequest;
import command.GuardResponse;

import gnu.io.*;

import java.util.Enumeration;

/**
 * Created by zzq on 16/5/16.
 */
public class GuardActor extends BaseActor {
    protected GuardActorConfig guardActorConfig;
    private BaseActor commActor;
    private static CommPortIdentifier portId;
    private static Enumeration portList;
    private short readFlag=0;
    private int temperater;
    private byte temp;
    private boolean alarmBlood;
    private boolean alarmBubble;
    private byte[] data = new byte[2];

    public GuardActor(GuardActorConfig guardActorConfig){
        this.guardActorConfig=guardActorConfig;
        portList=CommPortIdentifier.getPortIdentifiers();//读出串口列表
        //TO DO Initialize the GuardActor
    }
    @Override
    protected boolean processActorRequest(Request requests) {
        if (requests == GuardRequest.GUARD_START) {
            System.out.println("GuardRequest.GUARD_START");
            if(!start()){
                sendResponse(requests,GuardResponse.GUARD_ERROR);
            }
        }
        if(requests==GuardRequest.GUARD_SERIAL_NUM){
            guardActorConfig.setSerialPortNum((Integer)requests.getConfig().getData());
        }
        if (requests == GuardRequest.GUARD_DATA) {
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

        }
        //完整读取2字节后处理
        if (readFlag == 2) {
            serialDataProcess();
        }
        return false;
    }
    //串口数据处理
    protected void serialDataProcess(){
        readFlag = 0;
        temperater = (int) (data[0] % 32 + (data[1] & 0x7F) * 32);
        System.out.print("temperater:");
        System.out.println(temperater / 10.0);
        alarmBlood = (data[0] / 32 % 2 == 1);
        alarmBubble = (data[0] / 64 % 2 == 1);
        if(alarmBlood){
            System.out.println("BLOOD");
        }
        if(alarmBubble) {
            System.out.println("Bubble");
        }
    }

    @Override
    protected boolean processActorResponse(Response responses) {
        return false;
    }

    @Override
    public boolean start() {
        boolean successFlag=false;
        String winSerialPort="COM"+String.valueOf(guardActorConfig.readSerialPortNum());
        String unixSerialPort="/dev/term/"+String.valueOf((char)(guardActorConfig.readSerialPortNum()+64));
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


