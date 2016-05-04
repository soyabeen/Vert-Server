package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.model.Card;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Round;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

/**
 * Created by soyabeen on 04.05.16.
 */
public class LastPlayedCardTest {

    @InjectMocks
    private GameService gameService;

    @Mock
    private RoundService mockedRoundService;

    @Mock
    private GameRepository mockedGameRepo;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void playThreeCardsWithTwoPlayers () {
        // init
        Game game = new Game();
        game.setId(1L);
        game.setRoundId(1);

        Round round = new Round(game.getId(), game.getRoundId(), null, null, "");
        round.addNewlyPlayedCard(new Card(CardType.FIRE, 2L));
        round.addNewlyPlayedCard(new Card(CardType.DRAW, 3L));
        round.addNewlyPlayedCard(new Card(CardType.PUNCH, 2L));

        when(mockedGameRepo.findOne(1L)).thenReturn(game);
        when(mockedRoundService.getRoundById(game.getId(), game.getRoundId())).thenReturn(round);

        // execute
        Optional<Card> lastPlayed = gameService.getLastPlayedCardForGame(game.getId());
        Assert.assertTrue("LastPlayed should not be empty.", lastPlayed.isPresent());
        Card card = lastPlayed.get();
        Assert.assertThat("Is PINCH card.", card.getType(), is(CardType.PUNCH));
        Assert.assertThat("Is owner id is 2.", card.getOwnerId(), is(2L));
    }

    @Test
    public void noPlayedCardYetGivesEmptyOptional () {
        // init
        Game game = new Game();
        game.setId(1L);
        game.setRoundId(1);

        when(mockedGameRepo.findOne(1L)).thenReturn(game);
        when(mockedRoundService.getRoundById(game.getId(), game.getRoundId())).thenReturn(null);

        // execute
        Optional<Card> lastPlayed = gameService.getLastPlayedCardForGame(game.getId());
        Assert.assertFalse("LastPlayed optional should be empty.", lastPlayed.isPresent());
    }


}
