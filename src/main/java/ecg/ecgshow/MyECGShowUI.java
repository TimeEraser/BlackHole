/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ecg.ecgshow;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
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
public class MyECGShowUI extends JPanel {

	// Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel contentPane=new JPanel();

    private JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
    public static final int LEAD_COUNT=3;
	private ECGDataRefresher ECGDataReFresher;
	private JPanel ecgPanel;
    //Init Element
    private Integer WIDTH;
    private Integer HEIGHT;
    //ECGData
    private JPanel ECGData;
    private DateAxis[] dateAxises ;

    private TimeSeries[] ECGSeries ;
	private JLabel label_HI;
	private JLabel label_HB;
	private JLabel lblBeat;
	private JLabel label_APB;
	private JLabel label_VPB;
	private JLabel label_HT;
	private JLabel isAPCLabel;
	private JLabel isPVCLabel;
	private JLabel isBeatLabel;

	private JPanel panel_PI;

	private JLabel label_PI;
	private JLabel label_Name;
	private JLabel lblName;
	private JLabel label_ID;
	private JLabel lblId;

	private JPanel panel_OP;

	private JButton showException;
	private JButton stopRefresh;

	private JLabel label_OP;
//	private Analizer analizer;	
	
	
    public MyECGShowUI(String title, long timeZone) {		//构造方法
    	super();
       // initComponents(timeZone);
        WIDTH = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        HEIGHT = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        createECGData(timeZone);
    }

