package ct.ctshow;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by zzq on 16/5/26.
 */
public class CTDataRefresher  {

    private CTCurrentData ctCurrentData ;
    private AtomicBoolean INITIALIZED=new AtomicBoolean(false);
    public CTDataRefresher(CTCurrentData ctCurrentData){
        this.ctCurrentData=ctCurrentData;
    }
    public void refreshCTData(String imagePath){if(imagePath!=null) {
        INITIALIZED.set(true);
        ctCurrentData.refreshImage(imagePath);
    }
    }
    public void refreshResult(String result){ctCurrentData.refreshResult(result);}

    public double[] getCoordinate(){
        return ctCurrentData.getCoordinate();
    }
    public String getImagePath(){
        return ctCurrentData.getImagePath();
    }
    public CTCurrentData getMandelDraw(){return ctCurrentData;}
    public boolean Initialized(){return INITIALIZED.get();}


}
