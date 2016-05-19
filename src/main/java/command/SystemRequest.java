package command;

import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/16.
 */
public enum  SystemRequest implements  Request{
    BOOT,
    SHUTDOWN;
    private CommandConfig requestConfig=null;
    public CommandConfig setConfig(CommandConfig requestConfig) {
        this.requestConfig=requestConfig;
        return requestConfig;
    }
    public CommandConfig getConfig(){
        return requestConfig;
    }

}
