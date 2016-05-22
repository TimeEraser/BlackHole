package actor;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.management.monitor.Monitor;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;

import actor.config.MainUiActorConfig;
import com.alee.laf.WebLookAndFeel;
import command.*;


public class MainUiActor extends BaseActor{

	//Initialize parameter
		//Interactive element
		private CtActor ctActor;
		private GuardActor guardActor;
		private MonitorActor monitorActor;
		private BlackHoleActor blackHoleActor;
		private MobileActor mobileActor;
        //Interface element
		private Integer WIDTH;
		private Integer HEIGHT;
		private Integer MENU_FONT_SIZE;
		private Integer CONTENT_FONT_SIZE;
	
	
	public MainUiActor(MainUiActorConfig mainUiActorConfig){

		 ctActor=mainUiActorConfig.getCtActor();
		 guardActor=mainUiActorConfig.getGuardActor();
		 monitorActor=mainUiActorConfig.getMonitorActor();
		 blackHoleActor=mainUiActorConfig.getBlackHoleActor();
		 mobileActor=mainUiActorConfig.getMobileActor();

		 WIDTH=mainUiActorConfig.getWIDTH();
		 HEIGHT=mainUiActorConfig.getHeight();
		 MENU_FONT_SIZE=mainUiActorConfig.getMENU_FONT_SIZE();
		 CONTENT_FONT_SIZE=mainUiActorConfig.getCONTENT_FONT_SIZE();

	    }

	 
	    @Override
	    public boolean processActorRequest(Request  request) {
	    	if(request == SystemRequest.BOOT){
	    		sendResponse(request, SystemResponse.SYSTEM_MESSAGE,"mainUiActor收到SystemRequest.BOOT请求，启动mainUi");
	    		this.start();
				return true;
	    	}
	        return false;
	    }

	    @Override
	    public boolean processActorResponse(Response responses) {

			return false;
	    }

	    @Override
	    public boolean start()  {
			this.constructInterface();
	        return false;
	    }

	    @Override
	    public boolean shutdown() {
	        return false;
	    }


	private void  constructInterface(){
		managerStyle();
		managerElement();
	}


