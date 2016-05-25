package command;

import command.config.CommandConfig;

public enum  MainUiRequest implements Request {
	MAIN_UI_CT_CONFIG,
	MAIN_UI_CT_ANALYSIS,
	MAIN_UI_ECG_CONFIG,
	MAIN_UI_ECG_ANALYSIS,
	MAIN_UI_GUARD_DATA,
	MAIN_UI_MOBILE_DATA,
	MAIN_UI_ECG_STOP,
	MAIN_UI_ECG_START,
	MAIN_UI_GUARD_START,
	MAIN_UI_SHUTDOWN;
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

