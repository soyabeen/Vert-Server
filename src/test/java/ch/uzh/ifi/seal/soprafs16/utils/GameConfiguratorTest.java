package ch.uzh.ifi.seal.soprafs16.utils;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;


/**
 * Unit tests for the GameConfigurator class.
 *
 * Created by soyabeen on 08.04.16.
 */
public class GameConfiguratorTest {

    private GameConfigurator configurator;

    @Before
    public void setUp() {
        configurator = new GameConfigurator();
    }

    @Test(expected = IllegalArgumentException.class)
    public void toSmallPlayerNrThrowsException () {
        int toSmallPlayerNr = 1;
        configurator.createGameEmptyGameShellForNrOfPlayers(toSmallPlayerNr);
        Assert.fail("To small player nr should throw an IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void toBigPlayerNrThrowsException () {
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


}
