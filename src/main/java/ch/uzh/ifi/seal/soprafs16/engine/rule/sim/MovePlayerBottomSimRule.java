package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.constant.Distance;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;

/**
 * Created by soyabeen on 20.04.16.
 */
public class MovePlayerBottomSimRule extends MovePlayerSimRule {

    public MovePlayerBottomSimRule(int trainLength) {
        super(trainLength, Distance.DISTANCE_TO_MOVE_ON_BOTTOM, Positionable.Level.BOTTOM);
    }
    
}
