/**
 * Class SessionManager maintains the session's model objects for the
 * MouseCatElephant server.
 * 
 * @author  Adam Warner
 * @version 8/7/2015
 */

import java.io.IOException;
import java.util.HashMap;

public class SessionManager implements ViewListener 
{
	private HashMap<Integer, MouseCatElephantServerState> sessions = 
		new HashMap<Integer, MouseCatElephantServerState>();
	private int key = 1;
	
	/**
	 * Constructor for the SessionManager object.
	 */
	public SessionManager(){}
	
	//Exported Operations
	
	/**
	 * Connects a player to a session. Creates a new session if necessary.
	 * 
	 * @param  proxy			The proxy for the connecting player
	 * @param  name				The name of the player
	 * 
	 * @throws IOEXception		Thrown if an I/O error occurred
	 */
	public synchronized void join (ViewProxy proxy, String name)
		throws IOException
		{
			MouseCatElephantServerState model = sessions.get(key);
			int id = 1;
			if(model == null)
			{
				model = new MouseCatElephantServerState();
				sessions.put(key, model);
				id = 0;
			}
			model.addModelListener(proxy);
			proxy.setViewListener(model);
			proxy.setPlayerInfo(id, null);
			proxy.setSession(key);
			model.join(proxy, name);

			if (id == 1)
			{
				incrementKey();
			}
		}
	
	/**
	 * Removes a session if a player quits.
	 * 
	 * @param	id				The session id
	 * 
	 * @throws 	IOException		Thrown if an I/O error occurred
	 */
	public synchronized void quit(int id) throws IOException
	{
		sessions.remove(id);
		if (id == key) // true if a lone player quits
		{
			incrementKey();
		}
	}
	
	/**
	 * Changes the key used to track sessions.
	 */
	private synchronized void incrementKey()
	{
		// Get a new session key
		while( sessions.containsKey(key) )
		{
			if (key == -1)
			{
				key += 2;
			} else
				key++;
		}
	}
	
	/**
	 * Select an animal.
	 * 
	 * @param	a				The code for the animal selected
	 * 
	 * @throws	IOException		Thrown if an I/O error occurred
	 */
	public synchronized void selectAnimal(int a) throws IOException {}

	/**
	 * Begin a new round.
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public synchronized void newRound() throws IOException {}
}
