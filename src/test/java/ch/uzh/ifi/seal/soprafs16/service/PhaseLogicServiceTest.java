package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;
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
import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by antoio on 4/26/16.
 */
public class PhaseLogicServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(CharacterServiceIntegrationTest.class);

    @InjectMocks
    private PhaseLogicService phaseLogic;

    @Mock
    private RoundRepository roundRepo;

    @Mock
    private GameRepository gameRepo;

    @Mock
    private PlayerRepository playerRepo;

    @Mock
    private Round round;

    @Mock
    private Card card1, card2;

    private Player expectedPlayer1, expectedPlayer2;
    private Integer nthRound;
    private Game game;
    private Loot loot;
    private CardDeck playerDeck;
    private List<Card> starterDeck;
    List<Turn> turns;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        // List of turns needs to be generated
        turns = Arrays.asList(
                Turn.DOUBLE_TURNS,
                Turn.NORMAL,
                Turn.NORMAL,
                Turn.HIDDEN);

        nthRound = 1;
        game = new Game();
        game.setId(1L);
        round = new Round(game.getId(), nthRound, turns, RoundEndEvent.REBELLION);

        loot = new Loot(LootType.JEWEL, 1L, 1000, 0, Positionable.Level.BOTTOM);
        starterDeck = new ArrayList<>();
        starterDeck.add(card2);
        starterDeck.add(card2);
        starterDeck.add(card2);
        starterDeck.add(card2);
        starterDeck.add(card2);

        playerDeck = new CardDeck(starterDeck);

        expectedPlayer1 = new Player(loot, playerDeck);
        expectedPlayer1.setId(1L);
        expectedPlayer1.setCharacter(Character.GHOST);
        expectedPlayer1.setToken(UUID.randomUUID().toString());
        game.addPlayer(expectedPlayer1);

        expectedPlayer2 = new Player(loot, playerDeck);
        expectedPlayer2.setId(2L);
        expectedPlayer2.setCharacter(Character.DJANGO);
        expectedPlayer2.setToken(UUID.randomUUID().toString());
        game.addPlayer(expectedPlayer2);

        when(gameRepo.findOne(1L)).thenReturn(game);
        when(gameRepo.save(any(Game.class))).thenReturn(game);
        when(roundRepo.findByGameIdAndNthRound(game.getId(), nthRound)).thenReturn(round);
        when(playerRepo.findOne(1L)).thenReturn(expectedPlayer1);
        when(playerRepo.findOne(2L)).thenReturn(expectedPlayer2);
    }

    @Test
    public void testSetStartPlayer() {
        // Test for start player at beginning of game
        phaseLogic.setStartPlayer(game, nthRound, expectedPlayer1.getId());
        Assert.assertEquals(round.getStartPlayerId(), expectedPlayer1.getId());

        // Test for start player after finish of 1 round
        phaseLogic.setCurrentPlayer(game.getId(), nthRound, expectedPlayer2.getId());
        Assert.assertEquals(round.getStartPlayerId(), expectedPlayer2.getId());
    }

    @Test
    public void testSetCurrentPlayer() {
        phaseLogic.setCurrentPlayer(game.getId(), nthRound, expectedPlayer2.getId());

        Assert.assertEquals(game.getCurrentPlayerId(), expectedPlayer2.getId());
    }

    @Test
    public void testSetNextPlayer() {
        // Test with first player
        game.setCurrentPlayerId(expectedPlayer1.getId());
        phaseLogic.setNextPlayer(game.getId(), nthRound);
        Assert.assertEquals(game.getNextPlayerId(), expectedPlayer2.getId());

        // Test with second player
        game.setCurrentPlayerId(expectedPlayer2.getId());
        phaseLogic.setNextPlayer(game.getId(), nthRound);
        Assert.assertEquals(game.getNextPlayerId(), expectedPlayer1.getId());
    }

    @Test
    public void testGetNextPlayerId() {
        // Test with first player
        game.setCurrentPlayerId(expectedPlayer1.getId());
        Long result = phaseLogic.getNextPlayerId(game.getId(), nthRound);
        Assert.assertEquals(result, expectedPlayer2.getId());

        // Test with second player
        game.setCurrentPlayerId(expectedPlayer2.getId());
        result = phaseLogic.getNextPlayerId(game.getId(), nthRound);
        Assert.assertEquals(result, expectedPlayer1.getId());

    }
}
