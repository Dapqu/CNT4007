import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class PeerServer {

    private int port;
    private int peerID;
    private ServerSocket listener;

    PeerServer(int port, int peerID) {
        this.port = port;
        this.peerID = peerID;
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

    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for dealing with a single client's requests.
     */
    private class Handler extends Thread {
        private String message;    //message received from the client
        private String MESSAGE;    //uppercase message send to the client
        private Socket connection;
        private ObjectInputStream in; //stream read from the socket
        private ObjectOutputStream out;    //stream write to the socket
        private int otherPeerID; //The index number of the client

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
                try {
                    while (true) {
                        //check for handshake then
                        //MESSAGE HANDLER GOES HERE
                        message = (String) in.readObject();
                        //show the message to the user
                        System.out.println("Receive message: " + message + " from client ");
                        //Capitalize all letters in the message
                        MESSAGE = message.toUpperCase();
                        //send MESSAGE back to the client
                        sendMessage(MESSAGE);
                    }
                } catch (ClassNotFoundException classnot) {
                    System.err.println("Data received in unknown format");
                }
            } catch (IOException ioException) {
                System.out.println("Disconnect with Client ");
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
        public void sendMessage(String msg) {
            try {
                out.writeObject(msg);
                out.flush();
                System.out.println("Send message: " + msg + " to Client ");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
}