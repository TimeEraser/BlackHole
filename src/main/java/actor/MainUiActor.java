package actor;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.plaf.FontUIResource;

import actor.Listener.ButtonSwitchListener;
import actor.Listener.NoticeListener;
import actor.Listener.MenuSwitchListener;
import actor.config.MainUiActorConfig;
import com.alee.laf.WebLookAndFeel;
import command.*;
import ct.ctshow.MandelDraw;
import ecg.ecgshow.ECGDataRefresher;
import ecg.ecgshow.ECGShowUI;
import ecg.tcp.TCPConfig;

public class MainUiActor extends BaseActor{

	//Initialize parameter
	//Interactive element
	private CtActor ctActor;
	private GuardActor guardActor;
	private MonitorActor monitorActor;
	private BlackHoleActor blackHoleActor;
	private MobileActor mobileActor;

	//Interface element
	private JFrame InitializationInterface;
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
		if(request==SystemRequest.BOOT)
			start();
		if(request==MainUiRequest.MAIN_UI_ECG_CONFIG)
			sendRequest(monitorActor,MonitorRequest.MONITOR_ECG_DATA,getECGConnectInfo());
		if(request==MainUiRequest.MAIN_UI_CT_OPEN)
			sendRequest(ctActor,CtRequest.CT_OPEN_IMG,getCTImagePath());
		return false;
	}

	@Override
	public boolean processActorResponse(Response response) {
		if(response==GuardResponse.GUARD_ERROR){
			System.out.print("GuardResponse.GUARD_ERROR");
			return true;
		}
		if(response==MonitorResponse.MONITOR_SHUTDOWM){
			return true;
		}
		if(response==CtResponse.CT_OPEN_IMG) {
			System.out.print(response.getConfig().getData());
		}
		return false;
	}

	@Override
	public boolean start()  {
		this.constructInterface();
		return false;
	}

	@Override
	public boolean shutdown() {return false;}


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
		InitializationInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		InitializationInterface.setLayout(null);

		Container contentPane = InitializationInterface.getContentPane();	//容器
		Component CTComponent = createCTJPanel();							//内容块
		Component ECGComponent = createECGJPanel();
		Component GUARDComponent = createGUARDJPanel();
		Component MOBILEComponent = createMOBILEJPanel();
		contentPane.add(CTComponent);
		contentPane.add(ECGComponent);
		contentPane.add(GUARDComponent);
		contentPane.add(MOBILEComponent);

		JMenuBar mainMenu=new JMenuBar();
		JMenu sys = new JMenu();
		ImageIcon beginIcon = new ImageIcon(getIconImage("Icon/sys.png"));
		sys.setIcon(beginIcon);
		mainMenu.add(sys);

		JMenu ct=new JMenu("");
		ct.addMenuListener(new MenuSwitchListener(contentPane,CTComponent));
		ImageIcon ctIcon = new ImageIcon(getIconImage("Icon/ct.png"));
		ct.setIcon(ctIcon);
		mainMenu.add(ct);


		JMenu ecg=new JMenu("");
		ecg.addMenuListener(new MenuSwitchListener(contentPane,ECGComponent));
		ImageIcon ecgIcon = new ImageIcon(getIconImage("Icon/monitor.png"));
		ecg.setIcon(ecgIcon);
		mainMenu.add(ecg);

		JMenu guard=new JMenu("");
		guard.addMenuListener(new MenuSwitchListener(contentPane,GUARDComponent));
		guard.setHorizontalTextPosition(SwingConstants.RIGHT);
		ImageIcon guardIcon = new ImageIcon(getIconImage("Icon/guard.png"));
		guard.setIcon(guardIcon);
		JMenuItem guard_config=new JMenuItem("连接告警设备");
		guard_config.addActionListener(new NoticeListener(this,guardActor,MainUiRequest.MAIN_UI_GUARD_START));
		guard.add(guard_config);
		mainMenu.add(guard);

		JMenu mobile=new JMenu("");
		mobile.addMenuListener(new MenuSwitchListener(contentPane,MOBILEComponent));
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
		MandelDraw mandelDraw=new MandelDraw();

		JPanel CTData = new JPanel(new FlowLayout(FlowLayout.CENTER));
		CTData.setBorder(etchedBorder);
		CTData.setBounds((int)(WIDTH*0.05),(int)(HEIGHT*0.02),(int)(WIDTH*0.65),(int)(HEIGHT*0.81));
		GridBagConstraints c = new GridBagConstraints();
		CTData.add(mandelDraw);
		sendRequest(ctActor,CtRequest.CT_UI_CONFIG,mandelDraw);
		CTPanel.add(CTData);

		JPanel CTControl = new JPanel();
		CTControl.setLayout(new FlowLayout(FlowLayout.CENTER));
		//CTControl.setBorder(etchedBorder);
		CTControl.setBounds((int)(WIDTH*0.75),(int)(HEIGHT*0.05),(int)(WIDTH*0.2),(int)(HEIGHT*0.15));
		JButton CTOpen = new JButton();
		CTOpen.setText("打开CT图片");
		CTOpen.setIcon(new ImageIcon(getIconImage("Icon/open.png")));
		CTOpen.addActionListener(new NoticeListener(this,MainUiRequest.MAIN_UI_CT_OPEN));
		CTControl.add(CTOpen);
		JButton CTAnalyse = new JButton();
		CTAnalyse.setText("分析CT病灶");
		CTAnalyse.setIcon(new ImageIcon(getIconImage("Icon/analyse_min.png")));
		CTAnalyse.addActionListener(new NoticeListener(this,ctActor,MainUiRequest.MAIN_UI_CT_ANALYSIS));
		CTControl.add(CTAnalyse);
		CTPanel.add(CTControl);

		JPanel CTFocus = new JPanel();
		CTFocus.setBorder(etchedBorder);
		CTFocus.setBounds((int)(WIDTH*0.75),(int)(HEIGHT*0.25),(int)(WIDTH*0.2),(int)(HEIGHT*0.6));
		CTFocus.setLayout(new GridLayout(2,1));
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
		ecgConfig.addActionListener(new NoticeListener(this,MainUiRequest.MAIN_UI_ECG_CONFIG,this));
		ECGControl.add(ecgConfig);

		JButton ecgStart = new JButton();
		ecgStart.setText("开始传输");
		ecgStart.setIcon(new ImageIcon(getIconImage("Icon/start.png")));
		ecgStart.addActionListener(new NoticeListener(monitorActor,MonitorRequest.MONITOR_ECG_START));
		ButtonSwitchListener buttonSwitchListener=new ButtonSwitchListener();
		buttonSwitchListener.setText(0,"开始传输");
		buttonSwitchListener.setIcon(0,new ImageIcon(getIconImage("Icon/start.png")));
		buttonSwitchListener.setActionListener(0,new NoticeListener(monitorActor,MonitorRequest.MONITOR_ECG_START));
		buttonSwitchListener.setText(1,"暂停传输");
		buttonSwitchListener.setIcon(1,new ImageIcon(getIconImage("Icon/pause.png")));
		buttonSwitchListener.setActionListener(1,new NoticeListener(monitorActor,MonitorRequest.MONITOR_ECG_STOP));
		ecgStart.addActionListener(buttonSwitchListener);
		ECGControl.add(ecgStart);

		JButton ecgStop = new JButton();
		ecgStop.setText("停止传输");
		ecgStop.setIcon(new ImageIcon(getIconImage("Icon/stop.png")));
		ecgStop.addActionListener(new NoticeListener(monitorActor,MonitorRequest.MONITOR_SHUTDOWM));
		ECGControl.add(ecgStop);

		JButton ecgAnalyse = new JButton();
		ecgAnalyse.setText("心电图分析");
		ecgAnalyse.setIcon(new ImageIcon(getIconImage("Icon/analyse.png")));
		ecgAnalyse.addActionListener(new NoticeListener(monitorActor,MainUiRequest.MAIN_UI_ECG_ANALYSIS));
		ECGControl.add(ecgAnalyse);
		ECGPanel.add(ECGControl);

		JPanel ECGData = new JPanel();
		//ECGData.setBorder(etchedBorder);
		ECGData.setBounds((int)(WIDTH*0.05),(int)(HEIGHT*0.20),(int)(WIDTH*0.65),(int)(HEIGHT*0.65));
		ECGShowUI ecgShowUI=new ECGShowUI("ecg", 5000L);
		ECGData.add(ecgShowUI.getECGData());
		sendRequest(monitorActor,MonitorRequest.ECG_UI_CONFIG,ecgShowUI);
		ECGPanel.add(ECGData);

		JPanel ECGAnalyse = new JPanel();
		//ECGAnalyse.setBorder(etchedBorder);
		ECGAnalyse.setBounds((int)(WIDTH*0.75),(int)(HEIGHT*0.05),(int)(WIDTH*0.2),(int)(HEIGHT*0.8));
		ECGPanel.add(ECGAnalyse);

		ECGPanel.setVisible(false);
		return ECGPanel;
	}
	private JPanel createGUARDJPanel(){
		JPanel GUARDPanel= new JPanel(null);
		GUARDPanel.setVisible(false);
		return GUARDPanel;
	}

	private JPanel createMOBILEJPanel(){
		JPanel MOBILEPanel= new JPanel(null);
		MOBILEPanel.setVisible(false);
		return MOBILEPanel;
	}

	private Map<String, String> getECGConnectInfo(){
		TCPConfig tcpConfig = new TCPConfig(InitializationInterface,true);
		tcpConfig.setVisible(true);
		String host=tcpConfig.getjTextField1().getText();//主机域名
		String port=tcpConfig.getjTextField2().getText();//主机端口号
		String Id=tcpConfig.getjTextField3().getText(); //档案ID
		String Name=tcpConfig.getjTextField4().getText();//姓名
		String Sex=tcpConfig.getJRadioButtonName();//性别
		Map<String,String> connectInfo = new HashMap<>();
		connectInfo.put("host",host);
		connectInfo.put("port",port);
		connectInfo.put("Id",Id);
		connectInfo.put("Name",Name);
		connectInfo.put("Sex",Sex);
		return connectInfo;
	}
	private  String getCTImagePath(){
		String imagePath=null;
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fileChooser.showOpenDialog(null);
		if(returnVal==fileChooser.APPROVE_OPTION){
			imagePath = fileChooser.getSelectedFile().getAbsolutePath();
		}
		return imagePath;
	}
}
