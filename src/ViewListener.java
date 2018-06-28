/**
 * Interface ViewListener specifies the interface for an object that
 * is triggered by events from the view object in the MouseCatElephant project.
 * 
 * @author	Adam Warner
 * @version 7/30/2015
 */

import java.io.IOException;

public interface ViewListener 
{
	/**
	 * Join the server.
	 * 
	 * @param 	proxy			The proxy for the player joining
	 * @param	name			The name of the player joining
	 * 
	 * @throws  IOException		Thrown if an I/O error occurred
	 */
	public void join(ViewProxy proxy, String name) throws IOException;
	
	/**
	 * Select an animal.
	 * 
	 * @param	a				The code for the animal selected
	 * 
	 * @throws	IOException		Thrown if an I/O error occurred
	 */
	public void selectAnimal(int a) throws IOException;
	
	/**
	 * Begin a new round.
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void newRound() throws IOException;
	
	/**
	 * Quit the game.
	 * 
	 * @param	i				The exit code.
	 * 
	 * @throws 	IOException		Thrown if an I/O error occurred
	 */
	public void quit(int i) throws IOException;
	
}
