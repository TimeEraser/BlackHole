import actor.BaseActor;
import actor.BlackHoleActor;
import actor.MonitorActor;
import actor.config.BlackHoleConfig;
import command.MonitorRequest;
import actor.config.MonitorActorConfig;
import command.config.CommandConfig;

import java.util.Timer;

/**
 * Created by zzq on 16/5/16.
 */
public class BlackHole {
    public static void main(String[] args) {
        BlackHoleConfig blackHoleConfig = new BlackHoleConfig();
        BlackHoleActor blackHoleActor = new BlackHoleActor(blackHoleConfig);
        blackHoleActor.start();

        int i =0;
        while(i<100) {
            long start = System.currentTimeMillis();
            blackHoleActor.execute(MonitorRequest.Monitor_BLOOD_OXYGEN);
            long end = System.currentTimeMillis();
            System.out.println(end - start);
            i++;
        }
    }
}

