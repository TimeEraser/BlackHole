package actor;

import actor.config.CtActorConfig;

import actor.config.MainUiActorConfig;
import ct.ctshow.CTDataRefresher;
import ct.ctshow.ListFile;
import ct.ctshow.MandelDraw;
import ct.algorithm.feature.ImageFeature;
import ct.algorithm.randomforest.RandomForest;
import command.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CtActor extends BaseActor{
    private CTDataRefresher ctDataRefresher;

    private MandelDraw mandelDraw;
    private ObjectInputStream ois = null;
    private RandomForest randomForest ;
    //读取对象people,反序列化
    private ImageFeature imageFeature;
    private String result=null;
    private String filename=null;
    private ListFile listFile = new ListFile();
    //private JList CTList = new JList();
    int serialNum=0;


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
            //CTList = listFile.getList();


        }
        if(request==MainUiRequest.MAIN_UI_CT_SAVE){
//            serialNum++;
            saveImg();

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
//                String result=null;
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
    public void saveImg() {
        int response = JOptionPane.showConfirmDialog(null, "是否保存文件？", "保存文件", JOptionPane.YES_NO_OPTION);
        if (response == 0) {
            Date now = new Date(System.currentTimeMillis());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = dateFormat.format(now);
            Dimension size = ctDataRefresher.getMandelDraw().getSize();
            BufferedImage savedHistory = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = savedHistory.createGraphics();
            ctDataRefresher.getMandelDraw().paint(g);
            filename = result+time+".png";
            try {
                ImageIO.write(savedHistory, "png", new File("./res",filename));
                System.out.println("Saved Successfully!");
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }
    /*public String getFileName(){
        return filename;
    }
*/
    public String getResult(){
        return result;
    }

    /*public JList getCTList(){
        return CTList;
    }
*/


}
