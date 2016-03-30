package engine.effects;


import engine.User;

/**
 * Created by alexanderhofmann on 20/03/16.
 */
public class PunchEffect implements Effect {
    private User target;

    public PunchEffect(User target) {
        this.target = target;
    }

    @Override
    public void execute() {
        //target loose loot
    }
}
