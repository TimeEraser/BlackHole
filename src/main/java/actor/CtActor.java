package actor;

import actor.config.CtActorConfig;

import com.embededcontest.ctgui.MandelDraw;
import com.zju.lab.ct.algorithm.feature.ImageFeature;
import com.zju.lab.ct.algorithm.randomforest.RandomForest;
import command.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class CtActor extends BaseActor{
    private MainUiActor mainUiActor;

    private int x1,y1,x2,y2;
    private double[] coordinate;
    private int type;

    private MandelDraw imgPanel=new MandelDraw();
    private String imagePath=new String("null");
    private JLabel rstShow=new JLabel();
    private JLabel saveUse=new JLabel();
    private JPanel saveImgPanel=new JPanel();
    private JPanel ctSubFocus1 = new JPanel();
    private JPanel ctSubFocus2 = new JPanel();

    public CtActor(CtActorConfig ctActorConfig){
        //TO DO Initialize the GuardActor
    }
    @Override
    public boolean processActorRequest(Request  request ) {
        if(request== MainUiRequest.MAIN_UI_CT_CONFIG){
            ctConfig(request);
        }

        if(request==MainUiRequest.MAIN_UI_CT_ANALYSIS){
            ctAnalysis();
        }

        return false;
    }

    @Override
    public boolean processActorResponse(Response  responses) {return false;}

    @Override
    public boolean start() {return false;}

    @Override
    public boolean shutdown() {return false;}

    private void ctConfig(Request request){
        mainUiActor= (MainUiActor)request.getConfig().getSendActor();
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
        mainUiActor.getCTData().add(imgPanel);
            /*mainUiActor.getCTFocus().add(rstShow);*/
        ctSubFocus1.setLayout(new BorderLayout());
        ctSubFocus2.setLayout(new BorderLayout());
        ctSubFocus1.add(rstShow, BorderLayout.CENTER);
        ctSubFocus2.add(saveUse, BorderLayout.CENTER);
        mainUiActor.getCTFocus().add(ctSubFocus1);
        mainUiActor.getCTFocus().add(ctSubFocus2);
        mainUiActor.getMainUi().setVisible(true);
        System.out.println("CtRequest.CT_OPEN_IMG");
    }

    public void ctAnalysis(){
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
                mainUiActor.getCTFocus().add(saveUse);
            } catch (ClassNotFoundException e_analysis) {
                e_analysis.printStackTrace();
            }
        } catch (FileNotFoundException e_analysis) {
            e_analysis.printStackTrace();
        } catch (IOException e_analysis) {
            e_analysis.printStackTrace();
        }
    }



}
