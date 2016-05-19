package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.helper.GameBuilder;
import ch.uzh.ifi.seal.soprafs16.helper.PlayerBuilder;
import ch.uzh.ifi.seal.soprafs16.model.Game;
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
    private PlayerBuilder playerBuilder;


    @Test
    public void getPositionablesTest() {
        Game game = gameBuilder.init("getAvailableCharacters", "getAvailableCharacters")
                .setStatus(GameStatus.PENDING)
                .build();

        List<Positionable> result = positionService.listPositionablesForGame(game.getId());
        Assert.assertThat(result.size(), is(0));

        Player p1 = playerBuilder.init(true).build();
        Player p2 = playerBuilder.init(true).build();

        game = gameBuilder
                .addUser(p1)
                .addUser(p2)
                .addRandomLoot()
                .addRandomLoot()
                .build();

        logger.error(game.getPlayers().toString());
        result = positionService.listPositionablesForGame(game.getId());
        logger.error(result.toString());
        Assert.assertThat(result.size(), is(4));
    }
}
