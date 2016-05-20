package ecg.myals;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.alee.laf.WebLookAndFeel;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import ecg.model.GuardianData;
import ecg.model.PressureData;
import ecg.model.PumpSpeedData;
import ecg.tcp.TCPConfig;

public class MyMainWindow {
	private TCPConfig TCPC;
    private String surgeryNo = "unknown";
    private GuardianData guardianData;
    private PressureData pressureData;
    private PumpSpeedData pumpSpeedData;
    private FileOutputStream fos;	
	
    public void MakeWelcomeWindow() {		
        // TODO add your handling code here:
//        java.awt.EventQueue.invokeLater(new Runnable() {
            //    @Override
//            public void run() {
                if(TCPC == null)
                    TCPC = new TCPConfig();
                TCPC.setVisible(true);		//使得框架可见
                try {
                    fos = new FileOutputStream(surgeryNo+"_ecg.dat");
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//            }
//        });
    }	
	
	
	   private JLabel lbl;
	   private JMenuBar mb;
	   private JMenu ct,ecg,ext;
	   private JMenuItem ct_config,ct_analysis,ecg_config,ecg_analysis,clo;
	   public  MyMainWindow()
	   {
	    JFrame jf=new JFrame("医疗健康管理系统");
	    jf.setSize(800,600);
	    jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    Container contentPane=jf.getContentPane();
//	    JPanel contentPane=new JPanel();
//	    jf.setContentPane(contentPane);

	    mb=new JMenuBar();
	    ct=new JMenu("CT处理");
	    ecg=new JMenu("心电仪");
	    ext=new JMenu("退出");
	    ct_config=new JMenuItem("CT配置");
	    ct_analysis=new JMenuItem("CT分析");
	    ecg_config=new JMenuItem("心电仪配置");
	    ecg_analysis=new JMenuItem("心电仪分析");
	    clo=new JMenuItem("关闭窗口");
	    ct_config.addActionListener(new Handler1());
	    ct_analysis.addActionListener(new Handler1());
	    ecg_config.addActionListener(new Handler1());
	    ct_analysis.addActionListener(new Handler1());
	    clo.addActionListener(new Handler1());
	    mb.add(ct);
	    mb.add(ecg);
	    mb.add(ext);
	    ct.add(ct_config);
	    ct.addSeparator();			//菜单项之间的分割线
	    ct.add(ct_analysis);
	    ecg.add(ecg_config);
	    ecg.addSeparator();
	    ecg.add(ecg_analysis);
	    ext.add(clo);
	    jf.setJMenuBar(mb);
	    ct_config.setToolTipText("打开一幅CT图像");		//设置组件的工具提示功能
	    lbl=new JLabel("Menu Example一二三");
	    contentPane.add(lbl); 
//      double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
//    	double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight(); 
	    //jf.pack();
	    jf.setVisible(true);
	   }
	   public static void main(String args[])
	    { 
	   
		   try {
			   //UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");//Nimbus风格，jdk6 update10版本以后的才会出现
			   //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//当前系统风格
			   //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());//跨平台的Java风格
			   //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//windows风格			   
			   //"MS Song"
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
			   UIManager.setLookAndFeel( "com.alee.laf.WebLookAndFeel" );
			   } catch (Exception e) {
			   e.printStackTrace();
			   }

		   new MyMainWindow();
	    }   
	   
	   class Handler1 implements ActionListener
	     {     	
	      public void actionPerformed(ActionEvent e )  
	        { 
	          JMenuItem mi=(JMenuItem) e.getSource();
	          if (mi==ct_config)
	           lbl.setForeground(Color.green);
	          if (mi==ct_analysis)
	           lbl.setForeground(Color.yellow);
	          if (mi==ecg_config)
	        	  MakeWelcomeWindow();
	          if (mi==clo)
	           System.exit(0);
	            } 
  } 
	   
	   public String getSurgeryNo() {
			return surgeryNo;
		}
		public void setSurgeryNo(String surgeryNo) {
			this.surgeryNo = surgeryNo;
		}
	    

		public TCPConfig getTCPC() {
			return TCPC;
		}

		public void setTCPC(TCPConfig tCPC) {
			TCPC = tCPC;
		}


		public GuardianData getGuardianData() {
			return guardianData;
		}

		public void setGuardianData(GuardianData guardianData) {
			this.guardianData = guardianData;
		}

		public PressureData getPressureData() {
			return pressureData;
		}

		public void setPressureData(PressureData pressureData) {
			this.pressureData = pressureData;
		}

		public PumpSpeedData getPumpSpeedData() {
			return pumpSpeedData;
		}

		public void setPumpSpeedData(PumpSpeedData pumpSpeedData) {
			this.pumpSpeedData = pumpSpeedData;
		}

		public FileOutputStream getFos() {
			return fos;
		}

		public void setFos(FileOutputStream fos) {
			this.fos = fos;
		}	   
}
