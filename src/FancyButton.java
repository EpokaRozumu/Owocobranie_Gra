import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FancyButton extends JButton {
    public FancyButton(String text) {
        super();
        this.setText(text + " !");
        this.setBackground(Color.getColor("#00E436"));
        //this.setPreferredSize(new Dimension(100,100));
        this.setFont(new Font("Comics Sans MS", Font.PLAIN, 12));
        //this.setBorder(BorderFactory.createLineBorder(Color.GREEN, 4, true));
    }
}
