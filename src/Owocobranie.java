
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
        f.setLayout(new BoxLayout(f.getContentPane(),BoxLayout.PAGE_AXIS));
        GamePanel panel = new GamePanel();
        //panel.
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        //panel.setMaximumSize(new Dimension(800, 600));
        //panel.setBackground(Color.darkGray);
        f.add(panel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.LINE_AXIS));
        f.add(buttonPanel);
        FancyButton level1Button = new FancyButton("Level 1");
        buttonPanel.add(level1Button);
        FancyButton level2Button = new FancyButton("Level 2");
        buttonPanel.add(level2Button);
        FancyButton level3Button = new FancyButton("Level 3");
        buttonPanel.add(level3Button);
        f.pack();
        f.setVisible(true);
    }
}


