package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Player;
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

    private Player p1;

    private Player p2;

    @Test
    public void createDemoGameWithService() {

        // init demo game
        Game demo = demoModeService.initDemoGame();
        logger.debug(demo.toString());

        Assert.assertThat(demo.getStatus(), is(GameStatus.PLANNINGPHASE));
        Assert.assertTrue("Game name starts with demo.", demo.getName().startsWith("Demo-"));
        Assert.assertTrue("Game owner is Demo-1-. player", demo.getOwner().startsWith("DemoPlayer-1-"));
        for (Player p : demo.getPlayers()) {
            if (p.getUsername().startsWith("DemoPlayer-1-")) {
                p1 = p;
            } else {
                p2 = p;
            }
            for (Card c:p.getDeck()) {
                logger.debug(Boolean.toString(c.isOnHand()));
            }
            Assert.assertThat("Player " + p.getUsername() + " must have a loot.", p.getLoots().size(), is(1));
            logger.debug(p.toString());
        }

        for (Card c:p1.getDeck()) {
            logger.debug(Boolean.toString(c.isOnHand()));
        }
        for (Card c:p2.getDeck()) {
            logger.debug(Boolean.toString(c.isOnHand()));
        }

        logger.debug(p1.getHand().toString());
        // player 1, plays a move card
        TurnDTO dto = new TurnDTO();
        dto.setType(p1.getHand().get(0).getType());
        Move m1move = roundService.getMoveFromDTO(demo.getId(), p1.getToken(), dto);
        String res = roundService.makeAMove(demo.getId(), 1, m1move);

        Game gAfter1Card = gameService.loadGameFromRepo(demo.getId());
        logger.debug(gAfter1Card.toString());
//        Assert.assertEquals("Nr of moves played after 1.", "1", res);
        Assert.assertEquals("CurrPlayer nr 2 - after one played card.", p2.getId(), gAfter1Card.getCurrentPlayerId());

    }

}
