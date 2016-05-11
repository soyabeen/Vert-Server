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
        Player targetPlayer = (Player) player;
        return RuleUtils.isOnSameLevel(targetPlayer, level)
                && RuleUtils.isPlacedOnTrain(targetPlayer, trainLength);
    }


    /**
     * @param player
     * @param boardLength
     * @param distance
     * @param direction
     * @return
     */
    private List<Positionable> getPossiblePositions(Player player, int boardLength, int distance, int direction) {
        ArrayList<Positionable> emulatedPlayerPositions = new ArrayList<>();
        int playerPosition = player.getCar();
        for (int i = 1;
             i <= distance
                     && playerPosition + (direction * i) < boardLength
                     && playerPosition + (direction * i) >= 0;
             i++) {
            Player emulated = new Player();
            emulated.setUsername(player.getUsername());
            emulated.setLevel(player.getLevel());
            emulated.setCar(playerPosition + (direction * i));
            emulatedPlayerPositions.add(emulated);
        }
        return emulatedPlayerPositions;
    }

    @Override
    public List<Positionable> simulate(Positionable player) {
        ArrayList<Positionable> emulatedPlayers = new ArrayList<>();
        if (evaluate(player)) {
            emulatedPlayers.addAll(getPossiblePositions((Player) player, trainLength, distanceToMove, Direction.TO_HEAD.intValue()));
            emulatedPlayers.addAll(getPossiblePositions((Player) player, trainLength, distanceToMove, Direction.TO_TAIL.intValue()));
        }
        return emulatedPlayers;
    }

}
