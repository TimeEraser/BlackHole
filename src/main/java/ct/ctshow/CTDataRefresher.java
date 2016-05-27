package ct.ctshow;

import javax.swing.*;

/**
 * Created by zzq on 16/5/26.
 */
public class CTDataRefresher {
    MandelDraw mandelDraw ;
    public CTDataRefresher(MandelDraw mandelDraw){
       this.mandelDraw=mandelDraw;
    }
    public void refreshCTData(String imagePath){
        if(imagePath!=null)
            mandelDraw.refreshImage(imagePath);
    }
    public double[] getCoordinate(){
        return mandelDraw.getCoordinate();
    }
    public String getImagePath(){
        return mandelDraw.getImagePath();
    }
    public void refreshResult(String result){
        mandelDraw.refreshResult(result);
    }
}
