package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.*;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MoveRepository;
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
import java.util.UUID;

import static org.eclipse.persistence.jpa.jpql.Assert.fail;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
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
    private MoveRepository moveRepo;


    @Mock
    private Round round;

    @Mock
    private Card card1, card2;

    private Player player;
    private Game game;
    //private User user1;
    private Loot loot;
    private CardDeck playerDeck;
    private List<Card> starterDeck;
    private Move move, movePass;
    private Integer nthRound;
    private List<Turn> turns;
    private List<Card> hand;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        game = new Game();
        game.setId(1L);

        // List of turns needs to be generated
        turns = Arrays.asList(
                Turn.DOUBLE_TURNS,
                Turn.NORMAL,
                Turn.NORMAL,
                Turn.HIDDEN);
        nthRound = 1;
        round = new Round(game.getId(), nthRound, turns,RoundEndEvent.REBELLION);



        loot = new Loot(LootType.JEWEL, 1L, 1000, 0, Positionable.Level.BOTTOM);
        starterDeck = new ArrayList<>();
        starterDeck.add(card2);
        starterDeck.add(card2);
        starterDeck.add(card2);
        starterDeck.add(card2);
        starterDeck.add(card2);

        playerDeck = new CardDeck(starterDeck);

        player = new Player(loot, playerDeck);
        player.setCharacter(Character.GHOST);
        player.setToken(UUID.randomUUID().toString());

        card1 = new Card();
        card1.setOwnerId(player.getId());
        card1.setType(CardType.MOVE);

        card2 = new Card();
        card2.setOwnerId(player.getId());
        card2.setType(CardType.MOVE);

        // player has 4 Move cards
        hand = new ArrayList<>();
        hand.add(card2);
        hand.add(card2);
        hand.add(card2);
        hand.add(card2);

        player.setHand(hand);

        move = new Move();
        move.setGame(game);
        move.setId(1L);

        when(gameRepo.findOne(1L)).thenReturn(game);
        when(roundRepo.findByGameIdAndNthRound(game.getId(), nthRound)).thenReturn(round);
        when(playerRepo.findOne(anyLong())).thenReturn(player);
        when(moveRepo.save((Move) any())).thenReturn(move);
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

    /*
    @Test
    public void makeAMoveReturnsTurnId() {
        move.setPlayer(player);
        move.setPass(false);
        move.setPlayedCard(card1);


        String result = roundService.makeAMove(1L, 1, move);

        Assert.assertThat(result, is("1"));

        try {
            Integer.parseInt(result);
        } catch (Exception x_x) {
            Assert.assertFalse(x_x instanceof NumberFormatException);
        }
    }

    @Test
    public void makeAMovePlaysCard() {
        move.setPlayer(player);
        move.setPass(false);
        move.setPlayedCard(card1);

        int sizeBefore = playerRepo.findOne(move.getPlayedCard().getOwnerId()).getHand().size();
        roundService.makeAMove(1L, 1, move);
        int sizeAfter = playerRepo.findOne(move.getPlayedCard().getOwnerId()).getHand().size();

        Assert.assertThat(sizeAfter, is(sizeBefore - 1));
    }

    @Test
    public void makeAMovePassesTurn() {
        move = new Move();
        move.setPlayer(player);
        move.setGame(game);
        move.setId(1L);
        move.setPass(true);

        int sizeBefore = move.getPlayer().getHand().size();
        roundService.makeAMove(1L, 1, move);
        int sizeAfter = move.getPlayer().getHand().size();

        Assert.assertThat(sizeAfter, is(sizeBefore + 3));


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
    */

    @Test
    public void testGetRoundById() {

    }
}