package ecg.model;
//同步对象
public class SyncObject {
	String surgery_no;		//手术编号
	String time_stamp;		//时间标记
	public SyncObject(String surgery_no,String time_stamp) {	//构造方法SyncObject
		this.surgery_no=surgery_no;
		this.time_stamp=time_stamp;
	}
}
