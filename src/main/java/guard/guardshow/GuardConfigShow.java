package guard.guardshow;

import guard.guardDataProcess.GuardData;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by adminstrator on 2016/5/29.
 */
public class GuardConfigShow extends JDialog implements Observer{
    private JTextField serialPortNumText;
    private JTextField temperatureLowText;
    private JTextField temperatureHighText;
    private JTextField defaultLightValueText;
    private JTextField bloodLightValueText;
    private JTextField bubbleLightValueText;
    private JTextField bubbleHoldCountText;

    private int serialNum;
    private int temperatureLow;
    private int temperatureHigh;
    private int defaultLightValue;
    private int bloodLightValue;
    private int bubbleLightValue;
    private int bubbleHoldCount;
    private boolean confirmFlag=false;
    private boolean temperatureAdjustEnableFlag=false;
    private boolean defaultLightValueAdjustEnableFlag=false;
    private boolean bloodLightValueAdjustEnableFlag=false;
//    private boolean bubbleLightValueAdjustEnableFlag=false;
    private boolean beginAdjust=false;

    public GuardConfigShow(JFrame jFrame,Boolean model){
        super(jFrame,model);
    }
    //初始化构件
    public void initComponents() {
        JLabel serialPortLabel=new JLabel("端口号(1-26)");
        JLabel temperatureLowLabel=new JLabel("下限温度(15-35)");
        JLabel temperatureHighLabel=new JLabel("上限温度(30-45)");
        JLabel defaultLightValueLabel=new JLabel("正常工作透光值(0-1024)");
        JLabel bloodLightValueLabel=new JLabel("漏血报警阈值(0-1024)");
        JLabel bubbleLightValueLabel=new JLabel("气泡报警差值(10-50)");
        JLabel bubbleHoldCountLabel=new JLabel("气泡大小报警阈值(1-10)");

        serialPortNumText=new JTextField(String.valueOf(serialNum));
        temperatureLowText=new JTextField(String.valueOf(temperatureLow));
        temperatureHighText=new JTextField(String.valueOf(temperatureHigh));
        defaultLightValueText=new JTextField(String.valueOf(defaultLightValue));
        bloodLightValueText=new JTextField(String.valueOf(bloodLightValue));
        bubbleLightValueText=new JTextField(String.valueOf(bubbleLightValue));
        bubbleHoldCountText=new JTextField(String.valueOf(bubbleHoldCount));

        JButton confirmButton=new JButton();
        confirmButton.setText("确定");
        confirmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                confirmButtonActionPerformed(evt);
            }
        } );
        JButton temperatureAdjustButton=new JButton();
        temperatureAdjustButton.setText("温度校准");
        temperatureAdjustButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                temperatureAdjustButtonActionPerformed(evt);
            }
        } );
        JButton defaultLightValueAdjustButton=new JButton();
        defaultLightValueAdjustButton.setText("校准");
        defaultLightValueAdjustButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                defaultLightValueAdjustButtonActionPerformed(evt);
            }
        } );
        JButton bloodLightValueAdjustButton=new JButton();
        bloodLightValueAdjustButton.setText("校准");
        bloodLightValueAdjustButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                bloodLightValueAdjustButtonActionPerformed(evt);
            }
        } );
