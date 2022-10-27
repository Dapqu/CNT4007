/**
 * Initiates right after the TCP connection, general class functionalities for the handshake message interaction.
 */


import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class Handshake {
    private byte[] byteHeader = new byte[Constants.HANDSHAKE_HEADER_LENGTH];
    private final byte[] byteZeroBits = Constants.HANDSHAKE_ZERO_BITS.getBytes();
    private byte[] bytePeerID = new byte[Constants.HANDSHAKE_PEER_ID_LENGTH];

    private String header;
    private int peerID;

    public Handshake() {

    }

    public Handshake(int peerID) {
        this.peerID = peerID;
    }

    // Decodes Handshake message from byte array into string or integer accordingly.
    public static Handshake parseHandshakeMessage(byte[] messageReceived) {
        Handshake handshakeMessage = new Handshake();

        // Parse the header.
        handshakeMessage.byteHeader = Arrays.copyOf(messageReceived, Constants.HANDSHAKE_HEADER_LENGTH);
        handshakeMessage.setHeader(new String(handshakeMessage.byteHeader));

        // Parse the peer id.
        handshakeMessage.bytePeerID = Arrays.copyOfRange(messageReceived,
                Constants.HANDSHAKE_HEADER_LENGTH + Constants.HANDSHAKE_ZERO_BITS_LENGTH, Constants.HANDSHAKE_MESSAGE_LENGTH);
        handshakeMessage.setPeerID(parseInt(new String(handshakeMessage.bytePeerID)));

        return handshakeMessage;
    }

    // Encodes Handshake message from string and integer into byte array message.
    public static byte[] sendHandshakeMessage(Handshake handshakeMessage) {
        byte[] sendMessage = new byte[Constants.HANDSHAKE_MESSAGE_LENGTH];

        // Put header into the sending message.
        System.arraycopy(handshakeMessage.byteHeader, 0, sendMessage, 0, Constants.HANDSHAKE_HEADER_LENGTH);

        // Put zero bits into the sending message.
        System.arraycopy(handshakeMessage.byteZeroBits, 0, sendMessage,
                Constants.HANDSHAKE_HEADER_LENGTH, Constants.HANDSHAKE_ZERO_BITS_LENGTH);

        // Put peer id into the sending message.
        System.arraycopy(handshakeMessage.bytePeerID, 0, sendMessage,
                Constants.HANDSHAKE_HEADER_LENGTH + Constants.HANDSHAKE_ZERO_BITS_LENGTH, Constants.HANDSHAKE_PEER_ID_LENGTH);

        return sendMessage;
    }

    public int getPeerID() {
        return peerID;
    }

    public void setPeerID(int peerID) {
        this.peerID = peerID;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}