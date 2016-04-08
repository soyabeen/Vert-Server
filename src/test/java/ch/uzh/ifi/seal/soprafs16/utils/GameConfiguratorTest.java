package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.core.Is.is;


/**
 * Unit tests for the GameConfigurator class.
 * <p>
 * Created by soyabeen on 08.04.16.
 */
public class GameConfiguratorTest {

    private static final Logger logger = LoggerFactory.getLogger(GameConfiguratorTest.class);

    private GameConfigurator configurator;

    @Before
    public void setUp() {
        configurator = new GameConfigurator();
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSmallPlayerNrThrowsException() {
        int toSmallPlayerNr = 1;
        configurator.createGameEmptyGameShellForNrOfPlayers(toSmallPlayerNr);
        Assert.fail("To small player nr should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toBigPlayerNrThrowsException() {
        int toBigPlayerNr = 5;
        configurator.createGameEmptyGameShellForNrOfPlayers(toBigPlayerNr);
        Assert.fail("To big player nr should throw an IllegalArgumentException");
    }

    @Test
    public void maxPlayerNrWillCreateGame() {
        Game game = configurator.createGameEmptyGameShellForNrOfPlayers(GameConfigurator.MAX_PLAYERS);
        Assert.assertNotNull("Game should not be null", game);
        Assert.assertThat(game.getNumberOfPlayers(), is(GameConfigurator.MAX_PLAYERS));
    }

    @Test
    public void minPlayerNrWillCreateGame() {
        Game game = configurator.createGameEmptyGameShellForNrOfPlayers(GameConfigurator.MIN_PLAYERS);
        Assert.assertNotNull("Game should not be null", game);
        Assert.assertThat(game.getNumberOfPlayers(), is(GameConfigurator.MIN_PLAYERS));
    }


    @Test
    public void acceptablePlayerNrWillCreateGame() {
        int acceptablePlayerNr = 3;
        Game game = configurator.createGameEmptyGameShellForNrOfPlayers(acceptablePlayerNr);
        Assert.assertNotNull("Game should not be null", game);
        Assert.assertThat(game.getNumberOfPlayers(), is(acceptablePlayerNr));
    }

    @Test
    public void twoPlayersWillPlayWithThreeCars() {
        Game game = configurator.createGameEmptyGameShellForNrOfPlayers(2);
        Assert.assertNotNull("Game should not be null", game);
        Assert.assertThat(game.getNumberOfPlayers(), is(2));
        Assert.assertThat(game.getNrOfCars(), is(3));
    }

    @Test
    public void fourPlayersWillPlayWithFourCars() {
        Game game = configurator.createGameEmptyGameShellForNrOfPlayers(4);
        Assert.assertNotNull("Game should not be null", game);
        Assert.assertThat(game.getNumberOfPlayers(), is(4));
        Assert.assertThat(game.getNrOfCars(), is(4));
    }

    @Test
    public void normalGameHasPendingStatusAndListOfLoots() {
        int normalNrOfPlayers = 3;
        Game game = configurator.createGameEmptyGameShellForNrOfPlayers(normalNrOfPlayers);
        Assert.assertNotNull("Game should not be null", game);
        Assert.assertThat(game.getStatus(), is(GameStatus.PENDING));
        Assert.assertNotNull("Loots should not be ull", game.getLoots());
        Assert.assertTrue("Nr of loots is at least 1", game.getLoots().size() > 0);
    }

    @Test
    public void lootsListContainsEveryTypeAtLeastOnce() {
        List<Loot> loots = configurator.generateLootsForNCars(3);
        Assert.assertNotNull("Generated loot list can not be null.", loots);
        boolean hasBoxType = false;
        boolean hasJewelType = false;
        boolean hasPurseType = false;
        logger.debug("list loots:");
        for (Loot loot : loots) {
            logger.debug(" - " + loot.toString());
            switch (loot.getType()){
                case JEWEL: hasJewelType=true;
                    break;
                case STRONGBOX: hasBoxType=true;
                    break;
                case PURSE_BIG: hasPurseType=true;
                    break;
                case PURSE_SMALL: hasPurseType=true;
            }
        }
        Assert.assertTrue("Must contain jewel types.", hasJewelType);
        Assert.assertTrue("Must contain strong box types.", hasBoxType);
        Assert.assertTrue("Must contain purse types.", hasPurseType);
    }
}
