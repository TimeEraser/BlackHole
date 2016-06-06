package ecg.ecgshow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;

import org.jfree.chart.axis.DateAxis;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;

//数据刷新
public class ECGDataRefresher extends Observable implements Observer{
	public static final int SAMPLING=500;		//取样时间
	public static final int MAX_TIME=1;			//最大时间
	public static final int DATA_LENGTH=SAMPLING*MAX_TIME;//数据长度
	public static final int REFRESH_TIME=50;	//刷新时间50ms
	
	private Timer timer;					//定时器
	private ScheduledExecutorService scheduledExecutorService;
	private TimeSeries[] ecgSerises;		//时间序列数组
	private DateAxis[] dateAxises;          //时间轴

	private TimeSeries[] SystolicPressureSeries;
	private TimeSeries[] DiastolicPressureSeries;
	private DateAxis[] PressuredateAxises;

	private String filePath;				//文件路径
	private Millisecond time;				//几个毫秒

	private short[][] datas=new short[ECGShowUI.LEAD_COUNT][DATA_LENGTH];	//短整型二维数组
	private  short[] SystolicPressuredatas=new short[1];
	private  short[] DiastolicPressuredatas=new short[1];
	private  short[] HeartRatedatas=new short[2];
	private  short[] BloodOxygendatas=new short[2];


	private volatile int currentPoint=2;	//用于多线程的变量，现在的点
	private volatile int currentPoint2=2;
	private volatile int currentPoint3=2;

	public boolean isStopFlag() {
		return stopFlag;
	}

	public void setStopFlag(boolean stopFlag) {
		this.stopFlag = stopFlag;
	}

	private boolean stopFlag=true;			//停止标志
	private boolean everStop=false;

	private ECGOtherData ecgOtherData;


	public ECGDataRefresher(TimeSeries[] ecgSerises, String filePath){
		this.ecgSerises=ecgSerises;
		this.filePath=filePath;
		timer=new Timer();
	}
	public ECGDataRefresher(TimeSeries[] ecgSerises, DateAxis[] dateAxises,
							TimeSeries[] SystolicPressureSeries, TimeSeries[] DiastolicPressureSeries,
							DateAxis[] PressuredateAxises,short[] HeartRatedatas,short[] BloodOxygendatas
							)
	{
		this.ecgSerises=ecgSerises;
		this.dateAxises=dateAxises;
		this.SystolicPressureSeries=SystolicPressureSeries;
		this.DiastolicPressureSeries=DiastolicPressureSeries;
		this.PressuredateAxises=PressuredateAxises;
		this.HeartRatedatas=HeartRatedatas;
		this.BloodOxygendatas=BloodOxygendatas;
		this.scheduledExecutorService= Executors.newSingleThreadScheduledExecutor();
		//this.ecgOtherData=ecgOtherData;
		timer=new Timer();
	}
	public void cancel(){
		timer.cancel();		//定时任务执行过程中止
	}
	 
	public void startWithFile(){
		//datas=readFileByBytes(filePath);
		timer.scheduleAtFixedRate(new ReadFileDataTask(), 0, MAX_TIME*1000);
		time=new Millisecond(new Date(57600000L));

	}
	
	public void start(){
		time=new Millisecond();
		scheduledExecutorService.scheduleAtFixedRate(new AddToShowTask(), 0, 50, TimeUnit.MILLISECONDS);
	}
	
