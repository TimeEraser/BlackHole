/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ecg.ecgshow;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author MCH
 */
public class ECGShowUI extends JPanel {

	// Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel contentPane=new JPanel();

    private JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
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
    private DateAxis[] dateAxises ;
    private ECGOtherData ecgOtherData=new ECGOtherData();

    private TimeSeries[] ECGSeries ;
    private TimeSeries[] HeartRateSeries ;
    private TimeSeries[] PressureSeries ;
    private TimeSeries[] BloodOxygenSeries ;

    public ECGShowUI(String title, long timeZone) {		//构造方法
    	super();

        WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        createECGData(timeZone);
        createHeartRateData(timeZone);
        createPressureData(timeZone);
        createBloodOxygenData(timeZone);


    }

    private void createECGData(long timeZone) {
        ECGData = new JPanel(new GridLayout(LEAD_COUNT,1));
        dateAxises = new DateAxis[LEAD_COUNT];
        ECGSeries = new TimeSeries[LEAD_COUNT*2];
        for (int i = 0; i < LEAD_COUNT; i++) {
            TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(); //XYDataset 类型 TimeSeriesCollection
            ECGSeries[i] = new TimeSeries("导联" + (i+1));
            ECGSeries[i].setMaximumItemAge(timeZone);
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
            ECGData.add(chartpanel);
        }
    }

    private void createHeartRateData(long timeZone) {
        HeartRateData=new JPanel();



    }

    private void createPressureData(long timeZone){
        PressureData=new JPanel();
        dateAxises = new DateAxis[2];
        PressureSeries = new TimeSeries[2];

        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
        PressureSeries[0] = new TimeSeries("");
        PressureSeries[0].setMaximumItemAge(timeZone);
        PressureSeries[0].setMaximumItemCount(500);
        timeseriescollection.addSeries(PressureSeries[0]);

        //DateAxis dateaxis = new DateAxis("Time");
        dateAxises[0] = new DateAxis("");
        dateAxises[0].setTickLabelFont(new Font("SansSerif", 0, 12));
        dateAxises[0].setLabelFont(new Font("SansSerif", 0, 14));
        dateAxises[0].setTickLabelsVisible(true);
        dateAxises[0].setVisible(false);

        PressureSeries[1] = new TimeSeries("");
        PressureSeries[1].setMaximumItemAge(timeZone);
        PressureSeries[1].setMaximumItemCount(500);
        timeseriescollection.addSeries(PressureSeries[1]);

        //DateAxis dateaxis = new DateAxis("Time");
        dateAxises[1] = new DateAxis("");
        dateAxises[1].setTickLabelFont(new Font("SansSerif", 0, 12));
        dateAxises[1].setLabelFont(new Font("SansSerif", 0, 14));
        dateAxises[1].setTickLabelsVisible(true);
        dateAxises[1].setVisible(false);




        //NumberAxis numberaxis = new NumberAxis("ecg");
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
                (int) (WIDTH * 0.13), (int) (HEIGHT * 0.18), 0, 0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                true, false, false);

        chartpanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(0, 0, 0, 0)     //边界线条间距为0
                ,BorderFactory.createEmptyBorder()          //边界线条不可见
        ));
        PressureData.add(chartpanel);

    }

    private void createBloodOxygenData(long timeZone){
        BloodOxygenData=new JPanel();
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        JFrame jFrame=new JFrame();
        jFrame.setContentPane(new ECGShowUI("",5000L));
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

    public TimeSeries[] getPressureSeries() {
        return PressureSeries;
    }
    public void setPressureSeries(TimeSeries[] PressureSeries) {
        this.PressureSeries =PressureSeries;
    }

    public ECGOtherData getECGOtherData(){return ecgOtherData;}
    public void setEcgOtherData(ECGOtherData ecgOtherData){this.ecgOtherData=ecgOtherData;}



}
