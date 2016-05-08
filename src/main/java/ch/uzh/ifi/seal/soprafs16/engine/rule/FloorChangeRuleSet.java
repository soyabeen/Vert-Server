package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.FloorChangeExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.replace.MarshalRepRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.FloorChangeSimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class FloorChangeRuleSet extends RuleSet {

    @Override
    public List<Positionable> simulate(Game game, Player player) {
        FloorChangeSimRule floorChange = new FloorChangeSimRule();
        return floorChange.simulate(player);
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        FloorChangeExecRule floorChange = new FloorChangeExecRule();
        MarshalRepRule marshal = new MarshalRepRule(command.getGame());
        return marshal.replace(floorChange.execute(command));
    }
}
