/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ecg.ecgshow;

import java.awt.*;
import java.io.File;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

import guard.guardDataProcess.GuardData;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.plot.dial.*;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.GradientPaintTransformType;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.StandardGradientPaintTransformer;

import static javafx.scene.text.Font.loadFont;

/**
 *
 * @author MCH
 */
public class ECGShowUI extends JPanel implements Observer {

//	// Variables declaration - do not modify//GEN-BEGIN:variables
//    private JPanel contentPane=new JPanel();
//
//    private JPanel jPanel1;
//    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    public static final int LEAD_COUNT=3;
	//private JPanel ECGInfo;
    private JPanel HeartRateData;
    private JPanel PressureData;
    private JPanel BloodOxygenData;

    //Init Element
    private Integer WIDTH;
    private Integer HEIGHT;
    //ECGData
    private JPanel ECGData;
    private JPanel temperatureData;
    private JPanel lightValueData;

    private JLabel temperatureLabel;
    private DefaultValueDataset lightValueDataSet;
    private StandardDialRange normalDialRange;
    private StandardDialRange bloodDialRange;

    private DateAxis[] dateAxises ;
    private DateAxis[] PressuredateAxises ;
   // private ECGOtherData ecgOtherData=new ECGOtherData();

    private TimeSeries[] ECGSeries ;
    private TimeSeries[] HeartRateSeries ;
    private TimeSeries[] SystolicPressureSeries ;//收缩压
    private TimeSeries[] DiastolicPressureSeries ;//舒张压
    private TimeSeries[] BloodOxygenSeries ;

    private short[] HeartRatedatas;     //short型为16位，首位为符号位。
    private short[] BloodOxygendatas;



    public ECGShowUI(String title, long timeZone) {		//构造方法
    	super();

        WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        createECGData(timeZone);
        createHeartRateData(timeZone);
        createPressureData(timeZone);
        createBloodOxygenData(timeZone);
        createGuardData();

    }

