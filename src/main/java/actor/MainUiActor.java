package actor;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.StrokeBorder;
import javax.swing.plaf.FontUIResource;

import actor.Listener.ButtonSwitchListener;
import actor.Listener.NoticeListener;
import actor.Listener.MenuSwitchListener;
import actor.config.MainUiActorConfig;
import com.alee.laf.WebLookAndFeel;
import com.android.dx.command.Main;
import command.*;
import ctshow.CTHistoryData;

import ctshow.CTShowUI;
import ecg.ecgshow.ECGDataRefresher;
import ecg.ecgshow.ECGShowUI;
import ecg.tcp.TCPConfig;

import guard.guardDataProcess.GuardSerialDataProcess;
import guard.guardshow.*;
import util.ImageUtil;


import java.util.Timer;

public class MainUiActor extends BaseActor{

	//Initialize parameter
	//Interactive element
	private CtActor ctActor;
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

	private GuardSerialDataProcess guardSerialDataProcess;
	private GuardBottomShow guardBottomShow;

	private static boolean temperatureAlarmEnable=true;
	private static boolean bloodAlarmEnable=true;
	private static boolean bubbleAlarmEnable=true;


	public MainUiActor(MainUiActorConfig mainUiActorConfig){
		ctActor=mainUiActorConfig.getCtActor();
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
			sendRequest(blackHoleActor,MonitorRequest.MONITOR_ECG_DATA,getECGConnectInfo());
		if(request==MainUiRequest.MAIN_UI_CT_OPEN)
			sendRequest(blackHoleActor,CtRequest.CT_OPEN_IMG,getCTImagePath());
		if(request==MainUiRequest.MAIN_UI_GUARD_SERIAL_PORT_SET){
			sendRequest(blackHoleActor,GuardRequest.GUARD_SERIAL_ASK);
		}
		if(request==MainUiRequest.MAIN_UI_GUARD_TEMPERATURE_HIGH){
			if (temperatureAlarmEnable){
				temperatureAlarmEnable=false;
				createGuardErrorDialog("血温过高!!!");
				java.util.Timer timer=new Timer();
				timer.schedule(
						new java.util.TimerTask() {
							public void run(){
								temperatureAlarmEnable=true;
							}
						},60000
				);
			}
		}
		if(request==MainUiRequest.MAIN_UI_GUARD_TEMPERATURE_LOW){
			if (temperatureAlarmEnable){
				temperatureAlarmEnable=false;
				createGuardErrorDialog("血温过低!!!");
				java.util.Timer timer=new Timer();
				timer.schedule(
						new java.util.TimerTask() {
							public void run(){
								temperatureAlarmEnable=true;
							}
						},60000
				);
			}
		}
		if(request==MainUiRequest.MAIN_UI_GUARD_BLOOD_LEAK) {
			if(bloodAlarmEnable) {
				bloodAlarmEnable = false;
				createGuardErrorDialog("发生漏血!!!");
				java.util.Timer timer=new Timer();
				timer.schedule(
						new java.util.TimerTask() {
							public void run(){
								bloodAlarmEnable=true;
							}
						},60000
				);
			}
		}
		if(request== MainUiRequest.MAIN_UI_GUARD_BUBBLE) {
			if(bubbleAlarmEnable) {
				bubbleAlarmEnable = false;
				createGuardErrorDialog("出现气泡!!!");
				java.util.Timer timer=new Timer();
				timer.schedule(
						new java.util.TimerTask() {
							public void run(){
								bubbleAlarmEnable=true;
							}
						},60000
				);
			}

		}
		if(request==MainUiRequest.MAIN_UI_GUARD_START){
			sendRequest(blackHoleActor,MainUiRequest.MAIN_UI_GUARD_START);
		}
		if(request==MainUiRequest.MAIN_UI_ECG_START_SUCCESS){
			sendRequest(blackHoleActor,MonitorRequest.MONITOR_ECG_DATA_REFRESH);
		}
		return false;
	}

