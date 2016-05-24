package actor;

import java.awt.*;
import java.net.URL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

import actor.Listener.NoticeListener;
import actor.config.MainUiActorConfig;
import com.alee.laf.WebLookAndFeel;
import command.*;
import ecg.tcp.*;



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
		private Integer LEFT;
		private Integer TOP;

	    private TCPConfig TCPC;
	    private JFrame InitializationInterface;
		protected MainUiActorConfig mainUiActorConfig;

	
	public MainUiActor(MainUiActorConfig mainUiActorConfig){

		 this.mainUiActorConfig=mainUiActorConfig;
		 ctActor=mainUiActorConfig.getCtActor();
		 guardActor=mainUiActorConfig.getGuardActor();
		 monitorActor=mainUiActorConfig.getMonitorActor();
		 blackHoleActor=mainUiActorConfig.getBlackHoleActor();
		 mobileActor=mainUiActorConfig.getMobileActor();

		 WIDTH=mainUiActorConfig.getWIDTH();
		 HEIGHT=mainUiActorConfig.getHeight();
		 MENU_FONT_SIZE=mainUiActorConfig.getMENU_FONT_SIZE();
		 CONTENT_FONT_SIZE=mainUiActorConfig.getCONTENT_FONT_SIZE();
		LEFT = mainUiActorConfig.getLEFT();
		TOP = mainUiActorConfig.getTOP();
	    }

	 
	    @Override
	    public boolean processActorRequest(Request  request) {
	    	if(request == SystemRequest.BOOT){
	    		sendResponse(request, SystemResponse.SYSTEM_MESSAGE,"mainUiActor收到SystemRequest.BOOT请求，启动mainUi");
	    		this.start();
				return true;
	    	}
			if(request==GuardRequest.GUARD_START){
				sendRequest(guardActor,request);
			}
			if(request==MainUiRequest.MAIN_UI_ECG_CONFIG){

				sendRequest(monitorActor,request,request.getConfig().getData());
			}
			if(request==MainUiRequest.MAIN_UI_ECG_STOP){
				sendRequest(monitorActor,request);
			}
	        return false;
	    }

	    @Override
	    public boolean processActorResponse(Response responses) {
			if(responses==GuardResponse.GUARD_ERROR){
				System.out.print("GuardResponse.GUARD_ERROR");

			}
			if(responses==MonitorResponse.MONITOR_SHUTDOWM){
				InitializationInterface.getContentPane().removeAll();
				InitializationInterface.repaint();

			}

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
		WebLookAndFeel.globalControlFont  = new FontUIResource("Dialog",0, 12);
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
		InitializationInterface=new JFrame("医疗健康管理系统");
		InitializationInterface.setSize(WIDTH,HEIGHT);
		InitializationInterface.setLocation(LEFT,TOP);
		InitializationInterface.setLocationRelativeTo(null);	//设置窗口居中
		InitializationInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//menu
		InitializationInterface.setJMenuBar(managerMenu());
		//content
		//InitializationInterface.getContentPane().add(managerContent());
		
		InitializationInterface.setVisible(true);
	}

	private JMenuBar managerMenu() {
		JMenuBar mainMenu=new JMenuBar();
		JMenu sys = new JMenu();
		ImageIcon beginIcon = new ImageIcon(getIconImage("Icon/file.png"));
		sys.setIcon(beginIcon);
		mainMenu.add(sys);
		ImageIcon configIcon = new ImageIcon(getIconImage("Icon/config.png"));
		ImageIcon analysisIcon = new ImageIcon(getIconImage("Icon/analysis.png"));



		JMenu ct=new JMenu("");
		ImageIcon ctIcon = new ImageIcon(getIconImage("Icon/ct.png"));
		ct.setIcon(ctIcon);
			JMenuItem ct_config=new JMenuItem("CT配置");
			ct_config.setIcon(configIcon);
			ct_config.addActionListener(new NoticeListener(this,ctActor,MainUiRequest.MAIN_UI_CT_CONFIG));
			ct_config.setToolTipText("打开一张CT图像");
			JMenuItem ct_analysis=new JMenuItem("CT分析");
		ct_analysis.setIcon(analysisIcon);
			ct_analysis.addActionListener(new NoticeListener(this,ctActor,MainUiRequest.MAIN_UI_CT_ANALYSIS));
			ct.add(ct_config);
			ct.addSeparator();
			ct.add(ct_analysis);
		mainMenu.add(ct);


		JMenu ecg=new JMenu("");
		ImageIcon ecgIcon = new ImageIcon(getIconImage("Icon/monitor.png"));
		ecg.setIcon(ecgIcon);
			JMenuItem ecg_config=new JMenuItem("心电仪配置");
		ecg_config.setIcon(configIcon);
			JMenuItem ecg_analysis=new JMenuItem("心电仪停止");
		ecg_analysis.setIcon(analysisIcon);
			ecg_analysis.setEnabled(false);
			ecg_config.addActionListener(new EcgConfigListener(this,this,MainUiRequest.MAIN_UI_ECG_CONFIG,ecg_analysis));
			ecg_analysis.addActionListener(new NoticeListener(this,this,MainUiRequest.MAIN_UI_ECG_STOP));
			ecg.add(ecg_config);
			ecg.addSeparator();
			ecg.add(ecg_analysis);
		mainMenu.add(ecg);

		JMenu guard=new JMenu("");
		guard.setHorizontalTextPosition(SwingConstants.RIGHT);
		ImageIcon guardIcon = new ImageIcon(getIconImage("Icon/guard.png"));
		guard.setIcon(guardIcon);
			JMenuItem guard_config=new JMenuItem("连接告警设备");
			guard_config.addActionListener(new NoticeListener(this,this,GuardRequest.GUARD_START));
			guard.add(guard_config);
		mainMenu.add(guard);

		JMenu mobile=new JMenu("");
		ImageIcon phoneIcon = new ImageIcon(getIconImage("Icon/phone.png"));
		mobile.setIcon(phoneIcon);
			JMenuItem mobile_config=new JMenuItem("连接手机");
			mobile_config.addActionListener(new NoticeListener(this,mobileActor,MainUiRequest.MAIN_UI_MOBILE_DATA));
			mobile.add(mobile_config);
		mainMenu.add(mobile);

//		JMenu ext=new JMenu("退出");
//			JMenuItem clo=new JMenuItem("关闭窗口");
//			clo.addActionListener(new NoticeListener(this,blackHoleActor,SystemRequest.SHUTDOWN));
//			ext.add(clo);
//		mainMenu.add(ext);
		return mainMenu;
	}
	private JLabel managerContent() {
		JLabel lbl;
		lbl=new JLabel("---TEXT---");
		return lbl;
	}
	private URL getIconImage(String path){
		return this.getClass().getClassLoader().getResource(path);
	}

	class EcgConfigListener extends BaseActor implements ActionListener{
		private String host;//主机域名
		private String port;   //主机端口号
		private String Id;	//档案ID
		private String Name;//姓名
		private String Sex;	//性别

		private JMenuItem ecg_analysis;
		private MainUiActor mainUiActor;
		private BaseActor receiver;
		private Request request;
		private Object data;
		public  EcgConfigListener(MainUiActor mainUiActor, BaseActor receiver , Request request,JMenuItem ecg_analysis){
			this.mainUiActor=mainUiActor;
			this.receiver=receiver;
			this.request=request;
			this.ecg_analysis=ecg_analysis;
		}
		public EcgConfigListener(MainUiActor mainUiActor, BaseActor receiver ,Request request,Object data,JMenuItem ecg_analysis){
			this.mainUiActor=mainUiActor;
			this.receiver=receiver;
			this.request=request;
			this.data=data;
			this.ecg_analysis=ecg_analysis;
		}
		@Override
		public void actionPerformed(ActionEvent e) {

			TCPC = new TCPConfig(InitializationInterface, true);
			TCPC.setVisible(true);
			host=TCPC.getjTextField1().getText();
			//port=Integer.parseInt(TCPC.getjTextField2().getText());
			port=TCPC.getjTextField2().getText();
			Id=TCPC.getjTextField3().getText();
			Name=TCPC.getjTextField4().getText();
			Sex=TCPC.getJRadioButtonName();

			ArrayList<String> strArray = new ArrayList<String> ();
			strArray.add(host);
			strArray.add(port);
			strArray.add(Id);
			strArray.add(Name);
			strArray.add(Sex);

			System.out.println(host);
			System.out.println(port);
			System.out.println(Id);
			System.out.println(Name);
			System.out.println(Sex);

			sendRequest(receiver,request,strArray);
			ecg_analysis.setEnabled(true);
		}

		@Override
		protected boolean processActorRequest(Request requests) {
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

	public JFrame getInitializationInterface(){return  InitializationInterface;}


}
