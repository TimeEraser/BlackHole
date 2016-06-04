package guard.guardshow;

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
        dialtextannotation.setFont(new Font("Dialog", 0, 12));
        dialtextannotation.setRadius(0.1D);
        lightValueDialPlot.addLayer(dialtextannotation);

        DialValueIndicator dialValueIndicator=new DialValueIndicator(0);
        dialValueIndicator.setFont(new Font("Dialog", Font.PLAIN, 10));
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

        dialScale.setTickLabelFont(new Font("Dialog", 0, 8)); // 刻度盘刻度字体
        lightValueDialPlot.addScale(0,dialScale);

        DialPointer.Pointer  pointer=new DialPointer.Pointer();
        lightValueDialPlot.addPointer(pointer);
        lightValueDialPlot.mapDatasetToScale(0,0);
        DialCap dialCap=new DialCap();
        dialCap.setRadius(0.07D);
        JFreeChart lightValueDialChart=new JFreeChart(lightValueDialPlot);
        lightValueDialChart.setBackgroundPaint(null);
        lightValueDialChart.setTitle("当前透光度");
        lightValueDialChart.getTitle().setFont(new Font("Dialog", Font.BOLD , 14));
        ChartPanel lightValueDialChartPanel=new ChartPanel(lightValueDialChart,(int)(WIDTH*0.14),(int)(HEIGHT*0.26), 0,0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                true, false, false);
        this.setLayout(new BorderLayout());
        this.add(lightValueDialChartPanel);
    }

    @Override
    public void update(Observable o, Object arg) {
        dataSet.setValue(Integer.parseInt(((GuardData)arg).getLightValue()));
    }
}

