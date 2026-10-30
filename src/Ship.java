import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * class for painting the ball and shrinking it
 * @author Charles
 */
class Ship extends JButton implements ActionListener {
    Timer timer;
    private int x, y;

    public Ship(){
        x = 100;
        y = 100;
        setLocation(x,y);
        setSize(100, 100);
    }


    // Method that shrinks the ball to a specific size for timed animation
    public void setBallSize(int newSize) {
        if (newSize < 10) newSize = 10;
        setSize(newSize, newSize);
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


    public void resetPosition() {
        x = 100;
        y = 100;
        setLocation(x,y);
        setSize(100,100);
    }

    @Override
    public void actionPerformed(ActionEvent e) {}
}