package actor.config;

import actor.BlackHoleActor;
import actor.MainUiActor;

/**
 * Created by zzq on 16/5/16.
 */
public class GuardActorConfig {
    Integer GUARD_THREAD_POOL_SIZE;
    public  int serialPortNum=6;
    private BlackHoleActor blackHoleActor;
    public BlackHoleActor getBlackHoleActor(){
        return blackHoleActor;
    }
    public void setBlackHoleActor(BlackHoleActor blackHoleActor){
        this.blackHoleActor=blackHoleActor;
    }
    public void  setSerialPortNum(int serialPortNum){
        this.serialPortNum=serialPortNum;
    }
    public int getSerialPortNum(){
        return  this.serialPortNum;
    }
}
