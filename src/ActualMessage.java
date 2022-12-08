/**
 * One of our most important class to handle the Actual Messages that are going to be sent throughout the TCP connection.
 * Contains functions like reading the byte array messages and getting the individual components within them,
 * and handling those messages depending on their types.
 */
import java.lang.*;

public class ActualMessage {
    public enum MessageType {
        CHOKE,
        UNCHOKE,
        INTERESTED,
        NOT_INTERESTED,
        HAVE,
        BITFIELD,
        REQUEST,
        PIECE,
    }

    public MessageType messageType;
    public int messageLength;
    public byte[] messagePayload;
    public byte[] message;

    int SenderID;
    int ReceiverID;

    int PieceIndex;

    public ActualMessage(MessageType Type, byte[] Payload) {
        messageType = Type;
        messagePayload = Payload;
        messageLength = messagePayload.length;
        buildMessage();
    }

    public ActualMessage(MessageType Type) {
        messageType = Type;
        messageLength = 0;
        buildMessage();
    }

    //Build Actual message from byte
    public ActualMessage(byte[] message) {
        messageType = MessageType.values()[message[4]];
        messageLength = readMessageLength(message);
        messagePayload = getPayload(message);
    }

    private void buildMessage() {
        this.message =  new byte[4 + 1 + messageLength];
        // Adding length byte array
        byte[] a = intToByteArray(messageLength);
        System.arraycopy(a, 0, this.message, 0, 4);
        // Adding type byte
        this.message[4] = (byte)(messageType.ordinal());
        // Adding payload byte array
        if(messagePayload != null)
            System.arraycopy(messagePayload, 0, this.message, 5, messageLength);
    }


    public void readMessage(byte[] message){
        messageType = MessageType.values()[message[4]];
        messageLength = readMessageLength(message);
        messagePayload = getPayload(message);
    }

    //Message length might include the one byte type. Check if we have errors.
    public byte[] getPayload(byte[] message){
        byte[] payload = new byte[messageLength];
        System.arraycopy(message, 5, payload, 0, messageLength);
        return payload;
    }

    public int readMessageLength(byte[] message){
        byte[] temp = new byte[Constants.MESSAGE_LENGTH];
        System.arraycopy(message, 0, temp, 0, 4);

        // Convert temp from byte to int
        return byteArrayToInt(temp);
    }

    public MessageType getMessageType(){

        return messageType;
    }

    public int getMessageLength(){

        return messageLength;
    }

    public byte[] getMessagePayload(){

        return messagePayload;
    }

    public static int byteArrayToInt(byte[] bytes) {
        int value = 0;

        for (byte b : bytes) {
            // Shifting previous value 8 bits to right and
            // add it with next value
            value = (value << 8) + (b & 0xFF);
        }

        return value;
    }

    public static byte[] intToByteArray(int integer) {
        return new byte[] {
                (byte) ((integer >> 24) & 0xff),
                (byte) ((integer >> 16) & 0xff),
                (byte) ((integer >> 8) & 0xff),
                (byte) ((integer >> 0) & 0xff),
        };
    }
}