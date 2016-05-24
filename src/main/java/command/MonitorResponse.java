package command;

import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/16.
 */
public enum MonitorResponse implements Response {
    MONITOR_DATA,
    MONITOR_ECG_DATA,
    MONITOR_BLOOD_OXYGEN,
    MONITOR_SHUTDOWM,
    MONITOR_PRESSURE_DATA;
    //if CommandConfig==null abandon the response
    CommandConfig responseConfig=null;
    public CommandConfig setConfig(CommandConfig responseConfig) {
        this.responseConfig=responseConfig;
        return responseConfig;
    }
    public CommandConfig getConfig(){
        return responseConfig;
    }
}
