package engine.rules;


import engine.User;
import engine.effects.ShootEffect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by alex on 19.03.2016.
 */
public class ShootRuleEngine implements RuleEngineFactory {
    private static final Logger log = LoggerFactory.getLogger(ShootRuleEngine.class.getName());
    private User owner;
    private User target;

    public ShootRuleEngine(User owner) {
        this.owner = owner;
    }

    public boolean isValid() {
        target = new User("Player 2");
        return new ShootRule(owner, target).isValid();
    }

    public void execute() {
        if (isValid()) {
            new ShootEffect(owner, target).execute();
        } else {
            log.error("Invalid move\nPassing turn");
        }
    }
}
