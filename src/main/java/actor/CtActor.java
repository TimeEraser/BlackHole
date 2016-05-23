package actor;

import actor.config.CtActorConfig;

import com.embededcontest.ctgui.CTMainGUI1;
import command.*;
import command.config.CommandConfig;

import javax.swing.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CtActor extends BaseActor{
	public CtActor(CtActorConfig ctActorConfig){
       // ExecutorService threadPool = Executors.newFixedThreadPool(ctActorConfig.CT_THREAD_POOL_SIZE);
        //TO DO Initialize the GuardActor
    }
    @Override
    public boolean processActorRequest(Request  request ) {
        if(request== MainUiRequest.MAIN_UI_CT_CONFIG){
            Thread thread =new Thread(new Runnable() {
                @Override
                public void run() {
                    CTMainGUI1 jf = new CTMainGUI1();
                   // jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    jf.setSize(512,666);
                    jf.setVisible(true);
                }
            });
            thread.start();
            System.out.println("CtRequest.CT_OPEN_IMG");
        }
        return false;
    }

    @Override
    public boolean processActorResponse(Response  responses) {
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
