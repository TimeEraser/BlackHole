package com.embededcontest.ctgui;





import com.zju.lab.ct.algorithm.App;
import com.zju.lab.ct.algorithm.feature.ImageFeature;
import com.zju.lab.ct.algorithm.randomforest.RandomForest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;


/**
 * Created by ChaomingGu on 2016/5/19.
 * 这个界面用GridBagLayout布局，基本的选择图片，显示图片，分析图片和返回分析结果的功能都可以实现
 * 需要改进的是布局的效果；
 * 一个问题：确定打开图像后依然需要刷新才可以显示图像 */
public class CTMainGUI1 extends JFrame {
    private Container container;
    private GridBagLayout layout;
    private JButton open;
    private JButton analysis;
    private JButton remove;
    private GridBagConstraints c;
    public MandelDraw imgPanel;
    private JPanel textPanel;
    public String imagePath;
    private int x1,y1,x2,y2;
    private double[] coordinate;
    public int type;
    private JLabel rstShow, saveUse;
    private JPanel saveImgPanel;




    public CTMainGUI1(){
        //Set Layout
        container = new Container();
        setContentPane(container);
        layout = new GridBagLayout();
        c = new GridBagConstraints();
        container.setLayout(layout);

        open = new JButton("打开文件");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        container.add(open,c);

        analysis = new JButton("分析");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 0.5;
        container.add(analysis,c);

        remove = new JButton("清除");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        c.weightx = 0.5;
        container.add(remove,c);

        imgPanel = new MandelDraw();
        imgPanel.setSize(getPreferredSize());
        imgPanel.setBorder(BorderFactory.createEtchedBorder());
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.insets = new Insets(10,0,0,0);
        this.add(imgPanel,c);



        textPanel = new JPanel();
        rstShow = new JLabel();
        textPanel.add(rstShow);
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 3;
        c.insets = new Insets(10,0,0,0);
        textPanel.setBorder(BorderFactory.createEtchedBorder());
        c.anchor = GridBagConstraints.PAGE_END;
        container.add(textPanel,c);

        saveImgPanel = new JPanel();
        saveImgPanel.setSize(getPreferredSize());
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 2;
        c.gridy = 1;
        saveUse = new JLabel();
        saveImgPanel.setBorder(BorderFactory.createEtchedBorder());
        container.add(saveImgPanel,c);


        //设置打开文件按钮监听事件
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int returnVal = fileChooser.showOpenDialog(null);
                if(returnVal==fileChooser.APPROVE_OPTION){
                    imagePath = fileChooser.getSelectedFile().getAbsolutePath();
                    rstShow.setText("");
                    imgPanel.setIMAGE_ADDR(imagePath);
                    imgPanel.makeDraw();
                }
                rstShow.setText("可能病症：");
            }
        });


        analysis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    //实例化ObjectInputStream对象
            /*ObjectInputStream ois = new ObjectInputStream(App.class.getResourceAsStream("RandomForest"));*/
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream("conf/RandomForest"));

                    try {
                        //读取对象people,反序列化
                        RandomForest randomForest = (RandomForest) ois.readObject();
                        ImageFeature imageFeature = new ImageFeature();
                        /**
                         * 图像局部特征提取,参数：图象文件路径，划取区域坐标(x1,y1),(x2,y2)
                         * 返回:图像特征向量
                         */
                        double[] data = imageFeature.getFeature(imagePath, x1, y1, x2, y2);
                        /**
                         * 识别算法调用
                         * 参数：图像特征向量
                         * 返回：病变类型(int型)，1:正常;2:肝癌;3:肝血管瘤;4:肝囊肿;5:其他
                         */
                        coordinate = imgPanel.getCoordinate();
                        x1 = (int)coordinate[0];
                        y1 = (int)coordinate[1];
                        x2 = (int)coordinate[2];
                        y2 = (int)coordinate[3];
                        type = randomForest.predictType(data);
                        System.out.println("x1:"+x1+",y1:"+y1+",x2:"+x2+",y2:"+y2);
                        System.out.println("RandomForest predict:"+type);
                        switch (type){
                            case 1:
                                rstShow.setText("可能病症：正常");
                                break;
                            case 2:
                                rstShow.setText("可能病症：肝癌");
                                break;
                            case 3:
                                rstShow.setText("可能病症：肝血管瘤");
                                break;
                            case 4:
                                rstShow.setText("可能病症：肝囊肿");
                                break;
                            case 5:
                                rstShow.setText("可能病症：其他");
                        }

                        saveUse.setIcon(new ImageIcon(imgPanel.getSaveImgPath()));
                        saveImgPanel.add(saveUse);
                    } catch (ClassNotFoundException e_analysis) {
                        e_analysis.printStackTrace();
                    }
                } catch (FileNotFoundException e_analysis) {
                    e_analysis.printStackTrace();
                } catch (IOException e_analysis) {
                    e_analysis.printStackTrace();
                }


            }
        });

        /*exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });*/

        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                imgPanel.removeImg();
                saveImgPanel.remove(saveUse);
                rstShow.setText("");
            }
        });


    }




    public static void main(String[] args) {
        CTMainGUI1 jf = new CTMainGUI1();
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jf.setVisible(true);
    }

}



