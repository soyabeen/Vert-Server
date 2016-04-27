package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.FloorChangeSimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class FloorChangeRuleSet extends RuleSet {

    private static final Logger logger = LoggerFactory.getLogger(FloorChangeRuleSet.class);

    @Override
    public List<Positionable> simulate(Game game, Player player) {
        FloorChangeSimRule floorChange = new FloorChangeSimRule();
        return floorChange.simulate(player);
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        return null;
    }
}
