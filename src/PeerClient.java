import java.net.*;
import java.io.*;

public class PeerClient {
	Socket requestSocket;           //socket connect to the server
	ObjectOutputStream out;         //stream write to the socket
 	ObjectInputStream in;          //stream read from the socket
	byte[] message;                //message send to the server
	byte[] Recived;                //capitalized message read from the server
	private Peer peer;

	public PeerClient(Peer peer) {
		this.peer = peer;
	}

	void initiateHandshake()
	{
		try{
			//SEND MESSAGES
			//send handshake then
			requestSocket = new Socket(peer.getHostAddress(), peer.getPort());
			System.out.println("Connected to localhost in port 8000");
			//initialize inputStream and outputStream
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			
			//get Input from standard input
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			HandshakeHelper hs = new HandshakeHelper(peer.getPeerID());
			message = HandshakeHelper.CreateHandShakeMessage(hs);
			//Send the sentence to the server
			sendMessage(message);
			//Receive the upperCase sentence from the server
			Recived = (byte[]) in.readObject();
			//If we get the response we want(bitfield), start a loop of sending and reciving the data.
			//create the message and send it


		}
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
		} 
		catch ( ClassNotFoundException e ) {
            		System.err.println("Class not found");
        	} 
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//Close connections
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	//send a message to the output stream
	void sendMessage(byte[] msg)
	{
		try{
			//stream write the message
			out.writeObject(msg);
			out.flush();
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

}
