package command;

import command.config.CommandConfig;

public enum MobileResponse  implements Response{
    MOBILE_CONNECT,
    MOBILE_CONNECT_FAILED,
    MOBILE_SYNCHRONIZE,
    MOBILE_DISCONNECT;
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

