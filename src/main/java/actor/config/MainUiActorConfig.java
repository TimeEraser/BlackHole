package actor.config;

import actor.*;

import java.awt.Dimension;
import java.awt.Toolkit;


public class MainUiActorConfig {

	//Interactive element

	private CtActor ctActor;
	private GuardActor guardActor;
	private MobileActor mobileActor;
	private MonitorActor monitorActor;
	private BlackHoleActor blackHoleActor;
	//Interface element
	private Dimension screenSize= Toolkit.getDefaultToolkit().getScreenSize();
	private Integer screenWidth = screenSize.width;
	private Integer screenHeight = screenSize.height;
	private Integer WIDTH = (int)(screenWidth*0.8);
	private Integer Height = (int)(screenHeight*0.8);
	private Integer MENU_FONT_SIZE=12;
	private Integer CONTENT_FONT_SIZE=12;
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

	public Integer getWIDTH() {
		return WIDTH;
	}

	public void setWIDTH(Integer WIDTH) {
		this.WIDTH = WIDTH;
	}

	public Integer getHeight() {
		return Height;
	}

	public void setHeight(Integer height) {
		Height = height;
	}

	public Integer getCONTENT_FONT_SIZE() {
		return CONTENT_FONT_SIZE;
	}

	public void setCONTENT_FONT_SIZE(Integer CONTENT_FONT_SIZE) {
		this.CONTENT_FONT_SIZE = CONTENT_FONT_SIZE;
	}

	public Integer getMENU_FONT_SIZE() {
		return MENU_FONT_SIZE;
	}

	public void setMENU_FONT_SIZE(Integer MENU_FONT_SIZE) {
		this.MENU_FONT_SIZE = MENU_FONT_SIZE;
	}
}