	@Override
	public boolean processActorResponse(Response response) {

		if(response==MonitorResponse.MONITOR_SHUTDOWM){
			return true;
		}
		if(response==CtResponse.CT_OPEN_IMG) {
			System.out.print(response.getConfig().getData());
		}
		if (response==SystemResponse.SYSTEM_FAILURE){
			JOptionPane.showMessageDialog(null,response.getConfig().getData(),"系统错误",JOptionPane.ERROR_MESSAGE);
			System.out.println(response.getConfig().getData());
		}
		if (response==GuardResponse.GUARD_SERIAL_ASK){
			Map tempMap=createGuardConfigDialog((Map)response.getConfig().getData());
			if (tempMap!=null) {
				System.out.println("GuardSet");
				sendRequest(blackHoleActor, GuardRequest.GUARD_SERIAL_SET, tempMap);
			}
		}
		if(response==GuardResponse.GUARD_SERIAL_DATA_PROCESS){
			guardSerialDataProcess=(GuardSerialDataProcess)response.getConfig().getData();
		}
		if(response==MonitorResponse.MONITOR_ECG_DATA_REFRESH){
			ECGDataRefresher ecgDataRefresher=(ECGDataRefresher)response.getConfig().getData();
			if(ecgDataRefresher!=null) {
				ecgDataRefresher.addObserver(guardBottomShow);
			}
		}
		return false;
	}

