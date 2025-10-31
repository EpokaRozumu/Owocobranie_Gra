
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
    final int TIMER_SPEED = 100;
    int secondsPassed = 0;
    Font largeFont= new Font("Comic Sans MS", Font.BOLD, 50);
    Font smallFont;
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
                grid.updateAnimation(TIMER_SPEED);
                grid.handleMatching();
                //redSquare.updateAnimation(TIMER_SPEED);
                repaint();
            }
        });
        timer.start();
    }
    //all custom painting here
    public void paintComponent(Graphics g) {
        //g will actually be Graphics2d
        super.paintComponent(g);
        g.setFont(largeFont);
        g.drawString("Owocobranie", 200, 55);//may replace this with image of a title
        g.drawString(secondsPassed + "", 20, 50);
        redSquare.paintSquare(g);
        grid.paintGrid(g);
    }
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }
}
