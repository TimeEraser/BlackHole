package ecg.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jfree.data.time.Second;
//计划、体系
public class Scheme {
	public static final DateFormat dateFormat=new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//日期格式的常量dateFormat，并用简单日期格式SimpleDateFormat初始化
	public String solutionName;		//结果名字
	public Second  startTime;		//Second类的开始时间
	public Scheme(String solutionName,String start ) throws ParseException{	//构造方法Scheme，把异常抛给调用它的类
		Date date = dateFormat.parse(start);
		this.startTime=new Second(date);
		this.solutionName=solutionName;
	}
}
