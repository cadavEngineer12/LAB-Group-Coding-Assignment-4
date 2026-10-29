import javax.swing.*;
import java.awt.*;
import java.util.Random;

class Ship extends JButton{
		
	private int x, y;
	/**
	 * Constructor for the Ship class 
	 */
	public Ship(){

		x = 100;
		y = 100;
		setLocation(x,y);
		setSize(50, 50);

				
	}
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillOval(0, 0, getWidth(), getHeight());
    }
	
	public void bounceShip(int dx, int dy){
		x = x + dx;
		y = y + dy;
		super.setLocation(x, y);
	}
	
	/**
	 * Method to move the ship to a random location on the screen
	 */
	public void moveShip(){
		Random rand = new Random();
		
		x = rand.nextInt(Ball.FRAMEWIDTH - this.getWidth());
		y = rand.nextInt(Ball.FRAMEHEIGHT - this.getHeight());
		setLocation(x, y);
	}
	
	
}