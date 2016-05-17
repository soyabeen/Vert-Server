package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 09.05.2016.
 */
public class PunchExecRule implements ExecutionRule {

    @Override
    public boolean evaluate(ActionCommand command) {
        return command.getCurrentPlayer() instanceof  Player
                && command.getTargetPlayer() instanceof  Player;
    }


    @Override
    public List<Positionable> execute(ActionCommand command) {
        List<Positionable> result = new ArrayList<>();

        if (evaluate(command)) {
            Player victim = (Player) command.getTargetPlayer();

            if (command.getTargetPlayer() != null && !victim.getLoots().isEmpty()) {
                victim.dropLoot(command.getTargetLoot());
                Loot targetLoot = new Loot(command.getTargetLoot().getType(), command.getGame().getId(), command.getTargetLoot().getValue());
                targetLoot.setCar(victim.getCar());
                targetLoot.setLevel(victim.getLevel());
                targetLoot.setId(command.getTargetLoot().getId());
                result.add(targetLoot);
            }
            result.add(moveVictim(victim, command));
        }

        return result;
    }

    private Player moveVictim(Player victim, ActionCommand command) {
        Player emVictim = new Player();
        emVictim.setLevel(victim.getLevel());
        emVictim.setCar(victim.getCar() + command.getDirection().intValue());

        ActionCommand copy = new ActionCommand(CardType.MOVE, command.getGame(), victim, emVictim);
        return (Player) new MovePlayerExecRule().execute(copy).get(0);
    }
}
