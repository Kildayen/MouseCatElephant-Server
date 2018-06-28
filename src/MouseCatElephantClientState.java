/**
 * This class manages the MouseCatElephant client game state.
 * 
 * @author	Adam Warner
 * @version 8/2/2015
 */

import java.io.IOException;

public class MouseCatElephantClientState implements ModelListener 
{
	private ModelListener modelListener;
	private MouseCatElephantGameState game = new MouseCatElephantGameState();
	private int myID;

	/**
	 * Constructor for the MouseCatElephant game state object.
	 */
	public MouseCatElephantClientState(){}

	/**
	 * Sets the model listener for this object.
	 * 
	 * @param ml		The ModelListener object
	 */
	public synchronized void setModelListener(ModelListener ml) 
	{
		modelListener = ml;
	}
	
	/**
	 * Sets player information.
	 * 
	 * @param id				The ID of the player
	 * @param name				The name of the player
	 * 
	 * @throws IOException 		Thrown if an I/O error occurred
	 */
	public synchronized void setPlayerInfo(int id, String name) throws IOException
	{
		if (name == null)
		{
			myID = id;
		} else
		{
			if (id == myID)
			{
				modelListener.setPlayerInfo(0, name);
			} else
			{
				modelListener.setPlayerInfo(1, name);
			}
		}
	}
	
	/**
	 * Handle scoring information.
	 * 
	 * @param id				The player ID
	 * @param score				The score for that player
	 * 
	 * @throws IOException 		Thrown if an I/O error occurred
	 */
	public synchronized void reportScore(int id, int score) throws IOException
	{
		if (id == myID)
		{
			modelListener.reportScore(0, score);
		} else
		{
			modelListener.reportScore(1, score);
		}
	}
		
	/**
	 * Reports the outcome of the round.
	 * 
	 * @param animal1			The first animal
	 * @param vs				The action taken by the first animal
	 * @param animal2			The second animal
	 * 
	 * @throws IOException 		Thrown if an I/O error occurred
	 */
	public synchronized void reportOutcome(int animal1, int vs, int animal2) throws IOException
	{
		modelListener.reportOutcome(animal1, vs, animal2);
		modelListener.animalSelected(1, game.getPlayerChoice(1));
		
	}

	/**
	 * Handle the opposing players animal choice.
	 * 
	 * @param id				The player ID
	 * @param animal			The code for the animal selected (0 mouse, 1 cat, 2 elephant)
	 * 
	 * @throws IOException 		Thrown if an I/O error occurred
	 */
	public synchronized void animalSelected(int id, int animal) throws IOException
	{
		if (id != myID)
		{
			modelListener.animalSelected(1, 3);
			game.setPlayerChoice(1, animal);
		} else
			modelListener.animalSelected(0, animal);
	}

	/**
	 * Initializes a new round.
	 * 
	 * @throws IOException 		Thrown if an I/O error occurred
	 */
	public synchronized void newRoundSelected() throws IOException
	{	
		modelListener.newRoundSelected();
	}

	/**
	 * Quit.
	 */
	public synchronized void quit(){}
	
	/**
	 * Set session id.
	 */
	public synchronized void setSession(int id){}
}