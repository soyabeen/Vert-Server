package ch.uzh.ifi.seal.soprafs16.engine.rules;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Describes the needed methods to evaluate the applied
 * rules/restrictions to a game action.
 *
 * Created by soyabeen on 19.04.16.
 */
public interface IGameRule {

    // getEmulatedActions
    // getChosenAction

    /**
     * Checks the rule set/restriction, to know if the action can be executed.
     *
     * @return boolean True if the situation satisfy the rule set,
     * so that the action can be executed.
     */
    public boolean evaluate();


    /**
     * Execute the rule, actually do the
     */
    public List<Positionable> emulate();
}
