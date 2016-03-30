package engine;

/**
 * Created by alex on 19.03.2016.
 */
public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Position move(int x, int y) {
        this.x += x;
        this.y += y;

        return this;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
