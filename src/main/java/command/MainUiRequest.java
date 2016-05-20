package command;

import command.config.CommandConfig;

public enum  MainUiRequest implements Request {
	MAINUI_CT_CONFIG,
	MAINUI_CT_ANALYSIS,
	MAINUI_ECG_CONFIG,
	MAINUI_ECG_ANALYSIS,
	MAINUI_GUARD_DATA,
	MAINUI_MOBILE_DATA;
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

