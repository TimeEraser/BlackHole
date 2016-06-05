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
import javax.swing.*;

import guard.guardDataProcess.GuardData;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

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
    private JLabel lightValueLabel;
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
        HeartRatedatas=new short[1];
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
    }

    private void createPressureData(long timeZone){
        PressureData=new JPanel();
        PressuredateAxises = new DateAxis[1];
        SystolicPressureSeries= new TimeSeries[2];//0是测试空数据，1的实际数据
        DiastolicPressureSeries= new TimeSeries[2];

        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        SystolicPressureSeries[0] = new TimeSeries("");
        SystolicPressureSeries[0].setMaximumItemAge(timeZone);
        SystolicPressureSeries[0].setMaximumItemCount(50);
        timeseriescollection.addSeries(SystolicPressureSeries[0]);

        PressuredateAxises[0] = new DateAxis("");
        PressuredateAxises[0].setTickLabelFont(new Font("SansSerif", 0, 12));
        PressuredateAxises[0].setLabelFont(new Font("SansSerif", 0, 14));
        PressuredateAxises[0].setTickLabelsVisible(true);
        PressuredateAxises[0].setVisible(false);

        DiastolicPressureSeries[0] = new TimeSeries("");
        DiastolicPressureSeries[0].setMaximumItemAge(timeZone);
        DiastolicPressureSeries[0].setMaximumItemCount(50);
        timeseriescollection.addSeries(DiastolicPressureSeries[0]);

        NumberAxis numberaxis = new NumberAxis("Pressure");
        numberaxis.setTickLabelFont(new Font("SansSerif", 0, 12));
        numberaxis.setLabelFont(new Font("SansSerif", 0, 14));
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setVisible(false);
        numberaxis.setLowerBound(0D);
        numberaxis.setUpperBound(200D);

        XYLineAndShapeRenderer xylineandshaperenderer = new XYLineAndShapeRenderer(true,false);
        xylineandshaperenderer.setSeriesPaint(0, Color.GREEN);  //线段颜色为绿
        xylineandshaperenderer.setSeriesStroke(0,new BasicStroke(2));
        xylineandshaperenderer.setSeriesPaint(1, Color.LIGHT_GRAY);  //线段颜色为红
        xylineandshaperenderer.setSeriesStroke(1,new BasicStroke(5));

        XYPlot xyplot = new XYPlot(timeseriescollection, PressuredateAxises[0], numberaxis, xylineandshaperenderer);
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
        temperatureData.setLayout(new FlowLayout(FlowLayout.LEFT));
        temperatureData.setBounds(0,0,(int) (WIDTH * 0.28), (int) (HEIGHT * 0.15));
        temperatureData.setBackground(Color.BLACK);
        temperatureLabel=new JLabel("--.-");
        temperatureLabel.setFont(loadFont("LED.tff",64.0f));
        temperatureLabel.setBackground(Color.BLACK);
        temperatureLabel.setForeground(Color.GREEN);
        temperatureLabel.setBounds(0,0,200,100);
        temperatureLabel.setOpaque(true);
        JLabel temperatureLabelNamee=new JLabel("温度 ");
        temperatureLabelNamee.setFont(new Font("SansSerif", 0, 14));
        temperatureLabelNamee.setBackground(Color.BLACK);
        temperatureLabelNamee.setForeground(new Color(237, 65, 43));
        temperatureLabelNamee.setBounds(0,0,100,100);
        temperatureLabelNamee.setOpaque(true);    //设置控件不透明
        temperatureData.add(temperatureLabelNamee);
        temperatureData.add(temperatureLabel);

        lightValueData=new JPanel();
        lightValueData.setLayout(new FlowLayout(FlowLayout.LEFT));
        lightValueData.setBounds(0,0,(int) (WIDTH * 0.28), (int) (HEIGHT * 0.15));
        lightValueData.setBackground(Color.BLACK);
        lightValueLabel=new JLabel("----");
        lightValueLabel.setFont(loadFont("LED.tff",64.0f));
        lightValueLabel.setBackground(Color.BLACK);
        lightValueLabel.setForeground(Color.GREEN);
        lightValueLabel.setBounds(0,0,200,100);
        lightValueLabel.setOpaque(true);
        JLabel lightValueLabelName=new JLabel("透光度 ");
        lightValueLabelName.setFont(new Font("SansSerif", 0, 14));
        lightValueLabelName.setBackground(Color.BLACK);
        lightValueLabelName.setForeground(new Color(237, 65, 43));
        lightValueLabelName.setBounds(0,0,100,100);
        lightValueLabelName.setOpaque(true);    //设置控件不透明
        lightValueData.add(lightValueLabelName);
        lightValueData.add(lightValueLabel);
    }
    private void createBloodOxygenData(long timeZone){
        BloodOxygendatas=new short [1];
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
        lightValueLabel.setText(guardData.getLightValue());
    }

//    public ECGOtherData getECGOtherData(){return ecgOtherData;}
//    public void setEcgOtherData(ECGOtherData ecgOtherData){this.ecgOtherData=ecgOtherData;}



}
