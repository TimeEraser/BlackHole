package guard.guardDataProcess;

/**
 * Created by adminstrator on 2016/6/2.
 */
public class GuardData {
    private String temperature=null;
    private String lightValue=null;
    private String time=null;
    private String temperatureMessage=null;
    private String bloodMessage=null;
    private String bubbleMessage=null;
    private int normalCount;
    private int emptyCount;
    private int bloodCount;
    private int bubbleCount;
    private boolean countFish=false;
    private boolean onceReadCountFlag=false;
    public void setTemperature(String temperature){
        this.temperature=temperature;
    }
    public String getTemperature(){
        return temperature;
    }
    public void setLightValue(String lightValue){
        this.lightValue=lightValue;
    }
    public String getLightValue(){
        return lightValue;
    }
    public void setTime(String time){
        this.time=time;
    }
    public String getTime(){
        return time;
    }
    public void setTemperatureMessage(String temperatureMessage){
        this.temperatureMessage=temperatureMessage;
    }
    public String getTemperatureMessage(){
        return temperatureMessage;
    }
    public void setBloodMessage(String bloodMessage){
        this.bloodMessage=bloodMessage;
    }
    public String getBloodMessage(){
        return bloodMessage;
    }
    public void setBubbleMessage(String bloodMessage){
        this.bubbleMessage=bubbleMessage;
    }
    public String getBubbleMessage(){
        return bubbleMessage;
    }
    public void setCountMess(int normalCount,int emptyCount,int bloodCount,int bubbleCount){
        this.normalCount=normalCount;
        this.emptyCount=emptyCount;
        this.bloodCount=bloodCount;
        this.bubbleCount=bubbleCount;
        countFish=true;
    }
    public boolean isCountFish(){
        return countFish;
    }
    public int getBubbleCount(){
        return bubbleCount;
    }
    public int[] getCountMess(){
        int[] count=new int[4];
        count[0]=normalCount;
        count[1]=emptyCount;
        count[2]=bloodCount;
        count[3]=bubbleCount;
        if(countFish&&!onceReadCountFlag){
            onceReadCountFlag=true;
        }
        else if(onceReadCountFlag&&countFish){
            countFish=false;
            onceReadCountFlag=false;
        }
        return count;
    }
    public String getMessage(String targetName){
        switch (targetName) {
            case "Temperature":
                return getTemperatureMessage();
            case "Blood":
                return getBloodMessage();
            case "Bubble":
                return getBubbleMessage();
            default:
                return null;
        }
    }
    public String getValue(String targetName){
        switch (targetName){
            case "Temperature":
                return getTemperature();
            case "Blood":
                return getLightValue();
            case "Bubble":
                return String.valueOf(getBubbleCount());
            default:
                return null;
        }
    }
    public void guardDataInit(){
        temperature=null;
        lightValue=null;
        time=null;
        temperatureMessage=null;
        bloodMessage=null;
        bubbleMessage=null;
    }
}

