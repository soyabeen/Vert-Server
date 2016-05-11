package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.PunchExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.PunchSimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by devuser on 09.05.2016.
 */
public class PunchRuleSet extends RuleSet {
    @Override
    public List<Positionable> simulate(Game game, Positionable player) {
        return new PunchSimRule(game).simulate(player);
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        PunchExecRule punch = new PunchExecRule();
        return punch.execute(command);
    }
}
