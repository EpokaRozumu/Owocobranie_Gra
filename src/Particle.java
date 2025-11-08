import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class Particle {
    double x;
    double y;
    double dy;
    double dx;
    static final int SPEED = 7;
    int time_left = 20;
    Color color;
    Particle(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        //todo: set random direction, but maintain the same speed
        int randomnum = new Random().nextInt(360);
        int speedVariation = new Random().nextInt(3) - 2;
        this.dx =  Math.sin(randomnum)*SPEED + speedVariation;
        this.dy =  Math.cos(randomnum)*SPEED + speedVariation;

        this.color = color;
    }
    public void update() {
        time_left -= 1;
            x += dx;
            y += dy;

    }
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int)x,(int)y,10,10);
    }
}
