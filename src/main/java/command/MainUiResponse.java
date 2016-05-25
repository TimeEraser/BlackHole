package command;

import command.config.CommandConfig;

public enum MainUiResponse implements Response{
	MAINUI_CT_CONFIG,
	MAINUI_CT_AN_ANALYSIS,
	MAINUI_ECG_CONFIG,
	MAINUI_ECG_ANALYSIS,
	MAINUI_GUARD_DATA,
	MAIN_UI_ECG_STOP,
	MAIN_UI_ECG_START,
	MAIN_UI_GUARD_START,
	MAINUI_MOBILE_DATA;
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
