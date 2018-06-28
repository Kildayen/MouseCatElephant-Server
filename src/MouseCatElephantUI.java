//******************************************************************************
//
// File:    MouseCatElephantUI.java
// Package: ---
// Unit:    Class MouseCatElephantUI.java
//
// This Java source file is copyright (C) 2015 by Alan Kaminsky. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ark@cs.rit.edu.
//
// This Java source file is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by the Free
// Software Foundation; either version 3 of the License, or (at your option) any
// later version.
//
// This Java source file is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// details.
//
// You may obtain a copy of the GNU General Public License on the World Wide Web
// at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************

/**
 * Class MouseCatElephantUI provides the user interface for the Mouse Cat
 * Elephant network game.
 *
 * Source code:
 * @author  Alan Kaminsky
 * 
 * Modifications:
 * @author	Adam Warner
 * @version 8/28/2015
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class MouseCatElephantUI implements ModelListener
	{

// Hidden data members.

	private static final int GAP = 10;
	private static final int COL = 10;

	private JFrame frame;
	private JTextField myName;
	private JTextField myScore;
	private JTextField myChoice;
	private JTextField theirName;
	private JTextField theirScore;
	private JTextField theirChoice;
	private JTextField outcomeField;
	private JButton newRoundButton;
	private JButton mouseButton;
	private JButton catButton;
	private JButton elephantButton;
	
	private ViewListener viewListener;

// Hidden constructors.

	/**
	 * Construct a new Mouse Cat Elephant UI.
	 * 
	 * @param name		The name of the local player.
	 */
	private MouseCatElephantUI
		(String name)
		{
		// Set up window.
		frame = new JFrame ("Mouse Cat Elephant -- " + name);
		JPanel panel = new JPanel();
		panel.setLayout (new BoxLayout (panel, BoxLayout.Y_AXIS));
		frame.add (panel);
		panel.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));

		JPanel panel1 = new JPanel();
		panel1.setLayout (new BoxLayout (panel1, BoxLayout.X_AXIS));
		panel.add (panel1);
		myName = new JTextField (COL);
		myName.setEditable (false);
		myName.setHorizontalAlignment (JTextField.CENTER);
		panel1.add (myName);
		myScore = new JTextField (COL);
		myScore.setEditable (false);
		myScore.setHorizontalAlignment (JTextField.CENTER);
		panel1.add (myScore);
		myChoice = new JTextField (COL);
		myChoice.setEditable (false);
		myChoice.setHorizontalAlignment (JTextField.CENTER);
		panel1.add (myChoice);

		panel.add (Box.createVerticalStrut (GAP));

		JPanel panel2 = new JPanel();
		panel2.setLayout (new BoxLayout (panel2, BoxLayout.X_AXIS));
		panel.add (panel2);
		theirName = new JTextField (COL);
		theirName.setEditable (false);
		theirName.setHorizontalAlignment (JTextField.CENTER);
		panel2.add (theirName);
		theirScore = new JTextField (COL);
		theirScore.setEditable (false);
		theirScore.setHorizontalAlignment (JTextField.CENTER);
		panel2.add (theirScore);
		theirChoice = new JTextField (COL);
		theirChoice.setEditable (false);
		theirChoice.setHorizontalAlignment (JTextField.CENTER);
		panel2.add (theirChoice);

		panel.add (Box.createVerticalStrut (GAP));

		JPanel panel3 = new JPanel();
		panel3.setLayout (new BoxLayout (panel3, BoxLayout.X_AXIS));
		panel.add (panel3);
		outcomeField = new JTextField (COL);
		outcomeField.setEditable (false);
		outcomeField.setHorizontalAlignment (JTextField.CENTER);
		panel3.add (outcomeField);
		newRoundButton = new JButton ("New Round");
		panel3.add (newRoundButton);

		panel.add (Box.createVerticalStrut (GAP));

		JPanel panel4 = new JPanel();
		panel4.setLayout (new BoxLayout (panel4, BoxLayout.X_AXIS));
		panel.add (panel4);
		ClassLoader loader = getClass().getClassLoader();
		mouseButton = new JButton (new ImageIcon
			(loader.getResource ("mouse.png")));
		panel4.add (mouseButton);
		catButton = new JButton (new ImageIcon
			(loader.getResource ("cat.png")));
		panel4.add (catButton);
		elephantButton = new JButton (new ImageIcon
			(loader.getResource ("elephant.png")));
		panel4.add (elephantButton);

		// animal buttons are disabled until an opponent is found
		mouseButton.setEnabled(false);
		catButton.setEnabled(false);
		elephantButton.setEnabled(false);
		
		mouseButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				selectAnimal(0);
			}
		});
		
		catButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				selectAnimal(1);
			}
		});
		
		elephantButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				selectAnimal(2);
			}
		});
		
		newRoundButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				selectNewRound();
			}
		});
		
		// set behavior for closing game window
		frame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				try {
					viewListener.quit(0);
				} catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		});
		
		// Display window.
		frame.pack();
		frame.setVisible (true);
		}

