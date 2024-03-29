import java.net.*;
import java.io.*;

public class PeerClient extends Thread{
	Socket requestSocket;           //socket connect to the server
	ObjectOutputStream out;         //stream write to the socket
 	ObjectInputStream in;          //stream read from the socket
	byte[] message;                //message send to the server
	byte[] Received;                //capitalized message read from the server
	private Peer otherPeer;
	private boolean Unchoked;

	public boolean on = true;

	public PeerClient(Peer otherPeer) {
		this.otherPeer = otherPeer;
	}

	public void run()
	{
		try{
			requestSocket = new Socket(otherPeer.getHostAddress(), otherPeer.getPort());
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			//Logger.logTCPConnection(PeerHandler.peerID, otherPeer.getPeerID());
			//send handShake
			message = HandshakeHelper.sendHandshakeMessage();
			sendMessage(message);

			//Verify handshake response
			Received = readBuffer();
			if (!HandshakeHelper.VerifyHandShakeMessage(Received, otherPeer.getPeerID())) return;

			//send our bitfield
			ActualMessage MessageToSend = new ActualMessage(ActualMessage.MessageType.BITFIELD, otherPeer.getBitField());
			sendMessage(MessageToSend.message);

			//Received = readBuffer();
			//ActualMessage Ms = new ActualMessage(Received);
//			ConfigService.peerMap.get(returnedPeerID).setBitField(new byte[0]);
			//if(ConfigService.peerMap.get(PeerHandler.peerID).getHasFile()){
			//}
			// Assuming that every peer in the connection want to end up having everything there is with each other, aka hasFile = 1 and BitField all the same,
			// job is done, disconnect.
			while(on) {
				do {
					Received = readBuffer();
					ActualMessage Ms = new ActualMessage(Received);
					message = MessageHandler.handleMessage(Ms, ConfigService.peerMap.get(PeerHandler.peerID), otherPeer.getPeerID());
					if(message != null)
						sendMessage(message);
				} while (on);
			}
			//Guess a choke will turn off connected, but maybe put it in a bigger loop
			//If we get the response we want(handshake), start a loop of sending and reciving the data. Starting with bit field
			//create the message and send it


		}
		catch (ConnectException e) {
    			System.err.println("Connection refused. You need to initiate a server first.");
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
	public void sendMessage(byte[] msg)
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
