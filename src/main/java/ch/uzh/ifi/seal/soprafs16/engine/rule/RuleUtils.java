package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;

/**
 * Created by soyabeen on 27.04.16.
 */
public class RuleUtils {

    private RuleUtils() {
        // since this is a utility class
    }

    public static Positionable swapLevel(Positionable actor) {
        if (Positionable.Level.TOP == actor.getLevel()) {
            actor.setLevel(Positionable.Level.BOTTOM);
        } else {
            actor.setLevel(Positionable.Level.TOP);
        }
        return actor;
    }
}
