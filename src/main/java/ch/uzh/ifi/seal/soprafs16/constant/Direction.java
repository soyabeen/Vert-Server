package ch.uzh.ifi.seal.soprafs16.constant;

/**
 * Created by soyabeen on 20.04.16.
 */
public enum Direction {
    TO_HEAD(-1),
    TO_TAIL(1);

    private final int direction;

    Direction(int direction) {
        this.direction = direction;
    }

    public int intValue() {
        return direction;
    }
}
