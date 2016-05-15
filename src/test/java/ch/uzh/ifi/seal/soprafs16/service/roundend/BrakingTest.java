package ch.uzh.ifi.seal.soprafs16.service.roundend;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by devuser on 15.05.2016.
 */
public class BrakingTest {

    private static final Logger logger = LoggerFactory.getLogger(BrakingTest.class);

    List<Player> players;


    public BrakingTest() {
        PlayerBuilder pb = new PlayerBuilder();
        players = pb.getPlayersNoPersistence(Character.GHOST, Character.DJANGO, Character.BELLE, Character.DOC);
        Random r4nd0m = new Random();

        for (Player playerItem : players) {
            //r4nd0m.nextInt((max - min) + 1) + min;
            int randomCar = r4nd0m.nextInt(4);
            logger.debug("random generated car " + randomCar);
            playerItem.setCar(randomCar);
            playerItem.setLevel(Positionable.Level.TOP);
        }

    }

    @Test
    public void testAllPlayersMoveOneCar() {
        //printRandomCars();
        Game gayyyme = new Game();
        List<Positionable> result;

        Braking brakingEvent = new Braking();
        result = brakingEvent.execute(gayyyme, new ArrayList<Positionable>(players));

        for (int i=0; i < result.size(); i++) {
            logger.debug(players.get(i).getCharacter() + " is in Car: " + players.get(i).getCar());
            logger.debug( ((Player)result.get(i)).getCharacter() + " is in Car: " + result.get(i).getCar());
            Assert.assertEquals(players.get(i).getCar(), result.get(i).getCar());
        }
    }

    private void printRandomCars() {
        for (Player player : players) {
            logger.debug(player.getCharacter() + "is in Car: " + player.getCar());
        }
    }
}
