package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.constant.Distance;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

import java.util.List;

/**
 * Created by soyabeen on 20.04.16.
 */
public class MovePlayerTopSimRule extends MovePlayerSimRule {

    public MovePlayerTopSimRule(int trainLength) {
        super(trainLength, Distance.DISTANCE_TO_MOVE_ON_TOP, Positionable.Level.TOP);
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
