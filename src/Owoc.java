import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class Owoc {

    static final String[] gatunki = {"cytryna", "pomarancza","arbuz","wisnia","winogrono", "malina","jagoda"};
    static final String[] fruitImagePaths = {"cytryna.bmp","pomarancza.bmp","arbuz.bmp","wisnia.bmp","winogrono.bmp","malina.png", "jagoda.png"};
    static final String[] specials = {"horizontal", "vertical", "flower","bomb"};
    static final String[] specialPaths = {"horizontal_tag.png", "vertical_tag.png", "flower_tag.png", "bomb_tag.png"};
    static HashMap<String,Integer> collectedFruits = new HashMap<String, Integer>();

    static BufferedImage[] images = new BufferedImage[gatunki.length];
    static BufferedImage[] specialImages = new BufferedImage[specials.length];
    static final int BASE_SPEED = 10;
    static final int ANIMATION_DURATION = 500;

    //ParticleSource explosion;

    int x;
    int y;
    int width;
    int height;

    int matchesY = 0;//how many fruits are matched with it
    int matchesX = 0;
    boolean is_matched = false;

    boolean isAnimated;
    double relative_speed;

    AnimState animationState;
    int animationDelay = 0;
    int nextX;//for animation purposes
    int nextY;
    int prevX;//for finishing animation
    int prevY;

    String gatunek;
    String special;
    boolean deleteSpecialAfterExploding;
    int imageIndex = 0;//-1 if this fruit was exploded
    //image index is useful, because it takes less space if fruits hold only their type, and not a whole image
    //I hope it will make this program faster?
    int nextImageIndex = 0;


    public Owoc(int x, int y) {

        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 40;

        this.prevX = x;
        this.prevY = y;

        this.animationState = AnimState.READY;
        this.relative_speed = 1;
        this.special = "none";
        this.deleteSpecialAfterExploding = false;
        //this.explosion = new ParticleSource(x,y);
        //wylosuj typ owocu
        int randomNum = random();
        this.imageIndex = randomNum;
        this.gatunek = gatunki[randomNum];
//        if (this.gatunek == "cytryna" ) {
//            this.special = "bomb";
//        }
        //how to avoind repetion??
        if (collectedFruits.isEmpty()) {
            for (String gatunek : gatunki) {
                collectedFruits.put(gatunek, 0);
            }
        }
    }
    public static int getSpecialIndex(String a) {
        for (int i = 0; i < specials.length; i++) {
            if (specials[i].equals(a)) {
                return i;
            }
        }
        return -1;
    }
    public static BufferedImage getImage(int index) {
        return images[index];
    }
    public static int random() {
        return (int)(Math.random() * (gatunki.length -3 + GamePanel.selectedLevel));
    }
    public static void loadImages()  {
        for (int i = 0; i < gatunki.length; i++) {
            try {
                images[i] = ImageIO.read(new File(fruitImagePaths[i]));
            } catch (IOException e) {
                System.out.println("Error loading image "+ fruitImagePaths[i]);
                //throw new RuntimeException(e);
            }
        }
        for (int i=0; i<specials.length; i++) {
            try {
                specialImages[i] = ImageIO.read(new File(specialPaths[i]));
            } catch (IOException e) {
                System.out.println("Error loading image "+ specialPaths[i]);
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
        //todo???
        is_matched = false;
        if (imageIndex > -1) {
            gatunek = gatunki[imageIndex];
            collectedFruits.replace(gatunek, collectedFruits.get(gatunek)+1) ;
            if (special == "none" || deleteSpecialAfterExploding) {
                imageIndex = -1;
            }

        }
    }
    public  static int getCollectedFruitsOfIndex(int index) {
        return collectedFruits.get(gatunki[index]);
    }
    public static void eraseCollectedFruits() {
        collectedFruits.forEach((k,v)->{v = 0;});
    }

    public void updateAnimation(int timer_step) {
        //This function is called by Grid's update animation
        //which is called repeatadly by the Swing timer
        //explosion.update();
        if (animationDelay > 0) {
            animationDelay -= 1;
            return;
        }
        if (animationState == AnimState.EXPLODING) {
            if (height < 80) {
                height += 4;
                width += 4;
            } else {
                width = 40;
                height = 40;
                //imageIndex = nextImageIndex;
                animationState = AnimState.EXPLODED;
                collect();
            }
        }
        double dx = nextX - x;
        double dy = nextY - y;
        double distance = Math.sqrt(dx*dx + dy*dy);//distance to destination

        if (this.animationState == AnimState.SWAPPING || this.animationState == AnimState.RESWAPPING) {
            relative_speed = 0.5;//swapping should be slower than falling
            //todo: for some reason reswapping is still as fast as falling...
        } else {
            relative_speed = 1.2;
        }

        if (animationState == AnimState.SWAPPING || animationState == AnimState.RESWAPPING || animationState == AnimState.FALLING) {//if animation needs movement
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
    }
    public void paintOwoc(Graphics g) {
        //explosion.draw(g);
        int r = 35;
        if (imageIndex > -1) {
            g.drawImage(images[imageIndex],x-2-(width/2 - 20),y-2-(height/2 - 20),width,height,null);
            int specialIndex = getSpecialIndex(special);
            if (specialIndex > -1 && !(animationState == AnimState.EXPLODING && deleteSpecialAfterExploding==false)) {
                g.drawImage(specialImages[specialIndex],x+10,y+10,16*width/40,16*height/40,null);
            }
        }
    }
}