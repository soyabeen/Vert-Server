package ch.uzh.ifi.seal.soprafs16.engine.rules.sim;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Describes the needed methods to evaluate the applied
 * rules/restrictions to a game action.
 * <p>
 * Created by soyabeen on 19.04.16.
 */
public interface SimulationRule {

    /**
     * Checks the rule set/restriction, to know if the rule can be applied.
     *
     * @return boolean True if the situation satisfy the rule set,
     * so that the action can be executed.
     */
    public boolean evaluate(Positionable actor);


    /**
     * Execute the rule, actually do the
     */
    public List<Positionable> simulate(Positionable actor);
}
