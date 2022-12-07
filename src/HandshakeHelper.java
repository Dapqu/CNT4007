/**
 * Initiates right after the TCP connection, general class functionalities for the handshake message interaction.
 */


import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class HandshakeHelper {
    private static byte[] byteHeader = new byte[Constants.HANDSHAKE_HEADER_LENGTH];
    private static final byte[] byteZeroBits = Constants.HANDSHAKE_ZERO_BITS.getBytes();
    private static byte[] bytePeerID = new byte[Constants.HANDSHAKE_PEER_ID_LENGTH];

    private String header;
    private String peerID;

    public HandshakeHelper(String ID) {
        header = Constants.HANDSHAKE_HEADER;
        peerID = ID;
    }
    public HandshakeHelper() {
        header = Constants.HANDSHAKE_HEADER;
    }

    // Decodes Handshake message from byte array into string or integer accordingly.
    public static int parseHandshakeMessage(byte[] messageReceived) {
        // Parse the peer id.
        bytePeerID = Arrays.copyOfRange(messageReceived,
                Constants.HANDSHAKE_HEADER_LENGTH + Constants.HANDSHAKE_ZERO_BITS_LENGTH, Constants.HANDSHAKE_MESSAGE_LENGTH);

        String peerID = new String(bytePeerID);

        return Integer.parseInt(peerID);
    }

    public static boolean VerifyHandShakeMessage(byte[] messageReceived, int expectedPeerID) {
        byte[] temp = Arrays.copyOf(messageReceived, Constants.HANDSHAKE_HEADER_LENGTH);
        String string = new String(temp);
        if(!string.equals(Constants.HANDSHAKE_HEADER)) return false;
        return parseHandshakeMessage(messageReceived) == expectedPeerID;
    }

    // Encodes Handshake message from string and integer into byte array message.
    public static byte[] sendHandshakeMessage() {
        byte[] sendMessage = new byte[Constants.HANDSHAKE_MESSAGE_LENGTH];

        // Put header into the sending message.
        System.arraycopy(byteHeader, 0, sendMessage, 0, Constants.HANDSHAKE_HEADER_LENGTH);

        // Put zero bits into the sending message.
        System.arraycopy(byteZeroBits, 0, sendMessage,
                Constants.HANDSHAKE_HEADER_LENGTH, Constants.HANDSHAKE_ZERO_BITS_LENGTH);

        // Put peer id into the sending message.
        System.arraycopy(bytePeerID, 0, sendMessage,
                Constants.HANDSHAKE_HEADER_LENGTH + Constants.HANDSHAKE_ZERO_BITS_LENGTH, Constants.HANDSHAKE_PEER_ID_LENGTH);

        return sendMessage;
    }

    public int getPeerID() {
        return parseInt(peerID);
    }

    public void setPeerID(int peerID) {
        this.peerID = String.valueOf(peerID);
    }

    public String getHeader() {

        return header;
    }

    public void setHeader(String header) {

        this.header = header;
    }
}