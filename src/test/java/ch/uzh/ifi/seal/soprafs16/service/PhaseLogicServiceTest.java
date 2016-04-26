package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.constant.RoundEndEvent;
import ch.uzh.ifi.seal.soprafs16.constant.Turn;
import ch.uzh.ifi.seal.soprafs16.model.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
    private Round round;

    @Mock
    private Card card1, card2;

    private Player player;
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

        player = new Player(loot, playerDeck);
        player.setId(1L);
        player.setCharacter(Character.GHOST);
        player.setToken(UUID.randomUUID().toString());

        when(gameRepo.findOne(1L)).thenReturn(game);
        when(roundRepo.findByGameIdAndNthRound(game.getId(), nthRound)).thenReturn(round);
    }

    @Test
    public void testSetStartPlayer() {
        phaseLogic.setStartPlayer(game, nthRound, player.getId());

        Assert.assertEquals(round.getStartPlayerId(), player.getId());
    }
}
