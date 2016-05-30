package actor.config;

import actor.MainUiActor;

/**
 * Created by zzq on 16/5/16.
 */
public class GuardActorConfig {
    Integer GUARD_THREAD_POOL_SIZE;
    public  int serialPortNum=6;
    public void  setSerialPortNum(int serialPortNum){
        this.serialPortNum=serialPortNum;
    }
    public int getSerialPortNum(){
        return  this.serialPortNum;
    }
}
