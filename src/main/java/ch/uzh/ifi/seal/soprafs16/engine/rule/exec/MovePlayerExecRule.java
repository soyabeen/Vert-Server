package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by soyabeen on 20.04.16.
 */
public class MovePlayerExecRule implements ExecutionRule {

    private static final Logger logger = LoggerFactory.getLogger(MovePlayerExecRule.class);

    @Override
    public boolean evaluate(ActionCommand command) {
        if (!(command.getTargetPlayer() instanceof Player)) {
            return false;
        }
        Player targetPlayer = command.getTargetPlayer();
        if (!(Positionable.Level.BOTTOM == targetPlayer.getLevel() ||
                Positionable.Level.TOP == targetPlayer.getLevel())) {
            logger.debug("Player {} ({},{}) can't move horizontally on lower level, because he has an invalid level type!",
                    targetPlayer, targetPlayer.getCar(), targetPlayer.getLevel());
            return false;
        }
        if (targetPlayer.getCar() < 0 || targetPlayer.getCar() > command.getGame().getNrOfCars()) {
            logger.debug("Player {} ({},{}) can't move horizontally on lower level, because he is not positioned on the train!",
                    targetPlayer, targetPlayer.getCar(), targetPlayer.getLevel());
            return false;
        }
        return true;
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {


        Player newState = command.getCurrentPlayer();
        newState.setCar(command.getTargetPlayer().getCar());

        return null;
    }

}
