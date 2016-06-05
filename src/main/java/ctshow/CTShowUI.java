package ctshow;

import actor.BaseActor;

/**
 * Created by zzq on 16/6/1.
 */
public class CTShowUI {
    private CTCurrentData ctCurrentData;
    private CTHistoryData ctHistoryData;
    public CTShowUI(BaseActor boss){
        ctCurrentData=new CTCurrentData();
        ctHistoryData=new CTHistoryData(boss);
    }

    public CTHistoryData getCtHistoryData() {
        return ctHistoryData;
    }

    public CTCurrentData getCtCurrentData() {
        return ctCurrentData;
    }
}
