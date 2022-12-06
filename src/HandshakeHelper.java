/**
 * Initiates right after the TCP connection, general class functionalities for the handshake message interaction.
 */


import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class HandshakeHelper {
    private byte[] byteHeader = new byte[Constants.HANDSHAKE_HEADER_LENGTH];
    private final byte[] byteZeroBits = Constants.HANDSHAKE_ZERO_BITS.getBytes();
    private byte[] bytePeerID = new byte[Constants.HANDSHAKE_PEER_ID_LENGTH];

    private String header;
    private String peerID;

    public HandshakeHelper(String ID) {
        header = "P2PFILESHARINGPROJ";
        peerID = ID;
    }
    public HandshakeHelper() {
        header = "P2PFILESHARINGPROJ";
    }

    // Decodes Handshake message from byte array into string or integer accordingly.
    public static int parseHandshakeMessage(byte[] messageReceived) {
        // Parse the peer id.
        bytePeerID = Arrays.copyOfRange(messageReceived,
                Constants.HANDSHAKE_HEADER_LENGTH + Constants.HANDSHAKE_ZERO_BITS_LENGTH, Constants.HANDSHAKE_MESSAGE_LENGTH);

        String peerID = new String(bytePeerID);

        return handshakeHelperMessage;
    }

    // Encodes Handshake message from string and integer into byte array message.
    public static byte[] sendHandshakeMessage(HandshakeHelper handshakeHelperMessage) {
        byte[] sendMessage = new byte[Constants.HANDSHAKE_MESSAGE_LENGTH];

        // Put header into the sending message.
        System.arraycopy(handshakeHelperMessage.byteHeader, 0, sendMessage, 0, Constants.HANDSHAKE_HEADER_LENGTH);

        // Put zero bits into the sending message.
        System.arraycopy(handshakeHelperMessage.byteZeroBits, 0, sendMessage,
                Constants.HANDSHAKE_HEADER_LENGTH, Constants.HANDSHAKE_ZERO_BITS_LENGTH);

        // Put peer id into the sending message.
        System.arraycopy(handshakeHelperMessage.bytePeerID, 0, sendMessage,
                Constants.HANDSHAKE_HEADER_LENGTH + Constants.HANDSHAKE_ZERO_BITS_LENGTH, Constants.HANDSHAKE_PEER_ID_LENGTH);

        return sendMessage;
    }
    public byte[] createHandShakeMessage() {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            stream.write(header.getBytes(StandardCharsets.UTF_8));
            stream.write(new byte[10]);
            stream.write(peerID.getBytes(StandardCharsets.UTF_8));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return stream.toByteArray();
    }

    public static byte[] CreateHandShakeMessage(int peerID){
        String s = "P2PFILESHARINGPROJ0000000000" + peerID;
        byte[] byteArrray = s.getBytes();
        return byteArrray;
    }

    public void readHandShakeMessage(byte[] message){
        String msg = new String(message,StandardCharsets.UTF_8);
        this.peerID = msg.substring(28,32);
    }

    //public int getPeerID() {

        //return peerID;
    //}

    public void setPeerID(int peerID) {

        //this.peerID = peerID;
    }

    public String getHeader() {

        return header;
    }

    public void setHeader(String header) {

        this.header = header;
    }
}