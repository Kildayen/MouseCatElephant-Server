/**
 * This class provides the server-side model object for the MouseCatElephant game.
 * 
 * @author  Adam Warner
 * @version 8/4/2015
 */

import java.io.IOException;

public class MouseCatElephantServerState implements ViewListener 
{
	private ModelListener p1Listener, p2Listener;
	
	private int p1ID, p2ID;
	private MouseCatElephantGameState game = new MouseCatElephantGameState();

	/**
	 * Constructor for the MouseCatElephantServerState object.
	 */
	public MouseCatElephantServerState(){}
	
	/**
	 * Set the model listeners for this object. 
	 * 
	 * @param ml		The model listener object
	 */
	public synchronized void addModelListener(ModelListener ml)
	{
		if (p1Listener == null)
		{
			p1Listener = ml;
		} else
			p2Listener = ml;
	}
	
	/**
	 * Join a game.
	 * 
	 * @param proxy			The ViewProxy reference
	 * @param name			The name of the player
	 * 
	 * @throws IOException			Thrown if an I/O error occurred
	 */
	public synchronized void join(ViewProxy proxy, String name) throws IOException
	{
		if (p1Listener == proxy)
		{
			game.setPlayerName(p1ID, name);
			
			// provide name and score for player 1
			p1Listener.setPlayerInfo(p1ID, game.getPlayerName(p1ID));
			p1Listener.reportScore(p1ID, game.getPlayerScore(p1ID));
		} else if (p2Listener == proxy)
		{
			p2ID = 1;
			game.setPlayerName(p2ID, name);
			
			// provide name and score for player 2 to player 1
			p1Listener.setPlayerInfo(p2ID, game.getPlayerName(p2ID));
			p1Listener.reportScore(p2ID, game.getPlayerScore(p2ID));
			
			// provide names and scores for player 2
			p2Listener.setPlayerInfo(p1ID, game.getPlayerName(p1ID));
			p2Listener.setPlayerInfo(p2ID, game.getPlayerName(p2ID));
			p2Listener.reportScore(p1ID, game.getPlayerScore(p1ID));
			p2Listener.reportScore(p2ID, game.getPlayerScore(p2ID));
			
			newRound();
		}
	}
	
	/**
	 * Handle animal selection message.
	 * 
	 * @param  animal			The code for the animal selected
	 * 
	 * @throws IOException		Thrown if an I/O error occurred
	 */
	public synchronized void selectAnimal(int animal) throws IOException
	{
		if (animal < 3)
		{
			game.setPlayerChoice(p1ID, animal);
			p1Listener.animalSelected(p1ID, game.getPlayerChoice(p1ID));
			p2Listener.animalSelected(p1ID, game.getPlayerChoice(p1ID));
		} else
		{
			game.setPlayerChoice(p2ID, (animal - 3));
			p1Listener.animalSelected(p2ID, game.getPlayerChoice(p2ID));
			p2Listener.animalSelected(p2ID, game.getPlayerChoice(p2ID));
		}
		
		if (game.playersReady())
		{
			determineOutcome();
		}		
	}

	/**
	 * Send out a new round message.
	 * 
	 * @throws IOException			Thrown if an I/O error occurred
	 */
	public synchronized void newRound() throws IOException 
	{
		if (p2Listener != null)
		{
			p1Listener.newRoundSelected();
			p2Listener.newRoundSelected();
			
			//reset selections
			game.setPlayerChoice(p1ID, -1);
			game.setPlayerChoice(p2ID, -1);
		}
	}

	/**
	 * Handle quit messages.
	 * 
	 * @param  i					The error code ( 0 = no error )
	 * 
	 * @throws IOException			Thrown if an I/O error occurred
	 */
	public synchronized void quit(int i) throws IOException 
	{
		if (i != 0)
			System.out.println("Invalid message from client.");
		
		p1Listener.quit();
		if (p2Listener != null)
			p2Listener.quit();
	}
	
	/**
	 * Determine the result of the round and update score.
	 * 
	 * @throws IOException			Thrown if an I/O error occurred
	 */
	private synchronized void determineOutcome() throws IOException
	{
		// animal codes: mouse = 0, cat = 1, elephant = 2
		// vs codes: ties = 0, frightens = 1, eats = 2, stomps = 3
		int animal1 = 0, animal2 = 0, vs = 0;
		int score = 0;
		
		int p1Choice = game.getPlayerChoice(p1ID);
		int p2Choice = game.getPlayerChoice(p2ID);
		
		// test for outcomes
		if (p1Choice == p2Choice)	//check for ties		
		{
			animal1 = p1Choice;
			animal2 = p2Choice;
		} else if (p1Choice == 0 && p2Choice == 1) //mouse vs cat
		{
			animal1 = 1;
			vs = 2;
			animal2 = 0;
			score = 2;
		} else if (p1Choice == 0 && p2Choice == 2) //mouse vs elephant
		{
			animal1 = 0;
			vs = 1;
			animal2 = 2;
			score = 1;
		} else if (p1Choice == 1 && p2Choice == 0) //cat vs mouse
		{
			animal1 = 1;
			vs = 2;
			animal2 = 0;
			score = 1;
		} else if (p1Choice == 1 && p2Choice == 2) //cat vs elephant
		{
			animal1 = 2;
			vs = 3;
			animal2 = 1;
			score = 2;
		} else if (p1Choice == 2 && p2Choice == 0) //elephant vs mouse
		{
			animal1 = 0;
			vs = 1;
			animal2 = 2;
			score = 2;
		} else //elephant vs cat
		{
			animal1 = 2;
			vs = 3;
			animal2 = 1;
			score = 1;
		}
		
		// report outcome to clients
		p1Listener.reportOutcome(animal1, vs, animal2);
		p2Listener.reportOutcome(animal1, vs, animal2);
		
		// update score and report any change to clients
		if (score == 1)
		{
			game.incrementScore(p1ID);
			p1Listener.reportScore(p1ID, game.getPlayerScore(p1ID));
			p2Listener.reportScore(p1ID, game.getPlayerScore(p1ID));
		} else if (score == 2)
		{
			game.incrementScore(p2ID);
			p1Listener.reportScore(p2ID, game.getPlayerScore(p2ID));
			p2Listener.reportScore(p2ID, game.getPlayerScore(p2ID));
		}
	}
}
