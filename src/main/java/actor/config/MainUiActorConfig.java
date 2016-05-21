package actor.config;

import actor.*;


public class MainUiActorConfig {
	private Integer MAINUI_THREAD_POOL_SIZE=4;
	private MonitorActor monitorActor;
	private BlackHoleActor blackHoleActor;
	private CtActor ctActor;
	private GuardActor guardActor;
	protected MobileActor mobileActor;

	public Integer getMAINUI_THREAD_POOL_SIZE() {
		return MAINUI_THREAD_POOL_SIZE;
	}
	public void setMAINUI_THREAD_POOL_SIZE(Integer mAINUI_THREAD_POOL_SIZE) {
		MAINUI_THREAD_POOL_SIZE = mAINUI_THREAD_POOL_SIZE;
	}
	public MonitorActor getMonitorActor() {
		return monitorActor;
	}
	public void setMonitorActor(MonitorActor monitorActor) {
		this.monitorActor = monitorActor;
	}

	public BlackHoleActor getBlackHoleActor() {
		return blackHoleActor;
	}
	public void setBlackHoleActor(BlackHoleActor blackHoleActor) {
		this.blackHoleActor = blackHoleActor;
	}

	public CtActor getCtActor(){return ctActor;}
	public  void setCtActor(CtActor ctActor){this.ctActor=ctActor;}

	public GuardActor guardActor(){return  guardActor;}
	public  void setGuardActor(GuardActor guardActor){this.guardActor=guardActor;}

	public MobileActor mobileActor(){return  mobileActor;}
	public  void setMobileActor(MobileActor mobileActor){this.mobileActor=mobileActor;}


	
}

