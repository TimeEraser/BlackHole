package actor;

import actor.config.CtActorConfig;

import command.Request;
import command.Response;
import command.CtResponse;
import command.config.CommandConfig;

public class CtActor extends BaseActor{
	public CtActor(CtActorConfig ctActorConfig){
        //TO DO Initialize the GuardActor
    }
    @Override
    public boolean processActorRequest(Request  request ) {
        return false;
    }

    @Override
    public boolean processActorResponse(Response  responses) {
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
