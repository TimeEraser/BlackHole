package command;

import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/16.
 */
public enum  GuardRequest implements Request {
    GUARD_DATA,
    GUARD_BUBBLE,
    GUARD_BLOOD_LEAK,
    GUARD_TEMPERATURE,
    GUARD_ERROR;
    //if CommandConfig==null abandon the request
    private CommandConfig requestConfig=null;
    public CommandConfig setConfig(CommandConfig requestConfig) {
        this.requestConfig=requestConfig;
        return requestConfig;
    }
    public CommandConfig getConfig() {
        return requestConfig;
    }
}
