
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
    final int TIMER_SPEED = 100;//best - 30ms\
    int secondsPassed = 0;
    Font largeFont= new Font("Comic Sans MS", Font.BOLD, 50);
    Font smallFont = new Font("Comic Sans MS",Font.BOLD,12);
    Timer timer;
    public GamePanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                redSquare.moveSquare(e.getX(),e.getY());
                grid.handleSwapping(e);
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
    public void displayDebugInfo(Graphics g) {
        g.drawString(String.valueOf(grid.animationState), 20, 600);
        g.setFont(smallFont);
        g.drawString(secondsPassed + "", 20, 50);
        g.drawString("ZEBRANE OWOCE:", 20, 170);
        for (int i=0; i<Owoc.gatunki.length; i++) {
            g.drawString(Owoc.gatunki[i] + " " +Owoc.collectedFruits.get(Owoc.gatunki[i]), 20, 200+30*i);
        }
    }
    public void paintComponent(Graphics g) {
        //g will actually be Graphics2d
        super.paintComponent(g);
        g.setFont(largeFont);
        g.drawString("Owocobranie", 200, 55);//may replace this with image of a title

        displayDebugInfo(g);
        grid.paintGrid(g);
    }
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
