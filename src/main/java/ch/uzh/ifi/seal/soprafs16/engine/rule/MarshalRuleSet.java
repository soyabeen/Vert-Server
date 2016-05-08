package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.MoveMarshalExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.MovePlayerExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.replace.MarshalRepRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.SimulationRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.MovePlayerBottomSimRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 08.05.2016.
 */
public class MarshalRuleSet extends RuleSet {

    private static final Logger logger = LoggerFactory.getLogger(MarshalRuleSet.class);

    public List<Positionable> simulate(Game game, Marshal marshal) {
        List<SimulationRule> simRules = new ArrayList<>();
        simRules.add(new MovePlayerBottomSimRule(game.getNrOfCars()));

        List<Positionable> result = new ArrayList<>();
        for (SimulationRule simRule : simRules) {
            if (simRule.evaluate(marshal)) {
                result.addAll(simRule.simulate(marshal));
            }
        }
        return result;
    }

    @Override
    public List<Positionable> simulate(Game game, Player player) {
        Marshal marshal = new Marshal( game.getPositionMarshal() );

        return simulate(game, marshal);
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        List<Positionable> result = new ArrayList<>();
        MoveMarshalExecRule move = new MoveMarshalExecRule();

        logger.debug("eval: " + move.evaluate(command));

        // check if the marshal position is valid
        if(move.evaluate(command)) {
            result.addAll( move.execute(command) );
        }
        logger.debug("res: " + result.size());
        return result;
    }
}
