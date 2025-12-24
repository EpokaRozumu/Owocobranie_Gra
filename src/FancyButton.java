import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FancyButton extends JButton {
    int level;

    public FancyButton(int level) {
        super();
        this.level = level;
        //setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        setText("level" + level);
        //setBackground(Color.decode("#00E436"));
        this.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (level <= GamePanel.unlockedLevel) {
                    int chosenOption = JOptionPane.showConfirmDialog(null, "Do you want to start a new level?", "Level " + level, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (chosenOption == JOptionPane.YES_OPTION) {
                        //System.out.println("changing level to " + level);
                        GamePanel.selectedLevel = level;
                        GamePanel.newGameRequested = true;
                    }
                }

            }
        });
        //setBorderPainted(false);
        //this.setPreferredSize(new Dimension(100,100));
        //setBorder(BorderFactory.createLineBorder(Color.BLACK,2,true));

        //this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4, true));
    }
}
