/**
 * This class provides a mailbox manager for the MouseCatElephant server.
 * It keeps track of all view proxies, reads all incoming datagrams, and
 * forwards each datagram to the appropriate proxy.
 * 
 * @author  Adam Warner
 * @version 8/5/2015
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.HashMap;

public class MailboxManager 
{
	// Hidden data members
	private DatagramSocket mailbox;
	private byte[] message = new byte [128];
	private SessionManager sessionManager = new SessionManager();
	
	private HashMap<SocketAddress, ViewProxy> proxyMap =
		new HashMap<SocketAddress, ViewProxy>();
	
	/**
	 * Constructor for the MailboxManager object.
	 * 
	 * @param mailbox		The datagram mailbox
	 */
	public MailboxManager (DatagramSocket mailbox)
	{
		this.mailbox = mailbox;
	}
	
	/**
	 * Receive and process a datagram.
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void receiveMessage() throws IOException
	{
		DatagramPacket packet = new DatagramPacket (message, message.length);
		mailbox.receive(packet);
		SocketAddress clientAddress = packet.getSocketAddress();
		
		ViewProxy proxy = proxyMap.get (clientAddress);
		if (proxy == null)
		{
			proxy = new ViewProxy (mailbox, clientAddress);
			proxy.setViewListener(sessionManager);
			proxyMap.put(clientAddress, proxy);
		}
		
		int session = proxy.process(packet);	// if a session id is returned, that session is ending
		if (session != 0)
		{
			if (proxyMap.remove(clientAddress) != null)
			{
			sessionManager.quit(session);
			}
		}
	}
}