	@Override
	public boolean start()  {
		sendRequest(blackHoleActor,GuardRequest.GUARD_SERIAL_DATA_PROCESS);
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
		UIManager.put("OptionPane.yesButtonText", "是");
		UIManager.put("OptionPane.noButtonText", "否");
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

		JOptionPane.setRootFrame(InitializationInterface);	//设置窗体，以用于不提供窗体的类方法


		Container contentPane = InitializationInterface.getContentPane();	//容器
		JLayeredPane jLayeredPane=InitializationInterface.getLayeredPane();
		Component CTComponent = createCTJPanel();							//内容块
		Component ECGComponent = createECGJPanel();
		Component GUARDComponent = createGUARDJPanel();
		Component MOBILEComponent = createMOBILEJPanel();
		Component BOTTOMComponent=createBOTTOMJPanel();

		contentPane.add(CTComponent);
		contentPane.add(ECGComponent);
		contentPane.add(GUARDComponent);
		contentPane.add(MOBILEComponent);
		jLayeredPane.add(BOTTOMComponent,JLayeredPane.DRAG_LAYER);
		JMenuBar mainMenu=new JMenuBar();
		try {
			Image sysImage = ImageIO.read(getIconImage("Icon/sys.png"));
			BufferedImage sysBufferImage = ImageUtil.zoom(sysImage, (int) (WIDTH * 0.037), (int) (HEIGHT * 0.065), new Color(1f, 1f, 1f, 0f));
			JMenu sys = new JMenu();
			ImageIcon beginIcon = new ImageIcon(sysBufferImage);
			sys.setIcon(beginIcon);
			mainMenu.add(sys);

			Image ctImage = ImageIO.read(getIconImage("Icon/ct.png"));
			BufferedImage ctBufferImage = ImageUtil.zoom(ctImage, (int) (WIDTH * 0.074), (int) (HEIGHT * 0.065), new Color(1f, 1f, 1f, 0f));
			JMenu ct = new JMenu("");
			ct.addMenuListener(new MenuSwitchListener(contentPane, CTComponent));
			ImageIcon ctIcon = new ImageIcon(ctBufferImage);
			ct.setIcon(ctIcon);
			mainMenu.add(ct);

			Image monitorImage = ImageIO.read(getIconImage("Icon/monitor.png"));
			BufferedImage monitorBufferImage = ImageUtil.zoom(monitorImage, (int) (WIDTH * 0.074), (int) (HEIGHT * 0.065), new Color(1f, 1f, 1f, 0f));
			JMenu ecg = new JMenu("");
			ecg.addMenuListener(new MenuSwitchListener(contentPane, ECGComponent));
			ImageIcon ecgIcon = new ImageIcon(monitorBufferImage);
			ecg.setIcon(ecgIcon);
			mainMenu.add(ecg);

			Image guardImage = ImageIO.read(getIconImage("Icon/guard.png"));
			BufferedImage guardBufferImage = ImageUtil.zoom(guardImage, (int) (WIDTH * 0.074), (int) (HEIGHT * 0.065), new Color(1f, 1f, 1f, 0f));
			JMenu guard = new JMenu("");
			guard.addMenuListener(new MenuSwitchListener(contentPane, GUARDComponent));
			ImageIcon guardIcon = new ImageIcon(guardBufferImage);
			guard.setIcon(guardIcon);

			mainMenu.add(guard);

			Image phoneImage = ImageIO.read(getIconImage("Icon/phone.png"));
			BufferedImage phoneBufferImage = ImageUtil.zoom(phoneImage, (int) (WIDTH * 0.074), (int) (HEIGHT * 0.065), new Color(1f, 1f, 1f, 0f));
			JMenu mobile = new JMenu("");
			mobile.addMenuListener(new MenuSwitchListener(contentPane, MOBILEComponent));
			ImageIcon phoneIcon = new ImageIcon(phoneBufferImage);
			mobile.setIcon(phoneIcon);
			JMenuItem mobile_config = new JMenuItem("连接手机");
			mobile_config.addActionListener(new NoticeListener(mobileActor, MainUiRequest.MAIN_UI_MOBILE_DATA));
			mobile.add(mobile_config);
			mainMenu.add(mobile);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		InitializationInterface.setJMenuBar(mainMenu);
		InitializationInterface.setVisible(true);
	}

	private URL getIconImage(String path){
		return this.getClass().getClassLoader().getResource(path);
	}

	private JPanel createBOTTOMJPanel(){
		JPanel GuardBottom=new JPanel();
		guardBottomShow=new GuardBottomShow();
		guardSerialDataProcess.addObserver(guardBottomShow);
		GuardBottom.setBounds(0,(int)(HEIGHT*0.87),(int)(WIDTH*0.985),(int)(HEIGHT*0.15));
		GuardBottom.setLayout(new BorderLayout());
		GuardBottom.add(guardBottomShow);
		GuardBottom.setVisible(true);
		return GuardBottom;
	}

	private JPanel createGUARDJPanel(){
		JPanel GuardPanel=new JPanel(null);

		Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,Color.LIGHT_GRAY,Color.LIGHT_GRAY);
		GuardPanel.setBounds(0,0,WIDTH,(int)(HEIGHT*0.9));

		JPanel GUARDControl=new JPanel();
		GUARDControl.setBounds((int)(WIDTH*0.61),(int)(HEIGHT*0.01),(int)(WIDTH*0.37),(int)(HEIGHT*0.1));
		GUARDControl.setLayout(new FlowLayout(FlowLayout.LEFT,(int)(WIDTH*0.02),0));

		try {

			Image startImage= ImageIO.read(getIconImage("Icon/start.png"));
			BufferedImage startBufferImage = ImageUtil.zoom(startImage,(int)(WIDTH*0.037),(int)(HEIGHT*0.065),new Color(1f,1f,1f,0f));
			Image stopImage=ImageIO.read(getIconImage("Icon/stop.png"));
			BufferedImage stopBufferImage = ImageUtil.zoom(stopImage,(int)(WIDTH*0.037),(int)(HEIGHT*0.065),new Color(1f,1f,1f,0f));
			Image configImage=ImageIO.read(getIconImage("Icon/config.png"));
			BufferedImage configBufferImage = ImageUtil.zoom(configImage,(int)(WIDTH*0.037),(int)(HEIGHT*0.065),new Color(1f,1f,1f,0f));
			JButton GUARDConnect = new JButton();
			GUARDConnect.setText("连接报警设备");
			GUARDConnect.setIcon(new ImageIcon(startBufferImage));
			ButtonSwitchListener buttonSwitchListener=new ButtonSwitchListener();
			buttonSwitchListener.setText(0,"连接报警设备");
			buttonSwitchListener.setIcon(0,new ImageIcon(startBufferImage));
			buttonSwitchListener.setMessage(0,blackHoleActor,GuardRequest.GUARD_START);
			buttonSwitchListener.setText(1,"断开报警设备");
			buttonSwitchListener.setIcon(1,new ImageIcon(stopBufferImage));
			buttonSwitchListener.setMessage(1,blackHoleActor,GuardRequest.GUARD_SHUT_DOWN);
			GUARDConnect.addActionListener(buttonSwitchListener);
			GUARDControl.add(GUARDConnect);

			JButton GUARDConfigSet = new JButton();
			GUARDConfigSet.setText("报警参数配置");
			GUARDConfigSet.setIcon(new ImageIcon(configBufferImage));
			GUARDConfigSet.addActionListener(new NoticeListener(this,MainUiRequest.MAIN_UI_GUARD_SERIAL_PORT_SET));
			GUARDControl.add(GUARDConfigSet);
			GuardPanel.add(GUARDControl);
		}catch (IOException e) {
			e.printStackTrace();
		}

		JPanel TEMPERATUREShow = new JPanel();
		TemperatureShow temperatureShow=new TemperatureShow();
		guardSerialDataProcess.addObserver(temperatureShow);
		TEMPERATUREShow.setBounds(0,(int)(HEIGHT*0.005),(int)(WIDTH*0.6),(int)(HEIGHT*0.41));
		TEMPERATUREShow.setLayout(new BorderLayout());
		TEMPERATUREShow.add(temperatureShow);
		GuardPanel.add(TEMPERATUREShow);

		JPanel LIGHTValueShow=new JPanel();
		LightValueShow lightValueShow=new LightValueShow();
		LIGHTValueShow.setLayout(new BorderLayout());
		guardSerialDataProcess.addObserver(lightValueShow);
		LIGHTValueShow.setBounds((int)(WIDTH*0.03),(int)(HEIGHT*0.41),(int)(WIDTH*0.25),(int)(HEIGHT*0.38));
		LIGHTValueShow.setLayout(new BorderLayout());
		LIGHTValueShow.add(lightValueShow);
		GuardPanel.add(LIGHTValueShow);


		JPanel LIGHTValueDialShow=new JPanel();
		LightValueDialPlot lightValueDialPlot=new LightValueDialPlot();
		guardSerialDataProcess.addObserver(lightValueDialPlot);
		LIGHTValueDialShow.setBounds((int)(WIDTH*0.34),(int)(HEIGHT*0.41),(int)(WIDTH*0.2),(int)(HEIGHT*0.36));
		LIGHTValueDialShow.setLayout(new BorderLayout());
		LIGHTValueDialShow.add(lightValueDialPlot);
		GuardPanel.add(LIGHTValueDialShow);

		JPanel ALARMShow=new JPanel();
		AlarmShow alarmShow=new AlarmShow(guardSerialDataProcess);
		ALARMShow.setBounds((int)(WIDTH*0.63),(int)(HEIGHT*0.12),(int)(WIDTH*0.35),(int)(HEIGHT*0.63));

		ALARMShow.setLayout(new BorderLayout());
		ALARMShow.add(alarmShow);
		GuardPanel.add(ALARMShow);

		GuardPanel.setVisible(false);
		return GuardPanel;
	}
	private Map<String, String>  createGuardConfigDialog(Map<String, String> connectInfo){
		GuardConfigShow guardConfigShow = new GuardConfigShow(InitializationInterface,true);
		guardSerialDataProcess.addObserver(guardConfigShow);
		guardConfigShow.setSerialNum(Integer.parseInt(connectInfo.get("serialNum")));
		guardConfigShow.setTemperatureLow(Integer.parseInt(connectInfo.get("temperatureLow")));
		guardConfigShow.setTemperatureHigh(Integer.parseInt(connectInfo.get("temperatureHigh")));
		guardConfigShow.setDefaultLightValue(Integer.parseInt(connectInfo.get("defaultLightValue")));
		guardConfigShow.setEmptyLightValue(Integer.parseInt(connectInfo.get("emptyLightValue")));
		guardConfigShow.setBloodLightValue(Integer.parseInt(connectInfo.get("bloodLightValue")));
		guardConfigShow.setBubbleLightValue(Integer.parseInt(connectInfo.get("bubbleLightValue")));
		guardConfigShow.setBubbleHoldCount(Integer.parseInt(connectInfo.get("bubbleHoldCount")));
		guardConfigShow.initComponents();
		guardConfigShow.setVisible(true);
		if(guardConfigShow.getConfirmFlag()) {
			return getGuardConnectInfo(guardConfigShow);
		}
		else return null;
	}
	private Map<String, String> getGuardConnectInfo(GuardConfigShow guardConfigShow){
		String serialNum=String.valueOf(guardConfigShow.getSerialNum());
		String temperatureLow=String.valueOf(guardConfigShow.getTemperatureLow());
		String temperatureHigh=String.valueOf(guardConfigShow.getTemperatureHigh());
		String defaultLightValue=String.valueOf(guardConfigShow.getDefaultLightValue());
		String emptyLightValue=String .valueOf(guardConfigShow.getEmptyLightValue());
		String bloodLightValue= String.valueOf(guardConfigShow.getBloodLightValue());
		String bubbleLightValue=String.valueOf(guardConfigShow.getBubbleLightValue());
		String bubbleHoldCount=String.valueOf(guardConfigShow.getBubbleHoldCount());
		Map<String,String> connectInfo=new HashMap<>();
		connectInfo.put("serialNum",serialNum);
		connectInfo.put("temperatureLow",temperatureLow);
		connectInfo.put("temperatureHigh",temperatureHigh);
		connectInfo.put("defaultLightValue",defaultLightValue);
		connectInfo.put("emptyLightValue",emptyLightValue);
		connectInfo.put("bloodLightValue",bloodLightValue);
		connectInfo.put("bubbleLightValue",bubbleLightValue);
		connectInfo.put("bubbleHoldCount",bubbleHoldCount);
		return connectInfo;
	}
	private void createGuardErrorDialog(String displayString){
		GuardErrorShow guardErrorShow=new GuardErrorShow(InitializationInterface,true,displayString);
		guardErrorShow.setVisible(true);
	}
	//CT部分UI设置
	private JPanel createCTJPanel(){
		JPanel CTPanel= new JPanel(null);
		Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,Color.LIGHT_GRAY,Color.LIGHT_GRAY);	//创建一个具有“浮雕化”外观效果的边框，将组件的当前背景色用于高亮显示和阴影显示。
		CTPanel.setBounds(0,0,WIDTH,(int)(HEIGHT*0.9));