    private void createECGData(long timeZone) {
        ECGData = new JPanel(new GridLayout(LEAD_COUNT,1));
        dateAxises = new DateAxis[LEAD_COUNT];
        ECGSeries = new TimeSeries[LEAD_COUNT*2];
        for (int i = 0; i < LEAD_COUNT; i++) {
            TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
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
                    (int) (WIDTH * 10 / 20), (int) (HEIGHT * 9 / 55), 0, 0,
                    Integer.MAX_VALUE, Integer.MAX_VALUE, true, true, false,
                    true, false, false);

            chartpanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(0, 0, 0, 0)     //边界线条间距为0
                    ,BorderFactory.createEmptyBorder()          //边界线条不可见
            ));
            ECGData.add(chartpanel);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    private void initComponents(long timeZone) {		//初始化组件


        jScrollPane1 = new javax.swing.JScrollPane();		//滚动条
        jPanel1 = new JPanel();
        
        showException = new JButton();
        stopRefresh = new JButton();
        
        label_HI = new JLabel();
        label_HB = new JLabel();
        lblBeat = new JLabel();
        label_APB = new JLabel();
        label_VPB = new JLabel();
        label_HT = new JLabel();
        isAPCLabel = new JLabel();
        isPVCLabel = new JLabel();
        isBeatLabel = new JLabel();
        label_PI = new JLabel();
        label_Name = new JLabel();
        lblName = new JLabel();
        label_ID = new JLabel();
        lblId = new JLabel();
        label_OP = new JLabel();
        
        ecgPanel = new JPanel();
        panel_PI = new JPanel();
        panel_OP = new JPanel();


//      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
       // setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        showException.setText("异常");
        stopRefresh.setText("暂停");
        
        label_HI.setText("健康信息：");
        label_HB.setText("心率：");
        lblBeat.setText("beat");
        label_APB.setText("房性早搏：");
        label_VPB.setText("室性早搏：");
        label_HT.setText("心动过速：");
        isAPCLabel.setText("否");
        isPVCLabel.setText("否");
        isBeatLabel.setText("否");
        label_PI.setText("个人信息：");
        label_Name.setText("姓名：");
        lblName.setText("name");
        label_ID.setText("ID：");
        lblId.setText("id");
        label_OP.setText("操作：");

        javax.swing.GroupLayout ecgPanelLayout = new javax.swing.GroupLayout(ecgPanel);
        ecgPanel.setLayout(ecgPanelLayout);
        ecgPanelLayout.setHorizontalGroup(
          ecgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ecgPanelLayout.createSequentialGroup()
                .addGroup(ecgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                	.addGroup(ecgPanelLayout.createSequentialGroup()
                         .addGap(20, 20, 20)
                         .addComponent(label_HI))
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_HB)
                        .addGap(2, 2, 2)
                        .addComponent(lblBeat))
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_APB)
                        .addGap(2, 2, 2)
                        .addComponent(isAPCLabel))
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_VPB)
                        .addGap(2, 2, 2)
                        .addComponent(isPVCLabel))
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_HT)
                        .addGap(2, 2, 2)
                        .addComponent(isBeatLabel)))
                .addContainerGap(2, Short.MAX_VALUE))
        );
        ecgPanelLayout.setVerticalGroup(
        	ecgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
             .addGroup(ecgPanelLayout.createSequentialGroup()
                .addGroup(ecgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_HI)))
                .addGroup(ecgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_HB))
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblBeat)))
                .addGroup(ecgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_APB))
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(isAPCLabel)))
                .addGroup(ecgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_VPB))
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(isPVCLabel)))
                .addGroup(ecgPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_HT))
                    .addGroup(ecgPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(isBeatLabel)))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        
        javax.swing.GroupLayout panel_PILayout = new javax.swing.GroupLayout(panel_PI);
        panel_PI.setLayout(panel_PILayout);
        panel_PILayout.setHorizontalGroup(
        	panel_PILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_PILayout.createSequentialGroup()
                .addGroup(panel_PILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                	.addGroup(panel_PILayout.createSequentialGroup()
                         .addGap(20, 20, 20)
                         .addComponent(label_PI))
                    .addGroup(panel_PILayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_Name)
                        .addGap(2, 2, 2)
                        .addComponent(lblName))
                    .addGroup(panel_PILayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_ID)
                        .addGap(2, 2, 2)
                        .addComponent(lblId)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        panel_PILayout.setVerticalGroup(
        	panel_PILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_PILayout.createSequentialGroup()
                .addGroup(panel_PILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_PILayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_PI)))
                .addGroup(panel_PILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_PILayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_Name))
                    .addGroup(panel_PILayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblName)))
                .addGroup(panel_PILayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_PILayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_ID))
                    .addGroup(panel_PILayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblId)))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        
        javax.swing.GroupLayout panel_OPLayout = new javax.swing.GroupLayout(panel_OP);
        panel_OP.setLayout(panel_OPLayout);
        panel_OPLayout.setHorizontalGroup(
        	panel_OPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_OPLayout.createSequentialGroup()
                .addGroup(panel_OPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                	.addGroup(panel_OPLayout.createSequentialGroup()
                         .addGap(20, 20, 20)
                         .addComponent(label_OP))
                    .addGroup(panel_OPLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(showException)
                        .addGap(2, 2, 2)
                        .addComponent(stopRefresh)))
                .addContainerGap(90, Short.MAX_VALUE))
        );
        panel_OPLayout.setVerticalGroup(
        	panel_OPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_OPLayout.createSequentialGroup()
                .addGroup(panel_OPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_OPLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_OP)))
                .addGroup(panel_OPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_OPLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(showException))
                    .addGroup(panel_OPLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(stopRefresh)))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        //
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(ECGData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        )
                    )
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                    //
                .addGap(10, 10, 10)
                .addComponent(ECGData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(contentPane);
        contentPane.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        this.add(contentPane);
        //pack();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        JFrame jFrame=new JFrame();
        jFrame.setContentPane(new MyECGShowUI("",5000L));
        jFrame.pack();
        jFrame.setVisible(true);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	new MyECGShowUI("ecg",5000L).setVisible(true);
            }
        });
    }

    public ECGDataRefresher getECGDataReFresher() {
		return ECGDataReFresher;
	}
    public JPanel getecgPanel(){return ecgPanel;}
    public JPanel getECGData(){return ECGData;}

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

}
