package ctshow;

import actor.BaseActor;

import javax.swing.*;

/**
 * Created by zzq on 16/6/1.
 */
public class CTShowUI {
    private CTCurrentData ctCurrentData;
    private CTHistoryData ctHistoryData;
    private CTResultData ctResultData;
    private CTData ctData;
    public CTShowUI(BaseActor boss){
        ctCurrentData=new CTCurrentData();
        ctHistoryData=new CTHistoryData(boss);
        ctResultData=new CTResultData();
        ctData=new CTData(ctCurrentData,ctResultData,512,512);
//        ctData=new CTData(ctCurrentData,ctCurrentData.getResultImageJPanel());
    }

    public CTHistoryData getCtHistoryData() {
        return ctHistoryData;
    }

    public CTCurrentData getCtCurrentData()
    {
        return ctCurrentData;
    }
    public JPanel getCtData(){
        ctData.repaint();
        return ctData;
    }
    public void refreshResult(String result){
        ctResultData.ctResultRefresh(ctCurrentData.getCurrentImage().getWidth(),ctCurrentData.getCurrentImage().getHeight(),ctCurrentData.getFocus(), result);
//        ctCurrentData.refreshResult(result);
    }
    public void refreshCurrentImage(String imagePath ,boolean addMouseAdapterOrNot){
        if(addMouseAdapterOrNot){
            ctResultData.setVisible(false);
        }
        ctCurrentData.refreshImage(imagePath,addMouseAdapterOrNot);
    }
}