		CTShowUI ctShowUI = new CTShowUI(blackHoleActor);
		JPanel CTData = new JPanel(null);
		CTData.setBounds((int)(WIDTH*0.375)-256,(int)(HEIGHT*0.02),512,512);	//setBounds()设定的是四个值，分别是X坐标和y坐标（其中屏幕的左上角是原点）、宽和高
		CTData.add(ctShowUI.getCtData());

		CTPanel.add(CTData);
		JPanel CTControl = new JPanel();
		CTControl.setLayout(new FlowLayout(FlowLayout.CENTER));
		//CTControl.setBorder(etchedBorder);
		CTControl.setBounds((int)(WIDTH*0.75),(int)(HEIGHT*0.005),(int)(WIDTH*0.2),(int)(HEIGHT*0.29));
		try {
			Image openImage = ImageIO.read(getIconImage("Icon/open.png"));
			BufferedImage openBufferImage = ImageUtil.zoom(openImage, (int) (WIDTH * 0.028), (int) (HEIGHT * 0.049), new Color(1f, 1f, 1f, 0f));
 			Image analyseImage = ImageIO.read(getIconImage("Icon/analyse_min.png"));
			BufferedImage analyseBufferImage = ImageUtil.zoom(analyseImage, (int) (WIDTH * 0.028), (int) (HEIGHT * 0.049), new Color(1f, 1f, 1f, 0f));
 			Image saveImage = ImageIO.read(getIconImage("Icon/save_min.png"));
			BufferedImage saveBufferImage = ImageUtil.zoom(saveImage, (int) (WIDTH * 0.028), (int) (HEIGHT * 0.049), new Color(1f, 1f, 1f, 0f));
 			Image returnImage = ImageIO.read(getIconImage("Icon/ct_return.png"));
			BufferedImage returnBufferImage = ImageUtil.zoom(returnImage, (int) (WIDTH * 0.028), (int) (HEIGHT * 0.049), new Color(1f, 1f, 1f, 0f));
			JButton CTOpen = new JButton();
			CTOpen.setText(" 打开CT图片 ");
			CTOpen.setIcon(new ImageIcon(openBufferImage));
			CTOpen.addActionListener(new NoticeListener(this, MainUiRequest.MAIN_UI_CT_OPEN));
			CTControl.add(CTOpen);
			JButton CTAnalyse = new JButton();
			CTAnalyse.setText(" 分析CT病灶 ");
			CTAnalyse.setIcon(new ImageIcon(analyseBufferImage));
			CTAnalyse.addActionListener(new NoticeListener(this, ctActor, CtRequest.CT_ANALYSIS));
			CTControl.add(CTAnalyse);
			JButton CTSave = new JButton();
			CTSave.setText(" 保存CT结果  ");
			CTSave.setIcon(new ImageIcon(saveBufferImage));
			CTSave.addActionListener(new NoticeListener(this, ctActor, CtRequest.CT_SAVE));
			CTControl.add(CTSave);
			JButton returnCurrent = new JButton();
			returnCurrent.setText("返回当前数据");
			returnCurrent.setIcon(new ImageIcon(returnBufferImage));
			returnCurrent.addActionListener(new NoticeListener(blackHoleActor, CtRequest.CT_UI_RETURN));
			CTControl.add(returnCurrent);
			CTPanel.add(CTControl);
		}catch (IOException e) {
			e.printStackTrace();
		}
		JPanel CTHistory = new JPanel();
		CTHistory.setLayout(new BoxLayout(CTHistory,BoxLayout.Y_AXIS));
		CTHistory.setBorder(etchedBorder);
		CTHistory.setBounds((int)(WIDTH*0.75),(int)(HEIGHT*0.31),(int)(WIDTH*0.20),(int)(HEIGHT*0.47));
		CTHistory.add(ctShowUI.getCtHistoryData());
		CTPanel.add(CTHistory);

