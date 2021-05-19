package snake;

import java.util.*;
import java.awt.Color;

public abstract class Consumable {
	
    Color[][] arr;
    Snake snake;

    // final variables
    static final Color SNAKE = Color.GREEN;
    private static final Color APPLE = Color.RED;
    private static final Color PEACH = Color.PINK;
    private static final Color LEMON = Color.YELLOW;
    
    public Consumable(Snake snake) {
        this.arr = snake.board;
        this.snake = snake;
    }

    /**
     * randomly generates a new food in the board
     */
    void generate() {
        int yPos = (int) (1 + Math.random() * 8);
        int xPos = (int) (1 + Math.random() * 8);

        // find a position to place food 
        while (notPartOfSnake(yPos, xPos)) {
            yPos = (int) (1 + Math.random() * 8);
            xPos = (int) (1 + Math.random() * 8);
        }

        int num = (int) (Math.random() * 7);
        switch (num) {
            case 0:
                arr[yPos][xPos] = PEACH; // least likely to generate
                break;
            case 1:
            case 2:
                arr[yPos][xPos] = LEMON; // semi-likely to generate
                break;
            default:
                arr[yPos][xPos] = APPLE; // most likely to generate
        }
    }

    /**
     * Helper method for generate,
     * tells whether a position is part of the snake
     * 
     * @param yPos y position (arr[])
     * @param xPos (arr[][])
     * @return true if position is a position that
     *         is part of the snake, false otherwise
     */
    private boolean notPartOfSnake(int yPos, int xPos) {
        List<int[]> positions = snake.positions;

        for (int[] position : positions) {
            if (yPos == position[0] && xPos == position[1]) {
                return true;
            }
        }
        return false;
    }

    abstract void changeSnake();
}

class Apple extends Consumable {

    Apple(Snake snake) {
        super(snake);
    }

    /**
     * Increments apples in map & increases length of snake by 1
     */
    @Override
    void changeSnake() {
        Map<String, Integer> food = snake.food;
        int count = food.get("apples");
        food.put("apples", count + 1);

        List<int[]> positions = snake.positions;
        int[] tail = positions.get(positions.size() - 1);
        arr[tail[0]][tail[1]] = SNAKE;
        positions.add(tail);
    }

}

class Peach extends Consumable {

    Peach(Snake snake) {
        super(snake);
    }

    /**
     * Increments peaches in map and reverses direction
     */
    @Override
    void changeSnake() {
        Map<String, Integer> food = snake.food;
        int count = food.get("peaches");
        food.put("peaches", count + 1);

        snake.changeLives("+");
    }
}

class Lemon extends Consumable {

    Lemon(Snake snake) {
        super(snake);
    }

    /**
     * Increments lemons in map and reverses direction
     */
    @Override
    void changeSnake() {
        Map<String, Integer> food = snake.food;
        int count = food.get("lemons");
        food.put("lemons", count + 1);

        // reverse positions of snake, flip head and tail
        List<int[]> positions = snake.positions;
        Collections.reverse(positions);
        snake.reverseDirection(); // go opposite direction of old head

        // special case where new head will collide if
        // going in new direction, reverse direction again
        if (snake.collide()) {
            snake.reverseDirection();
        }
    }
}
