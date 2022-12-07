public class MessageHandler {
    //maybe return a message to send? This might be done separately though

    public static byte[] handleMessage(ActualMessage message, Peer peer1, int peer2ID){
        byte[] payload = message.getMessagePayload();
        switch(message.messageType){
            case CHOKE:
                handleChoke(peer2ID);
                Logger.logChoking(peer2ID, peer1.getPeerID());
                break;

            case UNCHOKE:
                handleUnchoke(peer2ID);
                sendRequest();
                Logger.logUnchoking(peer2ID, peer1.getPeerID());
                break;

            case INTERESTED:
                handleInterested(payload);
                Logger.logInterested(peer2ID, peer1.getPeerID());
                break;

            case NOT_INTERESTED:
                handleNotInterested(payload);
                Logger.logNotInterested(peer2ID, peer1.getPeerID());
                break;

            case HAVE:
                handleHave(payload, peer1);
                Logger.logHave(peer2ID, peer1.getPeerID(), PieceIndex);
                break;

            case BITFIELD:
                //peer1.setBitFieldMap(peer2ID, message.messagePayload);
                return handleBitfield(payload, peer1);
                break;

            case REQUEST:
                handleRequest(payload);
                Logger.logRequest(peer2ID, peer1.getPeerID());
                break;

            case PIECE:
                handlePiece(message, peer2ID);
                return sendHave(payload);
                break;
        }
        return null;
    }

    // Will need to add functionality for Choke
    public static void handleChoke(int peer2){
        ConfigService.peerMap.get(peer2).choked = true;
    }

    // Will need to add functionality Unchoke
    public static void handleUnchoke(int peer2){
        ConfigService.peerMap.get(peer2).choked = false;
    }

    // Will need to add functionality for interested
    public static void handleInterested(byte[] payload){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
    }

    // Will need to add functionality for not interested
    public static void handleNotInterested(byte[] payload){
        // TODO: Add parameter to this method maybe?
        // Since logger needs that info.
    }

    // Will need to add functionality for have
    public static byte[] handleHave(byte[] payload, Peer peer1){
        if (!peer1.getHasFile()) {
            if (payload != peer1.getBitField()) {
                return sendInterested();
            } else {
                return sendNotInterested();
            }
        }
        return null;
    }

    // Will need to add functionality for bitfield
    //Potentially own class
    public static byte[] handleBitfield(byte[] payload, Peer peer1){
        // TODO: Do we need log here?
        // Once A receives a 'bitfield' message from B, A will compare its bitfield with received bitfield, in this case the payload.
        // If B's bitfield, payload, has pieces that A's bitfield does not have, A sends 'interested' message, else sends 'not interested'.

        if (!peer1.getHasFile()) {
            if (payload != peer1.getBitField()) {
                return sendInterested();
            } else {
                return sendNotInterested();
            }
        }
        return null;
    }

    // Will need to add functionality for request
    public static void handleRequest(byte[] payload){
        // TODO: Do we need log here?
    }

    // Will need to add functionality for piece
    public static void handlePiece(ActualMessage message, int peer2){
        byte[] indexField = new byte[4];
        System.arraycopy(getPayload(message.message), 0, indexField, 0, 4);
        byte[] content = new byte[readMessageLength(message.message) - ((int) Math.log10(byteArrayToInt(indexField) + 1))];
        System.arraycopy(getPayload(message.message), 4, content, 0, readMessageLength(message.message) - (int) Math.log10(byteArrayToInt(indexField) + 1));

        ConfigService.peerMap.get(peer2).updateBitField(byteArrayToInt(indexField));
        // After piece message, it will be the start of download, thus we will be logging:
        //Logger.logDownload();
        //Logger.logCompelete();
    }

    public static byte[] getPayload(byte[] message){
        int messageLength = readMessageLength(message);
        byte[] payload = new byte[messageLength];
        System.arraycopy(message, 5, payload, 0, messageLength);
        return payload;
    }

    public static int readMessageLength(byte[] message){
        byte[] temp = new byte[Constants.MESSAGE_LENGTH];
        System.arraycopy(message, 0, temp, 0, 4);

        // Convert temp from byte to int
        return byteArrayToInt(temp);
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

    public static byte[] sendInterested(){
        ActualMessage interested = new ActualMessage(ActualMessage.MessageType.INTERESTED);
        return interested.message;
    }

    public static byte[] sendNotInterested(){
        ActualMessage notInterested = new ActualMessage(ActualMessage.MessageType.NOT_INTERESTED);
        return notInterested.message;
    }

    public byte[] sendChoke(){
        ActualMessage choke = new ActualMessage(ActualMessage.MessageType.CHOKE);
        return choke.message;
    }

    public byte[] sendUnchoke(){
        ActualMessage unchoke = new ActualMessage(ActualMessage.MessageType.UNCHOKE);
        return unchoke.message;
    }

    public static byte[] sendHave(byte[] payload) {
        byte[] indexField = new byte[4];
        System.arraycopy(payload, 0, indexField, 0, 4);
        ActualMessage have = new ActualMessage(ActualMessage.MessageType.HAVE, indexField);
        return have.message;
    }

    public static byte[] sendPiece(){
        ActualMessage piece = new ActualMessage(ActualMessage.MessageType.PIECE);
        return piece.message;
    }

    public static byte[] sendRequest(){
        ActualMessage request = new ActualMessage(ActualMessage.MessageType.REQUEST);
        return request.message;
    }
}