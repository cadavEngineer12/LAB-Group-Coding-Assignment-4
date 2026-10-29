import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Graphical User Interface for the SpaceShip example
 * creates and loads the frame using the Ship class
 *
 * @author Chase
 * @version 1.0
 */


public class Ball extends JPanel{
	
	//constants to represent the frame width and height
	//notice that they are public
	public static final int FRAMEHEIGHT = 500;
	public static final int FRAMEWIDTH = 500;
	private static final int STEPS = 500;
	private static final int DELAY = 20;
	
	//graphical objects declared at class level
	private JFrame frame;
	private JPanel actionPanel;
	private JPanel playArea;
		
	//a single spaceship for use in the game 
	private Ship ship;
	private ImageIcon shipImage;
	
	//buttons for starting and stopping the timer
	private JButton startButton;
	private JButton stopButton;
	
	private Timer shipTimer;
		
	int dx = 2;
	int dy = 3;

	
	/**
	 * Constructor for the SpaceShip class loads and displays the frame
	 */
	public Ball(){
		frame = new JFrame();
		actionPanel = new JPanel();
		playArea = new JPanel();
		startButton = new JButton("start");
		startButton.addActionListener(new startButtonHandler());
		stopButton = new JButton("stop");
		stopButton.addActionListener(new stopButtonHandler());
		
		frame.setLayout(null);
		
		actionPanel.setSize(FRAMEWIDTH, 40);
		actionPanel.setLocation(0, 0);
		actionPanel.add(startButton);
		actionPanel.add(stopButton);
		
		playArea.setSize(FRAMEWIDTH, FRAMEHEIGHT - actionPanel.getHeight());
		playArea.setLocation(0, actionPanel.getHeight());
		playArea.setLayout(null);
		
		frame.add(actionPanel);
		frame.add(playArea);
		frame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
		frame.setResizable(false);
		frame.setVisible(true); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ship = new Ship();

		playArea.add(ship);
		shipTimer = new Timer(10, new TimerListener());
		

    }


	
		/**
		 * This is a private helper method to determine if the ship needs to reverse course in the
		 * X dimension
		 *
		 * @return boolean representing whether or not the ship needs to bounce
		 */
		private boolean bounceX(){
			if (ship.getX() <= 0) {
				return true;
			}
			else if ((ship.getX() + ship.getWidth()) >= playArea.getWidth()) {
				return true;
			}
			return false;
		}
		
		/**
		 * This is a private helper method to determine if the ship needs to reverse course in the
		 * Y dimension
		 *
		 * @return boolean representing whether or not the ship needs to bounce
		 */
		private boolean bounceY(){
			if (ship.getY() <= 0) {
				return true;
			}
			else if ((ship.getY() + ship.getHeight() + 20) >= playArea.getHeight()) {
				return true;
			}
			return false;
		}



    /**
	 * private inner class startButtonHandler provides the action listener for the start button
	 */
	private class startButtonHandler implements ActionListener
	{	
		/**
		 * This method starts the timer 
		 * 
		 * @param e the action event handled by this method
		 */
		public void actionPerformed(ActionEvent e)
	    {
		    shipTimer.start();
		}
	}
	
	
	/**
	 * private inner class stopButtonHandler provides the action listener for the stop button
	 */
	private class stopButtonHandler implements ActionListener
	{
	    /**
	     * This method stops the timer 
	     * 
	     * @param e the action event handled by this method
	     */
	    public void actionPerformed(ActionEvent e)
	    {
		    shipTimer.stop();
		}
	}
	
	
	
		/**
		 * private inner class TimerListener provides the action listener for the swing Timer
		 */
		private class TimerListener implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				ship.bounceShip(dx, dy);
				playArea.paintImmediately(playArea.getVisibleRect());
						
				//sleep the thread to slow down the movement
				if (bounceX()) {
					dx = dx * -1;
				}
				if (bounceY()) {
					dy = dy * -1;
				}
			}
		}
	}
