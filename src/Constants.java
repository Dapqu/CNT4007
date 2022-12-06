/**
 * Stores constant variables that will be used throughout this project.
 * Values for these constant variables are taking from the project description document.
 */

public final class Constants {
    // Handshake message constants. (in bytes)
    public static final int HANDSHAKE_MESSAGE_LENGTH = 32;

    public static final String HANDSHAKE_HEADER = "P2PFILESHARINGPROJ";
    public static final int HANDSHAKE_HEADER_LENGTH = 18;

    public static final String HANDSHAKE_ZERO_BITS = "0000000000";
    public static final int HANDSHAKE_ZERO_BITS_LENGTH = 10;
    public static final int HANDSHAKE_PEER_ID_LENGTH = 4;

    // Actual message constants. (in bytes)
    public static final int MESSAGE_LENGTH = 4;
    public static final int MESSAGE_TYPE = 1;

    /*
    // Message types. (in string value)
    public static final int MESSAGE_TYPE_CHOKE = 0;
    public static final int MESSAGE_TYPE_UNCHOKE = 1;
    public static final int MESSAGE_TYPE_INTERESTED = 2;
    public static final int MESSAGE_TYPE_NOT_INTERESTED = 3;
    public static final int MESSAGE_TYPE_HAVE = 4;
    public static final int MESSAGE_TYPE_BITFIELD = 5;
    public static final int MESSAGE_TYPE_REQUEST = 6;
    public static final int MESSAGE_TYPE_PIECE = 7;
     */

    // Piece index field.
    public static final int PIECE_INDEX_FIELD_LENGTH = 4;

    private Constants() {

    }
}