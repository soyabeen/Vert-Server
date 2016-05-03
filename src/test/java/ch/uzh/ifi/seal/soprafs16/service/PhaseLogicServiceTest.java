package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.*;
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

    private Player expectedPlayer1, expectedPlayer2, expectedPlayer3;
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
                Turn.REVERSE,
                Turn.NORMAL,
                Turn.NORMAL,
                Turn.HIDDEN);

        nthRound = 1;
        game = new Game();
        game.setId(1L);
        game.setStatus(GameStatus.PLANNINGPHASE);
        round = new Round(game.getId(), nthRound, turns, RoundEndEvent.REBELLION);

        loot = new Loot(LootType.JEWEL, 1L, 1000, 0, Positionable.Level.BOTTOM);
        starterDeck = new ArrayList<>();
        starterDeck.add(card2);
        starterDeck.add(card2);
        starterDeck.add(card2);
        starterDeck.add(card2);
        starterDeck.add(card2);

        playerDeck = new CardDeck(starterDeck);

        // TODO: use player builder?
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
    public void testNormalTurn() {
        
    }
    
    @Test
    public void testHiddenTurn() {
        
    }
    
    @Test
    public void testReverseTurn() {
        expectedPlayer3 = new Player(loot, playerDeck);
        expectedPlayer3.setId(3L);
        expectedPlayer3.setCharacter(Character.BELLE);
        expectedPlayer3.setToken(UUID.randomUUID().toString());
        game.addPlayer(expectedPlayer3);

        //first player in test will be player 2
        game.setCurrentPlayerId(expectedPlayer2.getId());
        // TODO: round needs to be a reverse round (is momentarily in Test Setup)

        phaseLogic.advancePlayer(game.getId(), round.getNthRound());
        Assert.assertEquals(game.getCurrentPlayerId(), expectedPlayer1.getId());

        phaseLogic.advancePlayer(game.getId(), round.getNthRound());
        Assert.assertEquals(game.getCurrentPlayerId(), expectedPlayer3.getId());


    }
    
    @Test
    public void testDoubleTurn() {
        
    }
}
