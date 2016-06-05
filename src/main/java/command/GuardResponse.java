package command;

import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/16.
 */
public enum  GuardResponse implements Response {
    GUARD_DATA,
    GUARD_BUBBLE,
    GUARD_BLOOD_LEAK,
    GUARD_TEMPERATURE,
    GUARD_SERIAL_ASK,
    GUARD_SERIAL_SET,
    GUARD_SERIAL_DATA_PROCESS,
    GUARD_START,
    GUARD_ERROR;
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
