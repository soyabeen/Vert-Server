package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.mockito.Mockito.when;

/**
 * Created by soyabeen on 29.03.16.
 */
public class RoundServiceIntTest {
    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);

    @InjectMocks
    private RoundService roundService;

    @Mock
    private RoundRepository roundRepo;

    private Round round;
    private Game game;
    private List<Turn> turns;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        game = new Game();

        // List of turns needs to be generated
        turns = createRandomTurns();
        round = new Round(game, turns, RoundEndEvent.REBELLION);
        
        //when(gameRepo.findOne(1L)).thenReturn(game);
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
    public void testListOfTurns() {
        // FIXME: 3/30/16
    }
}
