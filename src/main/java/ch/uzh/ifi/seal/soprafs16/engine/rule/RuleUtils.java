package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by soyabeen on 27.04.16.
 */
public class RuleUtils {

    private static final Logger logger = LoggerFactory.getLogger(RuleUtils.class);

    private RuleUtils() {
        // since this is a utility class
    }

    public static boolean isOnSameLevel(Player player, Positionable.Level level) {
        boolean res = level == player.getLevel();
        if (!res) {
            logger.debug("Player {} ({},{}) has a different level than {}.",
                    player, player.getCar(), player.getLevel(), level);
        }
        return res;
    }

    public static boolean isPlacedOnTrain(Player player, int train) {
        boolean res = 0 <= player.getCar() && player.getCar() < train;
        if (!res) {
            logger.debug("Simulated player {} ({},{}) is not positioned on the train with length {}.",
                    player, player.getCar(), player.getLevel(), train);
        }
        return res;
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
