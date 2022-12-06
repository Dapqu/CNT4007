/**
 * One of our most important class to handle the Actual Messages that are going to be sent throughout the TCP connection.
 * Contains functions like reading the byte array messages and getting the individual components within them,
 * and handling those messages depending on their types.
 */

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
        messageLength = readMessageLength(Payload);
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

    /*
    public String getMessageTypeName(){

        return messageTypeName;
    }

     */

    public MessageType getMessageType(){

        return messageType;
    }

    public int getMessageLength(){

        return messageLength;
    }

    public byte[] getMessagePayload(){

        return messagePayload;
    }

    public int getSenderID(){
        return SenderID;
    }

    public int getReceiverID(){
        return ReceiverID;
    }

    public int getPieceIndex(){
        return PieceIndex;
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