package guard.guardDataProcess;

import actor.BaseActor;
import command.GuardRequest;
import command.Request;
import command.Response;
import gnu.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

/**
 * Created by xuda on 2016/5/25/025.
 */
public class SerialComm extends BaseActor implements SerialPortEventListener,Runnable {
    private CommPortIdentifier portId;
    private InputStream inputStream;
    private OutputStream outputStream;
    private SerialPort serialPort;
    private Thread readThread;
    private BaseActor commActor;
    private Request commRequest;
    private boolean successFlag=true;
    public SerialComm(BaseActor commActor,CommPortIdentifier portId,Request commRequest){
        this.commActor=commActor;
        this.portId=portId;
        this.commRequest=commRequest;
        try {
/* open方法打开通讯端口，获得一个CommPort对象。它使程序独占端口。如果端口正被其他应用程序占用，将使用
CommPortOwnershipListener事件机制，传递一个PORT_OWNERSHIP_REQUESTED事件。每个端口都关联一个
InputStream和一个OutputStream。如果端口是用open方法打开的，那么任何的getInputStream都将返回
相同的数据流对象，除非有close被调用。有两个参数，第一个为应用程序名；第二个参数是在端口打开
时阻塞等待的毫秒数。 */
            serialPort = (SerialPort) portId.open("GuardRead", 2000);
        } catch (PortInUseException e) {}
        if(serialPort!=null) {
            try {
            /*获取端口的输入流对象*/
                inputStream = serialPort.getInputStream();
                outputStream=serialPort.getOutputStream();
            } catch (IOException e) {
            }
            try {
        /*注册一个SerialPortEventListener事件来监听串口事件*/
                serialPort.addEventListener(this);
            } catch (TooManyListenersException e) {
            }
        /*数据可用*/
            serialPort.notifyOnDataAvailable(true);
            try {
       /*设置串口初始化参数，依次是波特率，数据位，停止位和校验*/
                serialPort.setSerialPortParams(9600,
                        SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1,
                        SerialPort.PARITY_NONE);
            } catch (UnsupportedCommOperationException e) {
            }
            readThread = new Thread(this);
            readThread.start();
        }
        else {
            successFlag=false;
        }
    }
    protected boolean processActorRequest(Request requests) {
        if(requests==GuardRequest.GUARD_ALARM){
            byte [] buffer=new byte[1];
            buffer[0]=(byte)0xf0;
            try {
                outputStream.write(buffer);
                outputStream.flush();
            }
            catch (IOException e){
            }
        }
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
    public boolean getSuccessFlag(){
        return successFlag;
    }

    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {}
    }
    public void stopRun(){
        readThread.interrupt();
        serialPort.close();
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
                sendRequest(commActor, GuardRequest.GUARD_ERROR);
                break;
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:/*Output buffer is empty，输出缓冲区清空*/
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
                        sendRequest(commActor,commRequest,readBuffer[iii]);
                    }
                } catch (IOException e) {}
                break;
        }
    }

}