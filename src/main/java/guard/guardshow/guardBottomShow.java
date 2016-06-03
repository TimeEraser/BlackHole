package guard.guardshow;

import guard.guardDataProcess.GuardData;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.*;

/**
 * Created by adminstrator on 2016/6/3.
 */
public class GuardBottomShow extends JPanel implements Observer{
    private Icon greenPoint;
    private Icon yellowPoint;
    private Icon redPoint;
    private Icon greyPoint;
    private JLabel connectStatePoint;
    private JLabel temperatureStatePoint;
    private JLabel lightValueStatePoint;
    private static int connectFlashFlag=0;
    private static boolean connectStartFlag=false;
    private static int connectLostCount=0;

    public GuardBottomShow(){
        JLabel connectState=new JLabel("连接状态");
        connectState.setFont(new Font("Dialog", 0, 12));
        connectState.setSize(100,16);
        JLabel temperatureState=new JLabel("血温状态");
        temperatureState.setFont( new Font("Dialog", 0, 12));
        JLabel lightValueState=new JLabel("管内流体状态");
        lightValueState.setFont( new Font("Dialog", 0, 12));
        greenPoint=new ImageIcon(getIconImage("Icon/greenPoint.png"));
        yellowPoint=new ImageIcon(getIconImage("Icon/yellowPoint.png"));
        redPoint=new ImageIcon(getIconImage("Icon/redPoint.png"));
        greyPoint=new ImageIcon(getIconImage("Icon/greyPoint.png"));
        connectStatePoint=new JLabel(redPoint);
        temperatureStatePoint=new JLabel(greyPoint);
        lightValueStatePoint=new JLabel(greyPoint);

        JPanel connectStatePanel=new JPanel();
        connectStatePanel.setLayout(new FlowLayout(FlowLayout.LEADING,20,0));
        connectStatePanel.setPreferredSize(new Dimension(150,20));
        connectStatePanel.add(connectState);
        connectStatePanel.add(connectStatePoint);
        JPanel temperatureStatePanel=new JPanel();
        temperatureStatePanel.setLayout(new FlowLayout(FlowLayout.LEADING,20,0));
        temperatureStatePanel.setPreferredSize(new Dimension(150,20));
        temperatureStatePanel.add(temperatureState);
        temperatureStatePanel.add(temperatureStatePoint);
        JPanel lightValueStatePanel=new JPanel();
        lightValueStatePanel.setLayout(new FlowLayout(FlowLayout.LEADING,20,0));
        lightValueStatePanel.setPreferredSize(new Dimension(150,20));
        lightValueStatePanel.add(lightValueState);
        lightValueStatePanel.add(lightValueStatePoint);

        setLayout(new FlowLayout(FlowLayout.LEADING,150,0));
        add(connectStatePanel);
        add(temperatureStatePanel);
        add(lightValueStatePanel);

        java.util.Timer timer=new java.util.Timer();
        timer.schedule(
                new java.util.TimerTask() {
                    public void run(){
                        if (connectStartFlag){
                            connectLostCount+=1;
                        }
                        if(connectLostCount>3){
                            connectStartFlag=false;
                            connectStatePoint.setIcon(redPoint);
                            connectStatePoint.setVisible(true);
                        }
                    }
                },2000,2000
        );
    }
    private URL getIconImage(String path){
        return this.getClass().getClassLoader().getResource(path);
    }
    @Override
    public void update(Observable o, Object arg) {
        GuardData guardData = (GuardData) arg;
        if(!connectStartFlag){
            connectStatePoint.setIcon(greenPoint);
            connectStartFlag=true;
            temperatureStatePoint.setIcon(greenPoint);
            lightValueStatePoint.setIcon(greenPoint);
        }
        connectLostCount=0;
        connectStatePoint.setVisible((connectFlashFlag%2==1));
        connectFlashFlag+=1;
        if(guardData.getMessage("Temperature")!=null){
            if(guardData.getMessage("Temperature").equals("血温正常")){
                temperatureStatePoint.setIcon(greenPoint);
            }
            else {
                temperatureStatePoint.setIcon(yellowPoint);
            }
        }
        if (guardData.getMessage("Blood")!=null){
            if(guardData.getMessage("Blood").equals("不再漏血")){
                lightValueStatePoint.setIcon(greenPoint);
            }
            else{
                lightValueStatePoint.setIcon(redPoint);
            }
        }
        if(guardData.getMessage("Bubble")!=null){
            if(guardData.getMessage("Bubble").equals("气泡消失")){
                lightValueStatePoint.setIcon(greenPoint);
            }
            else {
                lightValueStatePoint.setIcon(redPoint);
            }
        }
    }
}
