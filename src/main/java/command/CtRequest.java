package command;

import command.config.CommandConfig;

public enum CtRequest implements Request{
    CT_OPEN_IMG,
    CT_UI_CONFIG,
    CT_SHOW_HISTORY,
    CT_SAVE,
    CT_ANALYSIS;
    private CommandConfig requestConfig=null;
    public CommandConfig setConfig(CommandConfig requestConfig) {
        this.requestConfig=requestConfig;
        return requestConfig;
    }
    public CommandConfig getConfig(){
        return requestConfig;
    }


}
