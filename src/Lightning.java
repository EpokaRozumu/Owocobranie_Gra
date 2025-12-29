import java.awt.*;

public class Lightning {
    int x1, y1, x2, y2;
    int animation_delay, animation_duration;
    int width = 10;
    Lightning (int x1, int y1, int x2, int y2, int animation_delay) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.animation_delay = animation_delay;
        this.animation_duration = 20;
    }
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawLine(x1,y1,x2,y2);
        g.drawRect(x1,y1,x2-x1,y2-y1);
    }
    public void update() {
        if (animation_delay > 0) {
            animation_delay --;
        } else {
            animation_duration--;
        }
    }
}
