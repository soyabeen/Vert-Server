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
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
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


    @Test
    public void getPositionablesTest() {
        Game game = gameBuilder.init("getAvailableCharacters", "getAvailableCharacters")
                .setStatus(GameStatus.PENDING)
                .build();

        List<Positionable> result = positionService.listPositionablesForGame(game.getId());
        Assert.assertThat(result.size(), is(0));

        game = gameBuilder.addRandomUser()
                .addRandomUser()
                .addRandomLoot()
                .addRandomLoot()
                .build();

        logger.error(game.getPlayers().toString());
        // TODO: This returns each player twice but only in testing!!!
        result = positionService.listPositionablesForGame(game.getId());
        logger.error(result.toString());
        Assert.assertThat(result.size(), is(4));
    }
}
