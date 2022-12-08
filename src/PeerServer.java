import java.net.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class PeerServer extends Thread{

    private ServerSocket listener;
    int port;

    PeerServer(int port) {
        this.port = port;
    }

    public void run() {
        System.out.println("The server is running.");
        try {
            listener = new ServerSocket(port);
            while (true) {
                new Handler(listener.accept()).start();
            }
        } catch(Exception e){

        }
        finally {
            try {
                listener.close();
            }
            catch(Exception e){}
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
        public boolean on = true;

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
                received = readBuffer();
                otherPeerID = HandshakeHelper.parseHandshakeMessage(received);
                response = HandshakeHelper.sendHandshakeMessage();
                sendMessage(response);
                Logger.logConnected(PeerHandler.peerID, otherPeerID);
                while(on) {
                    while (on) {
                        received = readBuffer();
                        ActualMessage Ms = new ActualMessage(received);
                        response = MessageHandler.handleMessage(Ms, ConfigService.peerMap.get(PeerHandler.peerID), otherPeerID);
                        if(response != null)
                            sendMessage(response);
                    }
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
        public void sendMessage(byte[] msg) {
            try {
                out.writeObject(msg);
                out.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        public byte[] readBuffer(){
            byte[] response = null;
            while(response == null){
                try {
                    response = (byte[]) in.readObject();
                }
                catch(Exception e){}
            }
            return response;
        }
    }
}