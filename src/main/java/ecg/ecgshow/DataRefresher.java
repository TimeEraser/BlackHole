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

import javax.swing.JButton;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;

//数据刷新
//没有main方法
public class DataRefresher extends Observable implements Observer,ActionListener {
	public static final int SAMPLING=500;		//取样时间
	public static final int MAX_TIME=1;			//最大时间
	public static final int DATA_LENGTH=SAMPLING*MAX_TIME;//数据长度
	public static final int REFRESH_TIME=50;	//刷新时间
	
	private Timer timer;					//定时器
	private TimeSeries[] ecgSerises;		//时间序列数组
	private String filePath;				//文件路径
	private Millisecond time;				//几个毫秒
	private short[][] datas=new short[MyECGShowUI.LEAD_COUNT][DATA_LENGTH];	//短整型二维数组
	private volatile int currentPoint=2;	//用于多线程的变量，现在的点
	private boolean stopFlag=false;			//停止标志


	public DataRefresher(TimeSeries[] ecgSerises,String filePath){	//构造方法
		this.ecgSerises=ecgSerises;
		this.filePath=filePath;
		timer=new Timer();
	}
	public DataRefresher(TimeSeries[] ecgSerises){	//构造方法
		this.ecgSerises=ecgSerises;
		timer=new Timer();
	}
	public void cancel(){
		timer.cancel();		//定时任务执行过程中止
	}
	 
	public void startWithFile(){
		//datas=readFileByBytes(filePath);
		timer.scheduleAtFixedRate(new ReadFileDataTask(), 0, MAX_TIME*1000);
		time=new Millisecond(new Date(57600000L));
		timer.scheduleAtFixedRate(new AddToShowTask(), 0, REFRESH_TIME);			
	}
	
	public void start(){
		time=new Millisecond();
		timer.schedule(new AddToShowTask(), 0, REFRESH_TIME);
	}
	
	
	private short[][] readFileByBytes(String fileName) {
		//三导联。每导联10秒，采样率为500/s
		short[][] res=new short[MyECGShowUI.LEAD_COUNT][DATA_LENGTH];
        InputStream in = null;
        
        try {
        	//每次读三导联的一秒
        	byte[] tempbytes = new byte[SAMPLING*MyECGShowUI.LEAD_COUNT*2];
            in = new FileInputStream(fileName);
            int num=0;
            int numOfBytes;
            while ((numOfBytes=in.read(tempbytes))!= -1) {
            	for (int i = 0; i < MyECGShowUI.LEAD_COUNT; i++) {
            		for (int j = i*numOfBytes/MyECGShowUI.LEAD_COUNT; j < (i+1)*numOfBytes/MyECGShowUI.LEAD_COUNT; j+=2) {
            			res[i][num+(j-i*numOfBytes/MyECGShowUI.LEAD_COUNT)/2]=(short)(((tempbytes[j]&0xff)<<8)|(tempbytes[j+1]&0xff));
            		}
				}
            	num+=numOfBytes/MyECGShowUI.LEAD_COUNT/2;
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
		for (int i = 0; i < MyECGShowUI.LEAD_COUNT; i++) {
    		for (int j = i*numOfBytes/MyECGShowUI.LEAD_COUNT; j < (i+1)*numOfBytes/MyECGShowUI.LEAD_COUNT; j+=2) {
    			datas[i][(j-i*numOfBytes/MyECGShowUI.LEAD_COUNT)/2]=(short)(((message[j]&0xff)<<8)|(message[j+1]&0xff));
    		}
		}
	}
	
	/**
	 * 显示必须采用降采样的方法，
	 * @author Administrator
	 *
	 */
	class AddToShowTask extends TimerTask{
		private static final int LOWSAMPLE=5;
		@Override
		public void run() {
			if(datas!=null){
				for(int i=0;i<REFRESH_TIME/LOWSAMPLE/2;i++){
					if(currentPoint< DATA_LENGTH){
						if(stopFlag==false){
							for(int j=0;j<MyECGShowUI.LEAD_COUNT;j++){
								ecgSerises[j].add(time, datas[j][currentPoint]);//该方法在超过指定长度后会将最久的数据丢弃
							}
						}
						time=(Millisecond) time.next().next().next().next().next().next().next().next().next().next();
						currentPoint+=LOWSAMPLE;
					}
					else {
						currentPoint=2;
						//System.out.println(System.currentTimeMillis()-startTime);
						//cancel();
						//return;
					}
				}
			}
			else{
				System.out.println("数据尚未准备好！");
			}
		}	
	}

	class ReadFileDataTask extends TimerTask{

		@Override
		public void run() {
			datas=readFileByBytes(filePath);
			setChanged();
			System.out.println("通知分析数据");
		    notifyObservers(datas);
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
	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("get it");
		stopFlag=!stopFlag;
		JButton button=(JButton) e.getSource();
		String text=stopFlag?"开始":"暂停";
		button.setText(text);
	}

	public void setstopFlag(){stopFlag=!stopFlag;}


}
