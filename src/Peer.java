import java.util.Arrays;

public class Peer {
    private int peerID;
    private String hostAddress;
    private int port;
    private boolean hasFile;

    private byte[] bitField;

    public Peer() {

    }

    public Peer(int peerID, String hostAddress, int port, boolean hasFile) {
        this.peerID = peerID;
        this.hostAddress = hostAddress;
        this.port = port;
        this.hasFile = hasFile;

        makeBitField();
    }

    public void makeBitField() {
        int numOfPadBits = 8 - (ConfigurationFileVariables.numOfPieces % 8);
        // If the amount of pieces isn't divisible by 8, add padding bits.(0's)
        if (numOfPadBits != 8) {
            this.bitField = new byte[ConfigurationFileVariables.numOfPieces + numOfPadBits];
        }
        else {
            this.bitField = new byte[ConfigurationFileVariables.numOfPieces];
        }

        if (this.hasFile) {
            Arrays.fill(this.bitField, (byte) 1);

            // Change the padding bits at the end to 0s.
            for (int i = 0; i < numOfPadBits; i++) {
                bitField[ConfigurationFileVariables.numOfPieces + i] = 0;
            }
        }
        else {
            Arrays.fill(this.bitField, (byte) 0);
        }
    }

    public void updateBitField(int i) {
            this.bitField[i] = 1;
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