package guard.guardshow;

import guard.guardDataProcess.GuardData;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.net.URL;
import java.util.*;

/**
 * Created by adminstrator on 2016/6/3.
 */
public class GuardBottomShow extends JPanel implements Observer{
    private Icon greenPoint;
    private Icon redPoint;
    private JLabel connectStatePoint;
    private DefaultCategoryDataset temperatureDataSet;
    private BarRenderer temperatureRenderer;
    private DefaultPieDataset lightValueDataSet;
    private static int connectFlashFlag=0;
    private static boolean connectStartFlag=false;
    private static int connectLostCount=0;

    public GuardBottomShow(){
        JPanel connectStatePointPanel;
        JPanel temperatureStatePointPanel;
        JPanel lightValueStatePointPanel;
        JLabel connectState=new JLabel("报警模块连接状态");
        connectState.setFont(new Font("Dialog", 0, 14));
        connectState.setPreferredSize(new Dimension(120,45));
        JLabel temperatureState=new JLabel("血温状态");
        temperatureState.setFont( new Font("Dialog", 0, 14));
        temperatureState.setPreferredSize(new Dimension(60,45));
        JLabel lightValueState=new JLabel("管内流体状态");
        lightValueState.setFont( new Font("Dialog", 0, 14));
        lightValueState.setPreferredSize(new Dimension(90,45));
        greenPoint=new ImageIcon(getIconImage("Icon/green.png"));
        redPoint=new ImageIcon(getIconImage("Icon/red.png"));
        connectStatePoint=new JLabel(redPoint);
        connectStatePointPanel=new JPanel();
        connectStatePointPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        connectStatePointPanel.setPreferredSize(new Dimension(38,38));
        connectStatePointPanel.add(connectStatePoint);
        temperatureStatePointPanel=new JPanel();
        temperatureStatePointPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        temperatureStatePointPanel.add(createTemperatureChartPanel());
        lightValueStatePointPanel=new JPanel();
        lightValueStatePointPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        lightValueStatePointPanel.add(createLightValueChartPanel());

        JPanel connectStatePanel=new JPanel();
        connectStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,0));
        connectStatePanel.setPreferredSize(new Dimension(400,45));
        connectStatePanel.add(connectState);
        connectStatePanel.add(connectStatePointPanel);
        JPanel temperatureStatePanel=new JPanel();
        temperatureStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,0));
        temperatureStatePanel.setPreferredSize(new Dimension(400,45));
        temperatureStatePanel.add(temperatureState);
        temperatureStatePanel.add(temperatureStatePointPanel);
        JPanel lightValueStatePanel=new JPanel();
        lightValueStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT,20,0));
        lightValueStatePanel.setPreferredSize(new Dimension(200,45));
        lightValueStatePanel.add(lightValueState);
        lightValueStatePanel.add(lightValueStatePointPanel);

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(connectStatePanel);
        add(temperatureStatePanel);
        add(lightValueStatePanel);

        java.util.Timer timer=new java.util.Timer();
        timer.schedule(
                new java.util.TimerTask() {
                    public void run(){
                        if (connectStartFlag){
                            connectLostCount+=1;
                        }
                        if(connectLostCount>3){
                            connectStartFlag=false;
                            connectStatePoint.setIcon(redPoint);
                            connectStatePoint.setVisible(true);
                        }
                    }
                },2000,2000
        );
    }
    private ChartPanel createTemperatureChartPanel(){
        temperatureDataSet = new DefaultCategoryDataset();
        temperatureDataSet.addValue(0,"temperatureChart","temperature");
        temperatureRenderer=new BarRenderer();
        JFreeChart temperatureChart= ChartFactory.createBarChart(null,
                null, null, temperatureDataSet,
                PlotOrientation.VERTICAL, false, true, false);
        temperatureChart.setBorderVisible(false);
        temperatureChart.setBorderPaint(null);
        temperatureChart.setBackgroundPaint(null);
        CategoryPlot temperatureCategoryPlot = (CategoryPlot) temperatureChart.getPlot();
        temperatureCategoryPlot.getDomainAxis().setVisible(false);
        temperatureCategoryPlot.getRangeAxis().setVisible(false);
        temperatureCategoryPlot.getRangeAxis().setRange(15.0D,45.0D);
        temperatureCategoryPlot.setRangeMinorGridlinesVisible(false);
        temperatureCategoryPlot.setDomainCrosshairVisible(false);
        temperatureCategoryPlot.setRangeCrosshairVisible(false);
        temperatureCategoryPlot.setRangeGridlinesVisible(false);
        temperatureRenderer.setSeriesPaint(0,Color.GREEN);
        temperatureCategoryPlot.setRenderer(temperatureRenderer);
        ChartPanel temperatureChartPanel=new ChartPanel(temperatureChart,36,36, 0,0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, false, true, false,
                true, false, false);
        return temperatureChartPanel;
    }
    private ChartPanel createLightValueChartPanel(){
        JFreeChart lightValueChart=ChartFactory.createPieChart(null,createLightValueDataSet(),true,true,false);
        lightValueChart.getLegend().setVisible(false);
        lightValueChart.setBackgroundPaint(null);
        PiePlot piePlot=(PiePlot) lightValueChart.getPlot();
        piePlot.setBackgroundPaint(null);
        piePlot.setOutlinePaint(null);
        piePlot.setSectionOutlinesVisible(false);
        piePlot.setNoDataMessage(null);
        piePlot.setIgnoreNullValues(true);
        piePlot.setIgnoreZeroValues(true);
        piePlot.setSectionPaint("正常",Color.GREEN);
        piePlot.setSectionPaint("无管",Color.LIGHT_GRAY);
        piePlot.setSectionPaint("管内气泡",Color.YELLOW);
        piePlot.setSectionPaint("管内漏血",Color.RED);
        piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator(""));
        piePlot.setLabelBackgroundPaint(null);
        piePlot.setLabelShadowPaint(null);
        piePlot.setLabelOutlinePaint(null);
        piePlot.setLabelLinksVisible(false);
        piePlot.setLabelFont(new Font(null));
        piePlot.setShadowPaint(null);
        ChartPanel lightValuePieChartPanel=new ChartPanel(lightValueChart,38,38, 0,0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                true, false, false);
        lightValuePieChartPanel.setBackground(null);
        return lightValuePieChartPanel;
    }
    private PieDataset createLightValueDataSet(){
        lightValueDataSet=new DefaultPieDataset();
        lightValueDataSet.setValue("正常",0);
        lightValueDataSet.setValue("无管",0);
        lightValueDataSet.setValue("管内气泡",0);
        lightValueDataSet.setValue("管内漏血",0);
        return lightValueDataSet;
    }
    private URL getIconImage(String path){
        return this.getClass().getClassLoader().getResource(path);
    }
    @Override
    public void update(Observable o, Object arg) {
        GuardData guardData = (GuardData) arg;
        if(!connectStartFlag){
            connectStatePoint.setIcon(greenPoint);
            connectStartFlag=true;
        }
        connectLostCount=0;
        connectStatePoint.setVisible((connectFlashFlag%2==1));
        connectFlashFlag+=1;
        temperatureDataSet.setValue(Float.parseFloat(guardData.getTemperature()),"temperatureChart","temperature");
        if(guardData.getTemperatureMessage()!=null) {
            switch (guardData.getTemperatureMessage()) {
                case "血温正常":
                    temperatureRenderer.setSeriesPaint(0, Color.GREEN, true);
                    break;
                case "血温过高":
                    temperatureRenderer.setSeriesPaint(0, Color.RED, true);
                    break;
                case "血温过低":
                    temperatureRenderer.setSeriesPaint(0, Color.BLUE, true);
                    break;
                default:
                    ;
            }
        }
        if(guardData.isCountFish()){
            int[] temp=guardData.getCountMess();
            lightValueDataSet.setValue("正常",temp[0]);
            lightValueDataSet.setValue("无管",temp[1]);
            lightValueDataSet.setValue("管内气泡",temp[3]);
            lightValueDataSet.setValue("管内漏血",temp[2]);
        }
//        if(guardData.getMessage("Temperature")!=null){
//            if(guardData.getMessage("Temperature").equals("血温正常")){
//                temperatureStatePoint.setIcon(greenPoint);
//            }
//            else {
//                temperatureStatePoint.setIcon(yellowPoint);
//            }
//        }
//        if (guardData.getMessage("Blood")!=null){
//            if(guardData.getMessage("Blood").equals("不再漏血")){
//                lightValueStatePoint.setIcon(greenPoint);
//            }
//            else{
//                lightValueStatePoint.setIcon(redPoint);
//            }
//        }
//        if(guardData.getMessage("Bubble")!=null){
//            if(guardData.getMessage("Bubble").equals("气泡消失")){
//                lightValueStatePoint.setIcon(greenPoint);
//            }
//            else {
//                lightValueStatePoint.setIcon(redPoint);
//            }
//        }
    }
}



