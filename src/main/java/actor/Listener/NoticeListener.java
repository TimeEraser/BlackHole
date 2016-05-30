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

    private BaseActor receiver;
    private BaseActor responseRedirectActor;
    private Request request;
    private Object data;
    private Boolean redirect=false;
    public  NoticeListener( BaseActor receiver , Request request){
        this.receiver=receiver;
        this.request=request;
    }
    public NoticeListener(BaseActor receiver ,Request request,Object data){
        this(receiver,request);
        this.data=data;
    }
    public  NoticeListener(BaseActor responseRedirectActor, BaseActor receiver , Request request){
        this.responseRedirectActor=responseRedirectActor;
        this.receiver=receiver;
        this.request=request;
        redirect=true;
    }
    public NoticeListener(BaseActor responseRedirectActor, BaseActor receiver ,Request request,Object data){
        this(responseRedirectActor,receiver,request);
        this.data=data;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(redirect){
            sendRequest(responseRedirectActor,request,data);
        }
        else {
            sendRequest(receiver, request, data);
        }
    }

    @Override
    protected boolean processActorRequest(Request requests) {
        return false;
    }
    @Override
    protected boolean processActorResponse(Response responses) {
        if(redirect) redirectCommand(responseRedirectActor,responses);
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
