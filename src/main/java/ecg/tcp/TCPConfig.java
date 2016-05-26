package ecg.tcp;

import javax.swing.*;

public class TCPConfig extends JDialog {		//TCP配置界面

    // Variables declaration
    private JButton jButton1;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;
    private JTextField jTextField4;
    private JRadioButton jRadioButton1;
    private JRadioButton jRadioButton2;
    private String jRadioButtonName;
    // End of variables declaration

    public TCPConfig(JFrame jFrame,Boolean model) {		//构造方法
        super(jFrame,model);
        initComponents();		//初始化构件
    }

    private void initComponents() {      //初始化构件

        jLabel1 = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        jLabel4 = new JLabel();
        jLabel5 = new JLabel();
        jTextField1 = new JTextField();
        jTextField2 = new JTextField();
        jTextField3 = new JTextField();
        jTextField4 = new JTextField();
        jRadioButton1=new JRadioButton("男") ;
        jRadioButton2=new JRadioButton("女") ;
        jButton1 = new JButton();
        ButtonGroup buttonGroup=new ButtonGroup();       //单选按钮组
        buttonGroup.add(jRadioButton1);
        buttonGroup.add(jRadioButton2);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);		//不可更改大小

        jLabel1.setText("心电仪IP地址");
        jLabel2.setText("端口号");
        jLabel3.setText("档案ID");
        jLabel4.setText("姓名");
        jLabel5.setText("性别");

        jButton1.setText("确定");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
                                       public void actionPerformed(java.awt.event.ActionEvent evt) {
                                           jButton1ActionPerformed(evt);       //jButton1ActionPerformed这个方法在后面定义
                                       }
                                   }
        );     //按钮的监听事件

        GroupLayout layout = new GroupLayout(getContentPane());     //GroupLayout布局
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(jButton1)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jLabel5)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel2)
                                                        .addComponent(jLabel1)
                                                )
                                                .addGap(40, 40, 40)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jRadioButton1)
                                                                .addComponent(jRadioButton2)
                                                        )
                                                        .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 160, GroupLayout.PREFERRED_SIZE)
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
                                        .addComponent(jLabel1)
                                        .addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                )
                                .addGap(20,20,20)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addGroup(layout.createParallelGroup()
                                                .addComponent(jRadioButton1)
                                                .addComponent(jRadioButton2)
                                        )
                                )
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1)
                                .addContainerGap(23, Short.MAX_VALUE)
                        )
        );

        pack();		//调整此窗口的大小，以适合其子组件的首选大小和布局
        setLocationRelativeTo(null);    //窗口居中显示
    }//  初始化构件initComponents() 结束


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {  //监听事件
        // TODO add your handling code here:
            if(jRadioButton1.isSelected()){
                jRadioButtonName=jRadioButton1.getText();
            }
            else if (jRadioButton2.isSelected()){
                jRadioButtonName=jRadioButton2.getText();
            }
            this.dispose();
    }

    public JButton getjButton1() {
        return jButton1;
    }
    public void setjButton1(JButton jButton1) {
        this.jButton1 = jButton1;
    }

    public JTextField getjTextField1() {
        return jTextField1;
    }
    public void setjTextField1(JTextField jTextField1) {
        this.jTextField1 = jTextField1;
    }

    public JTextField getjTextField2() {
        return jTextField2;
    }
    public void setjTextField2(JTextField jTextField2) {
        this.jTextField2 = jTextField2;
    }

    public JTextField getjTextField3() {
        return jTextField3;
    }
    public void setjTextField3(JTextField jTextField3) {
        this.jTextField3 = jTextField3;
    }

    public JTextField getjTextField4() {
        return jTextField4;
    }
    public void setjTextField4(JTextField jTextField4) {
        this.jTextField4 = jTextField4;
    }

    public String getJRadioButtonName(){return jRadioButtonName;}

}