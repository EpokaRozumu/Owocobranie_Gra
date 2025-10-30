import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Owoc {

    static String[] gatunki = {"cytryna", "pomarancza","truskawka","wisnia","winogrono"};
    static String[] imagePaths = {"cytryna.bmp","pomarancza.bmp","arbuz.bmp","wisnia.bmp","winogrono.bmp"};
    static BufferedImage[] images = new BufferedImage[gatunki.length];


    static final int ANIMATION_DURATION = 500;

    int x;
    int y;

    int matchesY = 0;//how many fruits are matched with it
    int matchesX = 0;
    boolean is_matched = false;

    boolean isAnimated;
    String swapState = "READY";
    //enum SwapState {READY,SWAPPING, SWAPPED, RESWAPPING, RESWAPPED};

    int nextX;//for animation purposes
    int nextY;
    int prevX;//for finishing animation
    int prevY;

    String gatunek;
    int imageIndex = 0;
    int nextImageIndex = 0;


    public Owoc(int x, int y) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.swapState = "READY";
        //wylosuj typ owocu
        int randomNum = (int)(Math.random() * (gatunki.length));
        this.imageIndex = randomNum;
        this.gatunek = gatunki[randomNum];
    }
    public static void loadImages() {
        for (int i = 0; i < gatunki.length; i++) {
            try {
                images[i] = ImageIO.read(new File(imagePaths[i]));
            } catch (IOException e) {
                System.out.println("Error loading image "+imagePaths[i]);
                //throw new RuntimeException(e);

            }
        }

    }
    public void finishSwapping() {
        //quickly jump to previous position
        //because position on screen must match grid position
        x = prevX;
        y = prevY;
        imageIndex = nextImageIndex;
        isAnimated = false;
        if (swapState == "SWAPPING" ) {
            swapState = "SWAPPED";
        } else if (swapState == "RESWAPPING") {
            swapState = "RESWAPPED";
        }
    }

    public void updateAnimation(int timer_step) {
        //This function is called by Grid's update animation
        //which is called repeatadly by the Swing timer
        final int SPEED = 10;
        double dx = nextX - x;
        double dy = nextY - y;
        double distance = Math.sqrt(dx*dx + dy*dy);//distance to destination
        if (distance < SPEED) {
            finishSwapping();
        } else {
            //by Kuba Brzozowski:
            //w sensie przypomniał mi on równanie na wektor kierunkowy
            //którego zapomniałam

            //move at constant speed towards destination
            dx = SPEED * dx / distance;
            dy = SPEED * dy / distance;
            x += dx;
            y += dy;
            isAnimated = true;
        }
    }
    public void paintOwoc(Graphics g) {
        int r = 35;
//        g.setColor(Color.black);
//        g.drawOval(x,y,r,r);
//        g.setColor(kolor);
        //temp:

        //g.fillOval(x,y,r,r);

        g.drawImage(images[imageIndex],x-5,y-5,46,46,null);

    }
}