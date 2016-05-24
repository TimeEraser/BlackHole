package actor;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.FontUIResource;

import actor.Listener.NoticeListener;
import actor.Listener.SwitchListener;
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
	private Integer LEFT;
	private Integer TOP;


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
		JFrame InitializationInterface=new JFrame("医疗健康管理系统");
		InitializationInterface.setSize(WIDTH,HEIGHT);
		InitializationInterface.setLocation(LEFT,TOP);
		InitializationInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		InitializationInterface.setLayout(null);

		Container contentPane = InitializationInterface.getContentPane();
		Component CTComponent = createCTJPanel();
		Component ECGComponent = createECGJPanel();
		contentPane.add(CTComponent);
		contentPane.add(ECGComponent);

		JMenuBar mainMenu=new JMenuBar();
		JMenu sys = new JMenu();
		ImageIcon beginIcon = new ImageIcon(getIconImage("Icon/sys.png"));
		sys.setIcon(beginIcon);
		mainMenu.add(sys);;

		JMenu ct=new JMenu("");
		ct.addMenuListener(new SwitchListener(contentPane,CTComponent));
		ImageIcon ctIcon = new ImageIcon(getIconImage("Icon/ct.png"));
		ct.setIcon(ctIcon);
		mainMenu.add(ct);


		JMenu ecg=new JMenu("");
		ecg.addMenuListener(new SwitchListener(contentPane,ECGComponent));
		ImageIcon ecgIcon = new ImageIcon(getIconImage("Icon/monitor.png"));
		ecg.setIcon(ecgIcon);
		mainMenu.add(ecg);

		JMenu guard=new JMenu("");
		guard.addMenuListener(new SwitchListener(contentPane,null));
		guard.setHorizontalTextPosition(SwingConstants.RIGHT);
		ImageIcon guardIcon = new ImageIcon(getIconImage("Icon/guard.png"));
		guard.setIcon(guardIcon);
		JMenuItem guard_config=new JMenuItem("连接告警设备");
		guard_config.addActionListener(new NoticeListener(guardActor,MainUiRequest.MAIN_UI_GUARD_DATA));
		guard.add(guard_config);
		mainMenu.add(guard);

		JMenu mobile=new JMenu("");
		mobile.addMenuListener(new SwitchListener(contentPane,null));
		ImageIcon phoneIcon = new ImageIcon(getIconImage("Icon/phone.png"));
		mobile.setIcon(phoneIcon);
		JMenuItem mobile_config=new JMenuItem("连接手机");
		mobile_config.addActionListener(new NoticeListener(mobileActor,MainUiRequest.MAIN_UI_MOBILE_DATA));
		mobile.add(mobile_config);
		mainMenu.add(mobile);
		InitializationInterface.setJMenuBar(mainMenu);

		InitializationInterface.setVisible(true);
	}

	private URL getIconImage(String path){
		return this.getClass().getClassLoader().getResource(path);
	}
	private JPanel createCTJPanel(){
		JPanel CTPanel= new JPanel(null);
		Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,Color.LIGHT_GRAY,Color.LIGHT_GRAY);
		CTPanel.setBounds(0,0,WIDTH,(int)(HEIGHT*0.9));
		JPanel CTData = new JPanel();
		CTData.setBorder(etchedBorder);
		CTData.setBounds((int)(WIDTH*0.05),(int)(HEIGHT*0.05),(int)(WIDTH*0.65),(int)(HEIGHT*0.8));
		CTPanel.add(CTData);

		JPanel CTControl = new JPanel();
		CTControl.setLayout(new FlowLayout(FlowLayout.CENTER));
		//CTControl.setBorder(etchedBorder);
		CTControl.setBounds((int)(WIDTH*0.75),(int)(HEIGHT*0.05),(int)(WIDTH*0.2),(int)(HEIGHT*0.15));
		JButton CTOpen = new JButton();
		CTOpen.addActionListener(new NoticeListener(ctActor,MainUiRequest.MAIN_UI_CT_CONFIG));
		CTOpen.setText("打开CT图片");
		CTOpen.setIcon(new ImageIcon(getIconImage("Icon/open.png")));
		CTControl.add(CTOpen);
		JButton CTAnalyse = new JButton();
		CTAnalyse.setText("分析CT病灶");
		CTAnalyse.setIcon(new ImageIcon(getIconImage("Icon/analyse_min.png")));
		CTControl.add(CTAnalyse);
		CTPanel.add(CTControl);

		JPanel CTFocus = new JPanel();
		CTFocus.setBorder(etchedBorder);
		CTFocus.setBounds((int)(WIDTH*0.75),(int)(HEIGHT*0.25),(int)(WIDTH*0.2),(int)(HEIGHT*0.6));
		CTPanel.add(CTFocus);

		CTPanel.setVisible(false);
		return CTPanel;
	}
	private JPanel createECGJPanel(){
		JPanel ECGPanel= new JPanel(null);
		Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,Color.LIGHT_GRAY,Color.LIGHT_GRAY);
		ECGPanel.setBounds(0,0,WIDTH,(int)(HEIGHT*0.9));

		JPanel ECGControl = new JPanel();
		//ECGControl.setBorder(etchedBorder);
		ECGControl.setBounds((int)(WIDTH*0.05),(int)(HEIGHT*0.05),(int)(WIDTH*0.65),(int)(HEIGHT*0.10));
		ECGControl.setLayout(new FlowLayout(FlowLayout.CENTER));

		JButton ecgConfig = new JButton();
		ecgConfig.setText("监护仪配置");
		ecgConfig.setIcon(new ImageIcon(getIconImage("Icon/config.png")));
		ecgConfig.addActionListener(new NoticeListener(monitorActor,MainUiRequest.MAIN_UI_ECG_CONFIG));
		ECGControl.add(ecgConfig);
		JButton ecgStart = new JButton();
		ecgStart.setText("开始传输");
		ecgStart.setIcon(new ImageIcon(getIconImage("Icon/start.png")));
		ECGControl.add(ecgStart);
		JButton ecgStop = new JButton();
		ecgStop.setText("停止传输");
		ecgStop.setIcon(new ImageIcon(getIconImage("Icon/stop.png")));
		ECGControl.add(ecgStop);
		JButton ecgAnalyse = new JButton();
		ecgAnalyse.setText("心电图分析");
		ecgAnalyse.setIcon(new ImageIcon(getIconImage("Icon/analyse.png")));
		ecgAnalyse.addActionListener(new NoticeListener(monitorActor,MainUiRequest.MAIN_UI_ECG_ANALYSIS));
		ECGControl.add(ecgAnalyse);
		ECGPanel.add(ECGControl);

		JPanel ECGData = new JPanel();
		ECGData.setBorder(etchedBorder);
		ECGData.setBounds((int)(WIDTH*0.05),(int)(HEIGHT*0.20),(int)(WIDTH*0.65),(int)(HEIGHT*0.65));
		ECGPanel.add(ECGData);

		JPanel ECGAnalyse = new JPanel();
		ECGAnalyse.setBorder(etchedBorder);
		ECGAnalyse.setBounds((int)(WIDTH*0.75),(int)(HEIGHT*0.05),(int)(WIDTH*0.2),(int)(HEIGHT*0.8));
		ECGPanel.add(ECGAnalyse);

		ECGPanel.setVisible(false);
		return ECGPanel;
	}
}
