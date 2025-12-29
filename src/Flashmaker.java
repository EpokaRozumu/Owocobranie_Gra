import java.awt.*;
import java.util.HashSet;
import java.util.Set;
public class Flashmaker {
    Set<Flash> flashes = new HashSet<>();
    Set<Lightning> lightnings = new HashSet<>();
    public void drawFlashes(Graphics g) {
        for (Flash f : flashes) {
            f.draw(g);
        }
    }
    public void drawLightnings(Graphics g) {
        for (Lightning l : lightnings) {
            l.draw(g);
        }
    }
    public void update() {
        for (Flash f : flashes) {
            f.update();
        }
        for (Lightning l : lightnings) {
            l.update();
        }
        flashes.removeIf(f -> f.animation_duration <= 0);
        lightnings.removeIf(l -> l.animation_duration <= 0);
    }
    public void newFlash(int x, int y, int width, int height, int animation_delay) {
        Flash flash = new Flash(x,y,width,height,animation_delay);
        flashes.add(flash);
    }
    public void newLightning(int x1, int y1, int x2, int y2, int animation_duration) {
        Lightning lightning = new Lightning(x1,y1,x2,y2, animation_duration);
        lightnings.add(lightning);
    }
}
