/**
 * Defines the network proxy for the model object in the MouseCatElephant project.
 * The proxy resides in the server program and communicates with a client.
 * 
 * @author Adam Warner
 * @version 8/4/2015
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class ViewProxy implements ModelListener
{
	private DatagramSocket mailbox;
	private SocketAddress clientAddress;
	private ViewListener viewListener;
	private int id, session;
	
	/**
	 * Constructor for the ViewProxy object.
	 * 
	 * @param  mailbox			The mailbox for the server
	 * @param  clientAddress	The mailbox for the client
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public ViewProxy(DatagramSocket mailbox, SocketAddress clientAddress)
		throws IOException
	{
		this.mailbox = mailbox;
		this.clientAddress = clientAddress;
	}
	
	// Exported operations
	
	/**
	 * Set the view listener for this proxy object.
	 * 
	 * @param viewListener		The ViewListener
	 */
	public void setViewListener(ViewListener viewListener)
	{
		this.viewListener = viewListener;
	}
	
	/**
	 * Sends an animal selection message to the client.
	 * 
	 * @param  id				The id of the player making the selection
	 * @param  animal			The animal selected
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void animalSelected(int id, int animal) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);	
		
		out.writeByte('C');
		out.writeByte(id);
		out.writeByte(animal);
		out.close();
		
		byte[] payload = baos.toByteArray();
		mailbox.send(new DatagramPacket(payload, payload.length, clientAddress));
	}

	/**
	 * Signals the client that a new round has begun.
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void newRoundSelected() throws IOException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);	
		out.writeByte('R');
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(new DatagramPacket(payload, payload.length, clientAddress));
	}

	/**
	 * Sends the client a message describing the outcome of the round.
	 * 
	 * @param  a1				The first animal
	 * @param  vs				The action taken by the first animal
	 * @param  a2				The second animal
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void reportOutcome(int a1, int vs, int a2) throws IOException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		
		out.writeByte('O');
		out.writeByte(a1);
		out.writeByte(vs);
		out.writeByte(a2);
		out.close();
		
		byte[] payload = baos.toByteArray();
		mailbox.send(new DatagramPacket(payload, payload.length, clientAddress));
	}

	/**
	 * Sends a score message to the client.
	 * 
	 * @param  id				The id of the player
	 * @param  score			The score for the player
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void reportScore(int id, int score) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		
		out.writeByte('S');
		out.writeByte(id);
		out.writeByte(score);
		out.close();
		
		byte[] payload = baos.toByteArray();
		mailbox.send(new DatagramPacket(payload, payload.length, clientAddress));
	}

	/**
	 * Sends a player information message to the client.
	 * 
	 * @param		id
	 * @param		name
	 * 
	 * @exception	IOException
	 */
	public void setPlayerInfo(int id, String name) throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);

		if (name != null)
		{
			out.writeByte('N');
			out.writeUTF(name);
		} else
		{
			out.writeByte('I');
			if( id != 0)
			{
				this.id = 3;
			}
		}
		
		out.writeByte(id);
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(new DatagramPacket(payload, payload.length, clientAddress));
	}

	/**
	 * Send a quit message to the client.
	 * 
	 * @param IOException		Thrown if an I/O error occurred
	 */
	public void quit() throws IOException
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		out.writeByte('Q');
		out.close();
		byte[] payload = baos.toByteArray();
		mailbox.send(new DatagramPacket(payload, payload.length, clientAddress));
	}
	
	/**
	 * Set session id.
	 */
	public void setSession(int id)
	{
		session = id;
	}
	
	/**
	 * Processes an incoming datagram.
	 * 
	 * @param  datagram			The datagram
	 * @return discard			Return a non-zero id to discard this proxy
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public int process(DatagramPacket datagram) throws IOException
	{
		DataInputStream in = new DataInputStream(
			new ByteArrayInputStream(datagram.getData(), 0, datagram.getLength()));
		
		int choice;
		int discard = 0;
		String name;
		
		byte message = in.readByte();
		
		switch (message)
		{
		case 'J':	//join
			name = in.readUTF();
			viewListener.join(ViewProxy.this, name);
			break;
		case 'C':	//animal choice
			choice = in.readByte();
			viewListener.selectAnimal(choice + id);
			break;
		case 'R':	//new round
			viewListener.newRound();
			break;
		case 'Q':	//quit
			viewListener.quit(0);
			discard = session;
			break;
		default:	//quit with errors
			viewListener.quit(1);
			discard = session;
			break;
		}
		
		return discard;
	}
}
