package snake;
/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * 
 * @version 2.1, Apr 2017
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RunSnake implements Runnable {
    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Classic Snake Game");
        frame.setLocation(250, 250);
        
        // Status and Food Count panels
        final JPanel status_panel = new JPanel();
        final JLabel status = new JLabel("Lives Remaining: 1");
        status_panel.add(status);
        final JPanel food_panel = new JPanel();
        final JLabel food_status = new JLabel("Apples: 0 Lemons: 0 Peaches: 0");
        food_panel.add(food_status);
        
        // Info panel 
        final JPanel info_panel = new JPanel();
        info_panel.setLayout(new GridLayout(1,2));
        info_panel.add(status_panel);
        info_panel.add(food_panel);
        frame.add(info_panel, BorderLayout.SOUTH);

        // Main playing area
        final GameBoard game = new GameBoard(status, food_status);
        frame.add(game, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        final JButton reset = new JButton("Reset");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                game.reset();
            }
        });
        control_panel.add(reset);

        // Put the frame on the screen
        JOptionPane.showMessageDialog(
                frame, "This is the classic Snake"
                        + " game with some (cool) additional features! \n The objective"
                        + " is to grow the snake by eating as many apples as you can"
                        + " without colliding into the walls or into the body of the snake. \n"
                        + "\n Use the arrow keys to change the direction of the snake. "
                        + " However, you cannot move in the opposite direction (thereby hitting the"
                        + " snake's body), \n pressing an arrow key to move in the opposite"
                        + " direction will do nothing. \n "
                        + "\n A food will randomly generate at the beginning of the game"
                        + " as well as every time the previous food on the board is eaten."
                        + " \n Eating an apple (red square) will grow the snake"
                        + " by one. \n Eating a lemon (yellow square) will make the snake"
                        + " move in the opposite direction due to the sourness, and its tail will"
                        + " become the new head! \n Be careful when eating lemons - if moving in"
                        + " in the opposite direction results in collision, the new head will try"
                        + " move in the other direction. \n If that also results in collision, the"
                        + " snake will lose a life. But have no fear - eating a peach (pink square)"
                        + " will increase the snake's lives by 1. \n Peaches, however are the"
                        + " least likely to generate out of all three foods, with lemons next. \n"
                        + " \n The snake begins with one life and loses a life every time it"
                        + " collides into a wall or into itself. \n If it still has at least 1"
                        + " life then the board is cleared; the game restarts with the snake being"
                        + " of size 1 again. \n The game ends when the snake has 0 lives left."
                        + " You can also reset the game at any time. \n"
                        + " \n 333 BONUS: Eat 3 of each food to gain 3 additional lives! Can only"
                        + " be applied once per game."
                        + " \n Have fun and HAPPY GROWING!!",
                "Instructions", 1
        );
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        game.reset();
    }
}