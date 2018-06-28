/**
 * Defines the network proxy for the model object in the MouseCatElephant project.
 * The proxy resides in the client program and communicates with the server.
 * 
 * @author	Adam Warner
 * @version 8/2/2015
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class ModelProxy implements ViewListener
{
	private DatagramSocket mailbox;
	private SocketAddress destination;
	private ModelListener modelListener;
	private boolean haveQuit = false;

	/**
	 * Constructor for the model proxy object.
	 *
	 * @param  		socket  		The socket
	 * 
	 * @exception 	IOException		Thrown if an I/O error occurred
	 */
	 public ModelProxy(DatagramSocket mailbox, SocketAddress destination)
		throws IOException
	 {
		this.mailbox = mailbox;
		this.destination = destination;
	 }

	// Exported operations
	/**
	* Set the model listener object for this model proxy
	*
	* @param  modelListener  Model listener.
	*/
	public void setModelListener(ModelListener model)
	{
		this.modelListener = model;
		new ReaderThread().start();
	}

	/**
	 * Sends a join message to the server.
	 * 
	 * @param	proxy			The proxy
	 * @param	name			The name of the player joining the server
	 * 
	 * @throws	IOException 	Thrown if an I/O error occurred
	 */
	public void join(ViewProxy proxy, String name)
			throws IOException
	{	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		out.writeByte('J');
		out.writeUTF(name);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(new DatagramPacket(payload, payload.length, destination));
		}
	
	/**
	 * Sends an animal selected message to the server.
	 * 
	 * @param	a				The code for the animal selected (0 mouse, 1 cat, 2 elephant)
	 * 
	 * @throws	IOException 	Thrown if an I/O error occurred
	 */
	public void selectAnimal(int a) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		out.writeByte('C');
		out.writeByte(a);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(new DatagramPacket(payload, payload.length, destination));
	}

	/**
	 * Sends a new round selected message to the server.
	 * 
	 * @throws	IOException 	Thrown if an I/O error occurred
	 */
	public void newRound() throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		out.writeByte('R');
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(new DatagramPacket(payload, payload.length, destination));
	}
	
	/**
	 * Sends a quit message to the server and closes the program.
	 * 
	 * @param	i					The exit code
	 * 
	 * @throws 	IOException			Thrown if an I/O error occurred
	 */
	public void quit(int i) throws IOException
	{
		if (!haveQuit)
		{
			haveQuit = true;
			if (i != 0)
			{
				System.err.println("Invalid Message");
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(baos);
			out.writeByte('Q');
			out.close();
			byte[] payload = baos.toByteArray();
			mailbox.send(new DatagramPacket(payload, payload.length, destination));
		}
		System.exit(i);
	}
	
	/**
	* ReaderThread is a helper class that receives and processes messages from the network.
	*/
	private class ReaderThread extends Thread
	{
		public void run()
		{
			byte[] payload = new byte[128];
			try
			{
				mailbox.setSoTimeout(5000);		// set deadline for server response
				while(true)
				{	
					DatagramPacket packet = new DatagramPacket(
							payload, payload.length);
					mailbox.receive(packet);
					DataInputStream in = new DataInputStream(
						new ByteArrayInputStream(
							payload, 0, packet.getLength()));
					
					byte b = in.readByte();
					int [] msg = new int[3];
					String name;
					
					switch (b)
					{
						case 'I':	//ID
							msg[0] = in.readByte();	
							modelListener.setPlayerInfo(msg[0], null);
							mailbox.setSoTimeout(0);	// remove deadline for server response
							break;
						case 'N':	//name
							name = in.readUTF();
							msg[0] = in.readByte();
							modelListener.setPlayerInfo(msg[0], name);
							break;
						case 'S':	//score
							msg[0] = in.readByte();
							msg[1] = in.readByte();
							modelListener.reportScore(msg[0], msg[1]);
							break;
						case 'C':	//choice
							msg[0] = in.readByte();
							msg[1] = in.readByte();
							modelListener.animalSelected(msg[0], msg[1]);
							break;
						case 'O': 	//outcome
							msg[0] = in.readByte();
							msg[1] = in.readByte();
							msg[2] = in.readByte();
							modelListener.reportOutcome(msg[0], msg[1], msg[2]);
							break;
						case 'R':	//new round
							modelListener.newRoundSelected();
							break;
						case 'Q':	//quit
							quit(0);
							break;
						default:	//quit with errors
							quit(1);
							break;
					}
				}
			}
			catch (SocketTimeoutException e)
			{
				System.err.println("Timeout on connecting to server.");
				System.exit(1);
			}
			catch (IOException e){}
			finally
			{
				mailbox.close();
			}
		}
	}
}