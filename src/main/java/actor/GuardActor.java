package actor;

import actor.config.GuardActorConfig;
import command.Request;
import command.Response;

import command.GuardRequest;
import command.GuardResponse;

import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.io.*;
import java.util.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

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
                    SerialComm serialComm = new SerialComm(this,portId,portList);
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


class SerialComm extends BaseActor implements SerialPortEventListener,Runnable {
    protected static CommPortIdentifier portId;
    protected static Enumeration portList;
    protected InputStream inputStream;
    protected SerialPort serialPort;
    protected Thread readthread;
    protected BaseActor commActor;
    public SerialComm(BaseActor commActor,CommPortIdentifier portId,Enumeration portList){
        this.commActor=commActor;
        this.portId=portId;
        this.portList=portList;
        try {
/* open方法打开通讯端口，获得一个CommPort对象。它使程序独占端口。如果端口正被其他应用程序占用，将使用
CommPortOwnershipListener事件机制，传递一个PORT_OWNERSHIP_REQUESTED事件。每个端口都关联一个
InputStream和一个OutputStream。如果端口是用open方法打开的，那么任何的getInputStream都将返回
相同的数据流对象，除非有close被调用。有两个参数，第一个为应用程序名；第二个参数是在端口打开
时阻塞等待的毫秒数。 */
            serialPort = (SerialPort) portId.open("GuardRead", 2000);
        } catch (PortInUseException e) {}
        try {
            /*获取端口的输入流对象*/
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {}
        try {
        /*注册一个SerialPortEventListener事件来监听串口事件*/
            serialPort.addEventListener(this);
        } catch (TooManyListenersException e) {}
        /*数据可用*/
        serialPort.notifyOnDataAvailable(true);
        try {
       /*设置串口初始化参数，依次是波特率，数据位，停止位和校验*/
            serialPort.setSerialPortParams(9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {}
        readthread = new Thread(this);
        readthread.start();
    }
    protected boolean processActorRequest(Request requests) {
        return false;
    }

    protected boolean processActorResponse(Response responses) {
        return false;
    }

    public boolean start() {
        return false;
    }

    public boolean shutdown() {
        return false;
    }

    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {}
    }

    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch(serialPortEvent.getEventType()) {
            case SerialPortEvent.BI:/*Break interrupt,通讯中断*/
            case SerialPortEvent.OE:/*Overrun error，溢位错误*/
            case SerialPortEvent.FE:/*Framing error，传帧错误*/
            case SerialPortEvent.PE:/*Parity error，校验错误*/
            case SerialPortEvent.CD:/*Carrier detect，载波检测*/
            case SerialPortEvent.CTS:/*Clear to send，清除发送*/
            case SerialPortEvent.DSR:/*Data set ready，数据设备就绪*/
            case SerialPortEvent.RI:/*Ring indicator，响铃指示*/
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty，输出缓冲区清空*/
                sendRequest(commActor,GuardRequest.GUARD_ERROR);
                break;
            case SerialPortEvent.DATA_AVAILABLE:/*Data available at the serial port，端口有可用数据。读到缓冲数组，输出到终端*/
                byte[] readBuffer = new byte[20];
                int numBytes=0;
//                boolean alarmBlood=false;//漏血警报标志
//                boolean alarmBubble=false;//气泡警报标志
//                boolean readFlag=false;
                try {
                    while (inputStream.available() > 1) {
                        numBytes = inputStream.read(readBuffer);
                    }
                    for (int iii = 0; iii < numBytes; iii++) {
                        sendRequest(commActor,GuardRequest.GUARD_DATA,readBuffer[iii]);
                    }
                } catch (IOException e) {}
                break;
        }
    }
}