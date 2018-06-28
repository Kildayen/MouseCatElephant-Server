/**
 * Specifies the interface for an object that is triggered by events from the model object
 * in the MouseCatElephant game. 
 * 
 * @author	Adam Warner
 * @version 7/30/2015
 *
 */

import java.io.IOException;

public interface ModelListener 
{

	/**
	 * Report that an animal was selected by a player.
	 * 
	 * @param  id				The ID of the player making the selection
	 * @param  animal			The animal selected
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void animalSelected(int id, int animal) throws IOException;
	
	/**
	 * Report that a new round was selected.
	 */
	public void newRoundSelected() throws IOException;

	/**
	 * Report the outcome of a round.
	 * 
	 * @param a1				The first animal in the outcome result
	 * @param vs				The interaction between the two animals
	 * @param a2				The second animal in the outcome result
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void reportOutcome(int a1, int vs, int a2) throws IOException;
	
	/**
	 * Report the score for a player.
	 * 
	 * @param id				The ID of the player
	 * @param score				The score for that player
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void reportScore(int id, int score) throws IOException;
	
	/**
	 * Assigns player information
	 * 
	 * @param id				The ID of the player
	 * @param name				The name of the player
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void setPlayerInfo(int id, String name) throws IOException;
	
	/**
	 * Quit the game.
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public void quit() throws IOException;
	
	/**
	 * Assigns a session id.
	 * 
	 * @param id				The session id
	 */
	public void setSession(int id);
}
