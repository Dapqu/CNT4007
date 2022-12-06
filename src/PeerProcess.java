public class PeerProcess {
    public static void main(String[] args) {
        String peerID = args[0];
        PeerHandler peerHandler = new PeerHandler(Integer.parseInt(peerID));
        peerHandler.loadPeerInfo();
        peerHandler.startServer();
        peerHandler.reachOut();
    }
}