package actor.config;

import actor.BlackHoleActor;

/**
 * Created by zzq on 16/5/16.
 */
public class MonitorActorConfig {
    public Integer MONITOR_THREAD_POOL_SIZE=3;
    private BlackHoleActor blackHoleActor;
    public void setBlackHoleActor(BlackHoleActor blackHoleActor){
        this.blackHoleActor=blackHoleActor;
    }
    public BlackHoleActor getBlackHoleActor(){
        return blackHoleActor;
    }
}
