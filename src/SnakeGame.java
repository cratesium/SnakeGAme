import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame implements KeyListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private int delay = 75; // Initial delay
    private ArrayList<Point> snake;
    private Point apple;
    private char direction;
    private boolean running;
    private Timer timer;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        addKeyListener(this);

        snake = new ArrayList<>();
        direction = 'R';
        running = false; // Game starts in a paused state

        // Initialize snake at the center of the screen
        for (int i = 3; i >= 0; i--) {
            snake.add(new Point(WIDTH / 2 - i * UNIT_SIZE, HEIGHT / 2));
        }

        timer = new Timer(delay, e -> {
            if (running) {
                move();
                checkApple();
                checkCollisions();
                repaint();
            }
        });
    }

    public void startGame() {
        spawnApple();
        running = true;
        timer.start();
    }

    public void spawnApple() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        int y = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        apple = new Point(x, y);
    }

    public void move() {
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.set(i, new Point(snake.get(i - 1)));
        }

        switch (direction) {
            case 'U':
                snake.get(0).y -= UNIT_SIZE;
                break;
            case 'D':
                snake.get(0).y += UNIT_SIZE;
                break;
            case 'L':
                snake.get(0).x -= UNIT_SIZE;
                break;
            case 'R':
                snake.get(0).x += UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if (snake.get(0).equals(apple)) {
            snake.add(new Point(apple));
            spawnApple();
        }
    }

    public void checkCollisions() {
        // Check if snake hits the walls
        if (snake.get(0).x < 0 || snake.get(0).x >= WIDTH || snake.get(0).y < 0 || snake.get(0).y >= HEIGHT) {
            running = false;
        }

        // Check if snake hits itself
        for (int i = 1; i < snake.size(); i++) {
            if (snake.get(0).equals(snake.get(i))) {
                running = false;
                break;
            }
        }
    }

    public void paint(Graphics g) {
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // Draw walls
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, UNIT_SIZE);
        g.fillRect(0, 0, UNIT_SIZE, HEIGHT);
        g.fillRect(WIDTH - UNIT_SIZE, 0, UNIT_SIZE, HEIGHT);
        g.fillRect(0, HEIGHT - UNIT_SIZE, WIDTH, UNIT_SIZE);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
        }

        // Draw apple
        g.setColor(Color.RED);
        g.fillRect(apple.x, apple.y, UNIT_SIZE, UNIT_SIZE);

        if (!running) {
            gameOver(g);
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over", WIDTH / 2 - 120, HEIGHT / 2);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!running) {
            startGame();
            return;
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (direction != 'D')
                    direction = 'U';
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U')
                    direction = 'D';
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R')
                    direction = 'L';
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L')
                    direction = 'R';
                break;
            case KeyEvent.VK_R:
                restartGame();
                break;
            case KeyEvent.VK_PLUS:
                delay -= 10; // Decrease delay for faster speed
                timer.setDelay(delay);
                break;
            case KeyEvent.VK_MINUS:
                delay += 10; // Increase delay for slower speed
                timer.setDelay(delay);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void restartGame() {
        running = true;
        startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame game = new SnakeGame();
            game.setVisible(true);
        });
    }
}
