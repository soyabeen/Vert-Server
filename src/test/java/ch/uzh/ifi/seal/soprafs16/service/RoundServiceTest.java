package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.*;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.helper.GameBuilder;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.eclipse.persistence.jpa.jpql.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by soyabeen on 29.03.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class RoundServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);

    //@InjectMocks
    @Autowired
    private RoundService roundService;

    //@Mock
    @Autowired
    private GameRepository gameRepo;

    //@Mock
    @Autowired
    private RoundRepository roundRepo;

    @Autowired
    private DemoModeService demoModeService;


    private Game game;

    @Before
    public void init() {

        game = demoModeService.initDemoGame();
        game = gameRepo.save(game);

    }

    @Test
    public void listTurnsForRoundReturnsCorrectOrder() {
        List<Turn> result = roundService.listTurnsForRound(game.getId(), 1);
        Round r = roundRepo.findByGameIdAndNthRound(game.getId(),1);

        Assert.assertEquals(result.size(), r.getTurns().size());

        for(int i = 0; i < result.size(); i++) {
            Assert.assertThat(result.get(i), is(r.getTurns().get(i)));
        }
    }

    @Test
    public void listTurnsForRoundThrowsInvalidInputException() {
        try {
            roundService.listTurnsForRound(-1L, 1);
            fail("Illegal gameId, should throw InvalidInputException");

        } catch (Exception e) {
            Assert.assertTrue(e instanceof InvalidInputException);
        }

        try {
            roundService.listTurnsForRound(game.getId(), -1);
            fail("Illegal nthRound, should throw InvalidInputException");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof InvalidInputException);
        }
    }


    @Test
    public void makeAMoveTurnIdAndDeck() {
        Round r = roundRepo.findByGameIdAndNthRound(game.getId(), 1);
        r.addNewlyPlayedCard(new Card(CardType.DRAW,1L));
        r = roundRepo.save(r);

        Player p = gameRepo.findOne(game.getId()).getPlayers().get(0);

        Move move = new Move();
        move.setPlayer(p);
        move.setPass(false);
        move.setPlayedCard(p.getHand().get(0));
        move.setGame(game);
        move.setId(1L);

        roundService.makeAMove(game.getId(),1,move);

        Game g = gameRepo.findOne(game.getId());
        Assert.assertEquals(g.getTurnId(),2);



    }

}