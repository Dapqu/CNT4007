import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Integer.parseInt;
import java.util.HashSet;
import java.util.RandomAccess;

public class ConfigService {
    public static HashMap<Integer, Peer> peerMap = new HashMap<>();
    public static class ConfigurationFileVariables {
        public static int numberOfPreferredNeighbors;
        public static int unchokingInterval;
        public static int optimisticUnchokingInterval;
        public static String fileName;
        public static int fileSize;
        public static int pieceSize;

        // Calculates the amount of pieces in the file, which would help for setting up bitfield and further more interactions.
        public static int numOfPieces;
    }


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
            } else if (Objects.equals(line[0], "UnchokingInterval")) {
                ConfigurationFileVariables.unchokingInterval = parseInt(line[1]);
            } else if (Objects.equals(line[0], "OptimisticUnchokingInterval")) {
                ConfigurationFileVariables.optimisticUnchokingInterval = parseInt(line[1]);
            } else if (Objects.equals(line[0], "FileName")) {
                ConfigurationFileVariables.fileName = line[1];
            } else if (Objects.equals(line[0], "FileSize")) {
                ConfigurationFileVariables.fileSize = parseInt(line[1]);
            } else if (Objects.equals(line[0], "PieceSize")) {
                ConfigurationFileVariables.pieceSize = parseInt(line[1]);
            }

            // Debugging output.
            System.out.println(line[0] + " " + line[1]);

            // Reads the next line.
            st = br.readLine();
        }

        // Calculates the amount of pieces in the file, which would help for setting up bitfield and further more interactions.
        ConfigurationFileVariables.numOfPieces = (ConfigurationFileVariables.fileSize + ConfigurationFileVariables.pieceSize - 1)
                / ConfigurationFileVariables.pieceSize;

        br.close();
    }

    // Reads the PeerInfo.cfg.
    public static void readPeersInfo() throws Exception {

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

    public static byte[] getPiece(int pieceindex){
    try {
        RandomAccessFile file = new RandomAccessFile("File.txt", "r");

        int position = ConfigService.ConfigurationFileVariables.pieceSize * pieceindex;
        int size = ConfigService.ConfigurationFileVariables.pieceSize;
        if (pieceindex == ConfigService.ConfigurationFileVariables.numOfPieces - 1) {
            size = ConfigService.ConfigurationFileVariables.fileSize % ConfigService.ConfigurationFileVariables.pieceSize;
        }
        file.seek(position);
        byte[] data = new byte[size];
        file.read(data);
        return data;
    }
    catch (Exception e) {
        e.printStackTrace();

    }
        return new byte[0];

    }
    //get everyone else function

    //get unchoked function
    public static HashSet<Peer> getUnchoked(){
        HashSet<Peer> UnChoked = new HashSet<>();
        ConfigService.peerMap.forEach((peerID, peer) -> {
            if(PeerHandler.peerID != peerID){
                if(!peer.choked){
                    UnChoked.add(peer);
                }
            }
        });
        return UnChoked;
    }
    public static HashSet<Peer> getchoked(){
        HashSet<Peer> Choked = new HashSet<>();
        ConfigService.peerMap.forEach((peerID, peer) -> {
            if(PeerHandler.peerID != peerID){
                if(peer.choked){
                    Choked.add(peer);
                }
            }
        });
        return Choked;
    }

    public static HashSet<Peer> getInterestedInUs(){
        HashSet<Peer> Interested = new HashSet<>();
        ConfigService.peerMap.forEach((peerID, peer) -> {
            if(PeerHandler.peerID != peerID){
                if(peer.theyAreInterestedInUs){
                    Interested.add(peer);
                }
            }
        });
        return Interested;
    }

    public static HashSet<Peer> getNeighbors(){
        HashSet<Peer> Neighbors = new HashSet<>();
        ConfigService.peerMap.forEach((peerID, peer) -> {
            if(PeerHandler.peerID != peerID){
                Neighbors.add(peer);
            }
        });
        return Neighbors;
    }
    //get choked function

    //maybe return them as a map

}