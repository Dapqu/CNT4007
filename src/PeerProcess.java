import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Objects;

import static java.lang.Integer.parseInt;

public class PeerProcess {
    public static HashMap<Integer, PeerInfo> peerInfoMap = new HashMap<>();

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
            PeerInfo newPeerInfo = new PeerInfo(parseInt(line[0]), line[1], parseInt(line[2]), parseInt(line[3]) == 1);
            peerInfoMap.put(parseInt(line[0]), newPeerInfo);

            // Debugging output.
            newPeerInfo.printAllInfo();

            // Reads the next line.
            st = br.readLine();
        }

        br.close();
    }
}