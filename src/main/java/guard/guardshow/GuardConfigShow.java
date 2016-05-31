package guard.guardshow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adminstrator on 2016/5/29.
 */
public class GuardConfigShow extends JDialog{
    private JButton comFirmButton;
    private JLabel serialPortLable;
    private JTextField serialPortNumText;
    private int serialNum;
    public GuardConfigShow(JFrame jFrame,Boolean model){
        super(jFrame,model);
    }
    //初始化构件
    public void initComponents() {
        serialPortLable=new JLabel();
        serialPortLable.setText("端口号");
        serialPortNumText=new JTextField(String.valueOf(serialNum));
        comFirmButton=new JButton();
        comFirmButton.setText("确定");
        comFirmButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                comFirmButtonActionPerformed(evt);
            }
        } );
        GroupLayout layout = new GroupLayout(getContentPane());     //GroupLayout布局
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(comFirmButton)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(serialPortLable)
                                                )
                                                .addGap(40, 40, 40)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(serialPortNumText, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
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
                                        .addComponent(serialPortLable)
                                        .addComponent(serialPortNumText, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(20,20,20)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comFirmButton)
                                .addContainerGap(23, Short.MAX_VALUE)
                        )
        );

        pack();		//调整此窗口的大小，以适合其子组件的首选大小和布局
        setLocationRelativeTo(null);    //窗口居中显示
    }
    private void comFirmButtonActionPerformed(java.awt.event.ActionEvent evt){
        int temp=Integer.parseInt(serialPortNumText.getText());
        if(0<temp&&temp<27) {
            serialNum = temp;
        }
        this.dispose();
    }
//    public JButton getJButton(){
//        return comFirmButton;
//    }
//    public void setJButton(JButton jbutton){
//        comFirmButton=jbutton;
//    }
//    public JLabel getJLabel(){
//        return serialPortLable;
//    }
//    public void setJLabel(JLabel jLabel){
//        serialPortLable=jLabel;
//    }
//    public JTextField getJTextField(){
//        return serialPortNumText;
//    }
//    public void setJTextField(JTextField jTextField){
//        serialPortNumText=jTextField;
//    }
    public int getSerialNum(){
        return serialNum;
    }
    public void setSerialNum(int serialNum){
        this.serialNum=serialNum;
    }
}
