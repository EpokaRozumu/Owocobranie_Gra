import java.awt.*;
import java.util.HashSet;
import java.util.Set;
public class Flashmaker {
    Set<Flash> flashes = new HashSet<>();
    public void draw(Graphics g) {
        for (Flash f : flashes) {
            f.draw(g);
        }
    }
    public void update() {
        for (Flash f : flashes) {
            f.update();
        }
        flashes.removeIf(f -> f.animation_duration <= 0);
    }
    public void newFlash(int x, int y, int width, int height, int animation_delay) {
        Flash flash = new Flash(x,y,width,height,animation_delay);
        flashes.add(flash);
    }
}
