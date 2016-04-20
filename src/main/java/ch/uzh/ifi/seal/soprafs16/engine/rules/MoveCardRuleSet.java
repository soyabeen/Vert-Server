package ch.uzh.ifi.seal.soprafs16.engine.rules;

import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.rules.sim.MovePlayerBottomSimRule;
import ch.uzh.ifi.seal.soprafs16.engine.rules.sim.MovePlayerTopSimRule;
import ch.uzh.ifi.seal.soprafs16.engine.rules.sim.SimulationRule;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 20.04.16.
 */
public class MoveCardRuleSet implements RuleSet {


    @Override
    public List<Positionable> simulate(Game game, Player player) {

        List<Positionable> result = new ArrayList<>();

        List<SimulationRule> simRules = new ArrayList<>();
        simRules.add(new MovePlayerBottomSimRule(game.getNrOfCars()));
        simRules.add(new MovePlayerTopSimRule(game.getNrOfCars()));

        for (SimulationRule simRule : simRules) {
            if (simRule.evaluate(player)) {
                result.addAll(simRule.simulate(player));
            }
        }

        return result;
    }

    @Override
    public void execute(ActionCommand command) {

    }
}
