import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game {
    /**
     * Main method run to start and run the game.
     */
    public static void main(String[] args) {
        Runnable game = new snake.RunSnake(); 
        SwingUtilities.invokeLater(game);
    }
}
