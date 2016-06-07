package command;

import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/16.
 */
public enum  MonitorRequest implements Request {
    MONITOR_DATA,
    MONITOR_ECG_DATA,
    MONITOR_ECG_START,
    MONITOR_ECG_STOP,
    MONITOR_ECG_DATA_REFRESH,
    ECG_UI_CONFIG,
    MONITOR_BLOOD_OXYGEN,
    MONITOR_SHUTDOWM,
    MONITOR_PRESSURE_DATA;
    //if CommandConfig==null abandon the request
    private CommandConfig requestConfig=null;
    public CommandConfig setConfig(CommandConfig requestConfig) {
        this.requestConfig=requestConfig;
        return requestConfig;
    }
    public CommandConfig getConfig(){
        return requestConfig;
    }

}
