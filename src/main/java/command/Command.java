package command;

import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/18.
 */
public interface Command {
    public CommandConfig getConfig();
    public CommandConfig setConfig(CommandConfig commandConfig);
}
