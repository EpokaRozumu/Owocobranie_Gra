
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;


class GamePanel extends JPanel {
    Grid grid = new Grid(1);

    Memory memory = new Memory();

    final int TIMER_SPEED = 30;//best - 30ms\
    int secondsPassed = 0;
    Font largeFont= new Font("Comic Sans MS", Font.BOLD, 50);
    Font smallFont = new Font("Comic Sans MS",Font.BOLD,12);
    Font mediumFont = new Font("Comic Sans MS",Font.BOLD,20);

    float zoom = 1f;
    int goalFruitIndex ;
    int turnsLeft;
    int fruitsToWin;
    static int unlockedLevel;
    static int selectedLevel;
    static boolean newGameRequested = false;
    boolean gameRunning = true;
    boolean gameWon = false;
    public GamePanel() {
        unlockedLevel = memory.readSavedLevel();
        selectedLevel = 1;
        setupNewGame(selectedLevel);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.decode("#FFECEA"));


        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (gameRunning) {
                    boolean madeAMove = grid.handleSwapping(e,zoom);
                    if (madeAMove) {
                        turnsLeft--;
                    }
                    repaint();
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                repaint();
            }
        });
        Timer timer = new Timer(TIMER_SPEED,new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                grid.update(TIMER_SPEED);
                update();
                repaint();
            }
        });
        timer.start();
    }
    public void update() {
        if (newGameRequested) {
            setupNewGame(selectedLevel);
            newGameRequested = false;
        }
        if (fruitsToWin <= Owoc.getCollectedFruitsOfIndex(goalFruitIndex)) {//if all needed fruits are collected
            gameRunning = false;
            gameWon = true;
        } else if (turnsLeft <= 0) {
            gameRunning = false;
            gameWon = false;
        }
    }
    public void setupNewGame(int level) {
        grid = new Grid(level);
        turnsLeft = 20;
        fruitsToWin = 40;
        Owoc.eraseCollectedFruits();
        goalFruitIndex = Owoc.random();
        gameRunning = true;
        gameWon = false;
    }

    public void displayGameState(Graphics g, int x, int y) {
        g.setFont(mediumFont);
        if (gameRunning) g.drawString("Moves left: " + turnsLeft, x, y);
        int fruitsLeftToWin = fruitsToWin - Owoc.getCollectedFruitsOfIndex(goalFruitIndex);
        g.drawString(fruitsLeftToWin + "x    ", x+200, y);
        g.drawImage(Owoc.getImage(goalFruitIndex), x + 250,y-20,40,40,null);

    }
    public void displayDebugInfo(Graphics g) {
        //g.drawString(String.valueOf(grid.animationState), 20, 600);
        g.setFont(smallFont);
        //g.drawString(secondsPassed + "", 20, 50);
        //g.drawString("zoom" + zoom,120,10);
        //g.drawString("ZEBRANE OWOCE:", 20, 170);
    }

    public void displayCollectedFruits(Graphics g) {
        for (int i=0; i<Owoc.gatunki.length; i++) {
            g.drawString(Owoc.gatunki[i] + " " +Owoc.collectedFruits.get(Owoc.gatunki[i]), 20, 200+30*i);
        }
    }
    public void paintComponent(Graphics g) {
        //g will actually be Graphics2d
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(zoom,zoom);

        zoom = getHeight()/600.0f;
        super.paintComponent(g2);
        g.setFont((mediumFont));
        //g.drawString("Highest unlocked level: " + unlockedLevel,300,580);
        displayGameState(g2,250,110);
        g2.setFont(largeFont);
        g2.setColor(Color.decode("#FF9D81"));
        if (gameRunning) {
            g2.drawString("Owocobranie", 240, 55);//may replace this with image of a title
        } else if (gameWon) {
            g2.setColor(Color.getHSBColor(secondsPassed/10.0f, 1.0f, 1.0f));
            g2.drawString("YOU WIN!", 240, 55);
        } else {
            g2.setColor(Color.BLACK);
            g2.drawString("GAME OVER", 240, 55);
        }

        g2.setColor(Color.black);
        displayDebugInfo(g2);
        grid.paintGrid(g2);
        if (!gameRunning) {
            g.setColor(Color.red);
            g.setFont(largeFont);
            if (gameWon) {
                if (selectedLevel <= unlockedLevel) {
                    if (selectedLevel == unlockedLevel) {
                        unlockedLevel = selectedLevel + 1;
                        memory.writeSavedLevel(unlockedLevel);
                    }
                    g2.setFont(mediumFont);
                    g2.setColor(Color.getHSBColor(secondsPassed/100.0f, 1.0f, 1.0f));
                    g2.drawString("Level " + unlockedLevel + " unlocked!", 240, 120);
                }

            }
        }
        }

    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}

