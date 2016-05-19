package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.RobberyExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.RobberySimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by soyabeen on 27.04.16.
 */
public class RobberyRuleSet extends RuleSet {

    @Override
    public List<Positionable> simulate(Game game, Positionable player) {
        return new RobberySimRule(game).simulate(player);
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        RobberyExecRule robbery = new RobberyExecRule();
        return robbery.execute(command);
    }
}
