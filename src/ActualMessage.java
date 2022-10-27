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

    int SenderID;
    int ReciverID;

    int PieceIndex;

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

    public void handleMessage(byte[] message){
        readMessage(message);
        switch(messageType){
            case Constants.MESSAGE_TYPE_CHOKE:
                messageTypeName = "choke";
                handleChoke(message);
                break;

            case Constants.MESSAGE_TYPE_UNCHOKE:
                messageTypeName = "unchoke";
                handleUnchoke(message);
                break;

            case Constants.MESSAGE_TYPE_INTERESTED:
                messageTypeName = "interested";
                handleInterested(message);
                break;

            case Constants.MESSAGE_TYPE_NOT_INTERESTED:
                messageTypeName = "notInterested";
                handleNotInterested(message);
                break;

            case Constants.MESSAGE_TYPE_HAVE:
                messageTypeName = "have";
                handleHave(message);
                break;

            case Constants.MESSAGE_TYPE_BITFIELD:
                messageTypeName = "bitfield";
                handleBitfield(message);
                break;

            case Constants.MESSAGE_TYPE_REQUEST:
                messageTypeName = "request";
                handleRequest(message);
                break;

            case Constants.MESSAGE_TYPE_PIECE:
                messageTypeName = "piece";
                handlePiece();
                break;
        }
    }

    public void handleChoke(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logChoking(ReciverID, SenderID);
    }

    public void handleUnchoke(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logUnchoking(ReciverID, SenderID);
    }

    public void handleInterested(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logInterested(ReciverID, SenderID);
    }

    public void handleNotInterested(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logNotInterested(ReciverID, SenderID);
    }

    public void handleHave(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        getPayload(message);
        Logger.logHave(ReciverID, SenderID, PieceIndex);
    }

    public void handleBitfield(byte[] message){
        // TODO: Do we need log here?
        getPayload(message);
    }

    public void handleRequest(byte[] message){
        // TODO: Do we need log here?
        getPayload(message);
    }

    public void handlePiece(){
        // TODO: Do we need log here?
        getPayload(message);
        // After piece message, it will be the start of download, thus we will be logging:
        //Logger.logDownload();
        //Logger.logCompelete();
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