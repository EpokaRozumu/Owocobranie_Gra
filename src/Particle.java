import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Random;

public class Particle {
    int x;
    int y;
    double dy;
    double dx;
    static final int SPEED = 5;
    int time_left = 20;
    Color color;
    static Set<Particle> particles = new HashSet<Particle>();
    Particle(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        //todo: set random direction, but maintain the same speed
        int randomnum = new Random().nextInt(360);
        this.dx =  Math.sin(randomnum)*SPEED;
        this.dy =  Math.cos(randomnum)*SPEED;

        this.color = color;
        particles.add(this);//will particles change every time this particle changes?
    }
    public void explosion(int x, int y) {
        for (int i = 0; i < 30; i++) {
            new Particle(x, y, Color.red);
        }
    }
    public void update() {
        time_left -= 1;
        if (time_left <= 0) {
            particles.remove(this);
        } else {
            x += dx;
            y += dy;
        }
    }
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x,y,5,5);
    }
    public void drawAll(Graphics g) {
        for (Particle p : particles) {
            p.draw(g);
        }
    }
    public void updateAll() {
        for (Particle p : particles) {
            p.update();
        }
    }
}
