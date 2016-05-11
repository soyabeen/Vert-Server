package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class RobberyExecRule implements ExecutionRule {

    private static final Logger logger = LoggerFactory.getLogger(RobberyExecRule.class);

    private Loot getExistingFreeLoot(Long lootId, List<Loot> loots) {
        for (Loot l : loots) {
            if (lootId == l.getId()) {
                return l;
            }
        }
        throw new IllegalArgumentException("Unknown loot id " + lootId + "can't be picked up.");
    }

    private boolean isExistingFreeLoot(Long lootId, List<Loot> loots) {
        boolean res = false;
        try {
            res = getExistingFreeLoot(lootId, loots) != null;
        } catch (IllegalArgumentException e) {
            logger.info("Unknown loot id " + lootId, e);
            res = false;
        }
        return res;
    }

    @Override
    public boolean evaluate(ActionCommand command) {
        return command.getCurrentPlayer() instanceof Player
                && isExistingFreeLoot(command.getTargetLoot().getId(), command.getGame().getLoots());
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {

        Player actor = (Player) command.getCurrentPlayer();
        Loot lootToPick = getExistingFreeLoot(command.getTargetLoot().getId(), command.getGame().getLoots());

        lootToPick.setOwnerId(actor.getId());
        actor.addLoot(lootToPick);

        ArrayList<Positionable> result = new ArrayList<>();
        result.add(lootToPick);
        result.add(actor);
        return result;
    }
}
