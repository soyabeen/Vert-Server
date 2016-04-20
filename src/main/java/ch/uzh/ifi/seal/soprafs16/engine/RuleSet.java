package ch.uzh.ifi.seal.soprafs16.engine;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Defines the set of rules needed for a certain card/activity to calculate the possibilities
 * and to execute the actual chosen action.
 * <p>
 * Created by soyabeen on 19.04.16.
 */
public interface RuleSet {

    /**
     * Calculates the possible target objects and outcomes for a card/activity.
     *
     * @return Possibilities The positionable objects that are possible targets for this card.
     */
    public List<Positionable> simulate();

    public void execute();

}
