import actor.BlackHoleActor;
import actor.config.BlackHoleConfig;
import command.MonitorRequest;

/**
 * Created by zzq on 16/5/16.
 */
public class BlackHole {
    public static void main(String[] args) {
        BlackHoleConfig blackHoleConfig = new BlackHoleConfig();
        BlackHoleActor blackHoleActor = new BlackHoleActor(blackHoleConfig);
        blackHoleActor.start();
    }
}

