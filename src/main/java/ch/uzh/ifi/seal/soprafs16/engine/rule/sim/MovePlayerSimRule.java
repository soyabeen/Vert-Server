package ch.uzh.ifi.seal.soprafs16.engine.rule.sim;

import ch.uzh.ifi.seal.soprafs16.constant.Direction;
import ch.uzh.ifi.seal.soprafs16.engine.rule.RuleUtils;
import ch.uzh.ifi.seal.soprafs16.model.Marshal;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Rule to check, how a player can move horizontally.
 * <p>
 * Created by soyabeen on 19.04.16.
 */
public class MovePlayerSimRule implements SimulationRule {

    private static final Logger logger = LoggerFactory.getLogger(MovePlayerSimRule.class);

    private int trainLength;
    private int distanceToMove;
    private Positionable.Level level;

    public MovePlayerSimRule(int trainLength, int distanceToMove, Positionable.Level level) {
        this.trainLength = trainLength;
        this.distanceToMove = distanceToMove;
        this.level = level;
    }

    /**
     * Checks if a player is positioned on the train and on the bottom level.
     *
     * @return
     */
    @Override
    public boolean evaluate(Positionable player) {
        if (!(player instanceof Player || player instanceof Marshal)) {
            return false;
        }
        return RuleUtils.isOnSameLevel(player, level)
                && RuleUtils.isPlacedOnTrain(player, trainLength);
    }


    /**
     * @param player
     * @param boardLength
     * @param distance
     * @param direction
     * @return
     */
    private List<Positionable> getPossiblePositions(Positionable player, int boardLength, int distance, int direction) {
        ArrayList<Positionable> emulatedPlayerPositions = new ArrayList<>();
        Positionable marshalOrPlayer = player;
        int playerPosition = player.getCar();
        for (int i = 1;
             i <= distance
                     && playerPosition + (direction * i) < boardLength
                     && playerPosition + (direction * i) >= 0;
             i++) {

            // used to distinguish between Player and Marshal, since a Marshal is not a player
            // fixme: How to convert from Player to Positionable?
            if(player instanceof Player) {
                // only need Player instance to set username...
                Player emulated = new Player();
                emulated.setUsername( ((Player)player).getUsername());

                // after username is set continue with Positionable reference
                marshalOrPlayer = emulated;
            } else if (player instanceof Marshal) {
                // create new Positionable instance for emulation
                Positionable marshal = new Marshal(player.getCar());
                marshalOrPlayer = marshal;
            }

            marshalOrPlayer.setLevel(player.getLevel());
            marshalOrPlayer.setCar(playerPosition + (direction * i));
            emulatedPlayerPositions.add(marshalOrPlayer);

        }
        return emulatedPlayerPositions;
    }

    @Override
    public List<Positionable> simulate(Positionable player) {
        ArrayList<Positionable> emulatedPlayers = new ArrayList<>();
        if (evaluate(player)) {
            emulatedPlayers.addAll(getPossiblePositions(player, trainLength, distanceToMove, Direction.TO_HEAD.intValue()));
            emulatedPlayers.addAll(getPossiblePositions(player, trainLength, distanceToMove, Direction.TO_TAIL.intValue()));
        }
        return emulatedPlayers;
    }

}
