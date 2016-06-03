package ecg.ecgshow;

import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import javax.swing.*;
//test
public class DigitalTube {

    public static void createTimeSeriesChart() {
        JFreeChart timeSeriesChart = ChartFactory.createTimeSeriesChart(
                "", "", "", create0(), false,
                false, false);
        timeSeriesChart.setBackgroundPaint(Color.BLACK);
        XYPlot plot = timeSeriesChart.getXYPlot();
        plot.setBackgroundPaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        plot.setBackgroundPaint(Color.BLACK);



        setXYPolt(plot);
        timeSeriesChart.setTextAntiAlias(false);

        ChartFrame frame = new ChartFrame("TestPieChart", timeSeriesChart);
        frame.pack();
        frame.setVisible(true);
    }

    public static void setXYPolt(XYPlot plot) {
        plot.setDomainGridlinePaint( Color.GREEN);
        plot.setRangeGridlinePaint( Color.GREEN);
        // plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        XYItemRenderer r = plot.getRenderer();
        if (r instanceof XYLineAndShapeRenderer) {
            XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) r;
            renderer.setBaseShapesVisible(false);
            renderer.setBaseShapesFilled(false);

        }
    }

    public static void main(String[] args) {
        createTimeSeriesChart();
    }


    private static XYDataset create0() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        Day day = new Day(21, 9, 2008);
        Hour hour0=new Hour(0,day);
        TimeSeries timeSeries1 = new TimeSeries("1");
        timeSeries1.add(new Minute(0, hour0),0);
        timeSeries1.add(new Minute(10, hour0),0);

        TimeSeries timeSeries2 = new TimeSeries("2");
        timeSeries2.add(new Minute(10, hour0), 0);
        timeSeries2.add(new Minute(12, hour0), 25);

        TimeSeries timeSeries3 = new TimeSeries("3");
        timeSeries3.add(new Minute(12, hour0), 25);
        timeSeries3.add(new Minute(14, hour0), 50);

        TimeSeries timeSeries4 = new TimeSeries("4");
        timeSeries4.add(new Minute(4, hour0), 50);
        timeSeries4.add(new Minute(14, hour0), 50);

        TimeSeries timeSeries5 = new TimeSeries("5");
        timeSeries5.add(new Minute(2, hour0), 25);
        timeSeries5.add(new Minute(4, hour0), 50);

        TimeSeries timeSeries6 = new TimeSeries("6");
        timeSeries6.add(new Minute(0, hour0), 0);
        timeSeries6.add(new Minute(2, hour0), 25);

        TimeSeries timeSeries7 = new TimeSeries("7");
        timeSeries7.add(new Minute(2, hour0), 25);
        timeSeries7.add(new Minute(12, hour0), 25);

        dataset.addSeries(timeSeries1);
        dataset.addSeries(timeSeries2);
        dataset.addSeries(timeSeries3);
        dataset.addSeries(timeSeries4);
        dataset.addSeries(timeSeries5);
        dataset.addSeries(timeSeries6);
        dataset.addSeries(timeSeries7);
        return dataset;
    }

}
