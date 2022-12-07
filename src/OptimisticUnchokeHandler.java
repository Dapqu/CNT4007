import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class OptimisticUnchokeHandler implements Runnable{
    //every x seconds, loop over peerlist and get everyone with weChokedThem true
    //Choose among them who to unchoke
    private ScheduledFuture<?> job = null;
    private ScheduledExecutorService scheduler = null;


    public void startJob() {
        this.job = this.scheduler.scheduleAtFixedRate(this, ConfigService.ConfigurationFileVariables.unchokingInterval, ConfigService.ConfigurationFileVariables.unchokingInterval, TimeUnit.SECONDS);
    }
    public void run() {
        HashSet<Peer> chokedlist = new HashSet<>(ConfigService.getchoked());
        int size = chokedlist.size();
        int item = new Random().nextInt(size);
        int i = 0;
        Peer peerChosen = null;
        for(Peer obj : chokedlist)
        {
            if (i == item)
                peerChosen = obj;
            i++;
        }
        ActualMessage Ms = new ActualMessage(ActualMessage.MessageType.UNCHOKE);
        peerChosen.Client.sendMessage(Ms.message);

    }


}
