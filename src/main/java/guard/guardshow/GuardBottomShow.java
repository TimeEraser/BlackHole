package guard.guardshow;

import ecg.ecgshow.ECGDataRefresher;
import guard.guardDataProcess.GuardData;
import guard.guardDataProcess.GuardSerialDataProcess;
import mobile.TransData;
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
import util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by adminstrator on 2016/6/3.
 */
public class GuardBottomShow extends JPanel implements Observer {
    private Icon greenPoint;
    private Icon redPoint;
    private JLabel connectStatePoint;
    private JLabel ecgConnectStatePoint;
    private JLabel mobileConnectStatePoint;
    private DefaultCategoryDataset temperatureDataSet;
    private BarRenderer temperatureRenderer;
    private DefaultPieDataset lightValueDataSet;
    private static int guardConnectFlashFlag = 0;
    private static boolean guardConnectStartFlag = false;
    private static int guardConnectLostCount = 0;

    private static int ecgConnectFlashFlag = 0;
    private static boolean ecgConnectStartFlag = false;
    private static int ecgConnectLostCount = 0;

    private static int mobileConnectFlashFlag=0;
    private static boolean mobileConnectStateFlag=false;
    private static int mobileConnectLostCount=0;
    private int HEIGHT;

    public GuardBottomShow(int WIDTH,int HEIGHT) {
        this.HEIGHT=HEIGHT;
        JPanel connectStatePointPanel;
        JPanel ecgConnectStatePointPanel;
        JPanel mobileConnectStatePointPanel;
        JPanel temperatureStatePointPanel;
        JPanel lightValueStatePointPanel;
        JLabel connectState = new JLabel("报警模块连接");
        connectState.setFont(new Font("Dialog", 0, (int)(WIDTH*0.013)));
        connectState.setPreferredSize(new Dimension((int)(WIDTH*0.08),(int)(HEIGHT*0.074)));
        JLabel ecgConnectState = new JLabel("心电仪连接");
        ecgConnectState.setFont(new Font("Dialog", 0, (int)(WIDTH*0.013)));
        ecgConnectState.setPreferredSize(new Dimension((int)(WIDTH*0.07),(int)(HEIGHT*0.074)));
        JLabel mobileConnectState = new JLabel("手机连接");
        mobileConnectState.setFont(new Font("Dialog", 0, (int)(WIDTH*0.013)));
        mobileConnectState.setPreferredSize(new Dimension((int)(WIDTH*0.06),(int)(HEIGHT*0.074)));
        JLabel temperatureState = new JLabel("血温状态");
        temperatureState.setFont(new Font("Dialog", 0, (int)(WIDTH*0.013)));
        temperatureState.setPreferredSize(new Dimension((int)(WIDTH*0.06), (int)(HEIGHT*0.074)));
        JLabel lightValueState = new JLabel("管内流体状态");
        lightValueState.setFont(new Font("Dialog", 0, (int)(WIDTH*0.013)));
        lightValueState.setPreferredSize(new Dimension((int)(WIDTH*0.08), (int)(HEIGHT*0.074)));
        try {
            Image greenImage = ImageIO.read(getIconImage("Icon/green.png"));
            BufferedImage greenBufferImage = ImageUtil.zoom(greenImage, (int) (WIDTH * 0.0288), (int) (HEIGHT * 0.0525), new Color(1f, 1f, 1f, 0f));
            Image redImage = ImageIO.read(getIconImage("Icon/red.png"));
            BufferedImage redBufferImage = ImageUtil.zoom(redImage, (int) (WIDTH * 0.0288), (int) (HEIGHT * 0.0525), new Color(1f, 1f, 1f, 0f));
            greenPoint = new ImageIcon(greenBufferImage);
            redPoint = new ImageIcon(redBufferImage);
        }catch (IOException e){}

        connectStatePoint = new JLabel(redPoint);
        connectStatePointPanel = new JPanel();
        connectStatePointPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        connectStatePointPanel.setPreferredSize(new Dimension((int)(WIDTH*0.035),(int)(WIDTH*0.035)));
        connectStatePointPanel.add(connectStatePoint);
        ecgConnectStatePoint=new JLabel(redPoint);
        ecgConnectStatePointPanel = new JPanel();
        ecgConnectStatePointPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        ecgConnectStatePointPanel.setPreferredSize(new Dimension((int)(WIDTH*0.035),(int)(WIDTH*0.035)));
        ecgConnectStatePointPanel.add(ecgConnectStatePoint);
        mobileConnectStatePoint=new JLabel(redPoint);
        mobileConnectStatePointPanel = new JPanel();
        mobileConnectStatePointPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        mobileConnectStatePointPanel.setPreferredSize(new Dimension((int)(WIDTH*0.035),(int)(WIDTH*0.035)));
        mobileConnectStatePointPanel.add(mobileConnectStatePoint);

        temperatureStatePointPanel = new JPanel();
        temperatureStatePointPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        temperatureStatePointPanel.add(createTemperatureChartPanel());
        lightValueStatePointPanel = new JPanel();
        lightValueStatePointPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        lightValueStatePointPanel.add(createLightValueChartPanel());

        JPanel connectStatePanel = new JPanel();
        connectStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, (int)(WIDTH*0.00625), 0));
        connectStatePanel.setPreferredSize(new Dimension((int)(WIDTH*0.18), (int)(HEIGHT*0.0738)));
        connectStatePanel.add(connectState);
        connectStatePanel.add(connectStatePointPanel);
        JPanel ecgConnectStatePanel = new JPanel();
        ecgConnectStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, (int)(WIDTH*0.00625), 0));
        ecgConnectStatePanel.setPreferredSize(new Dimension((int)(WIDTH*0.18), (int)(HEIGHT*0.0738)));
        ecgConnectStatePanel.add(ecgConnectState);
        ecgConnectStatePanel.add(ecgConnectStatePointPanel);

        JPanel mobileConnectStatePanel = new JPanel();
        mobileConnectStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, (int)(WIDTH*0.00625), 0));
        mobileConnectStatePanel.setPreferredSize(new Dimension((int)(WIDTH*0.18), (int)(HEIGHT*0.0738)));
        mobileConnectStatePanel.add(mobileConnectState);
        mobileConnectStatePanel.add(mobileConnectStatePointPanel);

        JPanel temperatureStatePanel = new JPanel();
        temperatureStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, (int)(WIDTH*0.00625), 0));
        temperatureStatePanel.setPreferredSize(new Dimension((int)(WIDTH*0.18), (int)(HEIGHT*0.0738)));
        temperatureStatePanel.add(temperatureState);
        temperatureStatePanel.add(temperatureStatePointPanel);

        JPanel lightValueStatePanel = new JPanel();
        lightValueStatePanel.setLayout(new FlowLayout(FlowLayout.LEFT, (int)(WIDTH*0.00625), 0));
        lightValueStatePanel.setPreferredSize(new Dimension((int)(WIDTH*0.18), (int)(HEIGHT*0.0738)));
        lightValueStatePanel.add(lightValueState);
        lightValueStatePanel.add(lightValueStatePointPanel);

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(connectStatePanel);
        add(ecgConnectStatePanel);
        add(mobileConnectStatePanel);
        add(temperatureStatePanel);
        add(lightValueStatePanel);

        java.util.Timer timer = new java.util.Timer();
        timer.schedule(
                new java.util.TimerTask() {
                    public void run() {
                        if (guardConnectStartFlag) {
                            guardConnectLostCount += 1;
                        }
                        if (guardConnectLostCount > 3) {
                            guardConnectStartFlag = false;
                            connectStatePoint.setIcon(redPoint);
                            connectStatePoint.setVisible(true);
                        }
                        if(ecgConnectStartFlag) {
                            ecgConnectLostCount += 1;
                        }
                        if(ecgConnectLostCount>2){
                            ecgConnectStartFlag=false;
                            ecgConnectStatePoint.setIcon(redPoint);
                            ecgConnectStatePoint.setVisible(true);
                        }
                        if(mobileConnectStateFlag){
                            mobileConnectLostCount+=1;
                        }
                        if(mobileConnectLostCount>2){
                            mobileConnectStateFlag=false;
                            mobileConnectStatePoint.setIcon(redPoint);
                            mobileConnectStatePoint.setVisible(true);
                        }
                    }
                }, 2000, 2000
        );
    }

    private ChartPanel createTemperatureChartPanel() {
        temperatureDataSet = new DefaultCategoryDataset();
        temperatureDataSet.addValue(0, "temperatureChart", "temperature");
        temperatureRenderer = new BarRenderer();
        JFreeChart temperatureChart = ChartFactory.createBarChart(null,
                null, null, temperatureDataSet,
                PlotOrientation.VERTICAL, false, true, false);
        temperatureChart.setBorderVisible(false);
        temperatureChart.setBorderPaint(null);
        temperatureChart.setBackgroundPaint(null);
        CategoryPlot temperatureCategoryPlot = (CategoryPlot) temperatureChart.getPlot();
        temperatureCategoryPlot.getDomainAxis().setVisible(false);
        temperatureCategoryPlot.getRangeAxis().setVisible(false);
        temperatureCategoryPlot.getRangeAxis().setRange(15.0D, 45.0D);
        temperatureCategoryPlot.setRangeMinorGridlinesVisible(false);
        temperatureCategoryPlot.setDomainCrosshairVisible(false);
        temperatureCategoryPlot.setRangeCrosshairVisible(false);
        temperatureCategoryPlot.setRangeGridlinesVisible(false);
        temperatureRenderer.setSeriesPaint(0, Color.GREEN);
        temperatureCategoryPlot.setRenderer(temperatureRenderer);
        ChartPanel chartPanel=new ChartPanel(temperatureChart,(int)(HEIGHT*0.056),(int)(HEIGHT*0.056), 0, 0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, false, true, false,
                true, false, false);
        chartPanel.setLayout(new BorderLayout());
        return chartPanel;
    }

    private ChartPanel createLightValueChartPanel() {
        JFreeChart lightValueChart = ChartFactory.createPieChart(null, createLightValueDataSet(), true, true, false);
        lightValueChart.getLegend().setVisible(false);
        lightValueChart.setBackgroundPaint(null);
        PiePlot piePlot = (PiePlot) lightValueChart.getPlot();
        piePlot.setBackgroundPaint(null);
        piePlot.setOutlinePaint(null);
        piePlot.setSectionOutlinesVisible(false);
        piePlot.setNoDataMessage(null);
        piePlot.setIgnoreNullValues(true);
        piePlot.setIgnoreZeroValues(true);
        piePlot.setSectionPaint("正常", Color.GREEN);
        piePlot.setSectionPaint("无管", Color.LIGHT_GRAY);
        piePlot.setSectionPaint("管内气泡", Color.YELLOW);
        piePlot.setSectionPaint("管内漏血", Color.RED);
        piePlot.setLabelGenerator(null);
        piePlot.setLabelBackgroundPaint(null);
        piePlot.setLabelShadowPaint(null);
        piePlot.setLabelOutlinePaint(null);
        piePlot.setLabelLinksVisible(false);
        piePlot.setLabelFont(new Font(null));
        piePlot.setShadowPaint(null);
        ChartPanel lightValuePieChartPanel = new ChartPanel(lightValueChart, (int)(HEIGHT*0.056), (int)(HEIGHT*0.056), 0, 0,
                Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                true, false, false);
        lightValuePieChartPanel.setBackground(null);
        lightValuePieChartPanel.setLayout(new BorderLayout());
        return lightValuePieChartPanel;
    }

    private PieDataset createLightValueDataSet() {
        lightValueDataSet = new DefaultPieDataset();
        lightValueDataSet.setValue("正常", 0);
        lightValueDataSet.setValue("无管", 0);
        lightValueDataSet.setValue("管内气泡", 0);
        lightValueDataSet.setValue("管内漏血", 0);
        return lightValueDataSet;
    }

    private URL getIconImage(String path) {
        return this.getClass().getClassLoader().getResource(path);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof GuardSerialDataProcess) {
            GuardData guardData = (GuardData) arg;
            if (!guardConnectStartFlag) {
                connectStatePoint.setIcon(greenPoint);
                guardConnectStartFlag = true;
            }
            guardConnectLostCount = 0;
            connectStatePoint.setVisible((guardConnectFlashFlag % 2 == 1));
            guardConnectFlashFlag += 1;
            temperatureDataSet.setValue(Float.parseFloat(guardData.getTemperature()), "temperatureChart", "temperature");
            if (guardData.getTemperatureMessage() != null) {
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
            if (guardData.isCountFish()) {
                int[] temp = guardData.getCountMess();
                lightValueDataSet.setValue("正常", temp[0]);
                lightValueDataSet.setValue("无管", temp[1]);
                lightValueDataSet.setValue("管内气泡", temp[3]);
                lightValueDataSet.setValue("管内漏血", temp[2]);
            }
        }
        else if(o instanceof ECGDataRefresher){
            if(arg.equals("flash")) {
                if (!ecgConnectStartFlag) {
                    ecgConnectStatePoint.setIcon(greenPoint);
                    ecgConnectStartFlag = true;
                }
                ecgConnectLostCount = 0;
                ecgConnectStatePoint.setVisible(ecgConnectFlashFlag % 8 >= 4);
                ecgConnectFlashFlag += 1;
            }
        }
        else if (o instanceof TransData){
            if(!mobileConnectStateFlag){
                mobileConnectStatePoint.setIcon(greenPoint);
                mobileConnectStateFlag=true;
            }
            mobileConnectLostCount=0;
            mobileConnectStatePoint.setVisible(mobileConnectFlashFlag%4>=2);
            mobileConnectFlashFlag+=1;
        }
    }
}
