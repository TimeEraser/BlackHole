package command;

import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/16.
 */
public enum  GuardRequest implements Request {
    GUARD_DATA,
    GUARD_SERIAL_ASK,
    GUARD_SERIAL_SET,
    GUARD_SERIAL_DATA_PROCESS,
    GUARD_START,
    GUARD_SHUT_DOWN,
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
