package ecg.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

import actor.MonitorActor;
import ecg.ecgshow.ECGDataRefresher;
import ecg.ecgshow.MyECGShowUI;
import actor.MainUiActor;

import javax.swing.*;


public class TCPClient extends Thread{  //跑多线程
	private Socket s;
	private InputStream is;//输入流is
	private String host;//主机域名
	private int port;   //主机端口号
	private String Id;	//档案ID
	private String Name;//姓名
	private String Sex;	//性别
	private ECGDataRefresher ecgDataRefresher;

	private byte[] receivedBuffer = new byte[40000];//接收缓存，40000个字节
	private byte[] receivedTemp = new byte[1024];   //接收临时变量，1024个字节
	public boolean connectFlag = false;//布尔变量型 连接标志，初始时设为false没有连接
	public boolean stopFlag = false;   //布尔变量型 停止标志，初始时设为false 连接依旧存在
	private MyECGShowUI myECGShowUI;   //图形化界面

	public Socket getS() {	//获得socket
		return s;
	}
	public void setS(Socket s) {  //给TCPClient下的Socket类的实例s赋值
		this.s = s;
	}

	public InputStream getIs() {	//获得输入流is
		return is;
	}
	public void setIs(InputStream is) {	//给TCPClient下的InputStream类的实例is赋值
		this.is = is;
	}

	public String getHost() {		//获得主机名
		return host;
	}
	public void setHost(String host) {	//给TCPClient下的String类的实例host赋值
		this.host = host;
	}

	public int getPort() {		//获得端口
		return port;
	}
	public void setPort(int port) {	//给TCPClient下的int类的port赋值
		this.port = port;
	}

	public String getID(){return Id;}
	public void setID(String Id){this.Id=Id;}
	public String getNAME(){return Name;}

	public void setNAME(String Name){this.Name=Name;}

	public String getSEX(){return Sex;}
	public void setSEX(String Sex){this.Sex=Sex;}

	public MyECGShowUI getMyECGShowUI(){return myECGShowUI;}

	public TCPClient(ECGDataRefresher ecgDataRefresher){
		this.ecgDataRefresher=ecgDataRefresher;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		connect();
		startReceive();
		try {
			//is.close();
			s.shutdownInput();
			s.close();
			System.out.println("关闭成功！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void connect(){
		try {
			s = new Socket(host, port);	//用host和 port初始化Socket类的s变量
			connectFlag = true;		//	连接标志为true
         /* Create and display the form */  //创建并且显示图表
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
//					if (myECGShowUI == null) {
//						myECGShowUI = new MyECGShowUI("ecg", 5000L);
//						myECGShowUI.setBorder(BorderFactory.createEmptyBorder());
//
//					}
					myECGShowUI.setBorder(BorderFactory.createEmptyBorder());
//					mainUiActor.getECGData().add(myECGShowUI.getpanel_charts());
//					mainUiActor.getECGAnalyse().add(myECGShowUI.getecgPanel());
					//mainUiActor.getMainUi().setVisible(true);
				}
			});
		} catch (UnknownHostException e) {		//public void connect()对应的异常处理。未知的主机异常
			// TODO Auto-generated catch block
			e.printStackTrace();	//打印栈的跟踪
		} catch (IOException e) {	//IO异常
			// TODO Auto-generated catch block
			e.printStackTrace();	//打印栈的跟踪
		}
		try {		//处理InputStream的异常
			is = s.getInputStream();	//把Socket的实例s获得的输入流给InputStream类型的is
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void startReceive(){		//开始接收,startReceive方法
		int len = 0;	//初始化长度为0
		int lenTemp;	//长度len的临时变量
		while(true){		//这个方法就是做这个while循环
			//System.out.print(stopFlag);
			if(stopFlag){		//如果停止传输
				try {
					MonitorActor.getFos().close();	//关闭（信息获取？）
					myECGShowUI.setVisible(false);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;	//停止传输的话就跳出循环
			}
			try {		//如果没有停止传输
				lenTemp = is.read(receivedTemp);	//读入字节流,private byte[] receivedTemp = new byte[1024];   //接收临时变量，1024个字节
				System.arraycopy(receivedTemp, 0, receivedBuffer, len,
						lenTemp);	//System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length)
				len = lenTemp + len;	//用lenTemp更新receivedBuffer的长度len
				while (len >= 3 && len >= ((receivedBuffer[2] & 0xFF)*256 + (receivedBuffer[1] & 0xFF)+3)) {	//不知道是什么意思
					int frameLen = (receivedBuffer[2] & 0xFF)*256 + (receivedBuffer[1] & 0xFF)+3;	//帧长度frameLen

					if(receivedBuffer[0]==0x01){
				//!!!		MonitorActor.getFos().write(receivedBuffer, 16, 3000);
						ecgDataRefresher.refreshData(Arrays.copyOfRange(receivedBuffer, 16, 3016));
					}
					System.out.println(len);
					System.out.println(frameLen);
					System.arraycopy(receivedBuffer, frameLen, receivedBuffer, 0, len - frameLen);	//更新receivedBuffer
					len = len - frameLen;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
