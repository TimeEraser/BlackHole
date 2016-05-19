package actor;

import actor.config.GuardActorConfig;
import command.Request;
import command.Response;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * Created by zzq on 16/5/16.
 */
public class GuardActor extends BaseActor {
    public GuardActor(GuardActorConfig guardActorConfig){
        //TO DO Initialize the GuardActor
    }
    @Override
    protected boolean processActorRequest(Request requests){
        return false;
    }

    @Override
    protected boolean processActorResponse(Response responses) {
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
