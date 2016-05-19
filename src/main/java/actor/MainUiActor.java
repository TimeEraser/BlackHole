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
		 mainUiRunningExecutor.shutdown();
	    }

	 
	    @Override
	    public boolean processActorRequest(Request  request) {
	    	if(request == SystemRequest.BOOT){
	    		sendResponse(request, MainUiResponse.MAINUI_CT_AN_ANALYSIS,"");
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
			JFrame jf=new JFrame("主界面");
	    jf.setSize(800,600);
	    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    Container contentPane=jf.getContentPane();
//	    JPanel contentPane=new JPanel();
//	    jf.setContentPane(contentPane);

	    mb=new JMenuBar();
	    ct=new JMenu("CT");
	    ecg=new JMenu("心电仪");
	    guard=new JMenu("告警");
	    mobile=new JMenu("手机`	");
	    
	    ext=new JMenu("退出");
	    ct_config=new JMenuItem("CT配置");
	    ct_analysis=new JMenuItem("CT分析");
	    ecg_config=new JMenuItem("心电配置");
	    ecg_analysis=new JMenuItem("心电分析");
	    clo=new JMenuItem("关闭窗口");
	    ct_config.addActionListener(new CtConfigListener(mainUiActorConfig.getMonitorActor(), ct_config));
	    ct_analysis.addActionListener(new CtConfigListener(lbl, ct_config));
	    ecg_config.addActionListener(new CtConfigListener(lbl, ct_config));
	    ct_analysis.addActionListener(new CtConfigListener(lbl, ct_config));
	    clo.addActionListener(new CtConfigListener(lbl, ct_config));
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
	    ct_config.setToolTipText("��һ��CTͼ��");
	    lbl=new JLabel("Menu Exampleһ����");
	    contentPane.add(lbl); 
//   double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
// 	double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight(); 
	    //jf.pack();
	    		jf.setVisible(true);
	        return false;
	    }

	    @Override
	    public boolean shutdown() {
	        return false;
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
    	sendRequest(communcateActor, MainUiRequest.MAINUI_CT_AN_ANALYSIS);
     }
   }
@Override
protected boolean processActorRequest(Request requests) {
	// TODO �Զ����ɵķ������
	return false;
}
@Override
protected boolean processActorResponse(Response responses) {
	// TODO �Զ����ɵķ������
	return false;
}
@Override
public boolean start() {
	// TODO �Զ����ɵķ������
	return false;
}
@Override
public boolean shutdown() {
	// TODO �Զ����ɵķ������
	return false;
}
}




