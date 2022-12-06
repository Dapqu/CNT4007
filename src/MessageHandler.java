public class MessageHandler {
    //maybe return a message to send? This might be done separatly though
    public static void handleMessage(ActualMessage message){
        switch(message.messageType){
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
    public static void handleChoke(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logChoking(ReciverID, SenderID);
    }

    // Will need to add functionality Unchoke
    public static void handleUnchoke(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logUnchoking(ReciverID, SenderID);
    }

    // Will need to add functionality for interested
    public static void handleInterested(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logInterested(ReciverID, SenderID);
    }

    // Will need to add functionality for not interested
    public static void handleNotInterested(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        Logger.logNotInterested(ReciverID, SenderID);
    }

    // Will need to add functionality for have
    public static void handleHave(byte[] message){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
        getPayload(message);
        Logger.logHave(ReciverID, SenderID, PieceIndex);
    }

    // Will need to add functionality for bitfield
    //Potentially own class
    public static byte[] handleBitfield(byte[] message, Peer peer1){
        // TODO: Do we need log here?
        // Once A receives a 'bitfield' message from B, A will compare its bitfield with received bitfield, in this case the payload.
        // If B's bitfield, payload, has pieces that A's bitfield does not have, A sends 'interested' message, else sends 'not interested'.

        byte[] receivedBitField = getPayload(message);
        if (!peer1.getHasFile()) {
            if (receivedBitField != peer1.getBitField()) {
                return sendInterested();
            } else {
                return sendNotInterested();
            }
        }
    }

    // Will need to add functionality for request
    public static void handleRequest(byte[] message){
        // TODO: Do we need log here?
        Logger.logRequest(ReciverID, SenderID);
        getPayload(message);
    }

    // Will need to add functionality for piece
    public static void handlePiece(){
        // TODO: Do we need log here?
        getPayload(message);
        // After piece message, it will be the start of download, thus we will be logging:
        //Logger.logDownload();
        //Logger.logCompelete();
    }
}
