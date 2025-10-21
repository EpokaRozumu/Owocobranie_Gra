import java.awt.*;

public class RedSquare {
    private double xPos = 50;
    private double yPos = 50;
    private int width = 10;
    private int height = 10;



    private double nextX = xPos;
    private double nextY = yPos;
    boolean isAnimated = false;

    public void setX(int xPos) {
        this.xPos = xPos;
    }
    public int getX() {
        return (int) xPos;
    }
    public void setY(int yPos) {
        this.yPos = yPos;
    }
    public int getY() {
        return (int) yPos;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public void paintSquare(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(getX(), getY(), width, height);
        g.setColor(Color.black);
        g.drawRect(getX(), getY(), width, height);
    }
    public boolean moveSquare(int x, int y) {
        final int CURR_X = getX();
        final int CURR_Y = getY();
        final int CURR_W = getWidth();
        final int CURR_H = getHeight();
        final int OFFSET = 50;
        if ((CURR_X!=x) || (CURR_Y!=y)) {
            //setX(x);
            //setY(y);
            nextX = x;
            nextY = y;
            return true;
        }
        return false;
    }
    public boolean updateAnimation() {
        if (nextY == yPos && nextX == xPos) {
            isAnimated = false;
            return false;

        } else {
            final int SPEED = 15;
            double dx = nextX - xPos;
            double dy = nextY - yPos;
            double length = Math.sqrt(dx*dx + dy*dy);

            if (length < SPEED) {
                xPos = nextX;
                yPos = nextY;
                isAnimated = false;
                return false;
            } else {
                //by Kuba Brzozowski:
                dx = SPEED*dx/length;
                dy = SPEED*dy/length;
                xPos += dx;
                yPos += dy;
                return true;
            }
        }
    }

}
