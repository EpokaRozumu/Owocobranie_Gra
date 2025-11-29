
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


class GamePanel extends JPanel {
    RedSquare redSquare = new RedSquare();
    Grid grid = new Grid();
    final int TIMER_SPEED = 30;//best - 30ms\
    int secondsPassed = 0;
    Font largeFont= new Font("Comic Sans MS", Font.BOLD, 50);
    Font smallFont = new Font("Comic Sans MS",Font.BOLD,12);
    Font mediumFont = new Font("Comic Sans MS",Font.BOLD,20);
    float zoom = 1f;
    int goalFruitIndex ;
    int turnsLeft;
    int fruitsToWin;
    public GamePanel() {

        setupNewGame();
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                redSquare.moveSquare(e.getX(),e.getY());
                boolean madeAMove = grid.handleSwapping(e);
                if (madeAMove) {
                    turnsLeft--;
                }
                repaint();
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                redSquare.moveSquare(e.getX(),e.getY());
                repaint();
            }
        });
        Timer timer = new Timer(TIMER_SPEED,new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                grid.update(TIMER_SPEED);
                repaint();
            }
        });
        timer.start();
    }
    //all custom painting here
    public void setupNewGame() {
        turnsLeft = 40;
        fruitsToWin = 30;
        goalFruitIndex = Owoc.random();
    }
    public void displayGameState(Graphics g, int x, int y) {
        g.setFont(mediumFont);
        g.drawString("Moves left: " + turnsLeft, x, y);
        int fruitsLeftToWin = fruitsToWin - Owoc.getCollectedFruitsOfIndex(goalFruitIndex);
        g.drawString(fruitsLeftToWin + "x    ", x+200, y);
        g.drawImage(Owoc.getImage(goalFruitIndex), x + 250,y-20,40,40,null);

    }
    public void displayDebugInfo(Graphics g) {
        //g.drawString(String.valueOf(grid.animationState), 20, 600);
        g.setFont(smallFont);
        //g.drawString(secondsPassed + "", 20, 50);
        g.drawString("zoom" + zoom,120,10);
        //g.drawString("ZEBRANE OWOCE:", 20, 170);
    }

    public void displayCollectedFruits(Graphics g) {
        for (int i=0; i<Owoc.gatunki.length; i++) {
            g.drawString(Owoc.gatunki[i] + " " +Owoc.collectedFruits.get(Owoc.gatunki[i]), 20, 200+30*i);
        }
    }
    public void paintComponent(Graphics g) {
        //g will actually be Graphics2d
        zoom = getHeight()/600.0f;
        super.paintComponent(g);
        //displayCollectedFruits(g);
        displayGameState(g,250,110);
        g.setFont(largeFont);
        g.drawString("Owocobranie", 240, 55);//may replace this with image of a title

        displayDebugInfo(g);
        grid.paintGrid(g);
    }
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
