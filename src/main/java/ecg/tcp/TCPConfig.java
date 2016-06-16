package ecg.tcp;

import config.ConfigCenter;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

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
    private String jRadioButtonName=null;



    private   String filename = ConfigCenter.getString("ecg.tcp.TCPConfig.save");
    FileWriter fwriter = null;
    BufferedReader reader = null;
    File file=new File(filename );
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
        jTextField1 = new JTextField(null);
        jTextField2 = new JTextField(null);
        jTextField3 = new JTextField(null);
        jTextField4 = new JTextField(null);
        jRadioButton1=new JRadioButton("男") ;
        jRadioButton2=new JRadioButton("女") ;
        jRadioButtonName=new String();
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



        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                        jLabel1.setText(null);
                        jLabel2.setText(null);
                        jLabel3.setText(null);
                        jLabel4.setText(null);
                        jLabel5.setText(null);
                        jRadioButtonName=null;
                System.out.println("windowClosing");
            }
        });

        if(!file.exists()){
            try {
                fwriter= new FileWriter(filename);
                fwriter.write(jTextField1.getText());
                fwriter.write("\r\n");
                fwriter.write(jTextField2.getText());
                fwriter.write("\r\n");
                fwriter.write(jTextField3.getText());
                fwriter.write("\r\n");
                fwriter.write(jTextField4.getText());
                fwriter.write("\r\n");
                fwriter.write(jRadioButtonName);
                fwriter.write("\r\n");

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    fwriter.flush();
                    fwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            File file = new File(filename);
            String [] fileContent=new String[5];
            try {
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                int line = 1;   // 一次读入一行，直到读入null为文件结束
                while ((tempString = reader.readLine()) != null) {
                    System.out.println("line" + line + ": " + tempString);
                    // content=tempString;
                    fileContent[line-1]=tempString;
                    line++;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
            jTextField1.setText(fileContent[0]);
            jTextField2.setText(fileContent[1]);
            jTextField3.setText(fileContent[2]);
            jTextField4.setText(fileContent[3]);
            jRadioButtonName=fileContent[4];
            if(jRadioButtonName.equals((String )"男")){
                jRadioButton1.setSelected(true);
            }
            else if(jRadioButtonName.equals((String )"女")){
                jRadioButton2.setSelected(true);
            }

        }



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
        try {
            fwriter= new FileWriter(filename);
            fwriter.write(jTextField1.getText());
            fwriter.write("\r\n");
            fwriter.write(jTextField2.getText());
            fwriter.write("\r\n");
            fwriter.write(jTextField3.getText());
            fwriter.write("\r\n");
            fwriter.write(jTextField4.getText());
            fwriter.write("\r\n");
            fwriter.write(jRadioButtonName);
            fwriter.write("\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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