    private void createECGData(long timeZone) {
        ECGData = new JPanel(new GridLayout(LEAD_COUNT,1));
        dateAxises = new DateAxis[LEAD_COUNT];
        ECGSeries = new TimeSeries[LEAD_COUNT*2];
        for (int i = 0; i < LEAD_COUNT; i++) {
            TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(); //XYDataset 类型 TimeSeriesCollection
            ECGSeries[i] = new TimeSeries("导联" + (i+1));
            ECGSeries[i].setMaximumItemCount(500);
            ECGSeries[i+LEAD_COUNT]=new TimeSeries("");
            ECGSeries[i+LEAD_COUNT].setMaximumItemAge(timeZone);
            ECGSeries[i+LEAD_COUNT].setMaximumItemCount(2);

            timeseriescollection.addSeries(ECGSeries[i]);
            timeseriescollection.addSeries(ECGSeries[i+LEAD_COUNT]);

            //DateAxis dateaxis = new DateAxis("Time");
            dateAxises[i] = new DateAxis("");
            dateAxises[i].setTickLabelFont(new Font("SansSerif", 0, 12));
            dateAxises[i].setLabelFont(new Font("SansSerif", 0, 14));
            dateAxises[i].setTickLabelsVisible(true);
            dateAxises[i].setVisible(false);

            //NumberAxis numberaxis = new NumberAxis("ecg");
            NumberAxis numberaxis = new NumberAxis("ecg");
            numberaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
            numberaxis.setLabelFont(new Font("SansSerif", 0, 14));
            numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            numberaxis.setVisible(false);
            numberaxis.setLowerBound(1500D);
            numberaxis.setUpperBound(3000D);

            XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(true,false);
            xylineandshaperenderer.setSeriesPaint(0, Color.GREEN);  //线段颜色为绿
            xylineandshaperenderer.setSeriesStroke(0,new BasicStroke(2));
            xylineandshaperenderer.setSeriesPaint(1, Color.LIGHT_GRAY);  //线段颜色为红
            xylineandshaperenderer.setSeriesStroke(1,new BasicStroke(5));

            XYPlot xyplot = new XYPlot(timeseriescollection, dateAxises[i], numberaxis, xylineandshaperenderer);
            xyplot.setBackgroundPaint(Color.LIGHT_GRAY);
            xyplot.setDomainGridlinePaint(Color.LIGHT_GRAY);
            xyplot.setRangeGridlinePaint(Color.LIGHT_GRAY);
            xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
            xyplot.setBackgroundPaint(Color.BLACK);

            JFreeChart jfreechart = new JFreeChart(xyplot);
            jfreechart.setBackgroundPaint(new Color(237,237,237));//背景色为浅灰
            jfreechart.getLegend().setVisible(false);

            ChartPanel chartpanel = new ChartPanel(jfreechart,
                    (int) (WIDTH * 46/ 100), (int) (HEIGHT * 17/100), 0, 0,
                    Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                    true, false, false);

            chartpanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)     //边界线条间距为0
                    ,BorderFactory.createEmptyBorder()          //边界线条不可见
            ));
            chartpanel.setMouseZoomable(false); //禁止鼠标缩放
            ECGData.add(chartpanel);
        }
    }

    private void createHeartRateData(long timeZone) {
        HeartRatedatas=new short[2];
        HeartRateData=new JPanel();
        //HeartRateData.setLayout(new BorderLayout());
        HeartRateData.setLayout(new FlowLayout());
        HeartRateData.setBounds(0,0,(int)(WIDTH * 0.14), (int) (HEIGHT * 0.15));
        HeartRateData.setBackground(Color.BLACK);

        JLabel jLabel1=new JLabel("---");
        if (HeartRatedatas[0]==0x00||HeartRatedatas==null){
            jLabel1.setText("---");
        }
        else
        {
            jLabel1.setText(Short.toString((short)HeartRatedatas[0]));
        }

        jLabel1.setFont(loadFont("LED.tff",64.0f));
        jLabel1.setBackground(Color.BLACK);
        jLabel1.setForeground(Color.GREEN);
        jLabel1.setBounds(0,0,100,100);
        jLabel1.setOpaque(true);    //设置控件不透明

        JLabel jLabelName=new JLabel("心率 ");
        jLabelName.setFont(new Font("SansSerif", 0, 14));
        jLabelName.setBackground(Color.BLACK);
        jLabelName.setForeground(new Color(237, 65, 43));
        jLabelName.setBounds(0,0,100,100);
        jLabelName.setOpaque(true);    //设置控件不透明

        JLabel jLabelUnit=new JLabel(" bpm");
        jLabelUnit.setFont(new Font("SansSerif", 0, 14));
        jLabelUnit.setBackground(Color.BLACK);
        jLabelUnit.setForeground(Color.GREEN);
        jLabelUnit.setBounds(0,0,100,100);
        jLabelUnit.setOpaque(true);    //设置控件不透明

        HeartRateData.add(jLabelName);
        HeartRateData.add(jLabel1);
        HeartRateData.add(jLabelUnit);
        System.out.println("HeartRatedatas"+Short.toString(HeartRatedatas[0]));
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                jLabel1.setText( String.valueOf(HeartRatedatas[0]));
                HeartRateData.repaint();
            }
        },0,3, TimeUnit.SECONDS);
    }

    private void createPressureData(long timeZone){
        PressureData=new JPanel();
        PressuredateAxises = new DateAxis[1];
        SystolicPressureSeries= new TimeSeries[2];
        DiastolicPressureSeries= new TimeSeries[2];

        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        SystolicPressureSeries[0] = new TimeSeries("");
        SystolicPressureSeries[0].setMaximumItemAge(timeZone);
        SystolicPressureSeries[0].setMaximumItemCount(500);
        SystolicPressureSeries[1] = new TimeSeries("");
        SystolicPressureSeries[1].setMaximumItemAge(timeZone);
        SystolicPressureSeries[1].setMaximumItemCount(2);
        timeseriescollection.addSeries(SystolicPressureSeries[0]);
        timeseriescollection.addSeries(SystolicPressureSeries[1]);

        PressuredateAxises[0] = new DateAxis("");
        PressuredateAxises[0].setTickLabelFont(new Font("SansSerif", 0, 12));
        PressuredateAxises[0].setLabelFont(new Font("SansSerif", 0, 14));
        PressuredateAxises[0].setTickLabelsVisible(true);
        PressuredateAxises[0].setVisible(false);

        DiastolicPressureSeries[0] = new TimeSeries("");
        DiastolicPressureSeries[0].setMaximumItemAge(timeZone);
        DiastolicPressureSeries[0].setMaximumItemCount(500);
        DiastolicPressureSeries[1] = new TimeSeries("");
        DiastolicPressureSeries[1].setMaximumItemAge(timeZone);
        DiastolicPressureSeries[1].setMaximumItemCount(2);
        timeseriescollection.addSeries(DiastolicPressureSeries[0]);
        timeseriescollection.addSeries(DiastolicPressureSeries[1]);

        NumberAxis numberaxis = new NumberAxis("Pressure");
        numberaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
        numberaxis.setLabelFont(new Font("SansSerif", 0, 14));
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setVisible(false);
        numberaxis.setLowerBound(0D);
        numberaxis.setUpperBound(200D);

        XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(true,false);
        xylineandshaperenderer.setSeriesPaint(0, Color.GREEN);  //线段颜色为绿
        xylineandshaperenderer.setSeriesStroke(0,new BasicStroke(2));   //线粗
        xylineandshaperenderer.setSeriesPaint(1, Color.LIGHT_GRAY);  //线段颜色为红
        xylineandshaperenderer.setSeriesStroke(1,new BasicStroke(5));

        xylineandshaperenderer.setSeriesPaint(2, Color.ORANGE);  //线段颜色为绿
        xylineandshaperenderer.setSeriesStroke(2,new BasicStroke(2));   //线粗
        xylineandshaperenderer.setSeriesPaint(3, Color.LIGHT_GRAY);  //线段颜色为红
        xylineandshaperenderer.setSeriesStroke(3,new BasicStroke(5));



        //XYPlot xyplot = new XYPlot(timeseriescollection, PressuredateAxises[0], numberaxis, xylineandshaperenderer);
        XYPlot xyplot = new XYPlot(timeseriescollection, dateAxises[0], numberaxis, xylineandshaperenderer);

        xyplot.setBackgroundPaint(Color.LIGHT_GRAY);
        xyplot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        xyplot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        xyplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        xyplot.setBackgroundPaint(Color.BLACK);

        JFreeChart jfreechart = new JFreeChart(xyplot);
        jfreechart.setBackgroundPaint(new Color(237,237,237));//背景色为浅灰
        jfreechart.getLegend().setVisible(false);

        ChartPanel chartpanel = new ChartPanel(jfreechart,
                (int) (WIDTH * 0.155), (int) (HEIGHT * 0.18), 0, 0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                true, false, false);

        chartpanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0)     //边界线条间距为0
                ,BorderFactory.createEmptyBorder()          //边界线条不可见
        ));
        chartpanel.setMouseZoomable(false);
        PressureData.add(chartpanel);

    }
    private void createGuardData(){
        temperatureData=new JPanel();
        temperatureData.setLayout(new FlowLayout(FlowLayout.CENTER));
        temperatureData.setBounds(0,0,(int) (WIDTH * 0.28), (int) (HEIGHT * 0.15));
        temperatureData.setBackground(new Color(0,150,255));
        temperatureLabel=new JLabel("--.-");
        temperatureLabel.setFont(loadFont("LED.tff",64.0f));
        temperatureLabel.setBackground(new Color(0,150,255));
        temperatureLabel.setForeground(Color.RED);
        temperatureLabel.setBounds(0,0,200,100);
        temperatureLabel.setOpaque(true);
        JLabel temperatureLabelName=new JLabel("温度 ");
        temperatureLabelName.setFont(new Font("SansSerif", 0, 14));
        temperatureLabelName.setBackground(new Color(0,150,255));
        temperatureLabelName.setForeground(Color.BLACK);
        temperatureLabelName.setBounds(0,0,100,100);
        temperatureLabelName.setOpaque(true);    //设置控件不透明
        temperatureData.add(temperatureLabelName);
        temperatureData.add(temperatureLabel);

        lightValueData=new JPanel();
        lightValueData.setLayout(new BorderLayout());
        lightValueData.setBounds(0,0,(int)(WIDTH*0.28),(int)(HEIGHT*0.52));
        lightValueDataSet=new DefaultValueDataset();
        DialPlot lightValueDialPlot=new DialPlot();
        lightValueDialPlot.setDataset(lightValueDataSet);
        StandardDialFrame dialFrame=new StandardDialFrame();
        dialFrame.setVisible(false);
        lightValueDialPlot.setDialFrame(dialFrame);

        GradientPaint gradientpaint=new GradientPaint(new Point(), new Color(0, 200, 255), new Point(), new Color(170, 170, 220));
        DialBackground dialBackground=new DialBackground(gradientpaint);
        dialBackground.setGradientPaintTransformer(new StandardGradientPaintTransformer(
                GradientPaintTransformType.VERTICAL));
        lightValueDialPlot.setBackground(dialBackground);
        // 设置显示在表盘中央位置的信息
        DialTextAnnotation dialtextannotation = new DialTextAnnotation("");
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
        StandardDialRange bubbleDialRange =new StandardDialRange(0D, 500D, Color.black);
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
        ChartPanel lightValueDialChartPanel=new ChartPanel(lightValueDialChart,(int)(WIDTH*0.28),(int)(HEIGHT*0.52), 0,0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                true, false, false);
        lightValueData.add(lightValueDialChartPanel);

    }
    private void createBloodOxygenData(long timeZone){
        BloodOxygendatas=new short [2];
        BloodOxygenData=new JPanel();
        BloodOxygenData.setLayout(new FlowLayout());
        BloodOxygenData.setBounds(0,0,(int) (WIDTH * 0.14), (int) (HEIGHT * 0.15));
        BloodOxygenData.setBackground(Color.BLACK);

        JLabel jLabel1=new JLabel("---");
        if (BloodOxygendatas[0]==0x00||BloodOxygendatas==null){
            jLabel1.setText("---");
        }
        else {
            jLabel1.setText(Short.toString((short)BloodOxygendatas[0]));
        }
        System.out.println("BloodOxygendatas"+Short.toString(BloodOxygendatas[0]));
        jLabel1.setFont(loadFont("LED.tff",64.0f));
        jLabel1.setBackground(Color.BLACK);
        jLabel1.setForeground(Color.GREEN);
        jLabel1.setBounds(0,0,100,100);
        jLabel1.setOpaque(true);

        JLabel jLabelName=new JLabel("血氧 ");
        jLabelName.setFont(new Font("SansSerif", 0, 14));
        jLabelName.setBackground(Color.BLACK);
        jLabelName.setForeground(new Color(237, 65, 43));
        jLabelName.setBounds(0,0,100,100);
        jLabelName.setOpaque(true);    //设置控件不透明

        JLabel jLabelUnit=new JLabel(" %");
        jLabelUnit.setFont(new Font("SansSerif", 0, 14));
        jLabelUnit.setBackground(Color.BLACK);
        jLabelUnit.setForeground(Color.GREEN);
        jLabelUnit.setBounds(0,0,100,100);
        jLabelUnit.setOpaque(true);    //设置控件不透明

        BloodOxygenData.add(jLabelName);
        BloodOxygenData.add(jLabel1);
        BloodOxygenData.add( jLabelUnit);

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                jLabel1.setText( String.valueOf(BloodOxygendatas[0]));
                BloodOxygenData.repaint();
            }
        },0,3, TimeUnit.SECONDS);
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        JFrame jFrame=new JFrame();
        jFrame.setContentPane(new ECGShowUI("",5000L).HeartRateData);
        jFrame.pack();
        jFrame.setVisible(true);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	new ECGShowUI("ecg",5000L).setVisible(true);
            }
        });
    }

    //public JPanel getECGInfo(){return ECGInfo;}
    public JPanel getECGData(){return ECGData;}
    public JPanel getHeartRateData(){return HeartRateData;}
    public JPanel getPressureData(){return PressureData;}
    public JPanel getBloodOxygenData(){return BloodOxygenData;}
    public JPanel getTemperatureData(){
        return temperatureData;
    }
    public JPanel getLightValueData(){
        return lightValueData;
    }


    public TimeSeries[] getECGSeries() {
        return ECGSeries;
    }
    public void setECGSeries(TimeSeries[] ECGSeries) {
        this.ECGSeries = ECGSeries;
    }

    public DateAxis[] getDateAxises() {
        return dateAxises;
    }
    public void setDateAxises(DateAxis[] dateAxises) {
        this.dateAxises = dateAxises;
    }

    public DateAxis[] getPressuredateAxises () {
        return PressuredateAxises ;
    }
    public void setPressuredateAxises (DateAxis[] PressuredateAxises ) {
        this.PressuredateAxises  = PressuredateAxises ;
    }

    public TimeSeries[] getSystolicPressureSeries() {
        return SystolicPressureSeries;
    }
    public void setSystolicPressureSeries(TimeSeries[] SystolicPressureSeries) {
        this.SystolicPressureSeries =SystolicPressureSeries;
    }

    public TimeSeries[] getDiastolicPressureSeries() {
        return DiastolicPressureSeries;
    }
    public void setDiastolicPressureSeries(TimeSeries[] DiastolicPressureSeries) {
        this.DiastolicPressureSeries =DiastolicPressureSeries;
    }


    public short[] getHeartRatedatas(){
        return HeartRatedatas;

    }
    public short[] getBloodOxygendatas(){
        return BloodOxygendatas;
    }


    public static Font loadFont(String fontFileName, float fontSize)  //第一个参数是外部字体名，第二个是字体大小
    {
        try
        {
            File file = new File(fontFileName);
            InputStream aixing = ECGShowUI.class.getClassLoader().getResourceAsStream("LED.tff");
            Font dynamicFont = Font.createFont(Font.TRUETYPE_FONT, aixing);
            Font dynamicFontPt = dynamicFont.deriveFont(fontSize);
            aixing.close();
            return dynamicFontPt;
        }
        catch(Exception e)//异常处理
        {
            e.printStackTrace();
            return new java.awt.Font("宋体", Font.PLAIN, 64);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        GuardData guardData=(GuardData)arg;
        temperatureLabel.setText(guardData.getTemperature());

    }

//    public ECGOtherData getECGOtherData(){return ecgOtherData;}
//    public void setEcgOtherData(ECGOtherData ecgOtherData){this.ecgOtherData=ecgOtherData;}



}
