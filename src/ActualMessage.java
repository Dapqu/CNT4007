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
    int ReciverID;

    int PieceIndex;

    public ActualMessage(byte[] M){
        message = M;
    }

    public ActualMessage(int messageType) {
        messageType = messageType;
        messageLength = 1;
        messagePayload = new byte[0];
    }

    public ActualMessage(int Type, byte[] Payload) {
        messageType = Type;
        messagePayload = Payload;
        messageLength = readMessageLength(Payload);
    }

    public void readMessage(byte[] message){
        messageLength = readMessageLength(message);
        messageType = message[4];
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

    // handles the different types of messages
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

    // Will need to add functionality for Choke
    public void handleChoke(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logChoking(ReciverID, SenderID);
    }

    // Will need to add functionality Unchoke
    public void handleUnchoke(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logUnchoking(ReciverID, SenderID);
    }

    // Will need to add functionality for interested
    public void handleInterested(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logInterested(ReciverID, SenderID);
    }

    // Will need to add functionality for not interested
    public void handleNotInterested(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logNotInterested(ReciverID, SenderID);
    }

    // Will need to add functionality for have
    public void handleHave(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        getPayload(message);
        Logger.logHave(ReciverID, SenderID, PieceIndex);
    }

    // Will need to add functionality for bitfield
    //Potentially own class
    public void handleBitfield(byte[] message){
        // TODO: Do we need log here?
        getPayload(message);
    }

    // Will need to add functionality for request
    public void handleRequest(byte[] message){
        // TODO: Do we need log here?
        Logger.logRequest(ReciverID, SenderID);
        getPayload(message);
    }

    // Will need to add functionality for piece
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

    public int getSenderID(){
        return SenderID;
    }

    public int getReciverID(){
        return ReciverID;
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
}