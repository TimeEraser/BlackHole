package actor;

import command.Command;
import command.Request;
import command.Response;
import command.config.CommandConfig;

import java.util.concurrent.ExecutorService;

/**
 * Created by zzq on 16/5/16.
 */
public abstract class BaseActor {
    protected ExecutorService longRunningExecutor;
    protected boolean processCommand(Command command){
        if(command instanceof Request){
            processActorRequest((Request) command);
            return true;
        }
        if (command instanceof Response){
            processActorResponse((Response)command);
            return true;
        }
        return false;
    }
    //redirect Command
    protected boolean redirectCommand(BaseActor receiveActor,Command command){
        receiveActor.processCommand(command);
        return true;
    }
    //build CommandConfig
    protected Command buildCommandConfig(Command command){
        CommandConfig commandConfig = new CommandConfig(this);
        command.setConfig(commandConfig);
        return command;
    }
    protected Command buildCommandConfig(Command command,Object data){
        CommandConfig commandConfig = new CommandConfig(this,data);
        command.setConfig(commandConfig);
        return command;
    }
    //execute Command
    public boolean execute(Command command){
            this.processCommand(buildCommandConfig(command));
        return true;
    }
    public boolean execute(Command command,Object data){
            this.processCommand(buildCommandConfig(command,data));
        return true;
    }
    //send Command
    public boolean sendRequest(BaseActor receiveActor,Request Request){
            receiveActor.processCommand(buildCommandConfig(Request));
        return true;
    }
    public boolean sendRequest(BaseActor receiveActor,Request Request,Object data){
            receiveActor.processCommand(buildCommandConfig(Request,data));
        return true;
    }
    public boolean sendResponse(Request request,Response response,Object data){
        request.getConfig().getSendActor()
                .processCommand(buildCommandConfig(response,data));
        return true;
    }
    public boolean sendResponse(Request request,Response response){
        request.getConfig().getSendActor()
                .processCommand(buildCommandConfig(response));
        return true;
    }
    protected abstract boolean processActorRequest(Request requests);
    protected abstract boolean processActorResponse(Response responses);
    public abstract boolean start();
    public abstract boolean shutdown();
}
