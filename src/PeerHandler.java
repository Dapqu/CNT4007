import java.security.Permission;
import java.util.*;

public class PeerHandler {
    public static int peerID;
    public static PeerServer server;
    

    PeerHandler(int peerID) {
        this.peerID = peerID;
    }

    public void loadPeerInfo() {
        try {
            ConfigService.readCommonProperties();
            ConfigService.readPeersInfo();
        }
        catch (Exception e){
            System.out.println("GAY");
        }
    }

    public void startServer() {
        var peer = ConfigService.peerMap.get(peerID);
        server = new PeerServer(peer.getPort());
        try {
            server.start();
        }
        catch(Exception e){}
        Logger.startLogger("log_peer_" + peerID + ".log");
    }

    public void reachOut() {
        ConfigService.peerMap.forEach((peerID, peer) -> {
            if(this.peerID <= peerID) return;
            PeerClient peerClient = new PeerClient(peer);
            peer.Client = peerClient;
            Logger.logTCPConnection(PeerHandler.peerID, peerID);
            peerClient.run();
        });
    }
}
