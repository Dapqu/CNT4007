import java.util.HashSet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ChokeHandler implements Runnable{

    public int numOfPreferredNeighbors;
    private ScheduledFuture<?> job = null;
    private ScheduledExecutorService scheduler = null;
    private int interval;



    ChokeHandler(){
        numOfPreferredNeighbors = ConfigService.ConfigurationFileVariables.numberOfPreferredNeighbors;
    }

    public void startJob() {
        this.job = this.scheduler.scheduleAtFixedRate(this, 6, this.interval, TimeUnit.SECONDS);
    }


    public void run(){
        HashSet<Peer> unchokedlist = new HashSet<>(ConfigService.getUnchoked());
        HashSet<Peer> interested = new HashSet<>(ConfigService.getInterested());
        //list of prefed neighbors is just whoever is unchoked
        //to find download rate
        HashSet<Peer> neighbors = new HashSet<>(ConfigService.getNeighbors());
        neighbors.forEach(peer -> {
            peer.downloadRate = (ConfigService.ConfigurationFileVariables.pieceSize*ConfigService.ConfigurationFileVariables.numOfPieces)/ConfigService.ConfigurationFileVariables.unchokingInterval;
        });
        final int[] x = {0};
        final Peer[] chosenPeer = new Peer[0];
        for(int i = 0; i < ConfigService.ConfigurationFileVariables.numberOfPreferredNeighbors; i++){
            x[0] = 0;
            chosenPeer[0] = null;
            neighbors.forEach(peer -> {
                if(peer.Interested){
                    if(peer.downloadRate> x[0]){
                        x[0] = peer.downloadRate;
                        chosenPeer[0] = peer;
                    }
                }
                //chosenPeer[0] send unchoke
                neighbors.remove(chosenPeer[0]);
            });

        }

    }
}
