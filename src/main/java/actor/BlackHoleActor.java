package actor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import actor.config.BlackHoleConfig;
import actor.config.CtActorConfig;
import actor.config.GuardActorConfig;
import actor.config.MainUiActorConfig;
import actor.config.MobileActorConfig;
import actor.config.MonitorActorConfig;
import command.*;
import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/16.
 */
public class BlackHoleActor extends BaseActor {
	public ExecutorService longRunningExecutor;//自加把protected改成public
	
	private MonitorActor monitorActor;
    private GuardActor guardActor;
    private CtActor ctActor;
    private MobileActor mobileActor;
    private MainUiActor mainUiActor;
    
    public BlackHoleActor(BlackHoleConfig blackHoleConfig){
        //TO DO Initialize the BlackHoleActor 构造方法，进行config
    	longRunningExecutor=Executors.newFixedThreadPool(blackHoleConfig.BLACK_HOLE_THREAD_POOL_SIZE);
  
    	monitorActor=new MonitorActor(new MonitorActorConfig());//用相应的config配置相应的actor
    	guardActor=new GuardActor(new GuardActorConfig());		
    	ctActor=new CtActor(new CtActorConfig());
    	mobileActor=new MobileActor(new MobileActorConfig());
    	MainUiActorConfig mainUiActorConfig= new MainUiActorConfig();
    	mainUiActorConfig.setBlackHoleActor(this);
    	mainUiActorConfig.setMonitorActor(monitorActor);
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
        return false;
    }
    @Override
    protected boolean processActorResponse(Response response) {
       System.out.println(response.getConfig().getData());
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
