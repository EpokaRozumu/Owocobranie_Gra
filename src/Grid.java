import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Grid {

    //czy to na pewno najlepszy sposób przechowywania informacji o zaznaczonych owocach??
    int sel1x, sel1y, sel2x, sel2y;
    static final int SLOT_SPAN = 40;
    static final int GRID_OFFSET_X = 200;
    static final int GRID_OFFSET_Y = 100;

    Owoc[][] grid = new Owoc[10][10];

    public Grid() {
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
                if (grid[fruit_x][y].kolor == grid[fruit_x][fruit_y].kolor) {
                    length++;
                } else {
                    break;
                }
            }
        }
        //search downwards
        if (fruit_x <= (grid[0].length-1)) {//if not in bottom row
            for (int y = fruit_y+1; y < grid[0].length; y++) {
                if (grid[fruit_x][y].kolor == grid[fruit_x][fruit_y].kolor) {
                    length++;
                } else {
                    break;
                }
            }
        }

        return length;
    }
    private int getHorizontalLineLength(int fruit_x, int fruit_y) {
        int length = 1;
        //search to the left
        if (fruit_x > 0) {//if not in  leftmost row
            for (int x = fruit_x-1; x >= 0; x--) {
                if (grid[x][fruit_y].kolor == grid[fruit_x][fruit_y].kolor) {
                    length++;
                } else {
                    break;
                }
            }
        }
        //search downwards
        if (fruit_x < (grid.length-1)) {//if not in rightmost row
            for (int x = fruit_x+1; x < grid.length; x++) {
                if (grid[x][fruit_y].kolor == grid[fruit_x][fruit_y].kolor) {
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
        //set color destinations
        grid[sel1x][sel1y].nextKolor = grid[sel2x][sel2y].kolor;
        grid[sel2x][sel2y].nextKolor = grid[sel1x][sel1y].kolor;
        //System.out.println("Owoc1: x: "+sel1x+ " y: "  +sel1y+ "isAnimated: " + grid[sel1x][sel1y].isAnimated +"   Owoc2 : "+sel2x+" y: " +sel2y);
        //init animation
        grid[sel1x][sel1y].isAnimated = true;
        grid[sel2x][sel2y].isAnimated = true;
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
    public boolean handleSwapping(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        if ( GRID_OFFSET_X <mx && mx <GRID_OFFSET_X+10*SLOT_SPAN
            && GRID_OFFSET_Y < my && my <GRID_OFFSET_Y+10*SLOT_SPAN) {

            if (numSelectedFruits() == 0) {
                sel1x = screenToGridX(mx);
                sel1y = screenToGridY(my);
            } else if (numSelectedFruits() == 1) {
                sel2x = screenToGridX(mx);
                sel2y = screenToGridY(my);
                if (selectedFruitsAreAdjacent()) {
                    swapSelectedFruits();
                }
                //unselectAllFruits();
                return true;

            } else {
                System.out.println("Error: wrong selection order.");
                System.out.println("sel1x: "+sel1x+" sel1y: "+sel1y+" sel2x: "+sel2x+" sel2y: "+sel2y);
            }
        }

        return false;

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
    public void updateAnimation(int timer_step) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (grid[x][y].isAnimated) {
                    grid[x][y].updateAnimation(timer_step);
                } else if (numSelectedFruits() == 2){//if animation is not running, but fruits are still selected
//                    //swap positions and colors
//                    //positions
//                    int tempx = grid[sel1x][sel1y].x;
//                    int tempy = grid[sel1x][sel1y].y;
//
//                    grid[sel1x][sel1y].x = grid[sel2x][sel2y].x;
//                    grid[sel1x][sel1y].y = grid[sel2x][sel2y].y;
//
//                    grid[sel2x][sel2y].x = tempx;
//                    grid[sel2x][sel2y].y = tempy;
//
//                    //colors
//                    Color tempColor;
//                    tempColor = grid[sel2x][sel2y].kolor;
//                    grid[sel2x][sel2y].kolor = grid[sel1x][sel1y].kolor;//kolor 2 <= kolor 1
//                    grid[sel1x][sel1y].kolor = tempColor;
//                    //only then
                    unselectAllFruits();
                }
            }

        }
    }
    public void paintGrid(Graphics g) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                grid[x][y].paintOwoc(g);
                //g.drawString(x+""+y,gridToScreenX(x),gridToScreenY(y));
                //g.drawString(grid[x][y].matchesY+"",gridToScreenX(x),gridToScreenY(y));
                //g.drawString(grid[x][y].matchesX+"",gridToScreenX(x),gridToScreenY(y));
                if ((sel1x==x && sel1y==y) || (sel2x==x && sel2y==y)) {
                    g.setColor(Color.black);
                    g.drawRect(gridToScreenX(x), gridToScreenY(y), SLOT_SPAN, SLOT_SPAN);
                }
                if (grid[x][y].is_matched) {
                    g.setColor(Color.black);
                    g.fillOval(gridToScreenX(x)+10, gridToScreenY(y)+10, SLOT_SPAN/4, SLOT_SPAN/4);
                }
            }
        }
        if (sel1x != -1 && sel1y != -1) {
            g.drawString("selected1: " + grid[sel1x][sel1y].kolor, 100, 10);
        }
        if (sel2x != -1 && sel2y != -1) {
            g.drawString("selected2: " + grid[sel2x][sel2y].kolor,100,20);
        } else {
            g.drawString("selected2: none", 100, 20);
        }


    }
}
