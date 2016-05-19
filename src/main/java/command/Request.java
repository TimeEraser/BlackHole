package command;


import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/16.
 */
public interface Request extends Command{
    public CommandConfig setConfig(CommandConfig requestConfig);

}
