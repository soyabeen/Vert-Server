package ch.uzh.ifi.seal.soprafs16.engine.rules;

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
public class MovePlayerHorizontally implements IGameRule {

    private static final Logger logger = LoggerFactory.getLogger(MovePlayerHorizontally.class);
    private static final int ALLOWED_DISTANCE = 1;
    private static final int DIRECTION_TO_HEAD = -1;
    private static final int DIRECTION_TO_TAIL = 1;

    private Player player;
    private int trainLength;
    private int distanceToMove;

    public MovePlayerHorizontally(Player player, int trainLength, int distanceToMove) {
        this.player = player;
        this.trainLength = trainLength;
        this.distanceToMove = distanceToMove;
    }

    /**
     * Checks if a player is positioned on the train and on the bottom level.
     *
     * @return
     */
    @Override
    public boolean evaluate() {
        if (!(Positionable.Level.BOTTOM == player.getLevel() ||
                Positionable.Level.TOP == player.getLevel())) {
            logger.debug("Player {} ({},{}) can't move horizontally on lower level, because he has an invalid level type!",
                    player.getUsername(), player.getCar(), player.getLevel());
            return false;
        }
        if (player.getCar() < 0 || player.getCar() > trainLength) {
            logger.info("Player {} ({},{}) can't move horizontally on lower level, because he is not positioned on the train!",
                    player.getUsername(), player.getCar(), player.getLevel());
            return false;
        }
        return true;
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
    public List<Positionable> emulate() {
        ArrayList<Positionable> emulatedPlayers = new ArrayList<>();
        if (evaluate()) {
            emulatedPlayers.addAll(getPossiblePositions(player, trainLength, distanceToMove, DIRECTION_TO_HEAD));
            emulatedPlayers.addAll(getPossiblePositions(player, trainLength, distanceToMove, DIRECTION_TO_TAIL));
        }
        return emulatedPlayers;
    }

}
