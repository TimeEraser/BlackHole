package command;

import command.config.CommandConfig;

public enum CtResponse implements Response{
    CT_OPEN_IMG,
    CT_ANALYSIS;
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
