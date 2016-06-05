package ct.ctshow;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zzq on 16/5/26.
 */
public class CTDataRefresher  {
    private CTCurrentData ctCurrentData;
    private CTHistoryData ctHistoryData;
    private AtomicBoolean INITIALIZED=new AtomicBoolean(false);
    public CTDataRefresher(CTShowUI ctShowUI){
        ctCurrentData=ctShowUI.getCtCurrentData();
        ctHistoryData=ctShowUI.getCtHistoryData();
    }
    public void refreshCTData(String imagePath ,boolean isHistory){if(imagePath!=null) {
        INITIALIZED.set(true);
        ctCurrentData.refreshImage(imagePath,isHistory);
    }
    }
    public void refreshCurrentResult(String result){ctCurrentData.refreshResult(result);}
    public void refreshHistoryResult(String result){ctHistoryData.refresh(result);}
    public void addHistoryResult(String path){ctHistoryData.addHistory(path);}
    public double[] getCoordinate(){
        return ctCurrentData.getCoordinate();
    }
    public String getImagePath(){
        return ctCurrentData.getImagePath();
    }
    public CTCurrentData getMandelDraw(){return ctCurrentData;}
    public boolean Initialized(){return INITIALIZED.get();}


}