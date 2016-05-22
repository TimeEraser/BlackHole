package actor;

import actor.config.MobileActorConfig;
import command.MobileRequest;
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
        if(request == MobileRequest.MOBILE_CONNECT){

            System.out.println("MobileRequest.MOBILE_CONNECT");

        }


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
