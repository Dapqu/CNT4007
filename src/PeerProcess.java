import java.util.concurrent.TimeUnit;

public class PeerProcess {
    public static void main(String[] args) {
        String peerID = args[0];
        PeerHandler peerHandler = new PeerHandler(Integer.parseInt(peerID));
        Logger.startLogger("log_peer_" + peerID + ".log");
        peerHandler.loadPeerInfo();
        try {
            TimeUnit.SECONDS.sleep(1);
        }
        catch(Exception e){}
        peerHandler.startServer();
        peerHandler.reachOut();
        ChokeHandler cH = new ChokeHandler();
        cH.startJob();
        OptimisticUnchokeHandler oUcH = new OptimisticUnchokeHandler();
        oUcH.startJob();
    }
}