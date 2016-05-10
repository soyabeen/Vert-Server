package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by devuser on 09.05.2016.
 */
public class PunchSimRule implements SimulationRule {
    @Override
    public boolean evaluate(Positionable actor) {
        return false;
    }

    @Override
    public List<Positionable> simulate(Positionable actor) {
        return null;
    }
}
