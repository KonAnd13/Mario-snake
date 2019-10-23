import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;
    private Image mario;
    private Image cake;
    private int cakeX;
    private int cakeY;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left;
    private boolean right = true;
    private boolean up;
    private boolean down;
    private boolean inGame = true;

    public GameField() {
        setBackground(Color.BLACK);
        loadImages();
        initGame();
        addKeyListener(new KeyListener());
        setFocusable(true);
    }

    public void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer = new Timer(150, this);
        timer.start();
        createCake();
    }

    public void createCake() {
        cakeX = new Random().nextInt(20) * DOT_SIZE;
        cakeY = new Random().nextInt(20) * DOT_SIZE;
    }

    public void loadImages() {
        ImageIcon cakeIcon = new ImageIcon("cake.jpg");
        cake = cakeIcon.getImage();
        ImageIcon marioIcon = new ImageIcon("mario.jpg");
        mario = marioIcon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(cake, cakeX, cakeY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(mario, x[i], y[i], this);
            }
        } else {
            String s = "Game Over";
            g.setColor(Color.WHITE);
            g.drawString(s, 125, SIZE / 2);
        }
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left) {
            x[0] -= DOT_SIZE;
        } if (right) {
            x[0] += DOT_SIZE;
        } if (up) {
            y[0] -= DOT_SIZE;
        } if (down) {
            y[0] += DOT_SIZE;
        }
    }

    public void checkCake() {
        if (x[0] == cakeX && y[0] == cakeY) {
            dots++;
            createCake();
        }
    }

    public void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
            }
        }

        if (x[0] > SIZE) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (y[0] > SIZE) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkCake();
            checkCollisions();
            move();
        }
        repaint();
    }

    class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                right = false;
                left = false;
                up = true;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                right = false;
                left = false;
                down = true;
            }
        }
    }
}
