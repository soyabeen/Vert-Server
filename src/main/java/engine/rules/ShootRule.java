package engine.rules;

import engine.User;

/**
 * Created by alex on 19.03.2016.
 */
public class ShootRule implements IRule {

    private User owner;
    private User target;

    public ShootRule(User owner, User target) {
        this.owner = owner;
        this.target = target;
    }

    @Override
    public boolean isValid() {
        return isOnSameLevel() && hasBullets() && isInRange();
    }

    private boolean isOnSameLevel() {
        return owner.getPosition().getY() == target.getPosition().getY();
    }

    private boolean hasBullets() {
        return owner.getBulletsLeft() > 0;
    }

    private boolean isInRange() {
        if (isOnRoof()) {
            return !isOnSameCar();
        } else
            return isOnNextCar();
    }

    private boolean isOnRoof() {
        return owner.getPosition().getY() == 1;
    }

    private boolean isOnSameCar() {
        int distance = getDistanceBetweenTargetAndUser();
        return distance == 0;
    }

    private boolean isOnNextCar() {
        int distance = getDistanceBetweenTargetAndUser();
        return distance == 1;
    }

    private int getDistanceBetweenTargetAndUser() {
        int ownerX = owner.getPosition().getX();
        int targetX = target.getPosition().getY();

        return Math.abs(ownerX - targetX);
    }
}
