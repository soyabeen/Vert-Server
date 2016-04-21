package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.constant.Distance;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 20.04.16.
 */
public class MovePlayerExecRule implements ExecutionRule {

    private static final Logger logger = LoggerFactory.getLogger(MovePlayerExecRule.class);

    private boolean isOnSameLevel(Player player, Positionable.Level level) {
        boolean res = level == player.getLevel();
        if (!res) {
            logger.debug("Player {} ({},{}) has a different level than {}.",
                    player, player.getCar(), player.getLevel(), level);
        }
        return res;
    }

    private boolean isPlacedOnTrain(Player player, int train) {
        boolean res = 0 <= player.getCar() && player.getCar() < train;
        if (!res) {
            logger.debug("Simulated player {} ({},{}) is not positioned on the train with length {}.",
                    player, player.getCar(), player.getLevel(), train);
        }
        return res;
    }

    private boolean isInAllowedRange(Player current, Player target) {
        boolean res = false;
        int distance = Math.abs(current.getCar() - target.getCar());
        if (current.getLevel() == Positionable.Level.TOP) {
            res = distance <= Distance.DISTANCE_TO_MOVE_ON_TOP;
        } else {
            res = distance <= Distance.DISTANCE_TO_MOVE_ON_BOTTOM;
        }
        if (!res) {
            logger.debug("Simulated player {} is not positioned in allowed range of original player {}.",
                    target, current);
        }
        return res;
    }

    @Override
    public boolean evaluate(ActionCommand command) {
        return isOnSameLevel(command.getTargetPlayer(), command.getCurrentPlayer().getLevel())
                && isPlacedOnTrain(command.getTargetPlayer(), command.getGame().getNrOfCars())
                && isInAllowedRange(command.getCurrentPlayer(), command.getTargetPlayer());
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {

        Player newState = command.getCurrentPlayer();
        newState.setCar(command.getTargetPlayer().getCar());

        List<Positionable> result = new ArrayList<>();
        result.add(newState);

        return result;
    }

}
