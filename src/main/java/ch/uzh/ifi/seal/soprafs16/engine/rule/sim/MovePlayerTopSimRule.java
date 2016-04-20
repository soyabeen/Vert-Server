package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by soyabeen on 20.04.16.
 */
public class MovePlayerTopSimRule extends MovePlayerSimRule {

    private static final int DISTANCE_TO_MOVE = 3;

    public MovePlayerTopSimRule(int trainLength) {
        super(trainLength, DISTANCE_TO_MOVE);
    }

    @Override
    public boolean evaluate(Positionable player) {
        return super.evaluate(player);
    }

    @Override
    public List<Positionable> simulate(Positionable player) {
        return super.simulate(player);
    }
}
