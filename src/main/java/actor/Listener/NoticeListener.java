package actor.Listener;

import actor.BaseActor;
import actor.MainUiActor;
import command.Request;
import command.Response;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by zzq on 16/5/23.
 */
public class NoticeListener extends BaseActor implements ActionListener{
    private MainUiActor mainUiActor;

    private BaseActor receiver;
    //private BaseActor responseRedirectActor;

    private Request request;
    private Object data;
    //private Boolean redirect=false;
    public  NoticeListener(MainUiActor mainUiActor, BaseActor receiver , Request request){
        this.mainUiActor=mainUiActor;
        this.receiver=receiver;
        this.request=request;
    }
    public NoticeListener(MainUiActor mainUiActor, BaseActor receiver ,Request request,Object data){
        this.mainUiActor=mainUiActor;
        this.receiver=receiver;
        this.request=request;
        this.data=data;
    }
    @Override
    public void actionPerformed(ActionEvent e) {

        sendRequest(receiver,request,data);
    }

    @Override
    protected boolean processActorRequest(Request requests) {
        return false;
    }
    @Override
    protected boolean processActorResponse(Response responses) {
        return false;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean shutdown() {
        return false;
    }
}
