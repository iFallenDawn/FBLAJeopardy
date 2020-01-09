//Jordan Wang
//Title Screen
//Spec: Just a title screen with a quit and play button

import java.awt.*;
import java.awt.event.*;
import javax.swing.*; // JGui stuff
import javax.swing.border.Border;

public class Title extends JFrame
{
	/**
	 * 
	 */
	private FBLAJeopardyGUI f;
	private static final long serialVersionUID = 1L;
	private JLabel lblTitle, lblPicture, lblInstructions;
	private JTextArea txtCredits;
	private JButton btnPlay, btnQuit, btnInstructions;
	private ButtonListener listener;
	public Title()
	{
		super("Wang Program");
		/**Everything used to make the title screen, basically renaming of variables 
		 * in the FBLAJeopardyGUI class.
		 */
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		listener = new ButtonListener();
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		
		lblTitle = new JLabel("Welcome to FBLA Jeopardy 2019", SwingConstants.CENTER);
		lblTitle.setFont(new Font("Calibri", Font.BOLD, 32));
		
		ImageIcon icoLogo = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("fbla.png")));
		lblPicture = new JLabel(icoLogo);
		
		ImageIcon icoInst = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("instructions.jpg")));
		lblInstructions = new JLabel(icoInst);
		
		txtCredits = new JTextArea(5, 20);
		txtCredits.setText("Credits to orangefreesounds.com for the non-copyrighted jeopardy theme and the official FBLA "
				+ "website fbla-pbl.org for providing information to create questions\nThe rest of the program was created by Jordan Wang.\nProper citations"
				+ " found in the instructions.");
		txtCredits.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		txtCredits.setFont(new Font("Calibri", Font.BOLD, 14));
		txtCredits.setEditable(false);
		txtCredits.setLineWrap(true);
		txtCredits.setWrapStyleWord(true);
		
		btnPlay = new JButton("Play!");
		btnInstructions = new JButton("Instructions");
		btnQuit = new JButton("Quit");
		btnPlay.setFont(new Font("Calibri", Font.BOLD, 20));
		btnInstructions.setFont(new Font("Calibri", Font.BOLD, 20));
		btnQuit.setFont(new Font("Calibri", Font.BOLD, 20));
		btnPlay.addActionListener(listener);
		btnInstructions.addActionListener(listener);
		btnQuit.addActionListener(listener);
		
		JPanel pnlCenter = new JPanel();
		pnlCenter.add(lblPicture, BorderLayout.NORTH);
		pnlCenter.add(txtCredits, BorderLayout.SOUTH);
		
		JPanel pnlButtons = new JPanel();
		pnlButtons.setLayout(new BorderLayout());
		pnlButtons.add(btnPlay, BorderLayout.WEST);
		pnlButtons.add(btnInstructions, BorderLayout.CENTER);
		pnlButtons.add(btnQuit, BorderLayout.EAST);
		
		JPanel pnlLayout = new JPanel();
		pnlLayout.add(lblTitle, BorderLayout.NORTH);
		pnlLayout.add(pnlCenter, BorderLayout.CENTER);
		pnlLayout.add(pnlButtons, BorderLayout.SOUTH);
		
		cp.add(pnlLayout);		
		
		setSize(550, 350);
		setVisible(true);
	}
	public static void main(String[] args)
	{
		new Title();
	}
	//Another button listener that executes actions based off which button is pressed
	private class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			Object source = event.getSource();
			if(source == btnQuit)
			{
				JOptionPane.showMessageDialog(null, "Thanks for playing!");
				if(f != null)			
					f.dispose();
				dispose();
			}
			else if(source == btnPlay)
			{
				try 
				{
					f = new FBLAJeopardyGUI();
				} 
				catch (Exception e) 
				{
					JOptionPane.showMessageDialog(null, e);
				}
			}
			else if(source == btnInstructions)
			{
				JOptionPane.showMessageDialog(null, lblInstructions, "Instructions", JOptionPane.PLAIN_MESSAGE, null);
			}
		}
	}
}
