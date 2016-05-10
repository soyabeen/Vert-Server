package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.constant.Direction;
import ch.uzh.ifi.seal.soprafs16.engine.rule.RuleUtils;
import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devuser on 09.05.2016.
 */
public class MoveMarshalSimRule implements SimulationRule {

    private static final Logger logger = LoggerFactory.getLogger(MovePlayerSimRule.class);

    private int trainLength;
    private int distanceToMove;

    public MoveMarshalSimRule(int trainLength) {
        this.trainLength = trainLength;
        // Marshal can just move one car
        this.distanceToMove = 1;
    }

    @Override
    public boolean evaluate(Positionable actor) {
        if(!(actor instanceof Marshal)) {
            return false;
        }
        Marshal marshal = (Marshal) actor;
        return RuleUtils.isOnSameLevel(marshal, Positionable.Level.BOTTOM)
                && RuleUtils.isPlacedOnTrain(marshal, trainLength);
    }

    private List<Positionable> getPossiblePositions(Marshal marshal, int boardLength, int distance, int direction) {
        ArrayList<Positionable> emulatedMarshalPositions = new ArrayList<>();
        int marshalPosition = marshal.getCar();
        for( int i = 0;
            i <= distance
                && marshalPosition + (direction * distanceToMove) < boardLength
                && marshalPosition + (direction * distanceToMove) >= 0;
            i++) {
            // emulate the marshal and set new start position
            Marshal emulatedMarshal = new Marshal(marshal.getCar() + (direction * i));
            if(emulatedMarshal.getCar() != marshal.getCar()) {
                emulatedMarshalPositions.add(emulatedMarshal);
            }
        }
        return emulatedMarshalPositions;
    }

    @Override
    public List<Positionable> simulate(Positionable actor) {
        ArrayList<Positionable> emulatedPlayers = new ArrayList<>();
        if (evaluate(actor)) {
            emulatedPlayers.addAll(getPossiblePositions((Marshal) actor, trainLength, distanceToMove, Direction.TO_HEAD.intValue()));
            emulatedPlayers.addAll(getPossiblePositions((Marshal) actor, trainLength, distanceToMove, Direction.TO_TAIL.intValue()));
        }
        return emulatedPlayers;
    }
}
