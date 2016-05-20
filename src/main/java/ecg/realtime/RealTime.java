package ecg.realtime;

import java.util.Calendar;
//获得当前时间
public class RealTime {
	private Calendar realTime = Calendar.getInstance();
	
	public String getRealTime(){
		String month,day;
		int i;
		if((i=realTime.get(Calendar.MONTH)+1)<10)
			month = "0"+i;		//让月份保持2位数
		else month = ""+i;
		if((i=realTime.get(Calendar.DAY_OF_MONTH))<10)
			day = "0"+i;		//让天数保持2位数
		else day = ""+i;
		return realTime.get(Calendar.YEAR)+"-"+month+"-"+day+" "+getHMS();
	}
	
	public String getHMS(){		//获得小时、分钟、秒
		String hour,minute,second;
		int i;
		if((i=realTime.get(Calendar.HOUR_OF_DAY))<10)
			hour = "0"+i;
		else hour = ""+i;
		if((i=realTime.get(Calendar.MINUTE))<10)
			minute = "0"+i;
		else minute = ""+i;
		if((i=realTime.get(Calendar.SECOND))<10)
			second = "0"+i;
		else second = ""+i;
		return hour+":"+minute+":"+second;
	}
}
