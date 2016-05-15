package ch.uzh.ifi.seal.soprafs16.engine.rule.filter;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import ch.uzh.ifi.seal.soprafs16.utils.TargetFinder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 15.05.16.
 */
public class TucoAdditionalTargetFilterRule implements FilterRule {

    private TargetFinder tFinder;
    private ActionCommand command;

    public TucoAdditionalTargetFilterRule(ActionCommand command) {
        this.command = command;
        tFinder = new TargetFinder();
    }

    @Override
    public boolean evaluate(List<Positionable> actors) {
        return command.getCurrentPlayer() instanceof Player
                && Character.TUCO.equals(((Player) command.getCurrentPlayer()).getCharacter())
                && !actors.isEmpty();
    }

    @Override
    public List<Positionable> filter(List<Positionable> actors) {
        if(evaluate(actors)){

            // cast this stuff
            List<Player> players = new ArrayList<>();
            for (Positionable p : actors) {
                if(p instanceof Player){
                    players.add((Player) p);
                }
            }

            List<Positionable> result = new ArrayList<>();
            result.addAll(actors);
            result.addAll(tFinder.filterPlayersOnOppositeFloor(command.getCurrentPlayer(), players));
        }
        return actors;
    }
}
