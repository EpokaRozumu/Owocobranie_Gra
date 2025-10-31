import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//import java.util.ArrayList;
//this is a comment
public class Grid {
    //enum SwapState {READY,SWAPPING, SWAPPED, RESWAPPED, RESWAPPING};
    //czy to na pewno najlepszy sposób przechowywania informacji o zaznaczonych owocach??
    int sel1x, sel1y, sel2x, sel2y;
    static final int SLOT_SPAN = 40;
    static final int GRID_OFFSET_X = 200;
    static final int GRID_OFFSET_Y = 100;

    Owoc[][] grid = new Owoc[10][10];

    public Grid() {
        Owoc.loadImages();
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                grid[x][y] = new Owoc(gridToScreenX(x), gridToScreenY(y));
            }
        }
        unselectAllFruits();
    }

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
    private int numSelectedFruits() {
        if (sel1x == -1 && sel2x ==-1 && sel1y == -1 && sel2y == -1) {
            return 0;
        } else if (sel2x == -1 && sel2y == -1) {
            return 1;
        } else {
            return 2;
        }
    }
    private int getVerticalLineLength(int fruit_x, int fruit_y) {
        int length = 1;
        //search upwards
        if (fruit_y > 0) {//if not in top row
            for (int y = fruit_y-1; y >= 0; y--) {
                if (grid[fruit_x][y].imageIndex == grid[fruit_x][fruit_y].imageIndex) {
                    length++;
                } else {
                    break;
                }
            }
        }
        //search downwards
        if (fruit_x <= (grid[0].length-1)) {//if not in bottom row
            for (int y = fruit_y+1; y < grid[0].length; y++) {
                if (grid[fruit_x][y].imageIndex == grid[fruit_x][fruit_y].imageIndex) {
                    length++;
                } else {
                    break;
                }
            }
        }

        return length;//
    }
    private int getHorizontalLineLength(int fruit_x, int fruit_y) {
        int length = 1;
        //search to the left
        if (fruit_x > 0) {//if not in  leftmost row
            for (int x = fruit_x-1; x >= 0; x--) {
                if (grid[x][fruit_y].imageIndex == grid[fruit_x][fruit_y].imageIndex) {
                    length++;
                } else {
                    break;
                }
            }
        }
        //search downwards
        if (fruit_x < (grid.length-1)) {//if not in rightmost row
            for (int x = fruit_x+1; x < grid.length; x++) {
                if (grid[x][fruit_y].imageIndex == grid[fruit_x][fruit_y].imageIndex) {
                    length++;
                } else {
                    break;
                }
            }
        }

        return length;
    }
    private void unselectAllFruits() {
        sel1x = -1;
        sel1y = -1;
        sel2x = -1;
        sel2y = -1;
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
        if (grid[sel2x][sel2y].animationState == "READY") {
            grid[sel2x][sel2y].animationState = "SWAPPING";
            grid[sel1x][sel1y].animationState ="SWAPPING";
        } else if (grid[sel2x][sel2y].animationState == "SWAPPED") {
            System.out.println("Reswapping");
            grid[sel2x][sel2y].animationState = "RESWAPPING";
            grid[sel1x][sel1y].animationState = "RESWAPPING";
        }


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
    public void handleSwapping(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if ( GRID_OFFSET_X <mx && mx <GRID_OFFSET_X+10*SLOT_SPAN
                && GRID_OFFSET_Y < my && my <GRID_OFFSET_Y+10*SLOT_SPAN) {
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
    }
    public void handleMatching() {//where to put it ??
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
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
    public void explodeMatchedFruits() {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (grid[x][y].is_matched) {
                    grid[x][y].imageIndex = -1;
                }
            }
        }
    }
    public void handleFalling(int x, int y) {
        if (grid[x][y+1].imageIndex == -1) {

        }
    }
    public void handleSwappedFruits() {
        if (grid[sel1x][sel1y].animationState == "SWAPPED"&& grid[sel2x][sel2y].animationState == "SWAPPED") {
            //After the fruits are swapped for the fist time
            handleMatching();//checks matches for each fruit
            if (grid[sel1x][sel1y].is_matched || grid[sel2x][sel2y].is_matched) {//if matched
                grid[sel1x][sel1y].animationState = "READY";
                grid[sel2x][sel2y].animationState = "READY";
                //explodeMatchedFruits();//todo: uncomment
                unselectAllFruits();
                //now the program is ready for selecting new fruits
            } else {
                swapSelectedFruits();//start reswapping
                grid[sel1x][sel1y].animationState = "RESWAPPED";
                grid[sel2x][sel2y].animationState = "RESWAPPED";
                //Reswapping that starts here, ends in updateAnimation function
                //when fruits reach desired positions
            }
        } else if (grid[sel1x][sel1y].animationState == "RESWAPPED") {
            //disables reswapping after reswapping
            grid[sel1x][sel1y].animationState = "READY";
            grid[sel2x][sel2y].animationState = "READY";
            unselectAllFruits();
        }
    }
    public void updateAnimation(int timer_step) {
        boolean isAnyAnimated = false;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (grid[x][y].isAnimated) {
                    grid[x][y].updateAnimation(timer_step);
                    isAnyAnimated = true;
                }
            }
        }
        if (numSelectedFruits() == 2 && isAnyAnimated==false) {
            //if animation is not running, but fruits are still selected
            handleSwappedFruits();
        }
    }
    public void paintGrid(Graphics g) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                grid[x][y].paintOwoc(g);
                if ((sel1x==x && sel1y==y) || (sel2x==x && sel2y==y)) {
                    g.setColor(Color.black);
                    g.drawRect(gridToScreenX(x), gridToScreenY(y), SLOT_SPAN, SLOT_SPAN);
                }
                if (grid[x][y].is_matched) {
                    g.setColor(Color.black);
                    //match indication for testing purposes
                    g.fillOval(gridToScreenX(x)+14, gridToScreenY(y)+14, SLOT_SPAN/4, SLOT_SPAN/4);
                }
            }
        }
    }
}