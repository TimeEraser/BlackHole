package actor.config;

import actor.BlackHoleActor;

public class MobileActorConfig {
    Integer MOBILE_THREAD_POOL_SIZE;
    private BlackHoleActor blackHoleActor;
    public void setBlackHoleActor(BlackHoleActor blackHoleActor){
        this.blackHoleActor=blackHoleActor;
    }
    public BlackHoleActor getBlackHoleActor(){
        return blackHoleActor;
    }
}
