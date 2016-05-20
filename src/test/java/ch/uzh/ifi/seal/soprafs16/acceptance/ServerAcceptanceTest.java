package ch.uzh.ifi.seal.soprafs16.acceptance;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.core.Is.is;

/**
 * Created by soyabeen on 15.05.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class ServerAcceptanceTest {

    private static final Logger logger = LoggerFactory.getLogger(ServerAcceptanceTest.class);

    @Value("${local.server.port}")
    private int port;
    private URL base;
    private RestTemplate temp;

    @Before
    public void setUp() throws MalformedURLException {
        temp = new RestTemplate();
        base = new URL("http://localhost:" + port);
    }


    private Player createPlayer(String name) {
        Player playerShell = new Player(name);
        Player player = temp.postForObject(base + "/users", playerShell, Player.class);
        logger.debug("created {}", player.toString());

        Assert.assertNotNull("Created player is not null.", player);
        Assert.assertNotNull("Player id not null.", player.getId());
        Assert.assertNotNull("Player token not null.", player.getToken());
        Assert.assertEquals("Player name is " + name, name, player.getUsername());
        return player;
    }

    private Player assignCharacter(Game game, Player player, Character character) {
        Player assignedPlayer = temp.postForObject(base + "/games/" + game.getId() + "/players"
                + "?token=" + player.getToken()
                + "&character=" + character.toString(), null, Player.class);
        logger.debug("assigned {}", assignedPlayer.toString());

        Assert.assertNotNull("Assigned player is not null.", player);
        Assert.assertEquals("Player has character " + character, character, assignedPlayer.getCharacter());
        return assignedPlayer;
    }

    private void assignOwner(Game game, Player player, Character character) {
        temp.put(base + "/games/" + game.getId() + "/players"
                + "?token=" + player.getToken()
                + "&character=" + character.toString(), null);
    }


    private Game createGame(String gameName, Player owner) {
        Game gameShell = new Game();
        gameShell.setName(gameName);
        Game game = temp.postForObject(base + "/games?token=" + owner.getToken(), gameShell, Game.class);
        logger.debug("created {}", game.toString());

        Assert.assertNotNull("Created game is not null.", game);
        Assert.assertNotNull("Game id not null.", game.getId());
        Assert.assertNotNull("Game owner not null.", game.getOwner());
        Assert.assertEquals("Game owner is " + owner.getUsername(), owner.getUsername(), game.getOwner());
        Assert.assertEquals("Game name is " + gameName, gameName, game.getName());
        return game;
    }

    private Game getGameById(Game game) {
        return temp.getForObject(base + "/games/" + game.getId(), Game.class);
    }

    private Game startGame(Game game, Player owner) {
        temp.postForObject(base + "/games/" + game.getId() + "/start?token=" + owner.getToken(), null, Game.class);
        Game startedGame = getGameById(game);
        logger.debug("Started {}", game.toString());
        Assert.assertNotNull("Started game is not null.", startedGame);
        Assert.assertEquals("Started game is in planning phase.", GameStatus.PLANNINGPHASE, startedGame.getStatus());
        return startedGame;
    }

    public void checkStartedGamePlayerState(Player player) {
        Assert.assertNotNull("Player has id.", player.getId());
        Assert.assertNotNull("Player has token.", player.getToken());
        Assert.assertNotNull("Player has name.", player.getUsername());
        Assert.assertThat("Player has 6 bullets.", player.getBullets(), is(6));
        Assert.assertThat("Player has 1 loot.", player.getLoots().size(), is(1));
        Assert.assertTrue("Player has at least 6 cards in hand.", player.getHand().size() >= 6);
        Assert.assertThat("Player is on lower level.", player.getLevel(), is(Positionable.Level.BOTTOM));
        Assert.assertThat("Player is not injured.", player.getInjuries(), is(0));
        Assert.assertThat("Player made moves", player.getTotalMadeMoves(), is(0));
    }

    public void checkStartedGameState(Game game) {
        Assert.assertThat("Game is in planning phase.", game.getStatus(), is(GameStatus.PLANNINGPHASE));
        Assert.assertThat("Game has 3 players.", game.getNumberOfPlayers(), is(3));
        Assert.assertThat("Game has 4 train cars (inkl. locomotive).", game.getNrOfCars(), is(4));
        Assert.assertThat("Game is in round 1.", game.getRoundId(), is(1));
        Assert.assertThat("Game is in turn 1.", game.getTurnId(), is(1));

        // check player states
        Assert.assertNotNull("Game has players.", game.getPlayers());
        for (Player p : game.getPlayers()) {
            checkStartedGamePlayerState(p);
        }

        // check loots
        Assert.assertNotNull("Game has loots.", game.getLoots());
        for (Loot l : game.getLoots()) {
            Assert.assertNotNull("loot id not null.", l.getId());
            Assert.assertThat("Loot on lower level.", l.getLevel(), is(Positionable.Level.BOTTOM));
        }
    }

    @Test
    public void runAcceptanceTest() throws URISyntaxException {
        Player owner = createPlayer("owner");
        Game game = createGame("acceptance game", owner);

        assignOwner(game, owner, Character.DOC);
        Player assignedSecond = assignCharacter(game, createPlayer("second"), Character.CHEYENNE);
        Player assignedThird = assignCharacter(game, createPlayer("third"), Character.DJANGO);

        Game startedGame = startGame(game, owner);


    }

}
