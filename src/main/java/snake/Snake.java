package snake;

import java.util.*;
import java.util.List;
import java.awt.*;

class Snake {
    List<int[]> positions;
    Map<String, Integer> food;
    Color[][] board;
    private String direction; // either vert or horiz
    private String sign; // either positive or negative
    private int incre; // where snake is moving in arr
    private int lives;
    private boolean collided;

    // final variables
    private static final Color SNAKE = Color.GREEN;
    private static final Color BACKGROUND = Color.LIGHT_GRAY;
    
    public Snake(Color[][] arr, int yPos, int xPos) {
        positions = new LinkedList<int[]>();
        food = new HashMap<String, Integer>();
        food.put("apples", 0);
        food.put("lemons", 0);
        food.put("peaches", 0);

        int[] head = { yPos, xPos };
        positions.add(head);
        board = arr;
        incre = 1;
        lives = 1;
        collided = false;

        // snake will start off moving downwards
        direction = "vert";
        sign = "1";
    }

    /**
     * Moves the snake in the array
     * 
     * @param direction
     */
    public void move() {
        int headY = positions.get(0)[0];
        int headX = positions.get(0)[1];
        int[] newHeadPos = new int[2];
        Color oldColor = null;

        if (collide()) {
            collided = true;
            if (lives > 0) {
                changeLives("-");
            }
            return;
        }

        // move head in direction if in bounds
        if (direction.equals("vert")) { // move up or down
            if ((headY + incre >= 0 && incre < 0)
                    || (headY + incre < board.length && incre > 0)) {
                oldColor = board[headY + incre][headX];
                board[headY + incre][headX] = SNAKE;
                newHeadPos[0] = headY + incre;
                newHeadPos[1] = headX;
            }
        } else if ((direction.equals("horiz"))) {
            if ((headX + incre >= 0 && incre < 0)
                    || (headX + incre < board[0].length && incre > 0)) {
                oldColor = board[headY][headX + incre];
                board[headY][headX + incre] = SNAKE;
                newHeadPos[0] = headY;
                newHeadPos[1] = headX + incre;
            }
        }

        // update head in list only if snake actually moved
        if (newHeadPos != null) {
            positions.add(0, newHeadPos);
            updateSnake(headY, headX, oldColor);
        }
    }

    /**
     * determines whether moving will result in collision with
     * the wall or the snake body
     * 
     * @param direction
     * @param incre
     * @return true if moving will cause collision, false otherwise
     **/
    public boolean collide() {
        int[] head = positions.get(0);
        int yPos = head[0];
        int xPos = head[1];

        if (direction.equals("vert")) {
            // tests for collision with wall
            if ((incre < 0 && (yPos + incre < 0))
                    || (incre > 0 && (yPos + incre > board.length - 1))) {
                return true;
            }
            // tests for collision with snake body
            for (int[] position : positions.subList(1, positions.size())) {
                int existingYPos = position[0];
                int existingXPos = position[1];
                if (yPos + incre == existingYPos && xPos == existingXPos) {
                    return true;
                }
            }
        } else {
            if ((incre < 0 && xPos + incre < 0)
                    || (incre > 0 && xPos + incre > board[0].length - 1)) {
                return true;
            }
            for (int[] position : positions.subList(1, positions.size())) {
                int existingYPos = position[0];
                int existingXPos = position[1];
                if (yPos == existingYPos && xPos + incre == existingXPos) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Changes the direction of the snake. Acceptable inputs
     * are "vert,1", "vert,-1", "horiz,1" and "horiz,-1".
     * 
     * @param s new direction and new sign,
     *          split by a comma.
     */
    public void setDirection(String s) {
        String newDirection = s.split(",")[0];
        String newSign = s.split(",")[1];

        /**
         * do nothing if user attempted to move in reverse direction
         * (only invalid if snake is of length longer than 1)
         */
        if (newDirection.equals(direction) && !newSign.equals(sign)
                && positions.size() > 1) {
            return;
        }
        direction = newDirection;
        if (!sign.equals(newSign)) {
            sign = newSign;
            incre = -1 * incre;
        }
    }

    /**
     * Reverses the direction of the snake
     * (opposite direction of where head was originally going)
     */
    public void reverseDirection() {
        if (sign.equals("1")) {
            sign = "-1";
        } else {
            sign = "1";
        }
        incre = -1 * incre;
    }

    /**
     * Increments or decrements the snake's lives
     * 
     * @param s String representing positive or negative change
     */
    void changeLives(String s) {
        if (s.equals("+")) {
            lives++;
        }
        if (s.equals("-")) {
            lives--;
        }
    }

    /**
     * Helper method for move, updates each position in the snake
     * as well as performs changes of consumed food, if applicable
     * 
     * @param yPos y position in arr
     * @param xPos x postion in arr
     * @param oldColor original color of arr[y][x] 
     */
    private void updateSnake(int yPos, int xPos, Color oldColor) {
        // update parts in between head and tail
        if (positions.size() > 2) {
            for (int[] position : positions.subList(1, positions.size() - 2)) {
                board[yPos][xPos] = SNAKE;
                yPos = position[0];
                xPos = position[1];
            }
        }

        // update tail in list and array
        int[] tail = positions.get(positions.size() - 1);
        int tailY = tail[0];
        int tailX = tail[1];
        board[tailY][tailX] = BACKGROUND;
        Consumable food = null;

        // if snake consumed food, do an effect
        if (oldColor.equals(Color.red)) { // grows in length
            food = new Apple(this);
            food.changeSnake();
        }
        if (oldColor.equals(Color.pink)) { // gains a life
            food = new Peach(this);
            food.changeSnake();
        }
        positions.remove(positions.size() - 1);
        if (oldColor.equals(Color.yellow)) { // reverses direction
            food = new Lemon(this);
            food.changeSnake();
        }

        if (food != null) {
            food.generate(); // generate new food if snake consumed food
        }
    }

    /**
     * Tells whether the snake has any lives left
     * 
     * @return true if > 0 lives, false otherwise
     */
    public boolean isDead() {
        return lives == 0;
    }

    /**
     * Tells whether the snake has recently collided
     * 
     * @return true if collided, false otherwise
     */
    public boolean hasCollided() {
        if (collided) {
            collided = false; // reset
            return true;
        }
        return false;
    }

    /**
     * getter for lives field
     * 
     * @return number of lives left
     */
    public int getLives() {
        return lives;
    }
}
