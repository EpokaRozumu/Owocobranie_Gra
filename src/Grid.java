import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//import java.util.ArrayList;
//this is a comment
//list of known bugs:
//special sometimes dont do anything
//sometimes vertical or horizontal or flower trigger a cross???

public class Grid {
    int sel1x, sel1y, sel2x, sel2y;
    static final int SLOT_SPAN = 40;
    static final int GRID_OFFSET_X = 200;
    static final int GRID_OFFSET_Y = 150;

    AnimState animationState;
    ParticleSource particleSource;

    Owoc[][] grid = new Owoc[10][10];


    public Grid() {
        Owoc.loadImages();
        particleSource = new ParticleSource(50,50);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                grid[x][y] = new Owoc(gridToScreenX(x), gridToScreenY(y));
            }
        }
        unselectAllFruits();
    }
    //helper functions
        private int gridToScreenX(int x) {
            return x * SLOT_SPAN + GRID_OFFSET_X;
        }
        private int gridToScreenY(int y) {
            return y * SLOT_SPAN + GRID_OFFSET_Y;
        }
        private int screenToGridX(int x) {
            if (x < GRID_OFFSET_X || x > GRID_OFFSET_X + SLOT_SPAN*10) {
                System.out.println("No fruit matches mouse x");
                return -1;//
            }
            return (int)(x - GRID_OFFSET_X)/SLOT_SPAN;
        }
        private int screenToGridY(int y) {
            if (y < GRID_OFFSET_Y || y > GRID_OFFSET_Y + SLOT_SPAN*10) {
                System.out.println("No fruit matches mouse y");
                return -1;//
            }
            return (int)(y - GRID_OFFSET_Y)/SLOT_SPAN;
        }
        public AnimState getAnimationState() {
        //gets animation state of all grid
        //ready, falling, fallen, swapping,swapped;
         for (int x=0; x<10; x++) {
             for (int y=0; y<10; y++) {
                 if (grid[x][y].animationState != AnimState.READY) {
                     //if any fruit is falling, fallen, swapping or swapped -return its state
                     return grid[x][y].animationState;
                 }
             }
         }
         //if all fruits are ready - return READY
        return AnimState.READY;
        }
    //functions handling selected fruits
        private int numSelectedFruits() {
            if (sel1x == -1 && sel2x ==-1 && sel1y == -1 && sel2y == -1) {
                return 0;
            } else if (sel2x == -1 && sel2y == -1) {
                return 1;
            } else {
                return 2;
            }
        }
        private void unselectAllFruits() {
            sel1x = -1;
            sel1y = -1;
            sel2x = -1;
            sel2y = -1;
        }
    //functions for finding matched fruits
        private int sameFruitsAbove(int fruit_x, int fruit_y) {
            if (grid[fruit_x][fruit_y].imageIndex == -1) return 0;
            int l = 0;
            if (fruit_y > 0) {//if not in top row
                for (int y = fruit_y-1; y >= 1; y--) {//dont search the top row
                    if (grid[fruit_x][y].imageIndex == grid[fruit_x][fruit_y].imageIndex) {
                        l++;
                    } else {
                        break;
                    }
                }
            }
            return l;
        }
        private int sameFruitsBelow(int fruit_x, int fruit_y) {
        if (grid[fruit_x][fruit_y].imageIndex == -1) return 0;
        int l = 0;
            if (fruit_x <= (grid[0].length-1)) {//if not in bottom row
                for (int y = fruit_y+1; y < grid[0].length; y++) {
                    if (grid[fruit_x][y].imageIndex == grid[fruit_x][fruit_y].imageIndex) {
                        l++;
                    } else {
                        break;
                    }

                }
            }
            return l;
        }
        private int sameFruitsOnLeft(int fruit_x, int fruit_y) {
        if (grid[fruit_x][fruit_y].imageIndex == -1) return 0;
        int l = 0;
            if (fruit_x > 0) {//if not in  leftmost row
                for (int x = fruit_x-1; x >= 0; x--) {
                    if (grid[x][fruit_y].imageIndex == grid[fruit_x][fruit_y].imageIndex) {
                        l++;
                    } else {
                        break;
                    }
                }
            }
        return l;
        }
        private int sameFruitsOnRight(int fruit_x, int fruit_y) {
        if (grid[fruit_x][fruit_y].imageIndex == -1) return 0;
        int l = 0;
            if (fruit_x < (grid.length-1)) {//if not in rightmost row
                for (int x = fruit_x+1; x < grid.length; x++) {
                    if (grid[x][fruit_y].imageIndex == grid[fruit_x][fruit_y].imageIndex) {
                        l++;
                    } else {
                        break;
                    }
                }
            }
        return l;
        }
        private int getVerticalLineLength(int fruit_x, int fruit_y) {
            int length = 1;
            length += sameFruitsAbove(fruit_x,fruit_y);
            length += sameFruitsBelow(fruit_x,fruit_y);
            return length;//
        }
        private int getHorizontalLineLength(int fruit_x, int fruit_y) {
            int length = 1;
            length += sameFruitsOnLeft(fruit_x, fruit_y);
            length += sameFruitsOnRight(fruit_x, fruit_y);
            return length;
        }
        public void labelMatchedFruits() {//where to put it ??
            for (int x = 0; x < 10; x++) {
                for (int y = 1; y < 10; y++) {//dont check for the first row
                    grid[x][y].matchesY = getVerticalLineLength(x, y);
                    grid[x][y].matchesX = getHorizontalLineLength(x, y);
                    if (grid[x][y].matchesX>=3 || grid[x][y].matchesY>=3) {
                        grid[x][y].is_matched = true;
                    } else {
                        grid[x][y].is_matched = false;
                    }
                }
            }
        }
    //functions for swapping fruits
        private void swapValues(int x1, int y1, int x2, int y2) {
            //does not swap positions, only values
            int placeholder;
            placeholder = grid[x2][y2].imageIndex;
            grid[x2][y2].imageIndex = grid[x1][y1].imageIndex;
            grid[x1][y1].imageIndex = placeholder;
            String another = grid[x2][y2].special;
            grid[x2][y2].special = grid[x1][y1].special;
            grid[x1][y1].special = another;
        }
        private boolean selectedFruitsAreAdjacent() {
            if ((sel1x+1) == sel2x || (sel1x-1)==sel2x) {
                if (sel1y==sel2y) {
                    return true;
                }
            } else if ((sel1y+1) == sel2y || (sel1y-1)==sel2y) {
                if (sel1x==sel2x) {
                    return true;
                }
            }
            return false;
        }
        private void swapSelectedFruits() {
            //set animation destinations
            grid[sel1x][sel1y].nextX = grid[sel2x][sel2y].x;
            grid[sel1x][sel1y].nextY = grid[sel2x][sel2y].y;
            grid[sel2x][sel2y].nextX = grid[sel1x][sel1y].x;
            grid[sel2x][sel2y].nextY = grid[sel1x][sel1y].y;
            //set image destinations
            grid[sel1x][sel1y].nextImageIndex = grid[sel2x][sel2y].imageIndex;
            grid[sel2x][sel2y].nextImageIndex = grid[sel1x][sel1y].imageIndex;

            //init animation
            grid[sel1x][sel1y].isAnimated = true;
            grid[sel2x][sel2y].isAnimated = true;
            if (grid[sel2x][sel2y].animationState == AnimState.READY) {
                //remember position before animation
                grid[sel2x][sel2y].prevX = grid[sel2x][sel2y].x;
                grid[sel1x][sel1y].prevY = grid[sel1x][sel1y].y;

                grid[sel2x][sel2y].animationState = AnimState.SWAPPING;
                grid[sel1x][sel1y].animationState = AnimState.SWAPPING;
            } else if (grid[sel2x][sel2y].animationState == AnimState.SWAPPED) {
                System.out.println("Reswapping");
                grid[sel2x][sel2y].animationState = AnimState.RESWAPPING;
                grid[sel1x][sel1y].animationState = AnimState.RESWAPPING;
            }
        }
        public boolean handleSwapping(MouseEvent e, float zoom) {
            //returns true if a move is made
            int mx = (int) (e.getX()/zoom);
            int my = (int) (e.getY()/zoom);
            animationState = getAnimationState();
            boolean mxInGrid = GRID_OFFSET_X <mx && mx <GRID_OFFSET_X+10*SLOT_SPAN;
            boolean myInGrid = GRID_OFFSET_Y+SLOT_SPAN < my && my <GRID_OFFSET_Y+10*SLOT_SPAN;
            //                                 ^ fruits in top row are untouchable
            if (mxInGrid && myInGrid && animationState == AnimState.READY) {
                //handle selecting fruits
                if (numSelectedFruits() == 0) {
                    sel1x = screenToGridX(mx);
                    sel1y = screenToGridY(my);
                } else if (numSelectedFruits() == 1) {
                    int newx = screenToGridX(mx);
                    int newy = screenToGridY(my);
                    if (!(newx == sel1x && newy == sel1y)) {//if the second fruit is not the same as the first
                        sel2x = newx;
                        sel2y = newy;
                    }
                    if (selectedFruitsAreAdjacent()) {
                        swapSelectedFruits();
                        return true;
                    } else {
                        unselectAllFruits();
                    }
                    //unselectAllFruits();
                } else {
                    System.out.println("Error: wrong selection order.");
                    System.out.println("sel1x: "+sel1x+" sel1y: "+sel1y+" sel2x: "+sel2x+" sel2y: "+sel2y);
                    //unselectAllFruits();
                }
            }
            return false;
        }
        public void finishSwapping() {
            if (grid[sel1x][sel1y].animationState == AnimState.SWAPPED
                    && grid[sel2x][sel2y].animationState == AnimState.SWAPPED) {
                //After the fruits are swapped for the fist time
                labelMatchedFruits();//checks matches for each fruit
                if (grid[sel1x][sel1y].is_matched || grid[sel2x][sel2y].is_matched) {//if matched

                    //important - swap specials
                    String special_holder = grid[sel1x][sel1y].special;
                    grid[sel1x][sel1y].special = grid[sel2x][sel2y].special;
                    grid[sel2x][sel2y].special = special_holder;

                    grid[sel1x][sel1y].animationState = AnimState.READY;
                    grid[sel2x][sel2y].animationState = AnimState.READY;
                    beginExplodingAnimation();
                    unselectAllFruits();
                    //labelFallingFruits();
                    //beginFallingAnimation();
                    //now the program is ready for selecting new fruits
                } else {
                    swapSelectedFruits();//start reswapping
                    grid[sel1x][sel1y].animationState = AnimState.RESWAPPING;
                    grid[sel2x][sel2y].animationState = AnimState.RESWAPPING;
                    //Reswapping that starts here, ends in update function
                    //when fruits reach desired positions
                }
            } else if (grid[sel1x][sel1y].animationState == AnimState.RESWAPPED) {
                //disables reswapping after reswapping
                grid[sel1x][sel1y].animationState = AnimState.READY;
                grid[sel2x][sel2y].animationState = AnimState.READY;
                unselectAllFruits();
            }
        }
    //functions for exploding
        public void assingnSpecial(int x, int y) {
            //special will be assigned only at the end of animation
            if ((grid[x][y].matchesX >= 5 && sameFruitsOnRight(x,y) == 2)
                    || (grid[x][y].matchesY >= 5 && sameFruitsBelow(x,y) == 2)) {
                grid[x][y].special = "flower";
            }
            if (grid[x][y].matchesX >= 3 && grid[x][y].matchesY >= 3) {
                grid[x][y].special = "bomb";
            }
            else if ((grid[x][y].matchesY == 4) && sameFruitsBelow(x,y) == 3) {
                grid[x][y].special = "vertical";
            } else if ((grid[x][y].matchesX == 4) && sameFruitsOnRight(x,y) == 1) {
                grid[x][y].special = "horizontal";
            }
        }
        public boolean explodeSpecial(String name,int special_x, int special_y, int iteration) {
            System.out.println("exploding: "+name+"x:" +special_x + " y: " + special_y);
            System.out.println("animState: "+grid[special_x][special_y].animationState);
            //todo: invent a system of chain reactions that does not cause infinite loops
            //problem: bombs exploded by bombs do not explode other fruits
            switch (name) {
                case "flower":
                    for (int x=0;x<10;x++) {
                        for (int y=0;y<10;y++) {
                            if (grid[x][y].imageIndex == grid[special_x][special_y].imageIndex) {
                                if (grid[x][y].special == "none") explodeAFruit(x,y, iteration);
                            }
                        }
                    }
                    break;
                    case  "bomb"://todo:bomb is not working properly
                        for (int x=special_x-1;x<=(special_x+1);x++) {
                            for (int y=special_y-1;y<=(special_y+1);y++) {
                                if (0<=x && x< 10 && 0<=y && y < 10) {
                                    if (grid[x][y].special == "none") explodeAFruit(x,y, iteration);
                                }
                                try {
                                    if (grid[x][y].special == "none") explodeAFruit(x,y, iteration);
                                } catch (Exception e) {
                                    System.out.println(e);
                                }

                            }
                        }
                        break;
                    case "vertical":
                        for (int y=0; y<=9; y++) {
                            if (grid[special_x][y].special == "none") explodeAFruit(special_x,y, iteration);
                        }
                        break;

                    case "horizontal":
                        for (int x=0;x<=9;x++) {
                            if (grid[x][special_y].special == "none") explodeAFruit(x,special_y, iteration);
                        }
                        break;

            }
            return true;
        }
        public void finishExploding() {
            for (int x=0;x<10;x++) {
                for (int y=0;y<10;y++) {
                    if (grid[x][y].animationState == AnimState.EXPLODING ) {
                        //System.out.println("detected unfinished exploding animation");
                    }
                    if (grid[x][y].animationState == AnimState.EXPLODED) {
                        if (grid[x][y].deleteSpecialAfterExploding) grid[x][y].special = "none";//?
                        grid[x][y].isAnimated = false;
                        grid[x][y].animationState = AnimState.READY;
                    }
                }
            }
        }
        public boolean explodeAFruit(int x, int y, int iteration) {
            if (grid[x][y].animationState != AnimState.READY) {
                return false;
            }
            if (grid[x][y].special == "none") {
                assingnSpecial(x, y);
                grid[x][y].isAnimated = true;
                grid[x][y].animationState = AnimState.EXPLODING;
                if (grid[x][y].special == "none") {
                    grid[x][y].nextImageIndex = -1;
                } else {
                    grid[x][y].nextImageIndex = grid[x][y].imageIndex;
                }
                grid[x][y].deleteSpecialAfterExploding = false;//because we are creating a new special

            }
            else {
                grid[x][y].isAnimated = true;
                grid[x][y].animationState = AnimState.EXPLODING;
                grid[x][y].nextImageIndex = -1;
                grid[x][y].deleteSpecialAfterExploding = true;//because we are using up an existing special
                explodeSpecial(grid[x][y].special,x,y,0);

            }
            return true;
        }
        public void beginExplodingAnimation() {
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (grid[x][y].is_matched && grid[x][y].imageIndex > -1) {
                        explodeAFruit(x,y,0);
                    }
                }
            }
            //in case upper fruits were destroyed
            spawnNewFruits();
        }
    //falling functions
        public void labelFallingFruits() {
            //sets animation state to falling for each fruit that has empty space below
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 9; y++) {
                    if (grid[x][y+1].imageIndex == -1 && grid[x][y].imageIndex != -1
                            && grid[x][y+1].animationState == AnimState.READY && grid[x][y].isAnimated == false) {
                        //if this is an unempty fruit above an empty place
                        grid[x][y].animationState = AnimState.FALLING;
                        //stop all animations of this fruit
                        grid[x][y].width = 40;
                        grid[x][y].height = 40;
                        grid[x][y].x = gridToScreenX(x);
                        grid[x][y].y= gridToScreenY(y);
                        for (int y2 = 0; y2 < y; y2++) {
                            //not working when another fruit is falling exacly two spaces above or below
                            if (grid[x][y2].animationState ==AnimState.READY) {
                                grid[x][y2].animationState = AnimState.FALLING;

                            }
                        }
                    }
                }
            }

        }
        public void finishFallingAnimations() {
            for (int x=0; x<10; x++) {
                for (int y=9; y>=0; y--) {
                    //begin at the bottom and go up
                    if (grid[x][y].animationState ==AnimState.FALLEN) {
                        //System.out.println(x + " " + y);
                        //first-swap values
                        swapValues(x,y,x,y+1);
                        int placeholder;
                        //then reset above slot to previous position
                        grid[x][y].y = gridToScreenY(y);
                        grid[x][y].isAnimated = false;
                        grid[x][y].nextY = grid[x][y].y;
                        grid[x][y].animationState = AnimState.READY;
                    }
                }
            }
        }
        public void beginFallingAnimation() {
            for (int x=0; x<10; x++) {
                for (int y=0; y<10; y++) {
                    if (grid[x][y].animationState == AnimState.FALLING && !grid[x][y].isAnimated) {
                        grid[x][y].isAnimated = true;
                        grid[x][y].nextX = grid[x][y].x;
                        grid[x][y].nextY = grid[x][y].y + SLOT_SPAN;
                    }
                }
            }
        }
    //spawning new fruits
        public void spawnNewFruits() {
            for (int x=0; x<10; x++) {
                if (grid[x][0].imageIndex == -1) {
                    grid[x][0] = new Owoc(gridToScreenX(x), gridToScreenY(0));
                }
            }
        }
    //important functions
    public void update(int timer_step) {
        particleSource.update();
        boolean isAnyAnimated = false;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (grid[x][y].isAnimated) {
                    grid[x][y].updateAnimation(timer_step);
                    isAnyAnimated = true;
                }
            }
        }
        animationState = getAnimationState();
        finishExploding();
        if (numSelectedFruits() == 2 && isAnyAnimated==false) {
            //if animation is not running, but fruits are still selected
            finishSwapping();
        }
        if (animationState == AnimState.EXPLODED) {
            finishExploding();
            labelFallingFruits();
            beginFallingAnimation();
        }
        if (animationState==AnimState.FALLEN) {
            finishFallingAnimations();//make animation smoother (optional) - because now fruit wait a little after each falling step
            spawnNewFruits();
            labelFallingFruits();
            beginFallingAnimation();
            //if no new fruits are falling
            animationState = getAnimationState();
            if (animationState==AnimState.READY) {
                labelMatchedFruits();
                beginExplodingAnimation();
                labelFallingFruits();
                beginFallingAnimation();
                //will it work two times in a row?
            }
        }
        labelMatchedFruits();
    }
    public void displayFruitDebugInfo(int x, int y, Graphics g) {
        //for debugging
        //                if (grid[x][y].is_matched) {
//                    g.setColor(Color.black);
//                    //match indication for testing purposes
//                    g.fillOval(gridToScreenX(x)+13, gridToScreenY(y)+14, SLOT_SPAN/4, SLOT_SPAN/4);
//                }
//                if (grid[x][y].animationState == AnimState.FALLING) {
//                    g.setColor(Color.pink);
//                    g.fillOval(gridToScreenX(x)+16, gridToScreenY(y)+14, SLOT_SPAN/4, SLOT_SPAN/4);
//                }
        g.drawString(grid[x][y].animationState + "", grid[x][y].x, grid[x][y].y);
        g.setFont(new Font("Comic Sans", Font.BOLD, 8));
        g.drawString(x + "," + y, grid[x][y].x, grid[x][y].y+6);
    }
    public void paintGrid(Graphics g) {
        particleSource.draw(g);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                grid[x][y].paintOwoc(g);
                if ((sel1x==x && sel1y==y) || (sel2x==x && sel2y==y)) {
                    g.setColor(Color.black);
                    g.drawRect(gridToScreenX(x), gridToScreenY(y), SLOT_SPAN, SLOT_SPAN);
                }
                displayFruitDebugInfo(x,y,g);
            }
        }
        g.setColor(Color.decode("0xEEEEEE"));
        g.fillRect(gridToScreenX(0),gridToScreenY(0),SLOT_SPAN*10,SLOT_SPAN);
    }
}