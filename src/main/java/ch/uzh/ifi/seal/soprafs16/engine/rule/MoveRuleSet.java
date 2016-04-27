package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.ExecutionRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.MovePlayerExecRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.replace.MarshalRepRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.MovePlayerBottomSimRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.MovePlayerTopSimRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.SimulationRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 20.04.16.
 */
public class MoveRuleSet extends RuleSet {

    private static final Logger logger = LoggerFactory.getLogger(MoveRuleSet.class);

    @Override
    public List<Positionable> simulate(Game game, Player player) {
        List<SimulationRule> simRules = new ArrayList<>();
        simRules.add(new MovePlayerBottomSimRule(game.getNrOfCars()));
        simRules.add(new MovePlayerTopSimRule(game.getNrOfCars()));

        List<Positionable> result = new ArrayList<>();
        for (SimulationRule simRule : simRules) {
            if (simRule.evaluate(player)) {
                result.addAll(simRule.simulate(player));
            }
        }
        return result;
    }

    @Override
    public List<Positionable> execute(ActionCommand command) {
        List<Positionable> result = new ArrayList<>();
        MovePlayerExecRule move = new MovePlayerExecRule();
        MarshalRepRule marshal = new MarshalRepRule();

        logger.debug("eval: " + move.evaluate(command));
        if(move.evaluate(command)) {
           result.addAll(marshal.replace(move.execute(command)));
        }
        logger.debug("res: " + result.size());
        return result;
    }
}
