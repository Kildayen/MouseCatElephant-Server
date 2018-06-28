/**
 * This class creates a generic MouseCatElephant game state object used
 * by both client and server.
 * 
 * @author  Adam Warner
 * @version 8/5/2015
 */
public class MouseCatElephantGameState
{
	private int p1Score = 0, p2Score = 0;
	private int p1Choice = -1, p2Choice = -1;
	private String p1Name, p2Name;
	
	/**
	 * Constructor for the MouseCatElephantGameState object.
	 */
	public MouseCatElephantGameState(){}
	
	/**
	 * Sets player score.
	 * 
	 * @param id		The player id
	 * @param score		The score
	 */
	public synchronized void setPlayerScore(int id, int score)
	{
		if (id == 0)
		{
			p1Score = score;
		} else
			p2Score = score;
	}
	
	/**
	 * Gets player score.
	 * 
	 * @param  id			The player id
	 * @return score		The player score		
	 */
	public synchronized int getPlayerScore(int id)
	{
		int score;
		if (id == 0)
		{
			score = p1Score;
		} else
			score = p2Score;
		
		return score;
	}
	
	/**
	 * Sets the animal choice for a player.
	 * 
	 * @param id			The player id
	 * @param choice		The animal (-1 no selection, 0 mouse, 1 cat, 2 elephant)
	 */
	public synchronized void setPlayerChoice(int id, int choice)
	{
		if (id == 0)
		{
			p1Choice = choice;
		} else
			p2Choice = choice;
	}
	
	/**
	 * Gets the animal choice of a player.
	 * 
	 * @param  id			The player id
	 * @return choice		The animal (-1 no selection, 0 mouse, 1 cat, 2 elephant)
	 */
	public synchronized int getPlayerChoice(int id)
	{
		int choice;
		if (id == 0)
		{
			choice = p1Choice;
		} else
			choice = p2Choice;
		
		return choice;
	}
	
	/**
	 * Sets a player name.
	 * 
	 * @param id			The player id
	 * @param name			The player name
	 */
	public synchronized void setPlayerName(int id, String name)
	{
		if (id == 0)
		{
			p1Name = name;
		} else
			p2Name = name;
	}
	
	/**
	 * Gets a player name.
	 * 
	 * @param  id			The player id
	 * @return name			The player name
	 */
	public synchronized String getPlayerName(int id)
	{
		String name;
		if (id == 0)
		{
			name = p1Name;
		} else
			name = p2Name;
		
		return name;
	}
	
	/**
	 * Increase the score for a given player by one.
	 * 
	 * @param id			The player whose score is increased
	 */
	public synchronized void incrementScore(int id)
	{
		if (id == 0)
		{
			p1Score++;
		} else
			p2Score++;
	}
	
	/**
	 * Tests whether both players have selected an animal.
	 * 
	 * @return status		True if and only if both players have made their selections
	 */
	public synchronized boolean playersReady()
	{
		boolean status;
		if (p1Choice != -1 && p2Choice != -1)
		{
			status = true;
		} else
			status = false;
			
		return status;
	}
}
