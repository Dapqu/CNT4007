import java.util.HashSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ChokeHandler implements Runnable{

    public int numOfPreferredNeighbors;
    public int UnchokingInterval;
    private ScheduledFuture<?> job = null;
    private ScheduledExecutorService scheduler = null;
    private int interval;



    ChokeHandler(){
        preferredNeighbors = ConfigService.ConfigurationFileVariables.numberOfPreferredNeighbors;
        UnchokingInterval = ConfigService.ConfigurationFileVariables.unchokingInterval;
    }

    public void startJob() {
        this.job = this.scheduler.scheduleAtFixedRate(this, 6, this.interval, TimeUnit.SECONDS);
    }


    public void run(){
        HashSet<Peer> unchokedlist = new HashSet<>(ConfigService.getUnchoked());
        HashSet<Peer> interested = new HashSet<>(ConfigService.getInterested());
        //list of prefed neighbors is just whoever is unchoked

    }
}