// Exported operations.
	
	/**
	 * An object holding a reference to a Mouse Cat Elephant UI.
	 */
	private static class UIRef
		{
		public MouseCatElephantUI ui;
		}

	/**
	 * Construct a new Mouse Cat Elephant UI.
	 * 
	 * @param name		The name of the UI
	 */
	public static MouseCatElephantUI create
		(String name)
		{
		final UIRef ref = new UIRef();
		onSwingThreadDo (new Runnable()
			{
			public void run()
				{
				ref.ui = new MouseCatElephantUI (name);
				}
			});
		return ref.ui;
		}
	
	/**
	 * Set the view listener for this UI.
	 * 
	 * @param vl		The viewListener object
	 */
	public synchronized void setViewListener(ViewListener vl)
	{
		viewListener = vl;
	}
	
	/**
	 * Handle animal selection message from MouseCatElephantState object.
	 * 
	 * @param id	The ID of the player selecting the animal
	 * @param a		The code for the animal selected (0 mouse, 1 cat, 2 elephant)
	 */
	public synchronized void animalSelected(int id, int animal)
	{	
		String a = "";
		switch (animal)
		{
		case 0: a = "Mouse"; break;
		case 1: a = "Cat"; break;
		case 2: a = "Elephant"; break;
		case 3: a = "XXXX"; break;
		default: break;
		}
		
		doAnimalSelected(id, a);
	}

	/** 
	 * Handle new round message from MouseCatElephantState object.
	 */
	public synchronized void newRoundSelected()
	{	
		doNewRoundSelected();
	}
	
	/**
	 * Displays the outcome of the round.
	 * 
	 * @param a1	The first animal
	 * @param vs	The action taken by the first animal
	 * @param a2	The second animal
	 */
	public synchronized void reportOutcome(int a1, int vs, int a2)
	{
		String animal1 = "", action = "", animal2 = "";
		
		try {
			switch(a1)
			{
			case 0: animal1 = "Mouse"; break;
			case 1: animal1 = "Cat"; break;
			case 2: animal1 = "Elephant"; break;
			default: viewListener.quit(1); break;
			}
			
			switch(vs)
			{
			case 0: action = " ties "; break;
			case 1: action = " frightens "; break;
			case 2: action = " eats "; break;
			case 3: action = " stomps "; break;
			default: viewListener.quit(1); break;
			}
			
			switch(a2)
			{
			case 0: animal2 = "mouse"; break;
			case 1: animal2 = "cat"; break;
			case 2: animal2 = "elephant"; break;
			default: viewListener.quit(1); break;
			}
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		doReportOutcome((animal1 + action + animal2));
	}

	/**
	 * Handle player score message from MouseCatElephantState object.
	 * 
	 * @param id		The player whose score is being updated
	 * @param score		The new score
	 */
	public synchronized void reportScore(int id, int score)
	{
		doReportScore(id, score);
	}
	
	/**
	 * Handle player name message from MouseCatElephantState object.
	 * 
	 * @param id		The ID of the player
	 * @param name		The name of the player
	 */
	public synchronized void setPlayerInfo(int id, String name)
	{
		doSetPlayerInfo(id, name);
	}
	
	// Hidden operations.

	/**
	 * Handle animal selection by local player.
	 * 
	 * @param a		The code for the animal selected (0 mouse, 1 cat, 2 elephant)
	 */
	private synchronized void selectAnimal(int a)
	{		
		try 
		{
			viewListener.selectAnimal(a);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Handle new round selection by local player.
	 */
	private synchronized void selectNewRound()
	{
		try 
		{
			viewListener.newRound();
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/** 
	 * Update the player names in the UI.
	 * 
	 * @param id		The players ID
	 * @param name		The players name
	 */
	private synchronized void doSetPlayerInfo(int id, String name)
	{
		onSwingThreadDo(new Runnable()
		{
			public void run()
			{
				if (id == 0)
				{
					myName.setText(name);
				} else
					theirName.setText(name);
			}
		});	
	}
	
	/** 
	 * Update score in the UI.
	 * 
	 * @param id		The players ID
	 * @param score		The players score
	 */
	private synchronized void doReportScore(int id, int score)
	{
		onSwingThreadDo(new Runnable()
		{
			public void run()
			{
				if (id == 0)
				{
					myScore.setText("" + score);
				} else
					theirScore.setText("" + score);
			}
		});	
	}
	
	/** 
	 * Update the UI with the outcome of a round.
	 * 
	 * @param outcome		A string describing the interaction between the animals
	 */
	private synchronized void doReportOutcome(String outcome)
	{
		onSwingThreadDo(new Runnable()
		{
			public void run()
			{
				outcomeField.setText(outcome);
			}
		});	
	}
	
	/** 
	 * Update the UI after a new round is selected.
	 */
	private synchronized void doNewRoundSelected()
	{
		onSwingThreadDo(new Runnable()
		{
			public void run()
			{
				theirChoice.setText("");
				myChoice.setText("");
				outcomeField.setText("");
				
				mouseButton.setEnabled(true);
				catButton.setEnabled(true);
				elephantButton.setEnabled(true);
			}
		});	
	}
	
	/**
	 * Update the UI after animal selection.
	 * 
	 * @param id		The player making the selection
	 * @param animal	The animal selected
	 */
	private synchronized void doAnimalSelected(int id, String animal)
	{
		onSwingThreadDo(new Runnable()
		{
			public void run()
			{
				if (id == 0)
				{
					mouseButton.setEnabled(false);
					catButton.setEnabled(false);
					elephantButton.setEnabled(false);
					myChoice.setText(animal);
				} else
					theirChoice.setText(animal);
			}
		});	
	}
	
	/**
	 * Execute the given runnable object on the Swing thread.
	 */
	private static void onSwingThreadDo
		(Runnable task)
		{
		try
			{
			SwingUtilities.invokeAndWait (task);
			}
		catch (Throwable exc)
			{
			exc.printStackTrace (System.err);
			System.exit (1);
			}
		}

	/**
	 * Quit.
	 */
	public synchronized void quit() throws IOException {}
	
	/**
	 * Set session id.
	 */
	public synchronized void setSession(int id){}
	}