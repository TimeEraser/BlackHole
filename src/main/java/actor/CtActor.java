package actor;

import actor.Listener.NoticeListener;
import actor.config.CtActorConfig;

import ctshow.CTDataRefresher;
import ctshow.CTCurrentData;
import ct.algorithm.feature.ImageFeature;
import ct.algorithm.randomforest.RandomForest;
import command.*;
import ctshow.CTShowUI;
import ctshow.CTDataRefresher;
import util.FileUtil;
import util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.SocketPermission;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CtActor extends BaseActor{
    private  String CT_ANALYSE_RESULT_SAVE_ROOT;
    private  String CT_ANALYSE_RESULT_SAVE_FORMAT;
    private CTDataRefresher ctDataRefresher;
    private ObjectInputStream ois = null;
    private RandomForest randomForest ;
    //读取对象people,反序列化
    private ImageFeature imageFeature;
    //分析结果
    private String CTAnalyseResult=null;
    public CtActor(CtActorConfig ctActorConfig){
        //Result ROOT
        this.CT_ANALYSE_RESULT_SAVE_ROOT=ctActorConfig.getCT_ANALYSE_RESULT_SAVE_ROOT();
        this.CT_ANALYSE_RESULT_SAVE_FORMAT=ctActorConfig.getCT_ANALYSE_RESULT_SAVE_FORMAT();
        //获取图像特征
        imageFeature = new ImageFeature();
        //实例化ObjectInputStream对象
            /*ObjectInputStream ois = new ObjectInputStream(App.class.getResourceAsStream("RandomForest"));*/
        try {
            ois = new ObjectInputStream(new FileInputStream("conf/RandomForest"));
            randomForest= (RandomForest) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean processActorRequest(Request  request ) {
        if(request== CtRequest.CT_OPEN_IMG){
            CTAnalyseResult=null;
            ctDataRefresher.refreshCTData((String) request.getConfig().getData(),false);
        }
        if(request== CtRequest.CT_UI_CONFIG){
            ctDataRefresher=new CTDataRefresher((CTShowUI)request.getConfig().getData());
        }
        if(request==CtRequest.CT_ANALYSIS){
            ctAnalysis();
            //ctDataRefresher.refreshHistoryResult(CTAnalyseResult);
        }
        if(request==CtRequest.CT_SAVE){
            saveCTAnalyseResult();
        }
        if(request==CtRequest.CT_SHOW_HISTORY)
            ctDataRefresher.refreshCTData((String) request.getConfig().getData(),true);
        if(request==CtRequest.CT_UI_RETURN) {
            returnCTCurrentData();
        }
        return false;
    }

    @Override
    public boolean processActorResponse(Response  responses) {return false;}

    @Override
    public boolean start() {return false;}

    @Override
    public boolean shutdown() {return false;}


    public void ctAnalysis(){
        if(!ctDataRefresher.Initialized()){
            JOptionPane.showMessageDialog(null,"请打开CT图片","操作错误",JOptionPane.ERROR_MESSAGE);
        }else {
            try {
                /**
                 * 识别算法调用
                 * 参数：图像特征向量
                 * 返回：病变类型(int型)，1:正常;2:肝癌;3:肝血管瘤;4:肝囊肿;5:其他
                 */
                double[] coordinate = ctDataRefresher.getCoordinate();
                int x1 = (int) coordinate[0];
                int y1 = (int) coordinate[1];
                int x2 = (int) coordinate[2];
                int y2 = (int) coordinate[3];
                /**
                 * 图像局部特征提取,参数：图象文件路径，划取区域坐标(x1,y1),(x2,y2)
                 * 返回:图像特征向量
                 */
                double[] data = imageFeature.getFeature(ctDataRefresher.getImagePath(), x1 > x2 ? x2 : x1, y1 > y2 ? y2 : y1, x1 < x2 ? x2 : x1, y1 < y2 ? y2 : y1);
                int type = randomForest.predictType(data);
                System.out.println("x1:" + x1 + ",y1:" + y1 + ",x2:" + x2 + ",y2:" + y2);
                switch (type) {
                    case 1:
                        CTAnalyseResult = "正   常";
                        break;
                    case 2:
                        CTAnalyseResult = "肝   癌";
                        break;
                    case 3:
                        CTAnalyseResult = "肝血管瘤";
                        break;
                    case 4:
                        CTAnalyseResult = "肝  囊肿";
                        break;
                    case 5:
                        CTAnalyseResult = "其   他";
                }
                System.out.println("RandomForest predict:" + CTAnalyseResult);
                ctDataRefresher.refreshCurrentResult(CTAnalyseResult);
                ctDataRefresher.refreshHistoryResult(CTAnalyseResult);
            } catch (FileNotFoundException e_analysis) {
                e_analysis.printStackTrace();
            } catch (IOException e_analysis) {
                e_analysis.printStackTrace();
            }
        }
    }
    public void saveCTAnalyseResult() {
        if(CTAnalyseResult==null){
            JOptionPane.showMessageDialog(null,"请分析CT病灶","操作错误",JOptionPane.ERROR_MESSAGE);
        }else {
            Object[] options = {"是", "否"};
            int response = JOptionPane.showOptionDialog(null, "是否保存分析结果？", "保存分析结果", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (response == JOptionPane.YES_OPTION) {
                Date now = new Date(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                String time = dateFormat.format(now);
                Dimension size = ctDataRefresher.getMandelDraw().getSize();
                BufferedImage savedHistory = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = savedHistory.createGraphics();
                ctDataRefresher.getMandelDraw().paint(g);
                String folderPath=CT_ANALYSE_RESULT_SAVE_ROOT+"/"+CTAnalyseResult;
                FileUtil.makeDirs(folderPath);
                String filename = CTAnalyseResult + time +"."+CT_ANALYSE_RESULT_SAVE_FORMAT;
                try {
                    ImageIO.write(savedHistory, CT_ANALYSE_RESULT_SAVE_FORMAT, new File(folderPath, filename));
                    ctDataRefresher.addHistoryResult(folderPath+"/"+filename);
                    System.out.println("Saved Successfully!");
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    public void returnCTCurrentData(){
        if(!ctDataRefresher.Initialized()){
            JOptionPane.showMessageDialog(null,"请打开CT图片","操作错误",JOptionPane.ERROR_MESSAGE);
        }else {
            ctDataRefresher.returnToCurrentImage();
        }
    }

}



