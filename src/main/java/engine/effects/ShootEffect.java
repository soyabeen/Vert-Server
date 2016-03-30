package engine.effects;


import engine.User;

/**
 * Created by alex on 19.03.2016.
 */
public class ShootEffect implements Effect {
    private User owner;
    private User target;

    public ShootEffect(User owner, User target) {
        this.owner = owner;
        this.target = target;
    }

    public void execute() {
        target.getShot();
        owner.shoot();
    }
}
