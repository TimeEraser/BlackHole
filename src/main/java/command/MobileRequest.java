package command;

import command.config.CommandConfig;

public enum  MobileRequest implements Request{
    MOBILE_CONNECT,
    MOBILE_SYNCHRONIZE,
    MOBILE_DISCONNECT,
    MOBILE_GET_ECG_DATA_REFRESH;

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
