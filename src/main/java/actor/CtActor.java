package actor;

import actor.config.CtActorConfig;

import ct.ctshow.CTDataRefresher;
import ct.ctshow.MandelDraw;
import ct.algorithm.feature.ImageFeature;
import ct.algorithm.randomforest.RandomForest;
import command.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

public class CtActor extends BaseActor{
    private CTDataRefresher ctDataRefresher;
    private ObjectInputStream ois = null;
    private RandomForest randomForest ;
    //读取对象people,反序列化
    private ImageFeature imageFeature;
    public CtActor(CtActorConfig ctActorConfig){
        //TO DO Initialize the GuardActor
        //实例化ObjectInputStream对象
            /*ObjectInputStream ois = new ObjectInputStream(App.class.getResourceAsStream("RandomForest"));*/
        try {
            ois = new ObjectInputStream(new FileInputStream("conf/RandomForest"));
            randomForest= (RandomForest) ois.readObject();
            imageFeature = new ImageFeature();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean processActorRequest(Request  request ) {
        if(request== CtRequest.CT_OPEN_IMG){
            ctDataRefresher.refreshCTData((String) request.getConfig().getData());
        }
        if(request== CtRequest.CT_UI_CONFIG){
            ctDataRefresher=new CTDataRefresher((MandelDraw)request.getConfig().getData());
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


    public void ctAnalysis(){
        try {
                /**
                 * 识别算法调用
                 * 参数：图像特征向量
                 * 返回：病变类型(int型)，1:正常;2:肝癌;3:肝血管瘤;4:肝囊肿;5:其他
                 */
                double[]coordinate = ctDataRefresher.getCoordinate();
                int x1 = (int)coordinate[0];
                int y1 = (int)coordinate[1];
                int x2 = (int)coordinate[2];
                int y2 = (int)coordinate[3];
                /**
                 * 图像局部特征提取,参数：图象文件路径，划取区域坐标(x1,y1),(x2,y2)
                 * 返回:图像特征向量
                 */
                double[] data = imageFeature.getFeature(ctDataRefresher.getImagePath(), x1, y1, x2, y2);
                int type = randomForest.predictType(data);
                System.out.println("x1:"+x1+",y1:"+y1+",x2:"+x2+",y2:"+y2);
                System.out.println("RandomForest predict:"+type);
                String result=null;
                switch (type){
                    case 1:
                        result="正常";
                        break;
                    case 2:
                        result="肝癌";
                        break;
                    case 3:
                        result="肝血管瘤";
                        break;
                    case 4:
                        result="肝囊肿";
                        break;
                    case 5:
                        result="其他";
                }
                ctDataRefresher.refreshResult(result);
        } catch (FileNotFoundException e_analysis) {
            e_analysis.printStackTrace();
        } catch (IOException e_analysis) {
            e_analysis.printStackTrace();
        }
    }



}
