import java.awt.*;

public class Flash {
    int width;
    int height;
    int x;
    int y;
    int animation_duration;
    int animation_delay;
    Flash(int x, int y, int width, int height, int animation_delay) {
        animation_duration = 25;
        this.animation_delay = animation_delay;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void update() {
        if (animation_delay > 0) {
            animation_delay --;
        } else {
            animation_duration--;
        }
    }
    public void draw(Graphics g) {
        if (animation_delay > 0) return;
        g.setColor(Color.decode("#FFCCAA"));
        g.fillRect(x, y, width, height);
    }
}
