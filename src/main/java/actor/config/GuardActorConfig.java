package actor.config;

import actor.BlackHoleActor;
import actor.MainUiActor;

/**
 * Created by zzq on 16/5/16.
 */
public class GuardActorConfig {
    Integer GUARD_THREAD_POOL_SIZE;
    private   int serialPortNum=6;
    private BlackHoleActor blackHoleActor;
    private int temperatureLow=25;
    private int temperatureHigh=38;
    private int defaultLightValue=750;  //正常阈值
    private int emptyLightValue=450;  //空管阈值
    private int bloodLightValue=710;  //漏血阈值
    private int bubbleLightValue=20; //气泡差值
    private int bubbleHoldCount=1;   //最大气泡阈值
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
