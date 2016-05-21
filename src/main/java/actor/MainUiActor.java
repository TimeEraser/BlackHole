package actor;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
import command.Command;
import command.CtRequest;
import command.GuardRequest;
import command.MainUiRequest;
import command.MobileRequest;
import command.MobileResponse;
import command.MonitorRequest;
import command.Request;
import command.Response;
import command.MainUiResponse;
import command.SystemRequest;
import command.config.CommandConfig;


public class MainUiActor extends BaseActor{
	 	 
	private String name;
	private JLabel lbl;
	private JMenuBar mb;
	private JMenu ct,ecg,ext,guard,mobile;
	private JMenuItem ct_config,ct_analysis,ecg_config,ecg_analysis,clo;
	private MainUiActorConfig mainUiActorConfig;
	private MonitorActor monitorActor;
	public MainUiActor(MainUiActorConfig mainUiActorConfig){
	        //TO DO Initialize the MainUiActor
		 this.mainUiActorConfig=mainUiActorConfig;
		 ExecutorService mainUiRunningExecutor=Executors.newFixedThreadPool(mainUiActorConfig.getMAINUI_THREAD_POOL_SIZE());
		 //mainUiRunningExecutor.shutdown();
	    }

	 
	    @Override
	    public boolean processActorRequest(Request  request) {
	    	if(request == SystemRequest.BOOT){
	    		sendResponse(request, MainUiResponse.MAINUI_CT_AN_ANALYSIS,"收到SystemRequest.BOOT请求，启动mainUi");
	    		start();
	    	}
	        return false;
	    }

	    @Override
	    public boolean processActorResponse(Response responses) {

			return false;
	    }

	    @Override
	    public boolean start()  {
			makeUi();
	        return false;
	    }

	    @Override
	    public boolean shutdown() {
	        return false;
	    }


	private void  makeUi(){
		WebLookAndFeel.globalControlFont  = new FontUIResource("Dialog",0, 12);
		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		UIManager.put("ToolTip.font", new Font("Dialog", 0, 12));
		UIManager.put("Menu.font", new Font("Dialog", 0, 12));
		UIManager.put("MenuItem.font", new Font("Dialog", 0, 12));
		UIManager.put("Panel.font", new Font("Dialog", Font.PLAIN, 12));
		UIManager.put("Label.font", new Font("Dialog", 0, 12));
		UIManager.put("Button.font", new Font("Dialog", 0, 12));
		UIManager.put("CheckBox.font", new Font("Dialog", 0, 12));
		UIManager.put("ComboBox.font", new Font("Dialog", 0, 12));
		UIManager.put("RadioButton.font", new Font("Dialog", 0, 12));
		UIManager.put("TitledBorder.font", new Font("Dialog", 0, 12));
		UIManager.put("TabbedPane.font", new Font("Dialog", 0, 12));
		UIManager.put("List.font", new Font("Dialog", 0, 12));
		UIManager.put("InternalFrame.titleFont", new Font("Dialog", 0, 12));
		UIManager.put("CheckBoxMenuItem.font", new Font("Dialog", 0, 12));
		UIManager.put("Table.font", new Font("Dialog", 0, 12));
		UIManager.put("TableHeader.font", new Font("Dialog", 0, 12));
		UIManager.put("TextField.font", new Font("Dialog", 0, 12));
		UIManager.put("TextArea.font", new Font("Dialog", 0, 12));
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
		JFrame jf=new JFrame("医疗健康管理系统");
		jf.setSize(800,600);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Container contentPane=jf.getContentPane();
//	    JPanel contentPane=new JPanel();
//	    jf.setContentPane(contentPane);

		mb=new JMenuBar();
		ct=new JMenu("CT");
		ecg=new JMenu("心电仪");
		guard=new JMenu("告警");
		mobile=new JMenu("手机");
		ext=new JMenu("退出");

		ct_config=new JMenuItem("CT配置");
		ct_analysis=new JMenuItem("CT分析");
		ecg_config=new JMenuItem("心电仪配置");
		ecg_analysis=new JMenuItem("心电仪分析");
		clo=new JMenuItem("关闭窗口");

		ct_config.addActionListener(new CtConfigListener(mainUiActorConfig.getCtActor(), ct_config));
		ct_analysis.addActionListener(new CtAnalysisListener(mainUiActorConfig.getCtActor(), ct_analysis));
		ecg_config.addActionListener(new EcgConfigListener(mainUiActorConfig.getMonitorActor(), ecg_config));
		ecg_analysis.addActionListener(new EcgAnalysisListener(mainUiActorConfig.getMonitorActor(),ecg_analysis));
		clo.addActionListener(new CloseListener(mainUiActorConfig.getBlackHoleActor(),clo));

		mb.add(ct);
		mb.add(ecg);
		mb.add(guard);
		mb.add(mobile);
		mb.add(ext);
		ct.add(ct_config);
		ct.addSeparator();
		ct.add(ct_analysis);
		ecg.add(ecg_config);
		ecg.addSeparator();
		ecg.add(ecg_analysis);
		ext.add(clo);
		jf.setJMenuBar(mb);
		ct_config.setToolTipText("打开一张CT图像");
		lbl=new JLabel("Menu Example一二三");
		contentPane.add(lbl);
		jf.setVisible(true);
	}
}







