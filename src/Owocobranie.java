
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Owocobranie {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? " +
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Owocobranie");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBackground(Color.BLACK);
        //Po powiększeniu okna panel powinien pozostać na środku, a nie
        //w lewym górnym rogu.
        f.setLayout(new BoxLayout(f.getContentPane(), BoxLayout.Y_AXIS));
        GamePanel panel = new GamePanel();
        //panel.setMaximumSize(new Dimension(800, 600));
        //panel.setBackground(Color.darkGray);
        f.add(panel);
        f.pack();
        f.setVisible(true);
    }
}


