package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 08.05.2016.
 */
public class DrawRuleSet extends RuleSet {
    @Override
    public List<Positionable> simulate(Game game, Player player) {
        return new ArrayList<>();
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        return new ArrayList<>();
    }
}
