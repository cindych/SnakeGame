package snake;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.util.Map;

class BoardTest {

    @Test
    public void testMoveSimpleSnake() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();
        snake.setDirection("vert,-1");
        snake.move();
        assertEquals(Color.green, arr[7][8]);
        assertEquals(Color.lightGray, arr[8][8]);
    }

    @Test
    public void testMoveEatApple() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();

        arr[7][8] = Color.red;
        snake.setDirection("vert,-1");
        snake.move();
        assertEquals(Color.green, arr[7][8]);
        assertEquals(Color.green, arr[8][8]);
    }

    @Test
    public void testMoveLongSnake() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();

        // grow snake
        arr[9][8] = Color.red;
        arr[10][8] = Color.red;
        snake.setDirection("vert,1");
        snake.move();
        snake.move();
        assertEquals(Color.green, arr[8][8]);
        assertEquals(Color.green, arr[9][8]);
        assertEquals(Color.green, arr[10][8]);

        // move long snake
        snake.setDirection("horiz,-1");
        snake.move();
        snake.move();
        snake.setDirection("vert,-1");
        snake.move();
        assertEquals(Color.green, arr[9][6]);
        assertEquals(Color.green, arr[10][6]);
        assertEquals(Color.green, arr[10][7]);
        assertEquals(Color.lightGray, arr[10][8]);
    }

    @Test
    public void testCollideWallTrue() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();

        // move snake to edge
        int[] newPosition = { 0, 5 };
        snake.positions.remove(0);
        snake.positions.add(newPosition);
        arr[4][4] = Color.lightGray;
        arr[0][5] = Color.green;
        snake.setDirection("vert,-1");
        assertTrue(snake.collide());
        snake.move();
        // make sure that snake did not move
        assertEquals(Color.green, arr[0][5]);
    }

    @Test
    public void testCollideWallFalse() {
        Board gameboard = new Board();
        Snake snake = gameboard.getSnake();
        snake.setDirection("vert,-1");
        assertFalse(snake.collide());
    }

    @Test
    public void testCollideBodyTrue() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();

        arr[9][8] = Color.red;
        arr[10][8] = Color.red;
        arr[10][7] = Color.red;
        snake.setDirection("vert,1");
        snake.move();
        snake.move();
        snake.setDirection("horiz,-1");
        snake.move();
        snake.setDirection("vert,-1");
        snake.move();
        snake.setDirection("horiz, 1");
        assertTrue(snake.collide());
    }

    @Test
    public void testCollideBodyFalse() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();
        arr[5][4] = Color.red;
        arr[6][4] = Color.red;
        arr[6][3] = Color.red;
        snake.setDirection("vert,1");
        snake.move();
        snake.move();
        snake.setDirection("horiz,1");
        assertFalse(snake.collide());
    }

    @Test
    public void testSetDirection() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();
        int[] newPosition = { 7, 8 };
        snake.positions.add(newPosition);
        arr[7][8] = Color.green;
        int[] newPosition2 = { 7, 7 };
        snake.positions.add(newPosition2);
        arr[7][7] = Color.green;

        snake.setDirection("vert,1");
        snake.move();
        assertEquals(Color.green, arr[9][8]);
        assertEquals(Color.green, arr[8][8]);
        assertEquals(Color.green, arr[7][8]);
    }

    @Test
    public void testEatPeach() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();
        arr[9][8] = Color.pink;
        snake.setDirection("vert,1");
        snake.move();
        assertEquals(2, snake.getLives());
    }

    @Test
    public void testEatLemon() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();
        arr[9][8] = Color.red;
        arr[10][8] = Color.yellow;
        snake.setDirection("vert,1");
        snake.move();
        snake.move();

        // head and tail should have flipped
        int[] tail = snake.positions.get(0);
        int[] expected1 = { 9, 8 };
        int[] head = snake.positions.get(1);
        int[] expected2 = { 10, 8 };
        assertArrayEquals(expected1, tail);
        assertArrayEquals(expected2, head);

        snake.move();
        assertEquals(Color.green, arr[8][8]);
        assertEquals(Color.green, arr[9][8]);

        snake.move();
        assertEquals(Color.green, arr[7][8]);
        assertEquals(Color.green, arr[8][8]);
    }

    @Test
    public void testBonus() {
        Board gameboard = new Board();
        Snake snake = gameboard.getSnake();
        Map<String, Integer> food = snake.food;

        int count;
        for (int i = 0; i < 3; i++) {
            count = food.get("apples");
            food.put("apples", count + 1);
            count = food.get("peaches");
            food.put("peaches", count + 1);
            count = food.get("lemons");
            food.put("lemons", count + 1);
        }

        assertTrue(gameboard.checkBonus());
        assertEquals(4, snake.getLives());
    }

    @Test
    public void testGameOver() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();

        // make snake collide into wall
        int[] newPosition = { 18, 0 };
        snake.positions.remove(0);
        snake.positions.add(newPosition);
        arr[8][8] = Color.lightGray;
        arr[0][0] = Color.green;
        snake.setDirection("vert,1");
        snake.move();
        assertTrue(gameboard.checkGameStatus());
    }

    @Test
    public void testGameRestart() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();
        snake.changeLives("+");

        // make snake collide into wall
        int[] newPosition = { 18, 0 };
        snake.positions.remove(0);
        snake.positions.add(newPosition);
        arr[8][8] = Color.lightGray;
        arr[0][0] = Color.green;
        snake.setDirection("vert,1");
        snake.move();
        assertFalse(gameboard.checkGameStatus());

        gameboard.restart();
        assertEquals(1, snake.getLives());
        assertEquals(Color.green, arr[8][8]);
        assertEquals(1, snake.positions.size());
        snake.move();
        assertEquals(Color.green, arr[9][8]);
    }

    @Test
    public void testReset() {
        Board gameboard = new Board();
        Color[][] arr = gameboard.arr;
        Snake snake = gameboard.getSnake();
        snake.move();
        snake.changeLives("+");

        gameboard.reset();
        snake = gameboard.getSnake();
        assertEquals(Color.green, arr[8][8]);
        assertEquals(Color.lightGray, arr[9][8]);
        assertEquals(1, snake.getLives());
    }

}
