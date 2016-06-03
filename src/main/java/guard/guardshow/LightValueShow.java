package guard.guardshow;

import guard.guardDataProcess.GuardData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.RectangleEdge;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/6/2.
 */


public class LightValueShow extends JPanel implements Observer{
    private DefaultPieDataset lightValueDataSet;
    public LightValueShow(){
        JFreeChart lightValuePieChart;
        int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        lightValuePieChart= ChartFactory.createPieChart("过去10秒内透光度变化",createDataSet(),true,true,false);
//        lightValuePieChart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING,
//                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        lightValuePieChart.getTitle().setFont(new Font("Dialog", Font.BOLD , 14));
        lightValuePieChart.setBackgroundPaint(null);
        lightValuePieChart.getLegend().setItemFont(new Font("Dialog", 0, 12));
        lightValuePieChart.getLegend().setPosition(RectangleEdge.RIGHT);
        lightValuePieChart.setBorderPaint(null);
        PiePlot piePlot=(PiePlot) lightValuePieChart.getPlot();
        piePlot.setBackgroundPaint(null);
        piePlot.setOutlinePaint(null);
        piePlot.setCircular(false);
        setSection(piePlot);
        setLabel(piePlot);
        setNoDataMessage(piePlot);
        setNullAndZeroValue(piePlot);
        piePlot.setLabelLinksVisible(false);
//        LegendTitle legendTitle = new LegendTitle(lightValuePieChart.getPlot());//创建图例
//        legendTitle.setPosition(RectangleEdge.RIGHT);  //设置图例的位置
        ChartPanel lightValuePieChartPanel=new ChartPanel(lightValuePieChart,(int)(WIDTH*0.31),(int)(HEIGHT*0.29), 0,0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                true, false, false);
        lightValuePieChartPanel.setBackground(null);
        this.add(lightValuePieChartPanel);
    }
    private PieDataset createDataSet(){
        lightValueDataSet=new DefaultPieDataset();
        lightValueDataSet.setValue("正常",0);
        lightValueDataSet.setValue("无管",0);
        lightValueDataSet.setValue("管内气泡",0);
        lightValueDataSet.setValue("管内漏血",0);
        return lightValueDataSet;
    }
    private void setSection(PiePlot piePlot){
        piePlot.setSectionPaint("正常",Color.GREEN);
        piePlot.setSectionPaint("无管",Color.LIGHT_GRAY);
        piePlot.setSectionPaint("管内气泡",Color.YELLOW);
        piePlot.setSectionPaint("管内漏血",Color.RED);
        piePlot.setSectionOutlinesVisible(false);
    }
    private void setLabel(PiePlot pieplot) {
        //设置扇区标签显示格式：关键字：值(百分比)
        pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator(
                "{0}：{2} percent"));
        //设置扇区标签颜色
        pieplot.setLabelBackgroundPaint(new Color(220, 220, 220));
        pieplot.setLabelFont((new Font("Dialog", Font.PLAIN, 12)));
        pieplot.setLabelLinksVisible(false);
    }
    private void setNoDataMessage(PiePlot pieplot) {
        //设置没有数据时显示的信息
        pieplot.setNoDataMessage("请连接报警设备");
        //设置没有数据时显示的信息的字体
        pieplot.setNoDataMessageFont(new Font("Dialog", Font.BOLD, 14));
        //设置没有数据时显示的信息的颜色
        pieplot.setNoDataMessagePaint(Color.red);
    }
    private void setNullAndZeroValue(PiePlot piePlot) {
        //设置是否忽略0和null值
        piePlot.setIgnoreNullValues(true);
        piePlot.setIgnoreZeroValues(true);
    }
    @Override
    public void update(Observable o, Object arg) {
        GuardData guardData=(GuardData)arg;
        if(guardData.isCountFish()){
            int[] temp=guardData.getCountMess();
            lightValueDataSet.setValue("正常",temp[0]);
            lightValueDataSet.setValue("无管",temp[1]);
            lightValueDataSet.setValue("管内气泡",temp[3]);
            lightValueDataSet.setValue("管内漏血",temp[2]);
        }
    }
}
