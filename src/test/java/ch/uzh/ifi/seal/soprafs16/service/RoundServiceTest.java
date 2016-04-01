package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.eclipse.persistence.jpa.jpql.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

/**
 * Created by soyabeen on 29.03.16.
 */
public class RoundServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);

    @InjectMocks
    private RoundService roundService;

    @Mock
    private GameRepository gameRepo;

    @Mock
    private RoundRepository roundRepo;

    private Round round;
    private Game game;
    private List<Turn> turns;
    private Integer nthRound;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        // List of turns needs to be generated
        turns = Arrays.asList(new Turn[]{
                Turn.DOUBLE_TURNS,
                Turn.NORMAL,
                Turn.NORMAL,
                Turn.HIDDEN});
        nthRound = 1;

        game = new Game();
        game.setId(1L);

        round = new Round(game, nthRound, turns, RoundEndEvent.REBELLION);

        when(gameRepo.findOne(1L)).thenReturn(game);
        when(roundRepo.findByGameAndNthRound(game, nthRound)).thenReturn(round);
    }

    // TODO: optimize random turn generator
    private List<Turn> createRandomTurns() {
        // holds result
        List<Turn> turns = new ArrayList<>();

        Random rand = new Random();
        Integer max_no_of_turns = rand.nextInt(5-3) + 3;

        // holds indexes of events
        List<Integer> no_turns = new ArrayList<>(max_no_of_turns);

        // fill no_turns with values
        for(int i = 0; i < max_no_of_turns; i++) {
            no_turns.add(i);
        }

        // shuffle no_turns values
        Collections.shuffle(no_turns);

        // go through shuffled collection and add to turns
        for(Integer val : no_turns) {
            turns.add(Turn.values()[val]);
        }

        return turns;
    }

    @Test
    public void listTurnsForRoundReturnsCorrectOrder() {
        List<Turn> result = roundService.listTurnsForRound(1L, 1);

        Assert.assertThat(result.size(), is(turns.size()));

        for(int i = 0; i < turns.size(); i++) {
            Assert.assertThat(result.indexOf(i), is(turns.indexOf(i)));
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
            roundService.listTurnsForRound(1L, -1);
            fail("Illegal nthRound, should throw InvalidInputException");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof InvalidInputException);
        }

        try {
            roundService.listTurnsForRound(null, 1);
            fail("Illegal gameId, should throw InvalidInputException");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof InvalidInputException);
        }

        try {
            roundService.listTurnsForRound(1L, null);
            fail("Illegal gameId, should throw InvalidInputException");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof InvalidInputException);
        }
    }

    @Test
    public void testGetRoundById() {

    }
}