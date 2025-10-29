import java.awt.*;

public class Owoc {
    static String[] gatunki = {"banan", "pomarancza","truskawka","wisnia","winogrono"};
    static final int ANIMATION_DURATION = 500;
    //replace with images!
    static Color[] kolory = {Color.yellow, Color.orange, Color.pink, Color.red,Color.blue};
    int x;
    int y;

    int matchesY = 0;//how many fruits are matched with it
    int matchesX = 0;
    boolean is_matched = false;
    boolean isAnimated;
    //enum SwapState {READY,SWAPPING, SWAPPED, RESWAPPING, RESWAPPED};
    String swapState = "READY";
    int nextX;//for animation purposes
    int nextY;

    int prevX = x;//for
    int prevY = y;

    String gatunek;
    Color kolor;
    Color nextKolor;


    public Owoc(int x, int y) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.swapState = "READY";
        //wylosuj typ owocu
        int randomNum = (int)(Math.random() * (gatunki.length));
        this.gatunek = gatunki[randomNum];
        this.kolor = kolory[randomNum];
    }
    public void updateAnimation(int timer_step) {
        final int SPEED = 10;
        double dx = nextX - x;
        double dy = nextY - y;
        double length = Math.sqrt(dx*dx + dy*dy);
        if (length < SPEED) {
            //finish swapping
            //quickly snap to previous position and change color to next color??
            x = prevX;
            y = prevY;
            kolor = nextKolor;
            isAnimated = false;
            if (swapState == "SWAPPING" ) {
                swapState = "SWAPPED";
            } else if (swapState == "RESWAPPING") {
                swapState = "RESWAPPED";
            }
        } else {
            //by Kuba Brzozowski:
            //w sensie przypomniał mi on równanie na wektor kierunkowy
            //którego zapomniałam
            dx = SPEED * dx / length;
            dy = SPEED * dy / length;
            x += dx;
            y += dy;
            isAnimated = true;
        }
    }
    public void paintOwoc(Graphics g) {
        int r = 35;
        g.setColor(Color.black);
        g.drawOval(x,y,r,r);
        g.setColor(kolor);
        g.fillOval(x,y,r,r);

    }
}