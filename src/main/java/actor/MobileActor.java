package actor;

import actor.config.MobileActorConfig;
import command.Request;
import command.Response;
import command.config.CommandConfig;
import command.MobileResponse;

public class MobileActor extends BaseActor  {
	public MobileActor(MobileActorConfig mobileActorConfig){
        //TO DO Initialize the GuardActor
    }
    @Override
    public boolean processActorRequest(Request request) {
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
