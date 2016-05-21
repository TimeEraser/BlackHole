package command;

import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/21.
 */
public enum SystemResponse implements Response{
    SYSTEM_MESSAGE;
    private CommandConfig requestConfig=null;
    @Override
    public CommandConfig getConfig() {
        return null;
    }

    @Override
    public CommandConfig setConfig(CommandConfig responseConfig) {
        return null;
    }
}
