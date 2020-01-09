//Jordan Wang
//FBLAJeopardyGUI
//Spec: Creates the game and handles all the logic behind it

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*; // JGui stuff
import java.util.*;

import javax.swing.border.Border;
import javax.swing.Timer;

public class FBLAJeopardyGUI extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Just a list of variables that I used to create this program
	*	Mainly consists of Java GUI components along with variables to store data for later use. */
	private static Clip clip;
	private Container c;
	private ArrayList<Category> cList = new ArrayList<Category>();
	private ArrayList<Category> chosenCategories = new ArrayList<Category>();
	private Category byLaws, hFigures, history, mfDimes, membership, misc, byLaws2, misc2, nlcs, officers; //the ten different categories randomly chosen from when the program is started
	private JTextArea txtInput, txtQuestion;
	private JPanel pnlScores, pnlLayout, pnlInfo;
	private JLabel lblTeam1, lblTeam2, lblTurn, lblTitle, lblInstructions, lblTimer;
	private JButton btnQuit, btnInstructions, btnSubmit;
	private JButton[][] grid;
	private ButtonListener listener;
	private TimerListener tmrListener;
	private Timer timer;
	private String userInput, answer;
	private int team1, team2, addScore;
	private int time = 30;
	private int timerLoops = 0;
	private int count = 0;
	private int[][]answeredQs = new int[6][5];
	private boolean questionUp = false;
	private boolean storeTurn;
	private boolean team1Turn = true;
	public FBLAJeopardyGUI() throws Exception
	{
		super("Wang FBLA Jeopardy 2019");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Ensures that the program doesn't run in the background after being closed.

		c = getContentPane(); //Standard container to store elements in
		c.setLayout(new BorderLayout());
		JPanel cp = new JPanel(); //A panel to store a grid of buttons
		cp.setLayout(new GridLayout(6, 5)); 
		
		Border border = BorderFactory.createLineBorder(Color.BLACK); //Creates a border to be used around the text areas
		
		//Defaulting all the variables to a value.
		userInput = "";
		answer = "";
		team1 = 0;
		team2 = 0;
		grid = new JButton[6][5];
		
		listener = new ButtonListener(); //Allows for an action to go off if added to a button
		
		//The following lines of code serve to deal with user input into a text box.
		txtInput = new JTextArea(1, 10);
		txtInput.setFont(new Font("Calibri", Font.BOLD, 14));
		txtInput.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		//Creates a text box to store the question in a neat and secure manner
		txtQuestion = new JTextArea(2, 10);
		txtQuestion.setText("Select a question");
		txtQuestion.setFont(new Font("Calibri", Font.BOLD, 14));
		txtQuestion.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		txtQuestion.setEditable(false);
		txtQuestion.setLineWrap(true);
		txtQuestion.setWrapStyleWord(true);
		
		//Quit button to exit out of the game.
		btnQuit = new JButton("Quit");
		btnQuit.addActionListener(listener);
		btnSubmit = new JButton("Submit");
		btnSubmit.addActionListener(listener);
		
		//Handles the instructions button and an instructions to be displayed after a button click
		ImageIcon image = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("instructions.jpg")));
		lblInstructions = new JLabel(image);
		btnInstructions = new JButton("Instructions");
		btnInstructions.setFont(new Font("Calibri", Font.BOLD, 12));
		btnInstructions.addActionListener(listener);
		
		//Instantiates all the JLabels and centers the text.
		lblTeam1 = new JLabel("Team 1: " + team1, SwingConstants.CENTER);
		lblTeam2 = new JLabel("Team 2: " + team2, SwingConstants.CENTER);
		lblTurn = new JLabel("Team 1's Turn!", SwingConstants.CENTER);
		lblTitle = new JLabel("FBLA Jeopardy 2019", SwingConstants.CENTER);
		lblTimer = new JLabel("Time: " + time, SwingConstants.CENTER);
		lblTitle.setFont(new Font("Calibri", Font.BOLD, 32));
		lblTeam1.setFont(new Font("Calibri", Font.BOLD, 18));
		lblTeam2.setFont(new Font("Calibri", Font.BOLD, 18));
		lblTurn.setFont(new Font("Calibri", Font.BOLD, 20));
		lblTimer.setFont(new Font("Calibri", Font.BOLD, 18));
		
		//Creates a JPanel that adds each team's score as well as who's turn it is in specific locations.
		pnlScores = new JPanel();
		pnlScores.setLayout(new BorderLayout());
		pnlScores.add(lblTeam1, BorderLayout.WEST);
		pnlScores.add(lblTeam2, BorderLayout.EAST);
		pnlScores.add(lblTurn, BorderLayout.CENTER);
		
		/** Creates a JPanel that adds a text area for the question and a text area for user input
		 *  Along with that, adds the instructions and submit button for the user 
		 *  Again, everything in specific locations.
		 */
		pnlInfo = new JPanel();
		pnlInfo.setLayout(new BorderLayout());
		pnlInfo.add(txtQuestion, BorderLayout.NORTH);
		pnlInfo.add(btnInstructions, BorderLayout.WEST);
		pnlInfo.add(txtInput, BorderLayout.CENTER);
		pnlInfo.add(btnSubmit, BorderLayout.EAST);
		pnlInfo.add(lblTimer, BorderLayout.SOUTH);
		
		/** Creates yet another JPanel and adds the two JPanels created previously in specific locations
		 *  Adds a quit button to the bottom of the screen if the user ever wants to quit out of the game.
		 */
		pnlLayout = new JPanel();
		pnlLayout.setLayout(new BorderLayout());
		pnlLayout.add(pnlScores, BorderLayout.NORTH);
		pnlLayout.add(pnlInfo, BorderLayout.CENTER);
		pnlLayout.add(btnQuit, BorderLayout.SOUTH);	
		
		/** This block of code was used previously to read the text files in which the questions were located
		 *  However, when jarring the program, they were unable to be read using this.
 
			byLaws = new Category(new File("Bylaws.txt"), "By Laws");
			hFigures = new Category(new File("HistoricalFigures.txt"), "Historical Figures");
			history = new Category(new File("History.txt"), "History");
			mfDimes = new Category(new File("March For Dimes.txt"), "March For Dimes");
			membership = new Category(new File("Membership.txt"), "Membership");
		
		*/
		
		/** These were used in place of the previous block of code to ensure the files were able to be located
		 *  when the program was exported into a runnable jar file.
		 *  First an inputstream is used to get the text file
		 *  An inputstreamreader is used to read that text file
		 *  A new category object is made and added to the Category ArrayList
		 */
		InputStream inputstream = FBLAJeopardyGUI.class.getResourceAsStream("/Bylaws.txt");
		InputStreamReader inputReader = new InputStreamReader(inputstream);
		byLaws = new Category(inputReader, "Bylaws");	
		
		inputstream = FBLAJeopardyGUI.class.getResourceAsStream("/HistoricalFigures.txt");
		inputReader = new InputStreamReader(inputstream);
		hFigures = new Category(inputReader, "Historical Figures");
		
		inputstream = FBLAJeopardyGUI.class.getResourceAsStream("/History.txt");
		inputReader = new InputStreamReader(inputstream);
		history = new Category(inputReader, "History");
		
		inputstream = FBLAJeopardyGUI.class.getResourceAsStream("/March For Dimes.txt");
		inputReader = new InputStreamReader(inputstream);
		mfDimes = new Category(inputReader, "March For Dimes");
		
		inputstream = FBLAJeopardyGUI.class.getResourceAsStream("/Membership.txt");
		inputReader = new InputStreamReader(inputstream);
		membership = new Category(inputReader, "Membership");
		
		inputstream = FBLAJeopardyGUI.class.getResourceAsStream("/Misc.txt");
		inputReader = new InputStreamReader(inputstream);
		misc = new Category(inputReader, "Misc");
		
		inputstream = FBLAJeopardyGUI.class.getResourceAsStream("/Bylaws2.txt");
		inputReader = new InputStreamReader(inputstream);
		byLaws2 = new Category(inputReader, "Bylaws 2");
		
		inputstream = FBLAJeopardyGUI.class.getResourceAsStream("/Misc2.txt");
		inputReader = new InputStreamReader(inputstream);
		misc2 = new Category(inputReader, "Misc 2");
		
		inputstream = FBLAJeopardyGUI.class.getResourceAsStream("/NLCs.txt");
		inputReader = new InputStreamReader(inputstream);
		nlcs = new Category(inputReader, "NLCs");
		
		inputstream = FBLAJeopardyGUI.class.getResourceAsStream("/officers.txt");
		inputReader = new InputStreamReader(inputstream);
		officers = new Category(inputReader, "National Officers");

		
		/** Each of these categories were added to a category ArrayList.
		 *  The use of the cList ArrayList is to choose five categories randomly during each
		 *  run of the game, as shown in the for loop below.
		 */
		cList.add(byLaws);
		cList.add(hFigures);
		cList.add(history);
		cList.add(mfDimes);
		cList.add(membership);
		cList.add(misc);
		cList.add(byLaws2);
		cList.add(misc2);
		cList.add(nlcs);
		cList.add(officers);
		
		for(int i = 0; i < 5; i++)
		{
			int random = (int)(Math.random()*cList.size());
			chosenCategories.add(cList.remove(random));
		}
		
		/** This nested for loop handles the entire grid of JButtons
		 *  Checks specifically if it's the first row of buttons to insert the category name.
		 *  Otherwise, labels the button with the amount of points available.
		 *  Then adds the button to the JPanel cp created earlier after creating the button.
		 */
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[i].length; j++)
			{
				grid[i][j] = new JButton("");
				grid[i][j].addActionListener(listener);
				if(i == 0)
				{
					answeredQs[i][j] = 1;
					grid[i][j].setFont(new Font("Calibri", Font.BOLD, 13));
					grid[i][j].setMargin(new Insets(0,0,0,0));
					grid[i][j].removeActionListener(listener);
					grid[i][j].setText(chosenCategories.get(j).getName());
				}
				else
				{
					if(i == 1)
						grid[i][j].setText("10");
					if(i == 2)
						grid[i][j].setText("20");
					if(i == 3)
						grid[i][j].setText("30");
					if(i == 4)
						grid[i][j].setText("40");
					if(i == 5)
						grid[i][j].setText("50");
					grid[i][j].setFont(new Font("Calibri", Font.BOLD, 16));
				}
				cp.add(grid[i][j]);
			}
		}
		
		//Adding everything to the container after its been completely done
		c.add(lblTitle, BorderLayout.NORTH);
		c.add(cp, BorderLayout.CENTER);
		c.add(pnlLayout, BorderLayout.SOUTH);
		
		//c.setFocusable(true);
		/**Following block of code is used to exit out of the game when the user presses escape.*/
		InputMap im = ((JComponent) c).getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), "Escape");
		ActionMap ap = ((JComponent) c).getActionMap();
		ap.put("Escape", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Thanks for playing!");
				timerLoops = 0;
				time = 30;
				timer.stop();
				count = 0;
				stopMusic();
				dispose();
			}
		});
		
		/**This was used to instantiate and begin the timer with an interval of one second
		 * (The logic behind the timer is explained further down).
		 */
		tmrListener = new TimerListener();
		timer = new Timer(1000, tmrListener);
		
		setSize(600, 600);
		setVisible(true);
	}
	//Main method to start the program
	public static void main(String[] args) throws Exception
	{
		new FBLAJeopardyGUI();
	}
	
	//Detects whether or not a button was pressed
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object source = event.getSource();
			/**Each of these if statements serve to check if a specific button is pressed
			 * Executes statements based off which button is pressed
			 */
			if(source == btnQuit)
			{
				JOptionPane.showMessageDialog(null, "Thanks for playing!");
				timer.stop();
				stopMusic();
				dispose();
			}
			else if(source == btnSubmit)
			{
				/**First, this stops the thirty second timer that a team has to answer a question
				 * Then checks if there is currently a question enabled and stores the user's input in the text area in a String
				 * If the user's input has the answer or is equal to the answer (not case sensitive)
				 * Then the program displays a dialogue stating the answer is correct, adds the score to the respective team, and resets basically everything
				 * 
				 * If the answer is wrong, an opportunity is given to the other team to steal and the timer is reset
				 * The number of incorrect answers is stored in the variable count to guarantee the other team a chance to steal.
				 * If the other team gets an incorrect answer, no points are awarded and everything is reset again.
				 */
				timer.stop();
				if(questionUp)
				{
					userInput = txtInput.getText();
					if(userInput.contains(answer) || answer.equalsIgnoreCase(userInput))
					{
						JOptionPane.showMessageDialog(null, "That's correct!");
						if(team1Turn)
						{
							team1 += addScore;
						}
						else
						{
							team2 += addScore;
						}
						txtInput.setText("");
						txtQuestion.setText("The answer was '" + answer + "'\n" + "Select a question");
						lblTeam1.setText("Team 1: " + team1);
						lblTeam2.setText("Team 2: " + team2);
						team1Turn = storeTurn;
						if(team1Turn == true)
							lblTurn.setText("Team 1's Turn!");
						else
							lblTurn.setText("Team 2's Turn!");
						count = 0;
						timer.stop();
						enableButtons();
						stopMusic();
						checkWin();
					}
					else
					{
						JOptionPane.showMessageDialog(null, "Incorrect!");
						if(team1Turn && count == 0)
						{
							lblTurn.setText("Team 2 Steal!");
							txtInput.setText("");
							team1Turn = !team1Turn;
							count++;
							time = 30;
							lblTimer.setText("Time: " + time);
							timerLoops++;
							timer.stop();
							timer.start();
						}
						else if (team1Turn == false && count == 0)
						{
							lblTurn.setText("Team 1 Steal!");
							txtInput.setText("");
							team1Turn = !team1Turn;
							count++;
							time = 30;
							lblTimer.setText("Time: " + time);
							timerLoops++;
							timer.stop();
							timer.start();
						}
						else if(count == 1)
						{
							questionUp = false;
							txtQuestion.setText("No points were awarded.\nThe answer was '" + answer + "'\n" + "Select a question");
							txtInput.setText("");

							if(team1Turn == true)
								lblTurn.setText("Team 1's Turn!");
							else
								lblTurn.setText("Team 2's Turn!");
							count = 0;
							timerLoops = 0;
							time = 30;
							lblTimer.setText("Time: " + time);
							timer.stop();
							c.setFocusable(true);
							enableButtons();
							stopMusic();
							checkWin();
						}
					}
				}
			}
			else if(source == btnInstructions)
			{
				//Just displays the instructions
				JOptionPane.showMessageDialog(null, lblInstructions, "Instructions", JOptionPane.PLAIN_MESSAGE, null);
			}
			else
			{
				/**This deals with any button that is pressed other than the first row with the names of categories
				 * Disables the button, checks what category the question is and gets the questions and answers of that category
				 * Displays that question in the question text box and disables all the other available questions.
				 * Starts the timer for the question along with playing music
				 * Marks that value in a 2D array to be explained further down
				 */
				for(int row = 1; row < grid.length; row++)
				{
					for(int col = 0; col < grid[row].length; col++)
					{
						if(source == grid[row][col])
						{
							playMusic();
							questionUp = true;
							storeTurn = !team1Turn;
							//System.out.println(storeTurn);
							timer.start();
							grid[row][col].setEnabled(false);
							Category c = chosenCategories.get(col);
							String[] cQuestions = c.getQuestions();
							String[] cAnswers = c.getAnswers();
							answeredQs[row][col] = 1;
							answer = cAnswers[row-1];
							txtQuestion.setText(cQuestions[row-1]);
							if(row == 1)
								addScore = 10;
							else if(row == 2)
								addScore = 20;
							else if(row == 3)
								addScore = 30;
							else if(row == 4)
								addScore = 40;
							else if(row == 5)
								addScore = 50;
							disableAllButtons();
						}
					}
				}
			}
		}
	}
	
	/**Decrements the time and updates the timer label to reflect that
	 * Also checks how many times the timer has gone off and resets if it has gone off after the second team's chance to steal
	 * Resets everything if that occurs
	 */
	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			time--;
			lblTimer.setText("Time: " + time);
			if(time == 0)
			{
				count++;
				timer.stop();
				JOptionPane.showMessageDialog(null, "Time is up!");
				txtInput.setText("");
				team1Turn = !team1Turn;
				time = 30;
				//System.out.println("TLoop before: " + timerLoops);
				if(timerLoops == 0)
				{
					timerLoops++;
					timer.start();
					if(team1Turn)
					{
						lblTurn.setText("Team 1 Steal!");
					}
					else
						lblTurn.setText("Team 2 Steal!");
				}
				else if(timerLoops == 1)
				{
					questionUp = false;
					txtQuestion.setText("No points were awarded.\nThe answer was '" + answer + "'\n" + "Select a question");
					txtInput.setText("");
					lblTimer.setText("Time: " + time);
					team1Turn = storeTurn;
					if(team1Turn == true)
						lblTurn.setText("Team 1's Turn!");
					else
						lblTurn.setText("Team 2's Turn!");
					count = 0;
					timerLoops = 0;
					c.setFocusable(true);
					enableButtons();
					stopMusic();
					checkWin();
					//System.out.println("TLoop in: " + timerLoops);
				}
				//System.out.println("TLoop after: " + timerLoops);
			}
		}
	}
	
	
	/**This method looks through the answeredQs int array and checks which values are equal to 1
	 * (1 representing a question that's already been answered)
	 * Disables every button that's not equal to one
	 */
	public void disableAllButtons()
	{
		for(int i = 0; i < answeredQs.length; i++)
		{
			for(int j = 0; j < answeredQs[i].length; j++)
			{
				if(answeredQs[i][j] != 1)
				{
					grid[i][j].setEnabled(false);
				}
			}
		}
	}
	
	/**Enables the buttons based off the value of it in the answeredQs int array 
	 * If it's equal to 0 meaning unanswered, enable it
	 */
	public void enableButtons()
	{
		for(int i = 0; i < answeredQs.length; i++)
		{
			for(int j = 0; j < answeredQs[i].length; j++)
			{
				if(answeredQs[i][j] == 0)
				{
					grid[i][j].setEnabled(true);
				}
			}
		}
	}
	
	/**Handles all the music logic
	 * Loops the music twice to match the maximum length of each question (1 minute).
	 * Falls under fair use because the clip itself is exactly 30 seconds long and is only being looped twice.
	 */
	public static void playMusic()
	{
		try
		{
			InputStream music = FBLAJeopardyGUI.class.getResourceAsStream("/jeopardy.wav");
			InputStream buffer = new BufferedInputStream(music);
			if(buffer != null)
			{
				AudioInputStream audio = AudioSystem.getAudioInputStream(buffer);
				clip = AudioSystem.getClip();
				clip.open(audio);
				clip.start();
				clip.loop(2);
			}
		}
		catch(Exception ex)
		{
			//Nothing to see here . . .
		}
	}
	
	//Stops the music
	public static void stopMusic()
	{
		try
		{
			clip.stop();
		}
		catch(Exception ex)
		{
			
		}
	}
	/**Checks if all the questions were answered
	 */
	public boolean gameDone()
	{
		int count = 0;
		for(int i = 0; i < answeredQs.length; i++)
		{
			for(int j = 0; j < answeredQs[i].length; j++)
			{
				if(answeredQs[i][j] == 1)
				{
					count++;
					//System.out.println("increment at: " + i + " " + j);
				}
			}
		}
		//System.out.println("Count:" + count);
		//System.out.println(answeredQs.length * answeredQs[0].length);
		if(count == answeredQs.length * answeredQs[0].length)
			return true;
		else
			return false;
	}
	/**Checks if the game is over then which team won
	 * Then closes the game out.
	 */
	public void checkWin()
	{
		if(gameDone())
		{
			txtQuestion.setFont(new Font("Calibri", Font.BOLD, 22));
			if(team1 > team2)
			{
				txtQuestion.setText("The answer was '" + answer + "'\nTeam one won! Thanks for playing!");
				JOptionPane.showMessageDialog(null, "Team one won!");
			}
			else if(team2 > team1)
			{
				txtQuestion.setText("The answer was '" + answer + "'\nTeam two won! Thanks for playing!");
				JOptionPane.showMessageDialog(null, "Team two won!");
			}
			else if(team1 == team2)
			{
				txtQuestion.setText("The answer was '" + answer + "'\nIt's a draw! Thanks for playing!");
				JOptionPane.showMessageDialog(null, "It's a draw!");
			}
		}
	}
}

/*
https://www.fbla-pbl.org/competitive-event/computer-game-simulation-programming/
Topic: You are to create a game designed to test a member’s knowledge of FBLA.  
The game must produce questions to be presented to the user comprising a minimum of five 
different FBLA related topics. 

Topics may include competitive events, business skills, 
national officers and/or running for national office, national sponsors/partners, 
basic parliamentary procedure, national conference (NFLC and NLC) dates/locations, 
FBLA history, etc.  

The game must be able to be won.  For example, you must implement 
a system of rewards/penalties such as points, tokens, or levels. Questions
should be drawn from a question bank that presents the user a different assortment 
of questions every run of the game.  There must be an increase in difficulty as the 
levels increase.
Need to use keyboard, mouse, or touch screen
Instructions Selector
Quit at any point
Slides
//http://www.orangefreesounds.com/jeopardy-theme-song/

All data and programs should be contained in a master folder named STATE_ SCHOOL where your state and school are listed in that folder name format. Outside of the master folder, create a shortcut to the executable file. If the program requires a runtime player, create a shortcut outside the master folder to launch the runtime player installer.
Must be graphical in nature, not text based
An initial title page with the game title, user interface control instructions, and active buttons for Play and Quit
A quit command programmed to the escape key


*/