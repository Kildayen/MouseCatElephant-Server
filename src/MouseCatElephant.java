/**
 * Class MouseCatElephant is the main program for the client side of the MouseCatElephant project.
 * The command line arguments specify the host and port for the server and client, and the name of the player.
 * 
 * @author Adam Warner
 * @version 8/3/2015
 */

import java.net.InetSocketAddress;
import java.net.DatagramSocket;

public class MouseCatElephant
{
	
	/**
	 * Main program.
	 */
	public static void main(String[] args) throws Exception
	{
		if (args.length != 5) usage();
		String serverHost = args[0];
		String clientHost = args[2];
		String name = args[4];
		int serverPort = 0, clientPort = 0;
		
		try 
		{
			serverPort = Integer.parseInt(args[1]);
			clientPort = Integer.parseInt(args[3]);
		} catch (NumberFormatException e)
		{
			System.err.println("Invalid Port.");
			System.exit(1);
		}
		
		DatagramSocket mailbox = new DatagramSocket(
			new InetSocketAddress(clientHost, clientPort));
		
		MouseCatElephantClientState model = new MouseCatElephantClientState();
		MouseCatElephantUI view = MouseCatElephantUI.create(name);

		final ModelProxy proxy = new ModelProxy(
			mailbox, new InetSocketAddress(
				serverHost, serverPort));
		
		model.setModelListener(view);
		view.setViewListener(proxy);
		proxy.setModelListener(model);
		
		proxy.join(null, name);
	}

	/**
	 * Display a usage message and exit the program.
	 */
	private static void usage()
	{
		System.err.println("Usage: $ java MouseCatElephant <serverhost> <serverport> <clienthost> <clientport> <playername>");
		System.exit(1);
	}
}