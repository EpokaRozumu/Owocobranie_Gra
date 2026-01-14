
import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Owocobranie {
    static FancyButton buttons[];
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
        f.setLayout(new BoxLayout(f.getContentPane(),BoxLayout.PAGE_AXIS));
        GamePanel panel = new GamePanel();
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        f.add(panel);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.LINE_AXIS));
        f.add(buttonPanel);
        buttons = new FancyButton[3];
        for (int l = 0; l < 3; l++) {
            buttons[l] = new FancyButton(l+1);
            buttonPanel.add(buttons[l]);
        }
        Timer timer = new Timer(60,new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                update();
            }
        });

        timer.start();
        f.pack();
        f.setVisible(true);

    }
    private static void update() {
        for (int l=1;l<=3;l++) {
            if(GamePanel.unlockedLevel >= l) {
                buttons[l-1].setBackground(Color.WHITE);
            } else {
                buttons[l-1].setBackground(Color.DARK_GRAY);
            }
        }
    }

}


