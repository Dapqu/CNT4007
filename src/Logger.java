/**
 * Logging class, creates log file if there is none, write specific logs depending on the action being done by the TCP connection.
 * Time stamp in front of each log statement.
 */

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Logger {
    private static FileOutputStream fileStream;
    private static OutputStreamWriter fileWriter;

    // Creates a log file if it doesn't exist.
    public static void startLogger(String fileName) {
        try {
            File file = new File(fileName);

            if (file.createNewFile()) {
                System.out.println("Log file " + file.getName() + " created.");
            }
            else {
                System.out.println("Log file already exists.");
            }

            fileStream = new FileOutputStream(file);
            fileWriter = new OutputStreamWriter(fileStream);

        } catch (IOException e) {
            System.out.println("Logger starting error occurred.");
            throw new RuntimeException(e);
        }
    }

    // Writes given String to the log file.
    public static void writeLog(String log) {
        try {
            // [Time]: [message]
            fileWriter.write(time() + ": "+ log + "." + '\n');

            // Debug
            System.out.println(time() + ": "+ log + "." + '\n');
        } catch (IOException e) {
            System.out.println("Logger writing error occurred.");
            throw new RuntimeException(e);
        }
    }

    // Closes the log file and stop the logging process.
    public static void stopLogger() {
        try {
            fileWriter.flush();
            fileStream.close();
        } catch (IOException e) {
            System.out.println("Logger stopping error occurred.");
            throw new RuntimeException(e);
        }
    }

    // Gets the current date and times and returns it in a readable string.
    private static String time() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    // Whenever a peer makes a TCP connection to other peer, it generates the following log.
    public static void logTCPConnection(int hostPeerID, int connectedPeerID) {
        writeLog("Peer " + hostPeerID + " makes a connection to Peer " + connectedPeerID);
    }

    // Whenever a peer is connected from another peer, it generates the following log.
    public static void logConnected(int hostPeerID, int connectedPeerID) {
        writeLog("Peer " + hostPeerID + " is connected from Peer " + connectedPeerID);
    }

    // Whenever a peer changes its preferred neighborsIDs, it generates the following log.
    public static void logChangePreferredNeighbors(int hostPeerID, int[] neighborsIDs) {
        writeLog("Peer " + hostPeerID + " has the preferred neighborsIDs " + Arrays.toString(neighborsIDs));
    }

    // Whenever a peer changes its optimistically unchoked neighbor, it generates the following log.
    public static void logChangeOptUnchokedNeighbors(int hostPeerID, int unchokedPeerID) {
        writeLog("Peer " + hostPeerID + " has the optimistically unchoked neighbor " + unchokedPeerID);
    }

    // Whenever  a  peer  is  unchoked  by  a  neighbor  (which  means  when  a  peer  receives  an
    // unchoking message from a neighbor), it generates the following log.
    public static void logUnchoking(int PeerID_1, int PeerID_2) {
        writeLog("Peer " + PeerID_1 + " is unchoked by " + PeerID_2);
    }

    // Whenever a peer is choked by a neighbor (which means when a peer receives a choking
    // message from a neighbor), it generates the following log.
    public static void logChoking(int PeerID_1, int PeerID_2) {
        writeLog("Peer " + PeerID_1 + " is choked by " + PeerID_2);
    }

    // Whenever a peer receives a ‘have’ message, it generates the following log.
    public static void logHave(int recevingPeerID, int sendingPeerID, int pieceIndex) {
        writeLog("Peer " + recevingPeerID + " received the 'have' message from " + sendingPeerID + " for the piece " + pieceIndex);
    }

    // Whenever a peer receives an ‘interested’ message, it generates the following log.
    public static void logInterested(int recevingPeerID, int sendingPeerID) {
        writeLog("Peer " + recevingPeerID + " received the 'interested' message from " + sendingPeerID);
    }

    // Whenever a peer receives a ‘not interested’ message, it generates the following log.
    public static void logNotInterested(int recevingPeerID, int sendingPeerID) {
        writeLog("Peer " + recevingPeerID + " received the 'not interested' message from " + sendingPeerID);
    }

    // Whenever a peer finishes downloading a piece, it generates the following log.
    public static void logDownload(int recevingPeerID, int sendingPeerID, int pieceIndex, int numOfPieces) {
        writeLog("Peer " + recevingPeerID + " has downloaded the piece " + pieceIndex + " from " + sendingPeerID + ". Now the number of pieces it has is " + numOfPieces);
    }

    // Whenever a peer downloads the complete file, it generates the following log.
    public static void logCompelete(int hostPeerID) {
        writeLog("Peer " + hostPeerID + " has downloaded the complete file");
    }
}