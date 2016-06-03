package actor;

import actor.config.BlackHoleConfig;
import actor.config.CtActorConfig;
import actor.config.GuardActorConfig;
import actor.config.MainUiActorConfig;
import actor.config.MobileActorConfig;
import actor.config.MonitorActorConfig;
import command.*;

/**
 * Created by zzq on 16/5/16.
 */
public class BlackHoleActor extends BaseActor {
    //public ExecutorService longRunningExecutor;

    private MonitorActor monitorActor;
    private GuardActor guardActor;
    private CtActor ctActor;
    private MobileActor mobileActor;
    public  MainUiActor mainUiActor;

    public BlackHoleActor(BlackHoleConfig blackHoleConfig){
        //TO DO Initialize the BlackHoleActor

        monitorActor=new MonitorActor(new MonitorActorConfig());
        GuardActorConfig guardActorConfig=new GuardActorConfig();
        guardActorConfig.setBlackHoleActor(this);
        guardActor=new GuardActor(guardActorConfig);
        ctActor=new CtActor(new CtActorConfig());
        mobileActor=new MobileActor(new MobileActorConfig());
        MainUiActorConfig mainUiActorConfig= new MainUiActorConfig();
        mainUiActorConfig.setBlackHoleActor(this);
        mainUiActorConfig.setMonitorActor(monitorActor);
        mainUiActorConfig.setGuardActor(guardActor);
        mainUiActorConfig.setCtActor(ctActor);
        mainUiActorConfig.setMobileActor(mobileActor);
        mainUiActor=new MainUiActor(mainUiActorConfig);
    }
    @Override
    protected boolean processActorRequest(Request request) {
        if(request instanceof MonitorRequest)
            redirectCommand(monitorActor,request);
        if(request instanceof GuardRequest)
            redirectCommand(guardActor,request);
        if(request instanceof CtRequest)
            redirectCommand(ctActor,request);
        if(request instanceof MainUiRequest )
            redirectCommand(mainUiActor, request);
        if (request instanceof MobileRequest)
            redirectCommand(mobileActor,request);
        if(request ==SystemRequest.SHUTDOWN){
            System.exit(0);
        }
        return false;
    }
    @Override
    protected boolean processActorResponse(Response response) {
        System.out.println("blackHoleActor.processActorResponse:"+response.getConfig().getData());
        return false;
    }

    @Override
    public boolean start() {
        sendRequest(mainUiActor, SystemRequest.BOOT);
        return false;
    }

    @Override
    public boolean shutdown() {
        return false;
    }
}
