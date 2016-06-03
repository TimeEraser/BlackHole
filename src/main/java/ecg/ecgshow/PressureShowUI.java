package ecg.ecgshow;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
import java.awt.*;


public class PressureShowUI extends JPanel {

    private Integer WIDTH;
    private Integer HEIGHT;

    private DateAxis[] dateAxises ;
    private TimeSeries[] PressureSeries ;

    private ECGOtherData ecgOtherData=new ECGOtherData();

    public PressureShowUI(String title, long timeZone) {		//构造方法
        super();
        WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        createPressureData(timeZone);
    }

    private void createPressureData(long timeZone) {

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
        this.add(chartpanel);
    }



    public static void main(String args[]) {
        /* Create and display the form */
        JFrame jFrame=new JFrame();
        jFrame.setContentPane(new PressureShowUI("",5000L));
        jFrame.pack();
        jFrame.setVisible(true);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PressureShowUI("ecg",5000L).setVisible(true);
            }
        });
    }



    public TimeSeries[] getPressureSeries() {
        return PressureSeries;
    }
    public void setPressureSeries(TimeSeries[] PressureSeries) {
        this.PressureSeries =PressureSeries;
    }

    public DateAxis[] getDateAxises() {
        return dateAxises;
    }
    public void setDateAxises(DateAxis[] dateAxises) {
        this.dateAxises = dateAxises;
    }

    public ECGOtherData getECGOtherData(){return ecgOtherData;}
    public void setEcgOtherData(ECGOtherData ecgOtherData){this.ecgOtherData=ecgOtherData;}



}
