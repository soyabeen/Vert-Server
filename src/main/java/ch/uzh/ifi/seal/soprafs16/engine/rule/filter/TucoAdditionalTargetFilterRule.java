package ch.uzh.ifi.seal.soprafs16.engine.rule.filter;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Game;
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
    private Game game;
    private Player current;

    public TucoAdditionalTargetFilterRule(Game game, Player current) {
        this.game = game;
        this.current = current;
        tFinder = new TargetFinder();
    }

    @Override
    public boolean evaluate(List<Positionable> actors) {
        return Character.TUCO.equals(current.getCharacter());
    }

    @Override
    public List<Positionable> filter(List<Positionable> actors) {
        if (evaluate(actors)) {
            List<Positionable> result = new ArrayList<>();
            result.addAll(actors);
            result.addAll(tFinder.filterPlayersOnOppositeFloor(current, game.getPlayers()));
            return result;
        }
        return actors;
    }
}
