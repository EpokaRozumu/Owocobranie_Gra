import java.awt.*;
import java.util.HashSet;
import java.util.Set;
public class ParticleSource {
    Set<Particle> particles = new HashSet<Particle>();
    public ParticleSource(int x, int y) {
        for (int i=0; i<30; i++) {
            particles.add(new Particle(x,y, Color.red));
        }
    }
    public void draw(Graphics g) {
        for (Particle p : particles) {
            p.draw(g);
        }
    }
    public void update() {
        for (Particle p : particles) {
            p.update();
        }
        particles.removeIf(p -> p.time_left <= 0);
    }
}