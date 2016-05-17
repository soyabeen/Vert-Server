package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.constant.Distance;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.RuleUtils;
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

    private boolean isInAllowedRange(Positionable current, Positionable target) {
        boolean res;
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
        return RuleUtils.isOnSameLevel(command.getTargetPlayer(), command.getCurrentPlayer().getLevel())
                && RuleUtils.isPlacedOnTrain(command.getTargetPlayer(), command.getGame().getNrOfCars())
                && isInAllowedRange(command.getCurrentPlayer(), command.getTargetPlayer());
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        List<Positionable> result = new ArrayList<>();

        if (evaluate(command)) {
            Positionable newState = command.getCurrentPlayer();
            newState.setCar(command.getTargetPlayer().getCar());
            result.add(newState);
        }

        return result;
    }

}
