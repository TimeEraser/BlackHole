package ct.algorithm;

import ct.algorithm.feature.ImageFeature;
import ct.algorithm.randomforest.RandomForest;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wuhaitao
 * @date 2016/5/10 13:12
 */
public class App {
    /**
     * 识别模型生成
     */
    public void testGenerateModel(){
        Connection c = null;
        Statement stmt = null;
        List<Double[]> samples = null;
        RandomForest randomforest = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:/Users/macbook/Documents/IdeaProject/BlackHole/db/cad");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from feature");
            samples = new ArrayList<Double[]>(rs.getRow());
            while (rs.next()) {
                Double[] d = new Double[27];
                d[0] = rs.getDouble("f1");
                d[1] = rs.getDouble("f2");
                d[2] = rs.getDouble("f1");
                d[3] = rs.getDouble("f1");
                d[4] = rs.getDouble("f1");
                d[5] = rs.getDouble("f1");
                d[6] = rs.getDouble("f1");
                d[7] = rs.getDouble("f1");
                d[8] = rs.getDouble("f1");
                d[9] = rs.getDouble("f1");
                d[10] = rs.getDouble("f1");
                d[11] = rs.getDouble("f1");
                d[12] = rs.getDouble("f1");
                d[13] = rs.getDouble("f1");
                d[14] = rs.getDouble("f1");
                d[15] = rs.getDouble("f1");
                d[16] = rs.getDouble("f1");
                d[17] = rs.getDouble("f1");
                d[18] = rs.getDouble("f1");
                d[19] = rs.getDouble("f1");
                d[20] = rs.getDouble("f1");
                d[21] = rs.getDouble("f1");
                d[22] = rs.getDouble("f1");
                d[23] = rs.getDouble("f1");
                d[24] = rs.getDouble("f1");
                d[25] = rs.getDouble("f1");
                d[26] = (double) rs.getInt("label");
                samples.add(d);
            }
            rs.close();
            stmt.close();
            c.close();
            randomforest = new RandomForest(50, 4);
            randomforest.createForest(samples);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println("-----------------");
        try {
            //实例化ObjectOutputStream对象
            /*ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(App.class.getClassLoader().getResource("RandomForest").getFile()));*/
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("conf/RandomForest"));
            //将对象写入文件
            oos.writeObject(randomforest);
            oos.flush();
            oos.close();

            //实例化ObjectInputStream对象
            /*ObjectInputStream ois = new ObjectInputStream(App.class.getResourceAsStream("RandomForest"));*/
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("conf/RandomForest"));

            try {
                //读取对象people,反序列化
                RandomForest randomForest = (RandomForest) ois.readObject();
                double[] data = new double[samples.get(0).length];
                int sum = samples.size();
                int right = 0;
                for (Double[] sample : samples){
                    for (int i=0;i<sample.length;i++){
                        data[i] = sample[i];
                    }
                    if (randomForest.predictType(data) == (int)data[26]){
                        right++;
                    }
                }
                double accurancy = right*1.0/sum;
                System.out.println("RandomForest Accurancy:"+accurancy);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模型测试
     */
    public void test(){
        Connection c = null;
        Statement stmt = null;
        List<Double[]> samples = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:db/cad");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from feature");
            samples = new ArrayList<Double[]>(rs.getRow());
            while (rs.next()) {
                Double[] d = new Double[27];
                d[0] = rs.getDouble("f1");
                d[1] = rs.getDouble("f2");
                d[2] = rs.getDouble("f1");
                d[3] = rs.getDouble("f1");
                d[4] = rs.getDouble("f1");
                d[5] = rs.getDouble("f1");
                d[6] = rs.getDouble("f1");
                d[7] = rs.getDouble("f1");
                d[8] = rs.getDouble("f1");
                d[9] = rs.getDouble("f1");
                d[10] = rs.getDouble("f1");
                d[11] = rs.getDouble("f1");
                d[12] = rs.getDouble("f1");
                d[13] = rs.getDouble("f1");
                d[14] = rs.getDouble("f1");
                d[15] = rs.getDouble("f1");
                d[16] = rs.getDouble("f1");
                d[17] = rs.getDouble("f1");
                d[18] = rs.getDouble("f1");
                d[19] = rs.getDouble("f1");
                d[20] = rs.getDouble("f1");
                d[21] = rs.getDouble("f1");
                d[22] = rs.getDouble("f1");
                d[23] = rs.getDouble("f1");
                d[24] = rs.getDouble("f1");
                d[25] = rs.getDouble("f1");
                d[26] = (double) rs.getInt("label");
                samples.add(d);
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        try {

            //实例化ObjectInputStream对象
            /*ObjectInputStream ois = new ObjectInputStream(App.class.getResourceAsStream("RandomForest"));*/
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("conf/RandomForest"));

            try {
                //读取对象people,反序列化
                RandomForest randomForest = (RandomForest) ois.readObject();
                double[] data = new double[samples.get(0).length];
                int sum = samples.size();
                int right = 0;
                for (Double[] sample : samples){
                    for (int i=0;i<sample.length;i++){
                        data[i] = sample[i];
                    }
                    if (randomForest.predictType(data) == (int)data[26]){
                        right++;
                    }
                }
                double accurancy = right*1.0/sum;
                System.out.println("RandomForest Accurancy:"+accurancy);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用示例
     * 读取模型，识别CT局部病变
     */
    public void test2(){
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
                double[] data = imageFeature.getFeature(App.class.getClassLoader().getResource("IMG-0002-00011.jpg").getFile(), 0, 0, 125, 125);
                /**
                 * 识别算法调用
                 * 参数：图像特征向量
                 * 返回：病变类型(int型)，1:正常;2:肝癌;3:肝血管瘤;4:肝囊肿;5:其他
                 */
                int type = randomForest.predictType(data);
                System.out.println("RandomForest predict:"+type);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new App().testGenerateModel();
    }
}