class CtConfigListener extends BaseActor implements ActionListener
{     	
	JLabel lb1;
	JMenuItem jMenuItem;
	BaseActor communcateActor;
	public CtConfigListener(JLabel lb1,JMenuItem ct_config){
		this.lb1=lb1;
		this.jMenuItem=ct_config;
	}
	public CtConfigListener(BaseActor commuBaseActor ,JMenuItem ct_config) {
		this.communcateActor=commuBaseActor;
		this.jMenuItem=ct_config;
	}

 public void actionPerformed(ActionEvent e )  
   { 
     JMenuItem mi=(JMenuItem) e.getSource();
     if(mi==jMenuItem){
    	sendRequest(communcateActor, CtRequest.CT_OPEN_IMG);
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

	JLabel lb1;
	JMenuItem jMenuItem;
	BaseActor communcateActor;
	public CtAnalysisListener(JLabel lb1,JMenuItem ct_config){
		this.lb1=lb1;
		this.jMenuItem=ct_config;
	}
	public CtAnalysisListener(BaseActor commuBaseActor ,JMenuItem ct_analysis) {
		this.communcateActor=commuBaseActor;
		this.jMenuItem=ct_analysis;
	}



	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem) e.getSource();
		if(mi==jMenuItem){
			sendRequest(communcateActor, CtRequest.CT_ANALYSIS);
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
	JLabel lb1;
	JMenuItem jMenuItem;
	BaseActor communcateActor;
	public EcgConfigListener(JLabel lb1,JMenuItem ecg_config){
		this.lb1=lb1;
		this.jMenuItem=ecg_config;
	}
	public EcgConfigListener(BaseActor commuBaseActor ,JMenuItem ecg_config) {
		this.communcateActor=commuBaseActor;
		this.jMenuItem=ecg_config;
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem) e.getSource();
		if(mi==jMenuItem){
			sendRequest(communcateActor, MonitorRequest.MONITOR_ECG_DATA);
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
	JLabel lb1;
	JMenuItem jMenuItem;
	BaseActor communcateActor;
	public EcgAnalysisListener(JLabel lb1,JMenuItem ecg_analysis){
		this.lb1=lb1;
		this.jMenuItem=ecg_analysis;
	}
	public EcgAnalysisListener(BaseActor commuBaseActor ,JMenuItem ecg_analysis) {
		this.communcateActor=commuBaseActor;
		this.jMenuItem=ecg_analysis;
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem) e.getSource();
		if(mi==jMenuItem){
			sendRequest(communcateActor, MonitorRequest.MONITOR_ECG_DATA);
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
	JLabel lb1;
	JMenuItem jMenuItem;
	BaseActor communcateActor;
	public CloseListener(JLabel lb1,JMenuItem clo){
		this.lb1=lb1;
		this.jMenuItem=clo;
	}
	public CloseListener(BaseActor commuBaseActor ,JMenuItem clo) {
		this.communcateActor=commuBaseActor;
		this.jMenuItem=clo;
	}

	public void actionPerformed(ActionEvent e) {
		JMenuItem mi=(JMenuItem) e.getSource();
		if(mi==jMenuItem){
			sendRequest(communcateActor, SystemRequest.SHUTDOWN);
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