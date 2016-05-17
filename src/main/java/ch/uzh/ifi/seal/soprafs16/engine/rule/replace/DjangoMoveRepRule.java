package ch.uzh.ifi.seal.soprafs16.engine.rule.replace;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.Direction;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.MovePlayerExecRule;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 15.05.16.
 */
public class DjangoMoveRepRule implements ReplaceRule {

    private ActionCommand command;

    public DjangoMoveRepRule(ActionCommand command) {
        this.command = command;
    }

    private Player filterDjango(List<Positionable> players) {
        for (Positionable pos : players) {
            if (pos instanceof Player) {
                Player p = (Player) pos;
                if (Character.DJANGO.equals(p.getCharacter())) {
                    return p;
                }
            }
        }
        return null;
    }

    private Player filterById(Player player, List<Positionable> players) {
        for (Positionable pos : players) {
            if (pos instanceof Player) {
                Player p = (Player) pos;
                if (player.getId().equals(p.getId())) {
                    return p;
                }
            }
        }
        return null;
    }

    private Direction getDirectionToMove(Player source, Player target) {
        int raw = source.getCar() - target.getCar();
        if (raw < 0) {
            return Direction.TO_TAIL;
        }
        return Direction.TO_HEAD;
    }

    @Override
    public boolean evaluate(List<Positionable> actors) {
        return Character.DJANGO.equals(((Player) command.getCurrentPlayer()).getCharacter())
                && filterDjango(actors) != null
                && actors.size() >= 2;
    }

    @Override
    public List<Positionable> replace(List<Positionable> actors) {
        List<Positionable> result = new ArrayList<>();
        if (evaluate(actors)) {
            Player django = filterById((Player) command.getCurrentPlayer(), actors);
            Player target = filterById((Player) command.getTargetPlayer(), actors);

            Direction dirToMove = getDirectionToMove(django, target);
            Player newTargetPosition = new Player();
            newTargetPosition.setLevel(target.getLevel());
            newTargetPosition.setCar(target.getCar() + dirToMove.intValue());

            ActionCommand moveCommand = new ActionCommand(CardType.MOVE, command.getGame(), target, newTargetPosition);
            result.addAll(new MovePlayerExecRule().execute(moveCommand));
            result.add(django);
        }
        return result;
    }
}
