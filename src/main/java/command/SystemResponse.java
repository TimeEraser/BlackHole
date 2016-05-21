package command;

import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/21.
 */
public enum SystemResponse implements Response{
    SYSTEM_MESSAGE;
    private CommandConfig responseConfig=null;
    @Override
    public CommandConfig getConfig() {
        return responseConfig;
    }

    @Override
    public CommandConfig setConfig(CommandConfig responseConfig) {
        this.responseConfig=responseConfig;
        return responseConfig;
    }
}