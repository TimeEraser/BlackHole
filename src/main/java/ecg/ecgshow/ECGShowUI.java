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
	private JPanel ECGInfo;

    //Init Element
    private Integer WIDTH;
    private Integer HEIGHT;
    //ECGData
    private JPanel ECGData;
    private DateAxis[] dateAxises ;
    private ECGOtherData ecgOtherData=new ECGOtherData();

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
	
	
    public ECGShowUI(String title, long timeZone) {		//构造方法
    	super();
        initComponents(timeZone);
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
                    (int) (WIDTH * 51/ 100), (int) (HEIGHT * 17/100), 0, 0,
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
        
        ECGInfo = new JPanel();
        panel_PI = new JPanel();
        panel_OP = new JPanel();


//      setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
       // setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        
        showException.setText("异常");
        stopRefresh.setText("暂停");
        
        label_HI.setText("健康信息：");
        label_HB.setText("心率：");
        lblBeat.setText(ecgOtherData.getHeart_rate());
        label_APB.setText("收缩压：");
        label_VPB.setText("舒张压：");
        label_HT.setText("血氧：");
        isAPCLabel.setText(ecgOtherData.getSystolic_pressure());
        isPVCLabel.setText(ecgOtherData.getDiastolic_pressure());
        isBeatLabel.setText(ecgOtherData.getBlood_oxygen());
        label_PI.setText("个人信息：");
        label_Name.setText("姓名：");
        lblName.setText("name");
        label_ID.setText("ID：");
        lblId.setText("id");
        label_OP.setText("操作：");

        javax.swing.GroupLayout ECGInfoLayout = new javax.swing.GroupLayout(ECGInfo);
        ECGInfo.setLayout(ECGInfoLayout);
        ECGInfoLayout.setHorizontalGroup(
          ECGInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ECGInfoLayout.createSequentialGroup()
                .addGroup(ECGInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                	.addGroup(ECGInfoLayout.createSequentialGroup()
                         .addGap(20, 20, 20)
                         .addComponent(label_HI))
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_HB)
                        .addGap(2, 2, 2)
                        .addComponent(lblBeat))
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_APB)
                        .addGap(2, 2, 2)
                        .addComponent(isAPCLabel))
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_VPB)
                        .addGap(2, 2, 2)
                        .addComponent(isPVCLabel))
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(label_HT)
                        .addGap(2, 2, 2)
                        .addComponent(isBeatLabel)))
                .addContainerGap(2, Short.MAX_VALUE))
        );
        ECGInfoLayout.setVerticalGroup(
        	ECGInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
             .addGroup(ECGInfoLayout.createSequentialGroup()
                .addGroup(ECGInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_HI)))
                .addGroup(ECGInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_HB))
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lblBeat)))
                .addGroup(ECGInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_APB))
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(isAPCLabel)))
                .addGroup(ECGInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_VPB))
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(isPVCLabel)))
                .addGroup(ECGInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(label_HT))
                    .addGroup(ECGInfoLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(isBeatLabel)))
                .addContainerGap(10, Short.MAX_VALUE))
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
        jFrame.setContentPane(new ECGShowUI("",5000L).ECGInfo);
        jFrame.pack();
        jFrame.setVisible(true);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            	new ECGShowUI("ecg",5000L).setVisible(true);
            }
        });
    }

    public JPanel getECGInfo(){return ECGInfo;}
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

    public ECGOtherData getECGOtherData(){return ecgOtherData;}
    public void setEcgOtherData(ECGOtherData ecgOtherData){this.ecgOtherData=ecgOtherData;}

}
