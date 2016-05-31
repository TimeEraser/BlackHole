package guard.guardDataProcess;

import actor.BaseActor;
import command.GuardRequest;
import ecg.realtime.RealTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Observable;

/**
 * Created by adminstrator on 2016/5/28.
 */
public class GuardSerialDataProcess extends Observable {
    private File temperatureDataFile;
    private File alarmMessageDataFile;
    private static boolean alarmBloodSolved = true;
    private static boolean alarmBubbleSolved = true;
    private static short alarmFlag=0;

    public GuardSerialDataProcess(File temperatureDataFile, File alarmMessageDatafile) throws IOException {
        this.temperatureDataFile = temperatureDataFile;
        this.alarmMessageDataFile = alarmMessageDatafile;
    }

    public short process(byte[] data) throws IOException {
        boolean alarmBlood;
        boolean alarmBubble;
        float temperature;
        RealTime realTime = new RealTime();
        String nowTime = realTime.getHMS();
        byte[] temperatureOutputData;
        byte[] alarmOutputData;
        String result;

        FileOutputStream temperatureStream = new FileOutputStream(temperatureDataFile, true);
        temperature = (float) ((data[0] % 32 + (data[1] & 0x7F) * 32) / 10.0); //将传输过来的值转换为float型
        result=String.valueOf(temperature)+"\r\n";
        temperatureOutputData = result.getBytes();  //写入文件中
        temperatureStream.write(temperatureOutputData);
        temperatureStream.close();

        alarmBlood = (data[0] / 32 % 2 == 1);
        alarmBubble = (data[0] / 64 % 2 == 1);
        if (alarmBlood && alarmBloodSolved) {
            FileOutputStream alarmStream = new FileOutputStream(alarmMessageDataFile, true);
            alarmOutputData = (nowTime + ":" + "Blood\r\n").getBytes();
            alarmStream.write(alarmOutputData);
            alarmStream.close();
            result=result+nowTime+"_"+"Blood\r\n";
            alarmBloodSolved = false;
            alarmFlag=1;
        }
        else if (alarmBubble && alarmBubbleSolved) {
            FileOutputStream alarmStream = new FileOutputStream(alarmMessageDataFile, true);
            alarmOutputData = (nowTime + ":" + "Bubble\r\n").getBytes();
            alarmStream.write(alarmOutputData);
            alarmStream.close();
            result=result+nowTime + "_" + "Bubble\r\n";
            alarmBubbleSolved = false;
            alarmFlag=2;
        }
        else if ((!alarmBloodSolved)&&(!alarmBlood)) {
            FileOutputStream alarmStream = new FileOutputStream(alarmMessageDataFile, true);
            alarmOutputData = (nowTime + ":" + "Blood Solved\r\n").getBytes();
            alarmStream.write(alarmOutputData);
            alarmStream.close();
            result = result + nowTime + "_" + "Blood Solved\r\n";
            alarmBloodSolved = true;
            alarmFlag=0;
        }
        else if ((!alarmBubbleSolved)&&(!alarmBubble)) {
            FileOutputStream alarmStream = new FileOutputStream(alarmMessageDataFile, true);
            alarmOutputData = (nowTime + ":" + "Bubble Solved\r\n").getBytes();
            alarmStream.write(alarmOutputData);
            alarmStream.close();
            result=result+nowTime + "_" + "Bubble Solved\r\n";
            alarmBubbleSolved = true;
            alarmFlag=0;
        }
        else{
            alarmFlag=0;
        }
        setChanged();
        notifyObservers(result);
        return alarmFlag;
    }
}