	private short[][] readFileByBytes(String fileName) {
		//三导联。每导联10秒，采样率为500/s
		short[][] res=new short[ECGShowUI.LEAD_COUNT][DATA_LENGTH];
        InputStream in = null;
        
        try {
        	//每次读三导联的一秒
        	byte[] tempbytes = new byte[SAMPLING* ECGShowUI.LEAD_COUNT*2];
            in = new FileInputStream(fileName);
            int num=0;
            int numOfBytes;
            while ((numOfBytes=in.read(tempbytes))!= -1) {
            	for (int i = 0; i < ECGShowUI.LEAD_COUNT; i++) {
            		for (int j = i*numOfBytes/ ECGShowUI.LEAD_COUNT; j < (i+1)*numOfBytes/ ECGShowUI.LEAD_COUNT; j+=2) {
            			res[i][num+(j-i*numOfBytes/ ECGShowUI.LEAD_COUNT)/2]=(short)(((tempbytes[j]&0xff)<<8)|(tempbytes[j+1]&0xff));
            		}
				}
            	num+=numOfBytes/ ECGShowUI.LEAD_COUNT/2;
            }
            
        }catch (FileNotFoundException e){
        	System.out.println("指定文件路径不存在");
        }
        catch (ArrayIndexOutOfBoundsException e){
        	System.out.println("输入文件过长，超出预定格式！");
        }
        catch (IOException e) {
			System.out.println("文件流异常");
		}
        //关闭输入流
        finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                	System.out.println("文件流关闭失败");
                	e.printStackTrace();
                }
            }
         
        }
        return res;
    }
	
	public void refreshData(byte[] message){
		int numOfBytes=message.length;
		for (int i = 0; i < ECGShowUI.LEAD_COUNT; i++) {
    		for (int j = i*numOfBytes/ ECGShowUI.LEAD_COUNT; j < (i+1)*numOfBytes/ ECGShowUI.LEAD_COUNT; j+=2) {
    			datas[i][(j-i*numOfBytes/ ECGShowUI.LEAD_COUNT)/2]=(short)(((message[j]&0xff)<<8)|(message[j+1]&0xff));
    		}
		}
	}




	public void refreshHeartRate(byte[] message){
		HeartRatedatas[1]=(short)(((message[0])<<8)|(message[1]&0xff));

	}

	public void refreshSystolicPressure(byte[] message){
		SystolicPressuredatas[0]=(short)(((message[0])<<8)|(message[1]&0xff));
	}

	public void refreshDiastolicPressure(byte[] message){

		DiastolicPressuredatas[0]=(short)(((message[0])<<8)|(message[1]&0xff));
	}

	public void refreshBloodOxygen(byte[] message){
		BloodOxygendatas[1]=(short)message[0];
	}




	public void setEcgOtherData(ECGOtherData ecgOtherData){this.ecgOtherData=ecgOtherData;}
	public ECGOtherData getEcgOtherData(){return this.ecgOtherData;}
	
	/**
	 * 显示必须采用降采样的方法，
	 * @author Administrator
	 *
	 */
	class AddToShowTask implements Runnable{
		private static final int LOWSAMPLE=5;
		private long upperBound=0;
		private Millisecond time;
		@Override
		public void run() {
			long currentTimeMillis =System.currentTimeMillis();
			time = new Millisecond(new Date(currentTimeMillis));
			if(stopFlag==false) {

				if (currentTimeMillis > upperBound) {
					for (DateAxis d :
							dateAxises) {
						d.setRange(new Date(currentTimeMillis), new Date(currentTimeMillis + 5000));
					}
					upperBound = currentTimeMillis + 5000;
				}
			}
			if(datas!=null){
				for(int i=0;i<REFRESH_TIME/LOWSAMPLE/2;i++){
					if(currentPoint< DATA_LENGTH){
						if(stopFlag==false){
							for(int j = 0; j< ECGShowUI.LEAD_COUNT; j++){
								//ecgSerises[j].addOrUpdate(time, 2000);//该方法在超过指定长度后会将最久的数据丢弃
								//ecgSerises[j+ ECGShowUI.LEAD_COUNT].addOrUpdate(time, 2000);//该方法在超过指定长度后会将最久的数据丢弃
								ecgSerises[j].addOrUpdate(time, datas[j][currentPoint]);
								ecgSerises[j+ ECGShowUI.LEAD_COUNT].addOrUpdate(time, datas[j][currentPoint]);


								currentTimeMillis+=10;
							}

							HeartRatedatas[0]=HeartRatedatas[1];

							BloodOxygendatas[0]=BloodOxygendatas[1];
							//SystolicPressureSeries[0].addOrUpdate(time, SystolicPressuredatas[0]);
							//SystolicPressureSeries[1].addOrUpdate(time, SystolicPressuredatas[0]);
							//DiastolicPressureSeries[0].addOrUpdate(time, DiastolicPressuredatas[0]);
							//DiastolicPressureSeries[1].addOrUpdate(time, DiastolicPressuredatas[0]);

							SystolicPressureSeries[0].addOrUpdate(time, 40);
							SystolicPressureSeries[1].addOrUpdate(time, 40);
							DiastolicPressureSeries[0].addOrUpdate(time, 20);
							DiastolicPressureSeries[1].addOrUpdate(time, 20);

						}
						time= new Millisecond(new Date(currentTimeMillis));
						currentPoint+=LOWSAMPLE;
					}
					else {
						currentPoint=2;
					}
				}
			}
			else{
				System.out.println("数据尚未准备好！");
			}


		}	//run()  end
	}

	class ReadFileDataTask extends TimerTask{

		@Override
		public void run() {
			datas=readFileByBytes(filePath);
			setChanged();// 设置变化点
			System.out.println("通知分析数据");
		    notifyObservers(datas);// datas被改变
		}
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		System.out.println("被通知到");
		byte[] message=(byte[])arg;
		System.out.println("传递数据"+message[0]+message[1]);
		refreshData(message);
		System.out.println("获取数据"+datas[0]+" "+datas[1]);
		System.out.println("已更新");
		
	}


	public void setStopFlag(){
		stopFlag=true;
		everStop=true;
	}
	public void setStartFlag(){
		stopFlag=false;
	}


}
