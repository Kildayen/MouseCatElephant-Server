/**
 * Class MouseCatElephantServer is the main program for the server side of the MouseCatElephant project.
 * The command line arguments specify the host and port for the server.
 * 
 * @author  Adam Warner
 * @version 8/4/2015
 */

import java.net.InetSocketAddress;
import java.net.DatagramSocket;

public class MouseCatElephantServer
{

	public static void main(String[] args) throws Exception 
	{
		if (args.length != 2) usage();
		String host = args[0];
		int port = 0;
		
		try
		{
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e)
		{
			System.err.println("Invalid Port.");
			System.exit(1);
		}
		
		DatagramSocket mailbox = new DatagramSocket(
			new InetSocketAddress (host, port));
		
		MailboxManager manager = new MailboxManager(mailbox);
		
		for(;;)
		{
			manager.receiveMessage();
		}

	}
	
	/**
	 * Displays a usage message and exits the program.
	 */
	private static void usage()
	{
		System.err.println("Usage: java MouseCatElephantServer <host> <port>");
		System.exit(1);
	}
}