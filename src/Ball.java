import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
 * Graphical User Interface for the SpaceShip example
 * creates and loads the frame using the Ship class
 *
 * @author Chase
 * @author Charles
 * @version 1.0
 */


public class Ball extends JPanel{

    public static final int FRAMEHEIGHT = 500;
    public static final int FRAMEWIDTH = 600; // made wider for controls
    private static final int STEPS = 500;
    private static final int DELAY = 20;

    private JFrame frame;
    private JPanel actionPanel;
    private JPanel playArea;

    private Ship ship;

    private JButton startButton;
    private JButton pauseButton;
    private JButton resetButton;

    private JTextField timeField;
    private JLabel countdownLabel;
    private JProgressBar progressBar;

    private Timer shipTimer;

    int dx = 2;
    int dy = 3;

    // Shrink logic
    private double totalTime = 10.0; // default 10 seconds
    private double timeLeft = 0;
    private long lastUpdateTime;
    private boolean isPaused = false;
    private final int INITIAL_SIZE = 100;
    private final int MIN_SIZE = 10;
    private boolean isRunning = false;

    public Ball(){
        frame = new JFrame();
        actionPanel = new JPanel();
        playArea = new JPanel();

        // Below fields are the controls
        timeField = new JTextField("10", 5);
        countdownLabel = new JLabel("Time left: 0.00s");
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(150, 20));

        startButton = new JButton("Start");
        startButton.addActionListener(new startButtonHandler());

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new pauseButtonHandler());

        resetButton = new JButton("Reset");
        resetButton.addActionListener(new resetButtonHandler());

        frame.setLayout(null);

        actionPanel.setSize(FRAMEWIDTH, 80);
        actionPanel.setLocation(0, 0);
        actionPanel.setLayout(new FlowLayout());

        actionPanel.add(new JLabel("Shrink Time (s):"));
        actionPanel.add(timeField);
        actionPanel.add(startButton);
        actionPanel.add(pauseButton);
        actionPanel.add(resetButton);
        actionPanel.add(countdownLabel);
        actionPanel.add(progressBar);

        playArea.setSize(FRAMEWIDTH, FRAMEHEIGHT - actionPanel.getHeight());
        playArea.setLocation(0, actionPanel.getHeight());
        playArea.setLayout(null);
        playArea.setBackground(Color.WHITE);

        frame.add(actionPanel);
        frame.add(playArea);
        frame.setSize(FRAMEWIDTH, FRAMEHEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ship = new Ship();

        playArea.add(ship);
        shipTimer = new Timer(20, new TimerListener()); // 20ms ~ 50fps
    }

    private boolean bounceX(){
        if (ship.getX() <= 0) {
            return true;
        }
        else if ((ship.getX() + ship.getWidth()) >= playArea.getWidth()) {
            return true;
        }
        return false;
    }

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
     * This class implements the timer for the buttons
     * @author Charles
     */

    private class startButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            try {
                totalTime = Double.parseDouble(timeField.getText());
                if (totalTime <= 0) totalTime = 10;
            } catch (NumberFormatException ex) {
                totalTime = 10;
                timeField.setText("10");
            }

            timeLeft = totalTime;
            lastUpdateTime = System.currentTimeMillis();
            isRunning = true;
            isPaused = false;
            pauseButton.setText("Pause");
            ship.resetPosition();
            progressBar.setValue(0);
            countdownLabel.setText(String.format("Time left: %.2fs", timeLeft));
            shipTimer.start();
        }
    }

    /**
     * This class implements the rules for the pause button
     * @author Charles
     */
    private class pauseButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if (!isRunning) return;

            if (!isPaused) {
                // Pause
                shipTimer.stop();
                isPaused = true;
                pauseButton.setText("Resume");
            } else {
                // Resume
                lastUpdateTime = System.currentTimeMillis();
                isPaused = false;
                pauseButton.setText("Pause");
                shipTimer.start();
            }
        }
    }

    /**
     * THis class implements the rules for the reset button
     * @author Charles
     */

    private class resetButtonHandler implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            shipTimer.stop();
            isRunning = false;
            isPaused = false;
            timeLeft = 0;
            pauseButton.setText("Pause");
            ship.resetPosition();
            countdownLabel.setText("Time left: 0.00s");
            progressBar.setValue(0);
            playArea.repaint();
        }
    }

    /**
     * This class waits for the timer
     */

    private class TimerListener implements ActionListener
    {

        /**
         * This is for the bouncing of the ball
         * @param e the event to be processed
         */
        public void actionPerformed(ActionEvent e)
        {
            ship.bounceShip(dx, dy);

            if (bounceX()) {
                dx = dx * -1;
            }
            if (bounceY()) {
                dy = dy * -1;
            }

            // shrink and countdown logic
            if (isRunning && !isPaused) {
                long now = System.currentTimeMillis();
                double deltaSec = (now - lastUpdateTime) / 1000.0;
                lastUpdateTime = now;

                timeLeft -= deltaSec;
                if (timeLeft < 0) timeLeft = 0;

                // Update countdown label
                countdownLabel.setText(String.format("Time left: %.2fs", timeLeft));

                // Update progress bar
                double elapsed = totalTime - timeLeft;
                int percent = (int)((elapsed / totalTime) * 100);
                progressBar.setValue(percent);

                // Calculate new ball size
                double progress = elapsed / totalTime;
                if (progress > 1) progress = 1;
                int newSize = (int)(INITIAL_SIZE - (INITIAL_SIZE - MIN_SIZE) * progress);
                ship.setBallSize(newSize);

                if (timeLeft <= 0) {
                    shipTimer.stop();
                    isRunning = false;
                    countdownLabel.setText("Time left: 0.00s");
                }
            }

            playArea.paintImmediately(playArea.getVisibleRect());
        }
    }
}