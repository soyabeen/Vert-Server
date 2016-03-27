package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;

/**
 * Created by antoio on 3/26/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class PlayerServiceUTest {

    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);


    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserRepository userRepo;

    @Test
    public void testCreatePlayerForGame() {

        // Create a game for testing purpose
        Game game = new Game();
        game.setStatus(GameStatus.PENDING);
        game.setName("testCreatePlayerForGame");
        game.setOwner("testCreatePlayerForGame");
        game = gameRepo.save(game);

        logger.debug("Game saved to repo: " + game.getName());

        // Create a User for testing purpose
        User user = new User("duck", "donald");
        user.setPlayer(new Player());
        user.getPlayer().setCharacter(Character.DJANGO);
        user.setStatus(UserStatus.ONLINE);
        user.setToken("donald");
        userRepo.save(user);

        // Create another User for testing purpose
        User user2 = new User("duck", "daisy");
        user2.setPlayer(new Player());
        user2.getPlayer().setCharacter(Character.GHOST);
        user2.setStatus(UserStatus.ONLINE);
        user2.setToken("daisy");
        userRepo.save(user2);

        // Call test method
        playerService.createPlayerForGame(game.getId(), user.getToken());

        // Reload game from repo
        game = gameRepo.findOne(game.getId());
        Assert.assertThat(game.getUsers().size(), is(1));

        // Call test method again
        playerService.createPlayerForGame(game.getId(), user2.getToken());

        // Reload game from repo
        game = gameRepo.findOne(game.getId());
        Assert.assertThat(game.getUsers().size(), is(2));

    }

    @Test
    public void testListPlayersForGame() {

        // Create a game for testing purpose
        Game game = new Game();
        game.setStatus(GameStatus.PENDING);
        game.setName("testListPlayerForGame");
        game.setOwner("testListPlayerForGame");
        game = gameRepo.save(game);

        List<Player> result = playerService.listPlayersForGame(game.getId());
        Assert.assertThat(result, is(gameRepo.findOne(game.getId()).getUsers()));

        // Create a User for testing purpose
        User user = new User("el", "barto");
        user.setPlayer(new Player());
        user.getPlayer().setCharacter(Character.DJANGO);
        user.setStatus(UserStatus.ONLINE);
        user.setToken("barto");
        userRepo.save(user);

        playerService.createPlayerForGame(game.getId(), user.getToken());
        List<Player> testResult = playerService.listPlayersForGame(game.getId());

        Assert.assertThat( testResult.get(0).getCharacter(), is(user.getPlayer().getCharacter()) );
        Assert.assertThat( testResult.get(0).getOwner().getId(), is(user.getId()) );
    }
}
