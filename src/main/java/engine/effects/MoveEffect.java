package engine.effects;

import engine.Position;
import engine.User;
/**
 * Created by alex on 19.03.2016.
 */
public class MoveEffect implements Effect {
    private User target;
    private boolean directionRight;
    private int distance;

    public MoveEffect(User target) {
        this.target = target;
    }

    public void setDirectionRight(boolean directionRight) {
        this.directionRight = directionRight;
    }

    public void setdistance(int setdistance) {
        this.distance = setdistance;
    }

    public void execute() {
        Position newPosition = target.getPosition();
        if (directionRight) {
            newPosition.move(distance, 0);
        } else {
            newPosition.move(-distance, 0);
        }
        target.move(newPosition);


    }
}
