package actor.config;

import actor.BlackHoleActor;
import config.ConfigCenter;

import java.io.*;

/**
 * Created by zzq on 16/5/16.
 */
public class GuardActorConfig {
    Integer GUARD_THREAD_POOL_SIZE;
    private BlackHoleActor blackHoleActor;
    private int serialPortNum=6;
    private int temperatureLow=25;
    private int temperatureHigh=38;
    private int defaultLightValue=950;  //正常阈值
    private int emptyLightValue=500;  //空管阈值
    private int bloodLightValue=900;  //漏血阈值
    private int bubbleLightValue=20; //气泡差值
    private int bubbleHoldCount=1;   //最大气泡阈值
    public GuardActorConfig(){
        String filename = ConfigCenter.getString("guard.Config.save");
        File file=new File(filename);
        BufferedReader bufferedReader=null;
        if(file.exists()) {
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                serialPortNum=Integer.parseInt(bufferedReader.readLine());
                temperatureLow=Integer.parseInt(bufferedReader.readLine());
                temperatureHigh=Integer.parseInt(bufferedReader.readLine());
                defaultLightValue=Integer.parseInt(bufferedReader.readLine());
                emptyLightValue=Integer.parseInt(bufferedReader.readLine());
                bloodLightValue=Integer.parseInt(bufferedReader.readLine());
                bubbleLightValue=Integer.parseInt(bufferedReader.readLine());
                bubbleHoldCount=Integer.parseInt(bufferedReader.readLine());
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            FileWriter fileWriter=null;
            try {
                file.createNewFile();
                fileWriter = new FileWriter(filename);
                fileWriter.write(String.valueOf(serialPortNum) + "\r\n");
                fileWriter.write(String.valueOf(temperatureLow) + "\r\n");
                fileWriter.write(String.valueOf(temperatureHigh) + "\r\n");
                fileWriter.write(String.valueOf(defaultLightValue) + "\r\n");
                fileWriter.write(String.valueOf(emptyLightValue) + "\r\n");
                fileWriter.write(String.valueOf(bloodLightValue) + "\r\n");
                fileWriter.write(String.valueOf(bubbleLightValue) + "\r\n");
                fileWriter.write(String.valueOf(bubbleHoldCount) + "\r\n");
            }catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
//    private int normalHoldCount=10;

    public void  setSerialPortNum(int serialPortNum){
        this.serialPortNum=serialPortNum;
    }
    public int getSerialPortNum(){
        return  this.serialPortNum;
    }
    public BlackHoleActor getBlackHoleActor(){
        return blackHoleActor;
    }
    public void setBlackHoleActor(BlackHoleActor blackHoleActor){
        this.blackHoleActor=blackHoleActor;
    }
    public void setTemperatureLow(int temperatureLow){
        this.temperatureLow=temperatureLow;
    }
    public int getTemperatureLow(){
        return temperatureLow;
    }
    public void setTemperatureHigh(int temperatureHigh){
        this.temperatureHigh=temperatureHigh;
    }
    public int getTemperatureHigh(){
        return temperatureHigh;
    }
    public void setDefaultLightValue(int defaultLightValue){
        this.defaultLightValue=defaultLightValue;
    }
    public int getDefaultLightValue(){
        return defaultLightValue;
    }
    public void setBloodLightValue(int bloodLightValue){
        this.bloodLightValue=bloodLightValue;
    }
    public int getBloodLightValue(){
        return bloodLightValue;
    }
    public void setBubbleLightValue(int bubbleLightValue){
        this.bubbleLightValue=bubbleLightValue;
    }
    public int getBubbleLightValue(){
        return bubbleLightValue;
    }
    public int getEmptyLightValue(){
        return emptyLightValue;
    }
    public void setEmptyLightValue(int emptyLightValue){
        this.emptyLightValue=emptyLightValue;
    }
    public void setBubbleHoldCount(int bubbleHoldCount){
        this.bubbleHoldCount=bubbleHoldCount;
    }
    public int getBubbleHoldCount(){
        return bubbleHoldCount;
    }
//    public void setNormalHoldCount(int normalHoldCount){
//        this.normalHoldCount=normalHoldCount;
//    }
//    public int getNormalHoldCount(){
//        return normalHoldCount;
//    }

}