		sendRequest(ctActor,CtRequest.CT_UI_CONFIG,ctShowUI);
		CTPanel.setVisible(false);
		return CTPanel;
	}



	private JPanel createECGJPanel(){
		JPanel ECGPanel= new JPanel(null);
		Border etchedBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED,Color.LIGHT_GRAY,Color.LIGHT_GRAY);
		ECGPanel.setBounds(0,0,WIDTH,(int)(HEIGHT*0.9));

		JPanel ECGControl = new JPanel();
		//ECGControl.setBorder(etchedBorder);		//等会注释掉
		ECGControl.setBounds((int)(WIDTH*0.04),(int)(HEIGHT*0.02),(int)(WIDTH*0.75),(int)(HEIGHT*0.10));
		ECGControl.setLayout(new FlowLayout(FlowLayout.LEFT,(int)(WIDTH*0.02),0));
		try {
			Image startImage = ImageIO.read(getIconImage("Icon/start.png"));
			BufferedImage startBufferImage = ImageUtil.zoom(startImage, (int) (WIDTH * 0.037), (int) (HEIGHT * 0.065), new Color(1f, 1f, 1f, 0f));
			Image stopImage = ImageIO.read(getIconImage("Icon/stop.png"));
			BufferedImage stopBufferImage = ImageUtil.zoom(stopImage, (int) (WIDTH * 0.037), (int) (HEIGHT * 0.065), new Color(1f, 1f, 1f, 0f));
			Image configImage = ImageIO.read(getIconImage("Icon/config.png"));
			BufferedImage configBufferImage = ImageUtil.zoom(configImage, (int) (WIDTH * 0.037), (int) (HEIGHT * 0.065), new Color(1f, 1f, 1f, 0f));
			Image analyseImage = ImageIO.read(getIconImage("Icon/analyse.png"));
			BufferedImage analyseBufferImage = ImageUtil.zoom(analyseImage, (int) (WIDTH * 0.037), (int) (HEIGHT * 0.065), new Color(1f, 1f, 1f, 0f));
			Image pauseImage = ImageIO.read(getIconImage("Icon/pause.png"));
			BufferedImage pauseBufferImage = ImageUtil.zoom(pauseImage, (int) (WIDTH * 0.037), (int) (HEIGHT * 0.065), new Color(1f, 1f, 1f, 0f));
			JButton ecgConfig = new JButton();
			ecgConfig.setText("监护仪配置");
			ecgConfig.setIcon(new ImageIcon(configBufferImage));
			ecgConfig.addActionListener(new NoticeListener(this, MainUiRequest.MAIN_UI_ECG_CONFIG, this));
			ECGControl.add(ecgConfig);

			JButton ecgStart = new JButton();
			ecgStart.setText(" 开始传输 ");
			ecgStart.setIcon(new ImageIcon(startBufferImage));
			;
			ButtonSwitchListener buttonSwitchListener = new ButtonSwitchListener();
			buttonSwitchListener.setText(0, " 开始传输 ");
			buttonSwitchListener.setIcon(0, new ImageIcon(startBufferImage));
			buttonSwitchListener.setMessage(0, monitorActor, MonitorRequest.MONITOR_ECG_START);
			buttonSwitchListener.setText(1, " 暂停传输 ");
			buttonSwitchListener.setIcon(1, new ImageIcon(pauseBufferImage));
			buttonSwitchListener.setMessage(1, monitorActor, MonitorRequest.MONITOR_ECG_STOP);
			ecgStart.addActionListener(buttonSwitchListener);
			ECGControl.add(ecgStart);

			JButton ecgStop = new JButton();
			ecgStop.setText(" 停止传输 ");

			ecgStop.setIcon(new ImageIcon(startBufferImage));
			ecgStop.addActionListener(new NoticeListener(this, monitorActor, MonitorRequest.MONITOR_SHUTDOWM,ecgStart.getText()));

			ECGControl.add(ecgStop);

			JButton ecgAnalyse = new JButton();
			ecgAnalyse.setText("心电图分析");
			ecgAnalyse.setIcon(new ImageIcon(analyseBufferImage));
			ecgAnalyse.addActionListener(new NoticeListener(this, monitorActor, MainUiRequest.MAIN_UI_ECG_ANALYSIS));
			//ECGControl.add(ecgAnalyse);
			ECGPanel.add(ECGControl);
		}catch (IOException e){}
		JPanel ECGData = new JPanel();
		//ECGData.setBorder(etchedBorder);	//等会注释掉
		ECGData.setBounds((int)(WIDTH*0.02),(int)(HEIGHT*0.12),(int)(WIDTH*0.58),(int)(HEIGHT*0.66));
		ECGShowUI ecgShowUI=new ECGShowUI("ecg", 5000L);
		ECGData.add(ecgShowUI.getECGData());
		sendRequest(monitorActor,MonitorRequest.ECG_UI_CONFIG,ecgShowUI);
		ECGPanel.add(ECGData);

		JPanel HeartRate=new JPanel(new BorderLayout());
		HeartRate.setLayout(null);
		//HeartRate.setBorder(etchedBorder);	//等会注释掉
		HeartRate.setBounds((int)(WIDTH*0.61),(int)(HEIGHT*0.15),(int)(WIDTH*0.17),(int)(HEIGHT*0.18));
		HeartRate.add(ecgShowUI.getHeartRateData(),BorderLayout.CENTER);
		ECGPanel.add(HeartRate);

		JPanel Pressure=new JPanel();
		//Pressure.setBorder(etchedBorder);	//等会注释掉
		Pressure.setBounds((int)(WIDTH*0.594),(int)(HEIGHT*0.33),(int)(WIDTH*0.20),(int)(HEIGHT*0.22));
		Pressure.add(ecgShowUI.getPressureData());
		ECGPanel.add(Pressure);

		JPanel BloodOxygen=new JPanel(new BorderLayout());
		BloodOxygen.setLayout(null);
		//BloodOxygen.setBorder(etchedBorder);	//等会注释掉
		BloodOxygen.setBounds((int)(WIDTH*0.61),(int)(HEIGHT*0.57),(int)(WIDTH*0.17),(int)(HEIGHT*0.18));
		BloodOxygen.add(ecgShowUI.getBloodOxygenData(),BorderLayout.CENTER);
		ECGPanel.add(BloodOxygen);


		JPanel GuardDataPanel= new JPanel();
		//ECGAnalyse.setBorder(etchedBorder);	//等会注释掉
		GuardDataPanel.setBounds((int)(WIDTH*0.80),(int)(HEIGHT*0.15),(int)(WIDTH*0.18),(int)(HEIGHT*0.60));
		GuardDataPanel.setBackground(new Color(0,150,255));
		guardSerialDataProcess.addObserver(ecgShowUI);

		GuardDataPanel.add(ecgShowUI.getGuardDataPanel());
//		Temperature.setLayout(new BorderLayout());
//		Temperature.add(ecgShowUI.getTemperatureData(),BorderLayout.CENTER);
//
//		JPanel LightValue=new JPanel();
//		LightValue.setBounds((int)(WIDTH*0.80),(int)(HEIGHT*0.35),(int)(WIDTH*0.18),(int)(HEIGHT*0.32));
//		LightValue.setLayout(new BorderLayout());
//		LightValue.add(ecgShowUI.getLightValueData(),BorderLayout.CENTER);

		//ECGAnalyse.add(ecgShowUI.getECGInfo());
		ECGPanel.add(GuardDataPanel);

		ECGPanel.setVisible(false);
		return ECGPanel;
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

