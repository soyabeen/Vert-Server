package engine.rules;


import engine.User;

/**
 * Created by alexanderhofmann on 20/03/16.
 */
public class PunchRule implements IRule {
    private User owner;
    private User target;


    public PunchRule(User owner) {
        this.owner = owner;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    @Override
    public boolean isValid() {
        return isInRange();
    }

    private boolean isInRange() {
        return isOnSameCar() && isOnSameLevel();
    }

    private boolean isOnSameLevel() {
        return owner.getPosition().getY() == target.getPosition().getY();
    }

    private boolean isOnSameCar() {
        int distance = getDistanceBetweenTargetAndUser();
        return distance == 0;
    }

    private int getDistanceBetweenTargetAndUser() {
        int ownerX = owner.getPosition().getX();
        int targetX = target.getPosition().getY();

        return Math.abs(ownerX - targetX);
    }
}
