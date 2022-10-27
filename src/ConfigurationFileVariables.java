/**
 * Similar to Struct in C languages, using to store universal variables for future usage.
 * In this case, store specific variables from Common.cfg file being provided.
 */

public class ConfigurationFileVariables {
    public static int numberOfPreferredNeighbors;
    public static int unchokingInterval;
    public static int optimisticUnchokingInterval;
    public static String fileName;
    public static int fileSize;
    public static int pieceSize;

    // Calculates the amount of pieces in the file, which would help for setting up bitfield and further more interactions.
    public static int numOfPieces;

    private ConfigurationFileVariables() {

    }
}