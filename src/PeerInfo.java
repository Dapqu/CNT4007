public class PeerInfo {
    private int peerID;
    private String hostAddress;
    private int port;
    private boolean hasFile;

    public PeerInfo() {

    }

    public PeerInfo(int peerID, String hostAddress, int port, boolean hasFile) {
        this.peerID = peerID;
        this.hostAddress = hostAddress;
        this.port = port;
        this.hasFile = hasFile;
    }

    public int getPeerID() {
        return peerID;
    }

    public void setPeerID(int peerID) {
        this.peerID = peerID;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isHasFile() {
        return hasFile;
    }

    public void setHasFile(boolean hasFile) {
        this.hasFile = hasFile;
    }

    // Debugging method.
    public void printAllInfo() {
        System.out.println(this.peerID + " " + this.hostAddress + " " + this.port + " " + this.hasFile);
    }
}
