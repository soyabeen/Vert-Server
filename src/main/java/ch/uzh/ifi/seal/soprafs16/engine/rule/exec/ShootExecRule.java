package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class ShootExecRule implements ExecutionRule {

    private static final Logger logger = LoggerFactory.getLogger(ShootExecRule.class);

    public boolean hasBulletLeft(Player player) {
        boolean res = player.getBullets() > 0;
        if (!res) {
            logger.debug("Player {} has no bullets left.", player);
        }
        return res;
    }

    @Override
    public boolean evaluate(ActionCommand command) {
        return hasBulletLeft(command.getCurrentPlayer());
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        List<Positionable> result = new ArrayList<>();
        Player actor = command.getCurrentPlayer();
        actor.shoots();
        result.add(actor);

        Player target = command.getTargetPlayer();
        target.getsShotBy(actor);
        result.add(target);

        return result;
    }
}
