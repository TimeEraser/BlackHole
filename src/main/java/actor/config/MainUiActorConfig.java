package actor.config;

import actor.BlackHoleActor;
import actor.MonitorActor;


public class MainUiActorConfig {
	private Integer MAINUI_THREAD_POOL_SIZE=1;
	private MonitorActor monitorActor;
	private BlackHoleActor blackHoleActor;
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
	
}

