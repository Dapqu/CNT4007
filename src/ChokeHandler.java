import java.util.HashSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ChokeHandler implements Runnable{

    private ScheduledFuture<?> job = null;
    private ScheduledExecutorService scheduler;

    public void startJob() {
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.job = this.scheduler.scheduleAtFixedRate(this, 6, ConfigService.ConfigurationFileVariables.unchokingInterval, TimeUnit.SECONDS);
    }

    public void run(){
        HashSet<Peer> unchokedlist = new HashSet<>(ConfigService.getUnchoked());
        //HashSet<Peer> interested = new HashSet<>(ConfigService.getInterested());
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
                if(peer.theyAreInterestedInUs){
                    if(peer.downloadRate> x[0]){
                        x[0] = peer.downloadRate;
                        chosenPeer[0] = peer;
                    }
                }
                ActualMessage Ms = new ActualMessage(ActualMessage.MessageType.UNCHOKE);
                chosenPeer[0].Client.sendMessage(Ms.message);
                chosenPeer[0].choked = false;
                neighbors.remove(chosenPeer[0]);
            });
        }
        neighbors.forEach(peer -> {
            ActualMessage Ms = new ActualMessage(ActualMessage.MessageType.CHOKE);
            chosenPeer[0].Client.sendMessage(Ms.message);
            chosenPeer[0].choked = true;
        });
        final String[] temp = {""};
        ConfigService.getUnchoked().forEach(peer ->{
            temp[0] = temp[0] + Integer.toString(peer.getPeerID());
        });
        Logger.logChangePreferredNeighbors(PeerHandler.peerID,temp[0]);

    }
}
