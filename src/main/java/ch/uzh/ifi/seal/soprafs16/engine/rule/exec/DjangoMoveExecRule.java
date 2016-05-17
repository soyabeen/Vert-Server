package ch.uzh.ifi.seal.soprafs16.engine.rule.exec;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.Direction;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 15.05.16.
 */
public class DjangoMoveExecRule implements ExecutionRule {

    @Override
    public boolean evaluate(ActionCommand command) {
        return Character.DJANGO.equals(((Player) command.getCurrentPlayer()).getCharacter())
                && command.getTargetPlayer() != null;
    }

    private Direction getDirectionToMove(Player source, Player target) {
        int raw = source.getCar() - target.getCar();
        if (raw < 0) {
            return Direction.TO_HEAD;
        }
        return Direction.TO_TAIL;
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        List<Positionable> result = new ArrayList<>();
        if (evaluate(command)) {

            Player django = (Player) command.getCurrentPlayer();
            Player target = (Player) command.getTargetPlayer();

            Direction dirToMove = getDirectionToMove(django, target);
            Player newTargetPosition = new Player();
            newTargetPosition.setLevel(target.getLevel());
            newTargetPosition.setCar(target.getCar() + dirToMove.intValue());

            ActionCommand moveCommand = new ActionCommand(CardType.MOVE, command.getGame(), target, newTargetPosition);
            result.addAll(new MovePlayerExecRule().execute(moveCommand));
        }
        return result;
    }
}
