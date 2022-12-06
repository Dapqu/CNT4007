import java.security.Permission;
import java.util.*;

public class PeerHandler {
    public int peerID;
    public PeerServer server;

    PeerHandler(int peerID) {
        this.peerID = peerID;
    }

    public void loadPeerInfo() {
        try {
            ConfigService.readPeersInfo();
            ConfigService.readCommonProperties();
        }
        catch (Exception e){

        }
    }

    public void startServer() {
        var peer = ConfigService.peerMap.get(peerID);
        server = new PeerServer(peer.getPort(), peerID);
        Logger.startLogger("log_peer_" + peerID + ".log");
    }

    public void reachOut() {
        ConfigService.peerMap.forEach((peerID, peer) -> {
            if(this.peerID >= peerID) return;
            PeerClient peerClient = new PeerClient(peer);
            peerClient.initiateHandshake();
        });
    }
}