package snake;

import java.awt.Color;
import java.util.Map;

public class Board {

    Color[][] arr;
    private Snake snake;
    private boolean gameOver;
    private boolean bonusOne;

    // final variables
    private static final Color SNAKE = Color.GREEN;
    private static final Color BACKGROUND = Color.LIGHT_GRAY;
    
    public Board() {
        arr = new Color[19][19];
        // fill in arr with one-block snake at center
        fillInArr(arr);
        snake = new Snake(arr, 8, 8);
        bonusOne = false;
        gameOver = false;
    }

    /**
     * recolors arr and insantiates new snake
     */
    public void reset() {
        gameOver = false;
        bonusOne = false;
        fillInArr(arr);
        snake = new Snake(arr, 8, 8);
    }

    /**
     * recolors arr
     */
    public void restart() {
        fillInArr(arr);
        // update state of snake
        snake.positions.clear();
        int[] startingPos = { 8, 8 };
        snake.positions.add(startingPos);
        snake.setDirection("vert,1");
    }

    /**
     * Checks whether bonus has been achieved.
     * If true, adds 3 lives to the snake's current lives.
     * 
     * @return boolean indicating whether bonus
     *         has been achieved
     */
    public boolean checkBonus() {
        Map<String, Integer> map = snake.food;
        // bonus: extra life
        if (!bonusOne && map.get("apples") >= 3
                && map.get("lemons") >= 3 && map.get("peaches") >= 3) {
            snake.changeLives("+");
            snake.changeLives("+");
            snake.changeLives("+");
            bonusOne = true;
        }
        return bonusOne;
    }

    /**
     * Fills in array to starting board with
     * one-block snake at center
     * 
     * @param arr representing board
     */
    public void fillInArr(Color[][] arr) {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (i == 8 && j == 8) {
                    arr[i][j] = SNAKE;
                } else {
                    arr[i][j] = BACKGROUND;
                }
            }
        }
    }

    /**
     * Gives the status of the current game
     * 
     * @return true if game over, false otherwise
     */
    public boolean checkGameStatus() {
        if (snake.isDead()) {
            gameOver = true;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Getter for snake field
     * 
     * @return snake
     */
    public Snake getSnake() {
        return snake;
    }

}
