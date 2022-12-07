import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class PeerServer {

    private Peer peer;
    private ServerSocket listener;

    PeerServer() {
    }

    public void startSever(int port) throws Exception {
        System.out.println("The server is running.");
        listener = new ServerSocket(port);
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }

    }
    //Every peer has a new handler.
    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for dealing with a single client's requests.
     */
    private class Handler extends Thread {
        private byte[] received;    //message received from the client
        private byte[] response;    //uppercase message send to the client
        private Socket connection;
        private ObjectInputStream in; //stream read from the socket
        private ObjectOutputStream out;    //stream write to the socket
        private int otherPeerID;
        public boolean on;

        public Handler(Socket connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                //initialize Input and Output streams
                out = new ObjectOutputStream(connection.getOutputStream());
                out.flush();
                in = new ObjectInputStream(connection.getInputStream());
                //handle handshake and store ID then go into loop
                received = (byte[]) in.readObject();
                otherPeerID = HandshakeHelper.parseHandshakeMessage(received);
                while(on) {
                    try {
                        while (!ConfigService.peerMap.get(otherPeerID).choked) {
                            received = (byte[]) in.readObject();
                            ActualMessage Ms = new ActualMessage(received);
                            response = MessageHandler.handleMessage(Ms, peer, otherPeerID);
                            sendMessage(response);
                        }
                    } catch (ClassNotFoundException classnot) {
                        System.err.println("Data received in unknown format");
                    }
                }
            } catch (IOException ioException) {
                System.out.println("Disconnect with Client ");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                //Close connections
                try {
                    in.close();
                    out.close();
                    connection.close();
                } catch (IOException ioException) {
                    System.out.println("Disconnect with Client ");
                }
            }
        }

        //send a message to the output stream
        public void sendMessage(byte[] msg) {
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}