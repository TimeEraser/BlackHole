package ecg.ecgshow;

/**
 * Created by xuda on 2016/5/28/028.
 */
public class ECGOtherData {
    private String heart_rate;			//心率
    private String systolic_pressure;	//收缩压
    private String diastolic_pressure;	//舒张压
    private String blood_oxygen;		//血氧


    public String getHeart_rate(){return heart_rate;}
    public void setHeart_rate(String heart_rate){this.heart_rate=heart_rate;}

    public String getSystolic_pressure(){return systolic_pressure;}
    public void setSystolic_pressure(String systolic_pressure){this.systolic_pressure=systolic_pressure;}

    public String getDiastolic_pressure(){return diastolic_pressure;}
    public void setDiastolic_pressure(String diastolic_pressure){this.diastolic_pressure=diastolic_pressure;}

    public String getBlood_oxygen(){return blood_oxygen;}
    public void setBlood_oxygen(String blood_oxygen){this.blood_oxygen=blood_oxygen;}

}
