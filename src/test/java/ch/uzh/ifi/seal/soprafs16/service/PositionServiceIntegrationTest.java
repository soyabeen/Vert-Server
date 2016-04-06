package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.helper.GameBuilder;
import ch.uzh.ifi.seal.soprafs16.helper.LootBuilder;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.Positionable;
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

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by alexanderhofmann on 25/03/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class PositionServiceIntegrationTest {

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(PositionServiceIntegrationTest.class);

    @Autowired
    private PositionService positionService;

    @Autowired
    private GameBuilder gameBuilder;

    @Autowired
    private LootBuilder lootBuilder;

    @Autowired
    private PlayerBuilder playerBuilder;


    @Test
    public void getPositionablesTest() {
        Game game = gameBuilder.init("getAvailableCharacters", "getAvailableCharacters")
                .setStatus(GameStatus.PENDING)
                .build();

        List<Positionable> result = positionService.listPositionablesForGame(game.getId());
        Assert.assertThat(result.size(), is(0));

        Loot loot1 = lootBuilder.init(LootType.PURSE_SMALL, 250, 1, Positionable.Level.TOP).build();
        Loot loot2 = lootBuilder.init(LootType.JEWEL, 500, 1, Positionable.Level.TOP).build();

        Player player1 = playerBuilder.init("hans").addCharacter(Character.BELLE).build();
        Player player2 = playerBuilder.init("daisy").addCharacter(Character.GHOST).build();

        game = gameBuilder.addRandomUserAndPlayer(Character.BELLE)
                .addRandomUserAndPlayer(Character.GHOST)
                .addLoot(loot1)
                .addLoot(loot2)
                .build();

//        game.addUser(user1);
//        game.addUser(user2);
//        game.addLoot(loot2);
//        game = gameRepo.save(game);
//
//        userRepo.save(user1);
//
//        userRepo.save(user2);

        result = positionService.listPositionablesForGame(game.getId());
        Assert.assertThat(result.size(), is(4));
    }
}
