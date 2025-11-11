import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Owoc {

    static String[] gatunki = {"cytryna", "pomarancza","arbuz","wisnia","winogrono"};
    static String[] imagePaths = {"cytryna.bmp","pomarancza.bmp","arbuz.bmp","wisnia.bmp","winogrono.bmp"};

    static HashMap<String,Integer> collectedFruits = new HashMap<String, Integer>();

    static BufferedImage[] images = new BufferedImage[gatunki.length];
    static final int BASE_SPEED = 10;
    static final int ANIMATION_DURATION = 500;

    //ParticleSource explosion;

    int x;
    int y;

    int matchesY = 0;//how many fruits are matched with it
    int matchesX = 0;
    boolean is_matched = false;

    boolean isAnimated;
    double relative_speed = 1.0;

    AnimState animationState;
    int nextX;//for animation purposes
    int nextY;
    int prevX;//for finishing animation
    int prevY;

    String gatunek;
    int imageIndex = 0;//-1 if this fruit was exploded
    //image index is useful, because it takes less space if fruits hold only their type, and not a whole image
    //I hope it will make this program faster?
    int nextImageIndex = 0;


    public Owoc(int x, int y) {
        this.x = x;
        this.y = y;
        this.prevX = x;
        this.prevY = y;
        this.animationState = AnimState.READY;
        this.relative_speed = 1;
        //this.explosion = new ParticleSource(x,y);
        //wylosuj typ owocu
        int randomNum = (int)(Math.random() * (gatunki.length));
        this.imageIndex = randomNum;
        this.gatunek = gatunki[randomNum];

        //how to avoind repetion??
        if (collectedFruits.isEmpty()) {
            for (String gatunek : gatunki) {
                collectedFruits.put(gatunek, 0);
            }
        }
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
        if (animationState == AnimState.SWAPPING ) {
            animationState = AnimState.SWAPPED;
        } else if (animationState == AnimState.RESWAPPING) {
            animationState = AnimState.RESWAPPED;
        }
    }
    public void finishFalling() {
        //isAnimated = false;
        animationState = AnimState.FALLEN;
    }
    public void collect() {
       gatunek = gatunki[imageIndex];
       collectedFruits.replace(gatunek, collectedFruits.get(gatunek)+1) ;
    }

    public void updateAnimation(int timer_step) {
        //This function is called by Grid's update animation
        //which is called repeatadly by the Swing timer
        //explosion.update();
        double dx = nextX - x;
        double dy = nextY - y;
        double distance = Math.sqrt(dx*dx + dy*dy);//distance to destination

        if (this.animationState == AnimState.SWAPPING || this.animationState == AnimState.RESWAPPING) {
            relative_speed = 0.5;//swapping should be slower than falling
            //todo: for some reason reswapping is still as fast as falling...
        } else {
            relative_speed = 1.0;
        }

        if (distance < BASE_SPEED*relative_speed) {
            if (animationState == AnimState.FALLING ) {
                finishFalling();
            } else {
                finishSwapping();
            }
        } else {
            //by Kuba Brzozowski:
            //w sensie przypomniał mi on równanie na wektor kierunkowy
            //którego zapomniałam

            //move at constant speed towards destination
            dx = BASE_SPEED*relative_speed * dx / distance;
            dy = BASE_SPEED*relative_speed * dy / distance;
            x += dx;
            y += dy;
            isAnimated = true;
        }
    }
    public void paintOwoc(Graphics g) {
        //explosion.draw(g);
        int r = 35;
        if (imageIndex > -1) {
            g.drawImage(images[imageIndex],x-2,y-2,40,40,null);
        }
    }
}