package command;

import command.config.CommandConfig;

/**
 * Created by zzq on 16/5/16.
 */
public enum  ErrorMessage implements Command{
    BLACK_HOLE_ERROR(100) ,
    BLACK_HOLE_ACTOR_ERROR(200),
    MAINUI_ACTOR_ERROR(300),
    MONITOR_ACTOR_ERROR(400),
    CT_ACTOR_ERROR(500),
    GUARD_ACTOR_ERROR(600),
    MOBILE_ACTOR_ERROR(700);

    private Integer errorCode;
    private String message;
    private CommandConfig commandConfig;
    ErrorMessage(Integer errorCode){
        this.errorCode=errorCode;
    }
    public String setErrorMessage(String message){
        this.message=message;
        return message;
    }
    public CommandConfig getConfig() {
        return commandConfig;
    }

    public CommandConfig setConfig(CommandConfig commandConfig) {
        this.commandConfig=commandConfig;
        return commandConfig;
    }
    @Override
    public String toString() {
        return "ErrorMessage{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                '}';
    }
}
