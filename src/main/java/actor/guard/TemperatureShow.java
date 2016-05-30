package actor.guard;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.TextAnchor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by adminstrator on 2016/5/28.
 */
public class TemperatureShow extends JPanel implements Observer{
    private TimeSeries tempLine;
    public TemperatureShow() {
        ChartPanel chartPanel;
        // 创建时序图对象
        int WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        Second second=new Second();
        float firstTemperature=(float)25.0;
        tempLine=new TimeSeries("温度",Second.class);
        tempLine.add(second,firstTemperature);
        TimeSeriesCollection tempCollection = new TimeSeriesCollection(tempLine);
        JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("血温图","时间","血温",
                tempCollection,true,true,false);
        jfreechart.setBackgroundPaint(null);
        configFont(jfreechart);
        XYPlot xyPlot=jfreechart.getXYPlot();
        xyPlot.setDataset(1, tempCollection);
        xyPlot.setBackgroundPaint(null);
        xyPlot.setRangeGridlinesVisible(true);
        xyPlot.setRangeGridlinePaint(Color.BLACK);
        chartPanel=new ChartPanel(jfreechart,(int)(WIDTH*0.55),(int)(HEIGHT*0.67), 0,0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                true, false, false);
        this.add(chartPanel);
    }

    private void configFont(JFreeChart chart){
        // 配置字体
        Font xfont = new Font("Dialog", 0, 12);// X轴
        Font yfont = new Font("Dialog", 0, 12);// Y轴
        Font kfont = new Font("Dialog", 0, 12);// 底部
        Font titleFont = new Font("Dialog", Font.BOLD , 25) ; // 图片标题
        XYPlot plot = chart.getXYPlot();// 图形的绘制结构对象
        // 图片标题
        chart.setTitle(new TextTitle(chart.getTitle().getText(),titleFont));
        // 底部
        chart.getLegend().setItemFont(kfont);
        // X 轴
        ValueAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(xfont);// 轴标题
        domainAxis.setTickLabelFont(xfont);// 轴数值
        domainAxis.setTickLabelPaint(Color.BLACK) ; // 字体颜色
        domainAxis.setAutoRange(true);
        domainAxis.setFixedAutoRange(60000D);
        domainAxis.setVisible(true);
        // Y 轴
        ValueAxis rangeAxis = plot.getRangeAxis();
        rangeAxis.setLabelFont(yfont);
        rangeAxis.setLabelPaint(Color.BLACK) ; // 字体颜色
        rangeAxis.setTickLabelFont(yfont);
        rangeAxis.setRange(10.0D,40.0D);
        rangeAxis.setTickLabelsVisible(true);
        rangeAxis.setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        String stringTemp=(String)arg;
        float temperature=Float.parseFloat(stringTemp);
        Second second =new Second();
        tempLine.add(second,temperature);
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
    }

    @Override
    public void setBounds(Rectangle r) {
        super.setBounds(r);
    }
}