//        JButton bubbleLightValueAdjustButton=new JButton();
//        bubbleLightValueAdjustButton.setText("校准");
//        bubbleLightValueAdjustButton.addActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent evt){
//                bubbleLightValueButtonActionPerformed(evt);
//            }
//        } );
        JButton cancelButton=new JButton();
        cancelButton.setText("取消");
        cancelButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                cancelButtonActionPerformed(evt);
            }
        } );
        GroupLayout layout = new GroupLayout(getContentPane());     //GroupLayout布局
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(serialPortLabel, GroupLayout.Alignment.LEADING)
                                                        .addComponent(temperatureLowLabel,GroupLayout.Alignment.LEADING)
                                                        .addComponent(temperatureHighLabel,GroupLayout.Alignment.LEADING)
                                                        .addComponent(defaultLightValueLabel,GroupLayout.Alignment.LEADING)
                                                        .addComponent(bloodLightValueLabel,GroupLayout.Alignment.LEADING)
                                                        .addComponent(bubbleLightValueLabel,GroupLayout.Alignment.LEADING)
                                                        .addComponent(bubbleHoldCountLabel,GroupLayout.Alignment.LEADING)
                                                        .addComponent(confirmButton, GroupLayout.Alignment.CENTER)
                                                )
                                                .addGap(40, 40, 40)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(temperatureAdjustButton)
                                                )
                                                .addGap(40,40,40)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(serialPortNumText, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(temperatureLowText, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(temperatureHighText, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(defaultLightValueText, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(bloodLightValueText, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(bubbleLightValueText, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(bubbleHoldCountText, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(cancelButton, GroupLayout.Alignment.CENTER)
                                                )
                                                .addGap(20,20,20)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(defaultLightValueAdjustButton, GroupLayout.Alignment.CENTER)
                                                        .addComponent(bloodLightValueAdjustButton, GroupLayout.Alignment.CENTER)
//                                                        .addComponent(bubbleLightValueAdjustButton, GroupLayout.Alignment.CENTER)
                                                )
                                        )
                                )
                                .addContainerGap(85, Short.MAX_VALUE)
                        )
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(serialPortLabel)
                                        .addComponent(serialPortNumText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(temperatureLowLabel)
                                        .addComponent(temperatureLowText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(temperatureHighLabel)
                                        .addComponent(temperatureHighText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(defaultLightValueLabel)
                                        .addComponent(defaultLightValueText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(defaultLightValueAdjustButton, GroupLayout.Alignment.BASELINE)
                                )
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(bloodLightValueLabel)
                                        .addComponent(bloodLightValueText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(bloodLightValueAdjustButton, GroupLayout.Alignment.BASELINE)
                                )
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(bubbleLightValueLabel)
                                        .addComponent(bubbleLightValueText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//                                        .addComponent(bubbleLightValueAdjustButton, GroupLayout.Alignment.BASELINE)
                                )
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(bubbleHoldCountLabel)
                                        .addComponent(bubbleHoldCountText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(confirmButton)
                                        .addComponent(temperatureAdjustButton,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cancelButton,GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addContainerGap(23, Short.MAX_VALUE)
                        )
        );

        pack();		//调整此窗口的大小，以适合其子组件的首选大小和布局
        setLocationRelativeTo(null);    //窗口居中显示
    }
    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt){
        boolean errorFlag=false;
        int serialNum=Integer.parseInt(serialPortNumText.getText());
        if(0<serialNum&&serialNum<27) {
            this.serialNum=serialNum;
        }
        else{
            errorFlag=true;
        }
//        System.out.println(errorFlag);
        int temperatureLow=Integer.parseInt(temperatureLowText.getText());
        if(!errorFlag&&temperatureLow<35&&temperatureLow>15){
            this.temperatureLow=temperatureLow;
        }
        else {
            errorFlag=true;
        }
//        System.out.println(errorFlag);
        int temperatureHigh=Integer.parseInt(temperatureHighText.getText());
        if (!errorFlag&&temperatureHigh>30&&temperatureHigh<45){
            this.temperatureHigh=temperatureHigh;
        }
        else {
            errorFlag = true;
        }
//        System.out.println(errorFlag);
        int defaultLightValue=Integer.parseInt(defaultLightValueText.getText());
        if(!errorFlag&&defaultLightValue>0&&defaultLightValue<1024){
            this.defaultLightValue=defaultLightValue;
        }
        else{
            errorFlag=true;
        }
//        System.out.println(errorFlag);
        int bloodLightValue=Integer.parseInt(bloodLightValueText.getText());
        if(!errorFlag&&bloodLightValue>0&&defaultLightValue<1024){
            this.bloodLightValue=bloodLightValue;
        }
        else {
            errorFlag=true;
        }
//        System.out.println(errorFlag);
        int bubbleLightValue=Integer.parseInt(bubbleLightValueText.getText());
//        System.out.println(bubbleLightValue);
        if(!errorFlag&&bubbleLightValue>10&&bubbleLightValue<50){
            this.bubbleLightValue=bubbleLightValue;
        }
        else {
            errorFlag=true;
        }
//        System.out.println(errorFlag);
//        System.out.println(defaultLightValue);
//        System.out.println(bloodLightValue);
        if(!errorFlag&&defaultLightValue<bloodLightValue){
            errorFlag=true;
        }
//        System.out.println(errorFlag);
        int bubbleHoldCount=Integer.parseInt(bubbleHoldCountText.getText());
        if(!errorFlag&&bubbleHoldCount>=1&&bubbleHoldCount<=10){
            this.bubbleHoldCount=bubbleHoldCount;
        }
        else {
            errorFlag=true;
        }
//        System.out.println(errorFlag);
        if(errorFlag) {
            JOptionPane.showMessageDialog(null, "配置参数有误", "系统错误", JOptionPane.ERROR_MESSAGE);
        }
        else {
            confirmFlag = true;
            this.dispose();
        }
    }
    private void temperatureAdjustButtonActionPerformed(java.awt.event.ActionEvent evt){
        if(!temperatureAdjustEnableFlag) {
            temperatureAdjustEnableFlag = true;
            beginAdjust = true;
            java.util.Timer timer=new java.util.Timer();
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run(){
                            if(beginAdjust){
                                JOptionPane.showMessageDialog(null, "请先连接设备", "系统错误", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    },4000
            );
        }
    }
    private void defaultLightValueAdjustButtonActionPerformed(java.awt.event.ActionEvent evt){
        if(!defaultLightValueAdjustEnableFlag) {
            defaultLightValueAdjustEnableFlag = true;
            beginAdjust=true;
            java.util.Timer timer=new java.util.Timer();
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run(){
                            if(beginAdjust){
                                JOptionPane.showMessageDialog(null, "请先连接设备", "系统错误", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    },4000
            );
        }
    }
    private void bloodLightValueAdjustButtonActionPerformed(java.awt.event.ActionEvent evt){
        if(!bloodLightValueAdjustEnableFlag) {
            bloodLightValueAdjustEnableFlag = true;
            beginAdjust=true;
            java.util.Timer timer=new java.util.Timer();
            timer.schedule(
                    new java.util.TimerTask() {
                        public void run(){
                            if(beginAdjust){
                                JOptionPane.showMessageDialog(null, "请先连接设备", "系统错误", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    },4000
            );
        }
    }
//    private void bubbleLightValueButtonActionPerformed(java.awt.event.ActionEvent evt){
//        if(!bubbleLightValueAdjustEnableFlag) {
//            bubbleLightValueAdjustEnableFlag = true;
//            beginAdjust = true;
//            java.util.Timer timer=new java.util.Timer();
//            timer.schedule(
//                    new java.util.TimerTask() {
//                        public void run(){
//                            if(beginAdjust){
//                                JOptionPane.showMessageDialog(null, "请先连接设备", "系统错误", JOptionPane.ERROR_MESSAGE);
//                            }
//                        }
//                    },4000
//            );
//        }
//    }
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt){
        confirmFlag=false;
        this.dispose();
    }
    private void adjustTemperature(GuardData data){
        int nowTemperature=Integer.parseInt(data.getTemperature().substring(0,data.getTemperature().indexOf('.')));
        temperatureLowText.setText(String.valueOf(nowTemperature-5));
        temperatureHighText.setText(String.valueOf(nowTemperature+5));
        temperatureAdjustEnableFlag=false;
    }
    private void adjustDefaultLightValue(GuardData data){
        defaultLightValueText.setText(data.getLightValue());
        defaultLightValueAdjustEnableFlag=false;
    }
    private void adjustBloodLightValue(GuardData data){
        bloodLightValueText.setText(data.getLightValue());
        bloodLightValueAdjustEnableFlag=false;
    }
//    private void adjustBubbleLightValue(GuardData data){
//        bubbleLightValueText.setText(data.getLightValue());
//        bubbleLightValueAdjustEnableFlag=false;
//    }
    public void setTemperatureLow(int temperatureLow){
        this.temperatureLow=temperatureLow;
    }
    public int getTemperatureLow(){
        return temperatureLow;
    }
    public void setTemperatureHigh(int temperatureHigh){
        this.temperatureHigh=temperatureHigh;
    }
    public int getTemperatureHigh(){
        return temperatureHigh;
    }
    public void setDefaultLightValue(int defaultLightValue){
        this.defaultLightValue=defaultLightValue;
    }
    public int getDefaultLightValue(){
        return defaultLightValue;
    }
    public void setBloodLightValue(int bloodLightValue){
        this.bloodLightValue=bloodLightValue;
    }
    public int getBloodLightValue(){
        return bloodLightValue;
    }
    public void setBubbleLightValue(int bubbleLightValue){
        this.bubbleLightValue=bubbleLightValue;
    }
    public int getBubbleLightValue(){
        return bubbleLightValue;
    }
    public void setBubbleHoldCount(int bubbleHoldCount){
        this.bubbleHoldCount=bubbleHoldCount;
    }
    public int getBubbleHoldCount(){
        return bubbleHoldCount;
    }
    public int getSerialNum(){
        return serialNum;
    }
    public void setSerialNum(int serialNum){
        this.serialNum=serialNum;
    }
    public boolean getConfirmFlag(){
        return confirmFlag;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(temperatureAdjustEnableFlag){
            beginAdjust=false;
            adjustTemperature((GuardData)arg);
        }
        if(defaultLightValueAdjustEnableFlag){
            beginAdjust=false;
            adjustDefaultLightValue((GuardData)arg);
        }
        if(bloodLightValueAdjustEnableFlag){
            beginAdjust=false;
            adjustBloodLightValue((GuardData)arg);
        }
//        if (bubbleLightValueAdjustEnableFlag){
//            beginAdjust=false;
//            adjustBubbleLightValue((GuardData)arg);
//        }
    }
}

