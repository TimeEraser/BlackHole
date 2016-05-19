package command.config;

import actor.BaseActor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzq on 16/5/16.
 */
public class CommandConfig<T> {
    private BaseActor sendActor;
    private T data =null;
    public CommandConfig(BaseActor sendActor , T data){
            this.sendActor=sendActor;
            this.data=data;
    }
    public CommandConfig(BaseActor sendActor){
        this.sendActor=sendActor;
    }

    public BaseActor getSendActor() {
        return sendActor;
    }

    public T getData() {
        return data;
    }
}
