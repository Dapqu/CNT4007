/**
 * One of our most important class to handle the Actual Messages that are going to be sent throughout the TCP connection.
 * Contains functions like reading the byte array messages and getting the individual components within them,
 * and handling those messages depending on their types.
 */

public class ActualMessage {
    //public int pieces;
    public int messageType;
    public int messageLength;
    public String messageTypeName;
    public byte[] messagePayload;
    public byte[] message;

    public ActualMessage(byte[] M){
        message = M;
    }

    public void readMessage(byte[] message){
        messageLength = readMessageLength(message);
        messageType = message[4];
        messagePayload = getPayload(message);
    }
    //Message length might include the one byte type. Check if we have errors.
    public byte[] getPayload(byte[] message){
        byte[] payload = new byte[messageLength];
        for (int i = 0; i < messageLength; i++) {
            payload[i] = message[i + 5];
        }
        return payload;
    }

    public int readMessageLength(byte[] message){
        byte[] temp = new byte[Constants.MESSAGE_LENGTH];
        for (int i = 0; i < 4; i++) {
            temp[i] = message[i];
        }

        // Convert temp from byte to int
        return byteArrayToInt(temp);
    }

    public void handleMessageType(){
        switch(messageType){
            case Constants.MESSAGE_TYPE_CHOKE:
                messageTypeName = "choke";
                handleChoke();
                break;

            case Constants.MESSAGE_TYPE_UNCHOKE:
                messageTypeName = "unchoke";
                handleUnchoke();
                break;

            case Constants.MESSAGE_TYPE_INTERESTED:
                messageTypeName = "interested";
                handleInterested();
                break;

            case Constants.MESSAGE_TYPE_NOT_INTERESTED:
                messageTypeName = "notInterested";
                handleNotInterested();
                break;

            case Constants.MESSAGE_TYPE_HAVE:
                messageTypeName = "have";
                handleHave();
                break;

            case Constants.MESSAGE_TYPE_BITFIELD:
                messageTypeName = "bitfield";
                handleBitfield();
                break;

            case Constants.MESSAGE_TYPE_REQUEST:
                messageTypeName = "request";
                handleRequest();
                break;

            case Constants.MESSAGE_TYPE_PIECE:
                messageTypeName = "piece";
                handlePiece();
                break;
        }
    }

    public void handleChoke(){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        // Logger.logChoking(id1, id2);
    }

    public void handleUnchoke(){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        // Logger.logUnchoking(id1, id2);
    }

    public void handleInterested(){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        // Logger.logInterested(id1, id2);
    }

    public void handleNotInterested(){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        // Logger.logNotInterested(id1, id2);
    }

    public void handleHave(){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        // Logger.logHave(id1, id2, index);
    }

    public void handleBitfield(){
        // TODO: Do we need log here?
    }

    public void handleRequest(){
        // TODO: Do we need log here?
    }

    public void handlePiece(){
        // TODO: Do we need log here?
        // After piece message, it will be the start of download, thus we will be logging:
        // logDownload() and logComplete().
    }

    public String getMessageTypeName(){
        return messageTypeName;
    }

    public int getMessageType(){
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
}