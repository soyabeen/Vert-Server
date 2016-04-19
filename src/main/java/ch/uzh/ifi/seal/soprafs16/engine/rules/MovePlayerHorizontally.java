package ch.uzh.ifi.seal.soprafs16.engine.rules;

import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.jboss.logging.annotations.Pos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Rule to check, how a player can move horizontally.
 * <p>
 * Created by soyabeen on 19.04.16.
 */
public class MovePlayerHorizontally implements IGameRule {

    private static final Logger logger = LoggerFactory.getLogger(MovePlayerHorizontallyOnLowerLevel.class);
    private static final int ALLOWED_DISTANCE = 1;

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

    @Override
    public void emulate() {

//        if (evaluate()) {
//            int position = player.getCar();
//            for (int i = 1; i <; i++) {
//
//            }
//            if (0 < position) {
//                // players can move in direction to head/locomotive
//            }
//            if (position < trainLength) {
//                // players can move in direction to tail
//            }
//        }
    }
}
