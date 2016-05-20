package ecg.model;
//手术（操作）信息
public class OperationInfo {
	public String patientName;		//患者姓名
	public String gender;			//性别
	public String age;				//年龄
	public String treatMethod;		//治疗方法
	public String doctor;			//医生姓名
	public String time;				//时间
	public String operationNo;		//手术编号
	public String operationTime;	//手术时间
	//构造方法OperationInfo
	public OperationInfo(String patientName,String gender,String age, String treatMethod,String doctor,String operationNo,String time,String operationTime) {
		this.patientName=patientName;
		this.gender=gender;
		this.age=age;
		this.treatMethod=treatMethod;
		this.doctor=doctor;
		this.operationNo=operationNo;
		this.time=time;
		this.operationTime=operationTime;
	}
}
