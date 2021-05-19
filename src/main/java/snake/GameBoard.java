package snake;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Board board;
    private boolean restart = false;
    private boolean firstTime = true;
    private boolean playing = false;
    private JLabel status;
    private JLabel food_status;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 200;
    private static final Color SNAKE_OUTLINE = new Color(48, 138, 72);
    
    
    public GameBoard(JLabel status, JLabel food_status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        board = new Board();

        // creates Timer object
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start();

        // Enable keyboard focus on the game area
        setFocusable(true);

        // Handles user interaction
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    board.getSnake().setDirection("horiz,-1");
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    board.getSnake().setDirection("horiz,1");
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    board.getSnake().setDirection("vert,1");
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    board.getSnake().setDirection("vert,-1");
                }
            }

        });

        this.status = status;
        this.food_status = food_status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        board.reset();
        playing = true;
        status.setText("Lives Remaining: 1");
        firstTime = true;
        repaint();

        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (restart) {
            board.restart();
            firstTime = true;
            restart = false;
            requestFocusInWindow();
        }

        if (firstTime) {
            Consumable food = new Apple(board.getSnake());
            food.generate();
            firstTime = false;
        }

        if (playing) {
            // advance
            Snake snake = board.getSnake();
            snake.move();
            board.checkBonus();

            // check for the game end conditions
            if (snake.hasCollided()) {
                if (snake.isDead()) {
                    playing = false;
                    status.setText("You lose!");
                } else {
                    restart = true;
                }
            }

            // update the display
            status.setText("Lives Remaining: " + snake.getLives());
            String food = "Apples: " + snake.food.get("apples");
            food += " Lemons: " + snake.food.get("lemons");
            food += " Peaches: " + snake.food.get("peaches");
            food_status.setText(food);
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw board
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                Color color = board.arr[i][j];
                g.setColor(color);
                g.fillRect(27 * j, 27 * i, 27, 27);
                if (color.equals(Color.green)) { // draw outline for snake
                    g.setColor(SNAKE_OUTLINE);
                    g.drawRect(27 * j, 27 * i, 27, 27);
                }
            }
        }

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(513, 513);
    }
}