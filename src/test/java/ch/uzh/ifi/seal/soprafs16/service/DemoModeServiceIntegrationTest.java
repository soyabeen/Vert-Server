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
public class DemoModeServiceIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(PositionServiceIntegrationTest.class);

    @Autowired
    private DemoModeService demoModeService;

    @Autowired
    private GameService gameService;

    @Autowired
    private RoundService roundService;

    @Test
    public void createDemoGameWithService() {
        Game demo = demoModeService.initDemoGame();
        logger.debug(demo.toString());

        Assert.assertThat(demo.getStatus(), is(GameStatus.PLANNINGPHASE));
        Assert.assertTrue("Game name starts with demo." , demo.getName().startsWith("Demo-"));
    }

}
