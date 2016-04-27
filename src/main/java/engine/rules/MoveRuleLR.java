package engine.rules;


import engine.Position;
import engine.User;

/**
 * Created by alex on 19.03.2016.
 */
public class MoveRuleLR implements IRule {

    private static final int MAX_RANGE_ON_ROOF = 3;
    private static final int MIN_RANGE = 1;
    private static final int DEFAULT_MOVEMENT = 1;

    private User owner;
    private Position newPosition;
    private boolean directionRight;
    private int distance;

    public MoveRuleLR(User owner) {
        this.owner = owner;
    }

    public MoveRuleLR(User user, Position newPosition) {
        this.owner = user;
        this.newPosition = newPosition;
        this.distance = DEFAULT_MOVEMENT;
    }

    public void setDirectionRight(boolean directionRight) {
        this.directionRight = directionRight;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void updateNewPosition() {
        if (!directionRight) {
            distance *= -1;
        }

        newPosition = owner.getPosition().move(distance, 0);
    }

    @Override
    public boolean isValid() {
        return isPositive() && isInRange();
    }

    private boolean isPositive() {
        return newPosition.getX() > 0 || newPosition.getY() > 0;
    }

    private boolean isInRange() {

        int horizontalMovement = Math.abs(newPosition.getX() - owner.getPosition().getX());

        if (isOnRoof()) {
            return horizontalMovement <= MAX_RANGE_ON_ROOF;
        } else
            return horizontalMovement == MIN_RANGE;
    }

    private boolean isOnRoof() {
        return owner.getPosition().getY() > 0;
    }
}
