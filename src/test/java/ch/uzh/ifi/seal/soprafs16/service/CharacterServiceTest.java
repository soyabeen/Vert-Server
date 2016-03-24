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

import java.util.List;

import static org.hamcrest.CoreMatchers.*;

/**
 * Created by soyabeen on 24.03.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class CharacterServiceTest {

    Logger logger  = LoggerFactory.getLogger(CharacterServiceTest.class);

    @Autowired
    private CharacterService characterService;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserRepository userRepo;

    @Test
    public void getAll7UnfilteredCharacters() {
        Assert.assertThat(characterService.listCharacters().size(), is(7));
        Assert.assertThat(characterService.listCharacters(), hasItems(Character.values()));
    }

    @Test
    public void getAvailableCharacters() {

        User user1 = new User("hans", "wurst");
        user1.setPlayer(new Player());
        user1.getPlayer().setCharacter(Character.BELLE);
        user1.setStatus(UserStatus.ONLINE);
        user1.setToken("blabla");
        userRepo.save(user1);


        User user2 = new User("daisy", "duck");
        user2.setPlayer(new Player());
        user2.getPlayer().setCharacter(Character.GHOST);
        user2.setStatus(UserStatus.ONLINE);
        user2.setToken("blabladasd");
        userRepo.save(user2);

        Game game = new Game();
        game.setStatus(GameStatus.PENDING);
        game.setName("getAvailableCharacters");
        game.setOwner("getAvailableCharacters");
        game.addUser(user1);
        game.addUser(user2);
        game = gameRepo.save(game);

        user1.setGame(game);
        userRepo.save(user1);

        user2.setGame(game);
        userRepo.save(user2);


        logger.warn("saved game id:  "+       game.getId());
        List<Character> result = characterService.listAvailableCharactersByGame(game.getId());
        Assert.assertThat(result.size(), is(5));
        Assert.assertThat(result, hasItem(Character.CHEYENNE));
        Assert.assertTrue(!result.contains(Character.GHOST));
        Assert.assertTrue(!result.contains(Character.BELLE));

    }
}
