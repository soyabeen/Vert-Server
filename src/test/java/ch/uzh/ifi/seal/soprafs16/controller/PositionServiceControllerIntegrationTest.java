package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.constant.UserStatus;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
public class PositionServiceControllerIntegrationTest {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PositionServiceControllerIntegrationTest.class);

    @Value("${local.server.port}")
    private int port;
    private URL base;


    private RestTemplate template;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private LootRepository lootRepo;

    @Before
    public void setUp()
            throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
        this.template = new TestRestTemplate();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetPositionablesSuccess() throws URISyntaxException {
        User user = new User("Hans", "Mustermann");
        Player player = new Player();
        playerRepo.save(player);
        user.setPlayer(player);
        user.getPlayer().setCharacter(Character.BELLE);
        user.setStatus(UserStatus.ONLINE);
        user.setToken("blablaa");
        userRepo.save(user);

        Game game = new Game();
        game.setName("Game1game");
        game.setOwner("Owner1owner");
        game = gameRepo.save(game);


        URI posURI = new URI(base + "games/" + game.getId() + "/positions");

        List<Positionable> positionablesBefore = template.getForObject(posURI, List.class);
        Assert.assertEquals(0, positionablesBefore.size());

        game.addUser(user);
        game = gameRepo.save(game);

        List<Positionable> positionableAfter = template.getForObject(posURI, List.class);
        Assert.assertEquals(1, positionableAfter.size());

        Loot loot = new Loot(LootType.JEWEL, 500, Positionable.Level.BOTTOM);
        lootRepo.save(loot);

        game.addLoot(loot);
        gameRepo.save(game);

        positionableAfter = template.getForObject(posURI, List.class);
        Assert.assertEquals(2, positionableAfter.size());

        logger.debug(positionableAfter.toString());
    }
}
