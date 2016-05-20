/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ecg.myals;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import ecg.model.GuardianData;
import ecg.model.PressureData;
import ecg.model.PumpSpeedData;
import ecg.tcp.TCPConfig;


public class WelcomeWindow extends javax.swing.JFrame {

	private TCPConfig TCPC;
    public static WelcomeWindow welcomeWindow;
    private String surgeryNo = "unknown";
    private GuardianData guardianData;
    private PressureData pressureData;
    private PumpSpeedData pumpSpeedData;
    private FileOutputStream fos;	
	
	
	private static final long serialVersionUID = 1L;	//序列化的版本号
	/**
     * Creates new form WelcomeWindow
     */
    public WelcomeWindow() {		//构造方法
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

    public static void main(String args[]) {
    	
        welcomeWindow = new WelcomeWindow();
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
