package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
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
 * Created by alexanderhofmann on 25/03/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class PositionServiceIntegrationTest {

    private static final Logger logger  = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);


    @Autowired
    private PositionService positionService;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private LootRepository lootRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Test
    public void getPositionablesTest() {
        Game game = new Game();
        game.setStatus(GameStatus.PENDING);
        game.setName("getAvailableCharactersa");
        game.setOwner("getAvailableCharactersa");
        game = gameRepo.save(game);

        List<Positionable> result = positionService.listPositionablesForGame(game.getId());
        Assert.assertThat(result.size(), is(0));

        Loot loot1 = new Loot(LootType.PURSE, 250, Positionable.Level.TOP);
        Loot loot2 = new Loot(LootType.JEWEL, 500, Positionable.Level.TOP);
        lootRepo.save(loot1);
        lootRepo.save(loot2);

        User user1 = new User("hans1", "wurst1");
        Player p1 = new Player();
        user1.setPlayer(p1);
        user1.getPlayer().setCharacter(Character.BELLE);
        user1.setStatus(UserStatus.ONLINE);
        user1.setToken("blablaasd");
        playerRepo.save(p1);
        userRepo.save(user1);

        User user2 = new User("daisy1", "duck1");
        Player p2 = new Player();
        user2.setPlayer(p2);
        user2.getPlayer().addLoot(loot1);
        user2.getPlayer().setCharacter(Character.GHOST);
        user2.setStatus(UserStatus.ONLINE);
        user2.setToken("blabladasdads");
        playerRepo.save(p2);
        userRepo.save(user2);

        game.addUser(user1);
        game.addUser(user2);
        game.addLoot(loot2);
        game = gameRepo.save(game);

        userRepo.save(user1);

        userRepo.save(user2);

        result = positionService.listPositionablesForGame(game.getId());
        Assert.assertThat(result.size(), is(4));
    }
}
