package ctshow;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zzq on 16/5/26.
 */
public class CTDataRefresher  {
    private CTShowUI ctShowUI;
    private CTCurrentData ctCurrentData;
    private CTHistoryData ctHistoryData;
    private AtomicBoolean INITIALIZED=new AtomicBoolean(false);
    public CTDataRefresher(CTShowUI ctShowUI){
        this.ctShowUI=ctShowUI;
        ctCurrentData=ctShowUI.getCtCurrentData();
        ctHistoryData=ctShowUI.getCtHistoryData();
    }
    public void refreshCTData(String imagePath ,boolean addMouseAdapterOrNot){if(imagePath!=null) {
        INITIALIZED.set(true);
        ctShowUI.refreshCurrentImage(imagePath,addMouseAdapterOrNot);
    }
    }
    public void refreshCurrentResult(String result){ctShowUI.refreshResult(result);}
    public void refreshHistoryResult(String result){ctHistoryData.refresh(result);}
    public void addHistoryResult(String path){
        ctHistoryData.addHistory(path);
        ctShowUI.getCtData().repaint();
    }
    public double[] getCoordinate(){
        return ctCurrentData.getCoordinate();
    }
    public String getImagePath(){
        return ctCurrentData.getImagePath();
    }
    public JPanel getMandelDraw(){return ctShowUI.getCtData();}

    public boolean Initialized(){return INITIALIZED.get();}
    public void returnToCurrentImage() {ctCurrentData.returnToCurrentImage(false);}
}
