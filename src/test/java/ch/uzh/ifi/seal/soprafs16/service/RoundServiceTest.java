package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    @Mock
    private PlayerRepository playerRepo;

    @Mock
    private Player player;

    @Mock
    private Round round;

    //@Mock
    private Card card;

    private Game game;
    private Integer nthRound;
    private List<Turn> turns;
    private List<Card> hand;
    private Move move;

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

        player.setCharacter(Character.GHOST);

        card = new Card();
        card.setOwner(player);
        card.setType(CardType.MOVE);

        // player has 4 Move cards
        hand = new ArrayList<>();
        hand.add(card);
        hand.add(card);
        hand.add(card);
        hand.add(card);

        player.setHand(hand);

        move = new Move();
        move.setPlayedCard(card);
        move.setGame(game);
        move.setId(1L);

        when(gameRepo.findOne(1L)).thenReturn(game);
        when(roundRepo.findByGameAndNthRound(game, nthRound)).thenReturn(round);
        when(playerRepo.findOne( move.getPlayedCard().getOwner().getId() )).thenReturn(player);
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
    }

    @Test
    public void makeAMoveReturnsTurnId() {
        String result = roundService.makeAMove(1L, 1, move);

        Assert.assertThat(result, is("1"));
    }

}