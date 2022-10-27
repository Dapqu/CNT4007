/**
 * The main class that the project would be executed from, ex. PeerProcess 1001.
 * contains functions like reading the Common.cfg and PeerInfo.cfg file which stores the properties within.
 * And the main function to execute the whole BitTorrent like system.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class PeerProcess {
    public static HashMap<Integer, Peer> peerMap = new HashMap<>();

    // Reads the Common.cfg file.
    public static void readCommonProperties() throws Exception {
            File file = new File("Common.cfg");

            BufferedReader br = new BufferedReader(new FileReader(file));

            String st = br.readLine();

            while (st != null) {
                // Splits the line with separator, whitespace, into individual pieces.
                String[] line = st.split(" ");
                if (Objects.equals(line[0], "NumberOfPreferredNeighbors")) {
                    ConfigurationFileVariables.numberOfPreferredNeighbors = parseInt(line[1]);
                }
                else if (Objects.equals(line[0], "UnchokingInterval")) {
                    ConfigurationFileVariables.unchokingInterval = parseInt(line[1]);
                }
                else if (Objects.equals(line[0], "OptimisticUnchokingInterval")) {
                    ConfigurationFileVariables.optimisticUnchokingInterval = parseInt(line[1]);
                }
                else if (Objects.equals(line[0], "FileName")) {
                    ConfigurationFileVariables.fileName = line[1];
                }
                else if (Objects.equals(line[0], "FileSize")) {
                    ConfigurationFileVariables.fileSize = parseInt(line[1]);
                }
                else if (Objects.equals(line[0], "PieceSize")) {
                    ConfigurationFileVariables.pieceSize = parseInt(line[1]);
                }

                // Debugging output.
                System.out.println(line[0] + " " + line[1]);

                // Reads the next line.
                st = br.readLine();
            }

            br.close();
    }

    // Reads the PeerInfo.cfg.
    public static void readPeerInfo() throws Exception {
        File file = new File("PeerInfo.cfg");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st = br.readLine();

        while (st != null) {
            // Splits the line with separator, whitespace, into individual pieces.
            String[] line = st.split(" ");

            // Creating a new peerInfo Object which stores all the peer data from cfg file.
            Peer newPeer = new Peer(parseInt(line[0]), line[1], parseInt(line[2]), parseInt(line[3]) == 1);
            peerMap.put(parseInt(line[0]), newPeer);

            // Debugging output.
            newPeer.printAllInfo();

            // Reads the next line.
            st = br.readLine();
        }

        br.close();
    }

    public static void main(String[] args) throws Exception {
        int peerID = parseInt(args[0]);

        /*
          Debugging Logger Starts
         */
        int peerID2 = 1002;
        int[] peerIDs = {0, 1, 2};

        Logger.startLogger("log_peer_" + peerID + ".log");

        Logger.logTCPConnection(peerID, peerID2);
        Logger.logConnected(peerID, peerID2);
        Logger.logChangePreferredNeighbors(peerID, peerIDs);
        Logger.logChangeOptUnchokedNeighbors(peerID, peerID2);
        Logger.logUnchoking(peerID, peerID2);
        Logger.logChoking(peerID, peerID2);
        Logger.logHave(peerID, peerID2, 4);
        Logger.logInterested(peerID, peerID2);
        Logger.logNotInterested(peerID, peerID2);
        Logger.logDownload(peerID, peerID2, 4, 20);
        Logger.logCompelete(peerID);

        Logger.stopLogger();
        /*
          Debugging Logger Ends
         */

        readCommonProperties();
        readPeerInfo();
    }
}