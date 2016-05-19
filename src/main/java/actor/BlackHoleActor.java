package actor;

import actor.config.BlackHoleConfig;
import actor.config.GuardActorConfig;
import actor.config.MonitorActorConfig;
import command.*;
import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/16.
 */
public class BlackHoleActor extends BaseActor {
    private GuardActor guardActor;
    private MonitorActor monitorActor;
    public BlackHoleActor(BlackHoleConfig blackHoleConfig){
        //TO DO Initialize the BlackHoleActor
        guardActor=new GuardActor(new GuardActorConfig());
        monitorActor=new MonitorActor(new MonitorActorConfig());
    }
    @Override
    protected boolean processActorRequest(Request request) {
        if(request instanceof MonitorRequest)
            redirectCommand(monitorActor,request);
        if(request instanceof GuardRequest)
            redirectCommand(guardActor,request);
        return false;
    }
    @Override
    protected boolean processActorResponse(Response response) {
       System.out.println((String)response.getConfig().getData());
        return false;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean shutdown() {
        return false;
    }
}