	private void managerStyle() {
		WebLookAndFeel.globalControlFont  = new FontUIResource("Dialog",0, CONTENT_FONT_SIZE);
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		UIManager.put("ToolTip.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("Menu.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("MenuItem.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("Panel.font", new Font("Dialog", Font.PLAIN, CONTENT_FONT_SIZE));
		UIManager.put("Label.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("Button.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("CheckBox.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("ComboBox.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("RadioButton.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("TitledBorder.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("TabbedPane.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("List.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("InternalFrame.titleFont", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("CheckBoxMenuItem.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("Table.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("TableHeader.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("TextField.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		UIManager.put("TextArea.font", new Font("Dialog", 0, CONTENT_FONT_SIZE));
		try {
			UIManager.setLookAndFeel( "com.alee.laf.WebLookAndFeel" );
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private void managerElement() {
		JFrame InitializationInterface=new JFrame("医疗健康管理系统");
		InitializationInterface.setSize(WIDTH,HEIGHT);
		InitializationInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//menu
		InitializationInterface.setJMenuBar(managerMenu());
		//content
		InitializationInterface.getContentPane().add(managerContent());
		
		InitializationInterface.setVisible(true);
	}

	private JMenuBar managerMenu() {
		JMenuBar mainMenu=new JMenuBar();

		JMenu ct=new JMenu("CT");
			JMenuItem ct_config=new JMenuItem("CT配置");
			ct_config.addActionListener(new CtConfigListener(ctActor, ct_config));
			ct_config.setToolTipText("打开一张CT图像");
			JMenuItem ct_analysis=new JMenuItem("CT分析");
			ct_analysis.addActionListener(new CtAnalysisListener(ctActor, ct_analysis));
			ct.add(ct_config);
			ct.addSeparator();
			ct.add(ct_analysis);
		mainMenu.add(ct);


		JMenu ecg=new JMenu("心电仪");
			JMenuItem ecg_config=new JMenuItem("心电仪配置");
			ecg_config.addActionListener(new EcgConfigListener(monitorActor, ecg_config));
			JMenuItem ecg_analysis=new JMenuItem("心电仪分析");
			ecg_analysis.addActionListener(new EcgAnalysisListener(monitorActor,ecg_analysis));
			ecg.add(ecg_config);
			ecg.addSeparator();
			ecg.add(ecg_analysis);
		mainMenu.add(ecg);

		JMenu guard=new JMenu("告警");
			JMenuItem guard_config=new JMenuItem("连接告警设备");
			guard_config.addActionListener(new GuardConfigListener(guardActor,guard_config));
			guard.add(guard_config);
		mainMenu.add(guard);

		JMenu mobile=new JMenu("手机");
			JMenuItem mobile_config=new JMenuItem("连接手机");
			mobile_config.addActionListener(new MobileConfigListener(mobileActor,mobile_config));
			mobile.add(mobile_config);
		mainMenu.add(mobile);

		JMenu ext=new JMenu("退出");
			JMenuItem clo=new JMenuItem("关闭窗口");
			clo.addActionListener(new CloseListener(blackHoleActor,clo));
			ext.add(clo);
		mainMenu.add(ext);
		return mainMenu;
	}

	private JLabel managerContent() {
		JLabel lbl;
		lbl=new JLabel("---TEXT---");
		return lbl;
	}

}


 class CtConfigListener extends BaseActor implements ActionListener
{
	private JLabel lb1;
	private JMenuItem jMenuItem;
	private BaseActor communicateActor;
	public CtConfigListener(JLabel lb1,JMenuItem ct_config){
		this.lb1=lb1;
		this.jMenuItem=ct_config;
	}
	public CtConfigListener(BaseActor commuBaseActor ,JMenuItem ct_config) {
		this.communicateActor=commuBaseActor;
		this.jMenuItem=ct_config;
	}

 public void actionPerformed(ActionEvent e )  
   { 
     JMenuItem mi=(JMenuItem) e.getSource();
     if(mi==jMenuItem){

    	sendRequest(communicateActor,CtRequest.CT_OPEN_IMG);

     }
   }

	protected boolean processActorRequest(Request requests) {
		return false;
	}

	protected boolean processActorResponse(Response responses) {
		return false;
	}

	public boolean start() {
		return false;
	}

	public boolean shutdown() {
		return false;
	}
}

class CtAnalysisListener extends BaseActor implements ActionListener{

	private JLabel lb1;
	private JMenuItem jMenuItem;
	private BaseActor communicateActor;
	public CtAnalysisListener(JLabel lb1,JMenuItem ct_config){
		this.lb1=lb1;
		this.jMenuItem=ct_config;
	}
	public CtAnalysisListener(BaseActor commuBaseActor ,JMenuItem ct_analysis) {
		this.communicateActor=commuBaseActor;
		this.jMenuItem=ct_analysis;
	}



	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem) e.getSource();
		if(mi==jMenuItem){

			sendRequest(communicateActor, MainUiRequest.MAINUI_CT_ANALYSIS);

		}

	}
	protected boolean processActorRequest(Request requests) {
		return false;
	}

	protected boolean processActorResponse(Response responses) {
		return false;
	}

	public boolean start() {
		return false;
	}

	public boolean shutdown() {
		return false;
	}


}


class EcgConfigListener extends BaseActor implements ActionListener{
	private JLabel lb1;
	private JMenuItem jMenuItem;
	private BaseActor communicateActor;
	public EcgConfigListener(JLabel lb1,JMenuItem ecg_config){
		this.lb1=lb1;
		this.jMenuItem=ecg_config;
	}
	public EcgConfigListener(BaseActor commuBaseActor ,JMenuItem ecg_config) {
		this.communicateActor=commuBaseActor;
		this.jMenuItem=ecg_config;
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem) e.getSource();
		if(mi==jMenuItem){

			sendRequest(communicateActor, MonitorRequest.MONITOR_ECG_DATA);

		}

	}
	protected boolean processActorRequest(Request requests) {
		return false;
	}

	protected boolean processActorResponse(Response responses) {
		return false;
	}

	public boolean start() {
		return false;
	}

	public boolean shutdown() {
		return false;
	}

}

class EcgAnalysisListener extends BaseActor implements ActionListener{
	private JLabel lb1;
	private JMenuItem jMenuItem;
	private BaseActor communicateActor;
	public EcgAnalysisListener(JLabel lb1,JMenuItem ecg_analysis){
		this.lb1=lb1;
		this.jMenuItem=ecg_analysis;
	}
	public EcgAnalysisListener(BaseActor commuBaseActor ,JMenuItem ecg_analysis) {
		this.communicateActor=commuBaseActor;
		this.jMenuItem=ecg_analysis;
	}
	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem) e.getSource();
		if(mi==jMenuItem){

			sendRequest(communicateActor, MainUiRequest.MAINUI_ECG_ANALYSIS);

		}

	}
	protected boolean processActorRequest(Request requests) {
		return false;
	}

	protected boolean processActorResponse(Response responses) {
		return false;
	}

	public boolean start() {
		return false;
	}

	public boolean shutdown() {
		return false;
	}


}

class CloseListener extends BaseActor implements ActionListener{
	private JLabel lb1;
	private JMenuItem jMenuItem;
	private BaseActor communicateActor;
	public CloseListener(JLabel lb1,JMenuItem clo){
		this.lb1=lb1;
		this.jMenuItem=clo;
	}
	public CloseListener(BaseActor commuBaseActor ,JMenuItem clo) {
		this.communicateActor=commuBaseActor;
		this.jMenuItem=clo;
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem) e.getSource();
		if(mi==jMenuItem){
			sendRequest(communicateActor, SystemRequest.SHUTDOWN);
		}

	}
	protected boolean processActorRequest(Request requests) {
		return false;
	}

	protected boolean processActorResponse(Response responses) {
		return false;
	}

	public boolean start() {
		return false;
	}

	public boolean shutdown() {
		return false;
	}
}


class GuardConfigListener extends BaseActor implements ActionListener{

	private JLabel lb1;
	private JMenuItem jMenuItem;
	private BaseActor communicateActor;
	public GuardConfigListener(JLabel lb1,JMenuItem guard_config){
		this.lb1=lb1;
		this.jMenuItem=guard_config;
	}
	public GuardConfigListener(BaseActor commuBaseActor ,JMenuItem guard_config) {
		this.communicateActor=commuBaseActor;
		this.jMenuItem=guard_config;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem) e.getSource();
		if(mi==jMenuItem){
			sendRequest(communicateActor, GuardRequest.GUARD_DATA);
		}
	}

	@Override
	protected boolean processActorRequest(Request requests) {return false;}

	@Override
	protected boolean processActorResponse(Response responses) {return false;}

	@Override
	public boolean start() {return false;}

	@Override
	public boolean shutdown() {return false;}


}


class MobileConfigListener extends BaseActor implements ActionListener{

	private JLabel lb1;
	private JMenuItem jMenuItem;
	private BaseActor communicateActor;
	public MobileConfigListener(JLabel lb1,JMenuItem mobile_config){
		this.lb1=lb1;
		this.jMenuItem=mobile_config;
	}
	public MobileConfigListener(BaseActor commuBaseActor ,JMenuItem mobile_config) {
		this.communicateActor=commuBaseActor;
		this.jMenuItem=mobile_config;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem) e.getSource();
		if(mi==jMenuItem){
			sendRequest(communicateActor, MobileRequest.MOBILE_CONNECT);
		}
	}

	@Override
	protected boolean processActorRequest(Request requests) {return false;}

	@Override
	protected boolean processActorResponse(Response responses) {return false;}

	@Override
	public boolean start() {return false;}

	@Override
	public boolean shutdown() {return false;}


}