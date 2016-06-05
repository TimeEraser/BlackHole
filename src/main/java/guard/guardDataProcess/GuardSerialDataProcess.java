package guard.guardDataProcess;

import actor.BaseActor;
import actor.GuardActor;
import actor.config.GuardActorConfig;
import command.GuardRequest;
import ecg.realtime.RealTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Timer;

/**
 * Created by adminstrator on 2016/5/28.
 */
public class GuardSerialDataProcess extends Observable {
    private File temperatureDataFile;
    private File alarmMessageDataFile;
    private static boolean alarmTemperatureSolved=true;
    private static boolean alarmBloodSolved = true;
    private GuardActorConfig guardActorConfig;
    private static int normalCount=0;
    private static int emptyCount=0;
    private static int bloodCount=0;
    private static int bubbleCount=0;
    private static int lastLightValue=0;
    private GuardData guardData;

    public GuardSerialDataProcess(File temperatureDataFile, File alarmMessageDatafile, GuardActorConfig guardActorConfig) throws IOException {
        this.temperatureDataFile = temperatureDataFile;
        this.alarmMessageDataFile = alarmMessageDatafile;
        this.guardActorConfig=guardActorConfig;
        guardData=new GuardData();
        java.util.Timer timer=new Timer();
        timer.schedule(
                new java.util.TimerTask() {
                    public void run(){
                        guardData.setCountMess();
                        normalCount=0;
                        emptyCount=0;
                        bloodCount=0;
                        bubbleCount=0;
                    }
                },15000,10000
        );
    }

    public short process(byte[] data) throws IOException {
        boolean alarmBlood=false;
        boolean alarmBubble=false;
        float temperature;
        RealTime realTime = new RealTime();
        String nowTime = realTime.getHMS();
        byte[] temperatureOutputData;
        byte[] alarmOutputData;
        int temperatureLow=guardActorConfig.getTemperatureLow();
        int temperatureHigh=guardActorConfig.getTemperatureHigh();
//        int defaultLightValue=guardActorConfig.getDefaultLightValue();
        int bloodLightValue=guardActorConfig.getBloodLightValue();
        int bubbleLightValue=guardActorConfig.getBubbleLightValue();
        int emptyLightValue=guardActorConfig.getEmptyLightValue();
//        int bubbleHoldCount=guardActorConfig.getBubbleHoldCount();
//        int normalHoldCount=guardActorConfig.getBubbleHoldCount();
        int lightValue;
        short alarmFlag=0;
        guardData.guardDataInit();
        guardData.setTime(nowTime);

        FileOutputStream temperatureStream = new FileOutputStream(temperatureDataFile, true);
        temperature = (float) ((data[0] % 32 + (data[1] & 0x1F) * 32) / 10.0); //将传输过来的值转换为float型
        temperatureOutputData = (String.valueOf(temperature)+"\r\n").getBytes();  //写入文件中
        temperatureStream.write(temperatureOutputData);
        temperatureStream.close();
        guardData.setTemperature(String.valueOf(temperature));
        if((temperature<temperatureLow)&&alarmTemperatureSolved){
            alarmFlag|=0x04;
            guardData.setTemperatureMessage("血温过低");
            alarmTemperatureSolved=false;
        }
        else if((temperature>temperatureHigh)&&alarmTemperatureSolved){
            alarmFlag|=0x08;
            guardData.setTemperatureMessage("血温过高");
            alarmTemperatureSolved=false;
        }
        else if ((!alarmTemperatureSolved)&&(temperature>temperatureLow)&&(temperature<temperatureHigh)){
            guardData.setTemperatureMessage("血温正常");
            alarmTemperatureSolved=true;
        }

        lightValue=(data[2]&0x1F) % 32 + (data[3] & 0x1F) * 32;
//        System.out.print("lightValue=");
//        System.out.println(lightValue);
//        System.out.print("lastLightValue=");
//        System.out.println(lastLightValue-bubbleLightValue);
        guardData.setLightValue(String.valueOf(lightValue));

        if(lightValue<emptyLightValue){
            emptyCount+=1;
        }
        else if(lightValue<=lastLightValue-bubbleLightValue){
            alarmBubble=true;
            bubbleCount+=1;
        }
        else if ((lightValue<bloodLightValue)&&(bubbleCount==0)){
            alarmBlood=true;
            bloodCount+=1;
        }
        else {
            normalCount+=1;
        }
        guardData.countMessageRefresh(normalCount,emptyCount,bloodCount,bubbleCount);
        lastLightValue=lightValue;
//        System.out.println(alarmBubble);
//        if(bubbleCount>bubbleHoldCount){
//            alarmBubble=true;
//        }
//        if(alarmBubble&&(bubbleCount<=bubbleHoldCount)&&(normalCount>=normalHoldCount)){
//            alarmBubble=false;
//        }

        if((alarmBlood && alarmBloodSolved)||alarmBubble) {
            if (alarmBlood) {
                FileOutputStream alarmStream = new FileOutputStream(alarmMessageDataFile, true);
                alarmOutputData = (nowTime + ":" + "Blood\r\n").getBytes();
                alarmStream.write(alarmOutputData);
                alarmStream.close();
                guardData.setBloodMessage("发生漏血");
                alarmBloodSolved = false;
                alarmFlag|=0x01;
            }
            if (alarmBubble) {
                FileOutputStream alarmStream = new FileOutputStream(alarmMessageDataFile, true);
                alarmOutputData = (nowTime + ":" + "Bubble\r\n").getBytes();
                alarmStream.write(alarmOutputData);
                alarmStream.close();
                guardData.setBubbleMessage("出现气泡");
                alarmFlag|=0x02;
            }
        }
//        else if(((!alarmBloodSolved) && (!alarmBlood))||((!alarmBubbleSolved) && (!alarmBubble))) {
        else if ((!alarmBloodSolved) && (!alarmBlood)) {
            FileOutputStream alarmStream = new FileOutputStream(alarmMessageDataFile, true);
            alarmOutputData = (nowTime + ":" + "Blood Solved\r\n").getBytes();
            alarmStream.write(alarmOutputData);
            alarmStream.close();
            guardData.setBloodMessage("不再漏血");
            alarmBloodSolved = true;
        }
//            if ((!alarmBubbleSolved) && (!alarmBubble)) {
//                FileOutputStream alarmStream = new FileOutputStream(alarmMessageDataFile, true);
//                alarmOutputData = (nowTime + ":" + "Bubble Solved\r\n").getBytes();
//                alarmStream.write(alarmOutputData);
//                alarmStream.close();
//                guardData.setBubbleMessage("气泡消失");
//                alarmBubbleSolved = true;
//            }
//        }
        guardData.setBloodLightValue(bloodLightValue);
        guardData.setBubbleLightValue(bubbleLightValue);
        setChanged();
        notifyObservers(guardData);
        return alarmFlag;
    }
}
