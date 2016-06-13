package guard.guardshow;

import com.sun.jnlp.ApiDialog;
import guard.guardDataProcess.GuardData;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.dial.*;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.StandardGradientPaintTransformer;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/6/4.
 */
public class LightValueDialPlot extends JPanel implements Observer{
    private DefaultValueDataset dataSet;
    private StandardDialRange normalDialRange;
    private StandardDialRange bloodDialRange;
    private StandardDialRange bubbleDialRange;
    public LightValueDialPlot(){
        int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        dataSet=new DefaultValueDataset();
        DialPlot lightValueDialPlot=new DialPlot();
        lightValueDialPlot.setDataset(dataSet);
        StandardDialFrame dialFrame=new StandardDialFrame();
        dialFrame.setVisible(false);
        lightValueDialPlot.setDialFrame(dialFrame);

        GradientPaint gradientpaint=new GradientPaint(new Point(), new Color(0, 200, 255), new Point(), new Color(170, 170, 220));
        DialBackground dialBackground=new DialBackground(gradientpaint);
        dialBackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.VERTICAL));
        lightValueDialPlot.setBackground(dialBackground);
        // 设置显示在表盘中央位置的信息
        DialTextAnnotation dialtextannotation = new DialTextAnnotation("透光值");
        dialtextannotation.setFont(new Font("Dialog", 0, (int)(0.016*HEIGHT)));
        dialtextannotation.setRadius(0.1D);
        lightValueDialPlot.addLayer(dialtextannotation);

        DialValueIndicator dialValueIndicator=new DialValueIndicator(0);
        dialValueIndicator.setFont(new Font("Dialog", Font.PLAIN, (int)(0.011*HEIGHT)));
        dialValueIndicator.setOutlinePaint(Color.darkGray);
        dialValueIndicator.setRadius(0.4D);
        dialValueIndicator.setAngle(-90.0);
        lightValueDialPlot.addLayer(dialValueIndicator);

        StandardDialScale dialScale = new StandardDialScale();
        dialScale.setLowerBound(0D); // 最底表盘刻度
        dialScale.setUpperBound(1024); // 最高表盘刻度
        dialScale.setMajorTickIncrement(100);
        dialScale.setStartAngle(-120D); // 弧度为120,刚好与人的正常视觉对齐
        dialScale.setExtent(-300D); // 弧度为300,刚好与人的正常视觉对齐
        dialScale.setTickRadius(0.85D); // 值越大,与刻度盘框架边缘越近
        dialScale.setTickLabelOffset(0.1D); // 值越大,与刻度盘刻度越远0

        bloodDialRange =new StandardDialRange(500D,750D, Color.red);
        bloodDialRange.setInnerRadius(0.52000000000000002D);
        bloodDialRange.setOuterRadius(0.55000000000000004D);
        lightValueDialPlot.addLayer(bloodDialRange);
        //设置刻度范围（橘黄色）
        bubbleDialRange =new StandardDialRange(0D, 500D, Color.black);
        bubbleDialRange.setInnerRadius(0.52000000000000002D);
        bubbleDialRange.setOuterRadius(0.55000000000000004D);
        lightValueDialPlot.addLayer(bubbleDialRange);
        //设置刻度范围（绿色）
        normalDialRange =new StandardDialRange(750D,1024D, Color.green);
        normalDialRange.setInnerRadius(0.52000000000000002D);
        normalDialRange.setOuterRadius(0.55000000000000004D);
        lightValueDialPlot.addLayer(normalDialRange);

        dialScale.setTickLabelFont(new Font("Dialog", 0,(int)(0.011*HEIGHT))); // 刻度盘刻度字体
        lightValueDialPlot.addScale(0,dialScale);

        DialPointer.Pointer  pointer=new DialPointer.Pointer();
        lightValueDialPlot.addPointer(pointer);
        lightValueDialPlot.mapDatasetToScale(0,0);
        DialCap dialCap=new DialCap();
        dialCap.setRadius(0.07D);
        JFreeChart lightValueDialChart=new JFreeChart(lightValueDialPlot);
        lightValueDialChart.setBackgroundPaint(null);
        lightValueDialChart.setTitle("当前透光度");
        lightValueDialChart.getTitle().setFont(new Font("Dialog", Font.BOLD , (int)(HEIGHT*0.018)));
        ChartPanel lightValueDialChartPanel=new ChartPanel(lightValueDialChart,(int)(WIDTH*0.14),(int)(HEIGHT*0.26), 0,0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                true, false, false);
        lightValueDialChartPanel.setMouseZoomable(false); //禁止鼠标缩放
        this.setLayout(new BorderLayout());
        this.add(lightValueDialChartPanel);
    }
//    public StandardDialRange getNormalDialRange(){
//        return normalDialRange;
//    }
//    public StandardDialRange getBloodDialRange(){
//        return bloodDialRange;
//    }
//    public StandardDialRange getBubbleDialRange(){
//        return bubbleDialRange;
//    }
    @Override
    public void update(Observable o, Object arg) {
        GuardData guardData=(GuardData)arg;
        dataSet.setValue(Integer.parseInt(guardData.getLightValue()));
        normalDialRange.setBounds((double) guardData.getBloodLightValue(),1024D);
        bloodDialRange.setBounds((double)guardData.getEmptyLightValue(),(double)guardData.getBloodLightValue());
        bubbleDialRange.setBounds(0,(double)guardData.getEmptyLightValue());
    }
}

