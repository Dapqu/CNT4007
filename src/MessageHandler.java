import java.util.Arrays;
import java.util.Random;

public class MessageHandler {
    //maybe return a message to send? This might be done separately though

    public static byte[] handleMessage(ActualMessage message, Peer peer1, int peer2ID){
        byte[] payload = message.getMessagePayload();
        byte[] response = null;
        switch(message.messageType){
            case CHOKE:
                handleChoke(peer2ID);
                Logger.logChoking(peer2ID, peer1.getPeerID());
                break;

            case UNCHOKE:
                handleUnchoke(peer2ID);
                Logger.logUnchoking(peer2ID, peer1.getPeerID());
                response = sendRequest(peer1);
                break;

            case INTERESTED:
                handleInterested(peer2ID);
                Logger.logInterested(peer2ID, peer1.getPeerID());
                break;

            case NOT_INTERESTED:
                handleNotInterested(peer2ID);
                Logger.logNotInterested(peer2ID, peer1.getPeerID());
                break;

            case HAVE:
                handleHave(payload, peer1);
                Logger.logHave(peer2ID, peer1.getPeerID(), byteArrayToInt(Arrays.copyOf(payload, Constants.PIECE_INDEX_FIELD_LENGTH)));
                break;

            case BITFIELD:
                //peer1.setBitFieldMap(peer2ID, message.messagePayload);
                response = handleBitfield(payload, peer1);
                break;

            case REQUEST:
                Logger.logRequest(peer2ID, peer1.getPeerID());
                response = handleRequest(payload);
                break;

            case PIECE:
                handlePiece(message, peer2ID);
                response = sendHave(payload);
                break;
        }
        return response;
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
    public static void handleInterested(int peer2){
        ConfigService.peerMap.get(peer2).theyAreInterestedInUs = true;
        // Since logger needs that info.
    }

    // Will need to add functionality for not interested
    public static void handleNotInterested( int peer2){
        ConfigService.peerMap.get(peer2).theyAreInterestedInUs = false;
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
    public static byte[] handleRequest(byte[] payload){
        return sendPiece(byteArrayToInt(payload));
    }

    // Will need to add functionality for piece
    public static void handlePiece(ActualMessage message, int peer2){
        byte[] indexField = new byte[Constants.PIECE_INDEX_FIELD_LENGTH];
        System.arraycopy(getPayload(message.message), 0, indexField, 0, Constants.PIECE_INDEX_FIELD_LENGTH);
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
        System.arraycopy(message, 0, temp, 0, Constants.MESSAGE_LENGTH);

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
        byte[] indexField = new byte[Constants.PIECE_INDEX_FIELD_LENGTH];
        System.arraycopy(payload, 0, indexField, 0, Constants.PIECE_INDEX_FIELD_LENGTH);
        ActualMessage have = new ActualMessage(ActualMessage.MessageType.HAVE, indexField);
        return have.message;
    }

    public static byte[] sendPiece(int index){
        ActualMessage piece = new ActualMessage(ActualMessage.MessageType.PIECE, ConfigService.getPiece(index));
        return piece.message;
    }

    public static byte[] sendRequest(Peer peer1){
        Random rand = new Random();
        int randomBit = -1;

        do {
            randomBit = rand.nextInt(peer1.getBitField().length);
        } while (peer1.getBitField()[randomBit] == 1);

        ActualMessage request = new ActualMessage(ActualMessage.MessageType.REQUEST, new byte[]{peer1.getBitField()[randomBit]});
        return request.message;
    }
}