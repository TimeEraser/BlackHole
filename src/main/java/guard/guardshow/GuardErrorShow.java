package guard.guardshow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by adminstrator on 2016/5/29.
 */
public class GuardErrorShow extends JDialog {
    private String displayString;
    public GuardErrorShow(JFrame jFrame, Boolean model,String displayString) {
        super(jFrame, model);
        this.displayString=displayString;
        initComponents();
    }
    private void initComponents() {
        JLabel displayLabel=new JLabel();
        displayLabel.setText(displayString);
        JButton comFirmButton=new JButton();
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
                                                        .addComponent(displayLabel)
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
                                        .addComponent(displayLabel)
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
        this.dispose();
    }
}