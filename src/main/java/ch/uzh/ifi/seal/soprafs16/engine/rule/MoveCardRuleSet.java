package ch.uzh.ifi.seal.soprafs16.engine.rule;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rule.exec.ExecutionRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.MovePlayerBottomSimRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.MovePlayerTopSimRule;
import ch.uzh.ifi.seal.soprafs16.engine.rule.sim.SimulationRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 20.04.16.
 */
public class MoveCardRuleSet extends RuleSet {

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
    public void execute(ActionCommand command) {
        List<ExecutionRule> execRules = new ArrayList<>();

//        List<Positionable> result = new ArrayList<>();
//        for (SimulationRule simRule : simRules) {
//            if (simRule.evaluate(player)) {
//                result.addAll(simRule.simulate(player));
//            }
//        }
//        return result;
    }
}
