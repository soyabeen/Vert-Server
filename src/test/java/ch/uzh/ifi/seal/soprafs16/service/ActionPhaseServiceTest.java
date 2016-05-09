package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.*;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.engine.ActionCommand;
import ch.uzh.ifi.seal.soprafs16.engine.GameEngine;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.LootRepository;
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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by mirkorichter on 09.05.16.
 */
public class ActionPhaseServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ActionPhaseServiceTest.class);

    @InjectMocks
    private ActionPhaseService actionService;

    @Mock
    private GameRepository mockedGameRepo;

    @Mock
    private RoundRepository mockedRoundRepo;

    @Mock
    private PlayerRepository mockedPlayerRepo;

    @Mock
    private LootRepository mockedLootRepo;

    @Mock
    private GameEngine mockedGameEngine;

    @Mock
    private ActionCommand mockedActionCommand;

    @Mock
    private PhaseLogicService mockedPhaseLogic;

    private Game game;
    private Round round1;
    private Round round2;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);


        game = new Game();
        game.setId(1L);
        game.setRoundId(1);
        game.setNrOfCars(3);
        game.setCurrentPlayerId(1L);

        Player player1 = new Player();
        player1.setId(1L);
        player1.setUsername("P1");
        player1.setCar(1);
        player1.setLevel(Positionable.Level.BOTTOM);

        Player player2 = new Player();
        player2.setId(2L);
        player2.setUsername("P2");
        player2.setCar(2);
        player2.setLevel(Positionable.Level.BOTTOM);

        game.addPlayer(player1);
        game.addPlayer(player2);

        round1 = new Round(game.getId(), game.getRoundId(), null, null, "");
        round2 = new Round(game.getId(), 2, null, null, "");

        Loot loot = new Loot(LootType.JEWEL, 1L, 500, 1, Positionable.Level.BOTTOM);
        loot.setId(1L);
        loot.setOwnerId(2L);
        List<Loot> loots = new ArrayList<>();
        loots.add(loot);
        game.setLoots(loots);

        when(mockedGameRepo.findOne(1L)).thenReturn(game);
        when(mockedRoundRepo.findByGameIdAndNthRound(game.getId(), 1)).thenReturn(round1);
        when(mockedRoundRepo.findByGameIdAndNthRound(game.getId(), 2)).thenReturn(round2);
        when(mockedPlayerRepo.findOne(1L)).thenReturn(player1);
        when(mockedPlayerRepo.findOne(2L)).thenReturn(player2);
        when(mockedLootRepo.findOne(1L)).thenReturn(loot);
    }

    @Test
    public void sendPossibilitiesReturnsPossibilities() {

        round1.addNewlyPlayedCard(new Card(CardType.FIRE,1L));
        game.setCurrentPlayerId(1L);

        ActionCommand command = new ActionCommand(CardType.FIRE,game,
                mockedPlayerRepo.findOne(game.getCurrentPlayerId()),null);

        mockedGameEngine = new GameEngine();

        TurnDTO possibilities = new TurnDTO();

        try {
            List<Positionable> positionables = mockedGameEngine.simulateAction(command);
            possibilities.addPlayer((Player) positionables.get(0));
        }
        catch (InvocationTargetException e) {
        }

        possibilities.setType(CardType.FIRE);

        TurnDTO actual = actionService.sendPossibilities(1L);



        Assert.assertEquals(possibilities.getPlayers(),actual.getPlayers());
        Assert.assertEquals(possibilities.getType(),actual.getType());

    }

    @Test
    public void executeActionUpdatesPlayer() {

        game.setRoundId(2);
        game.setCurrentPlayerId(2L);
        round2.addNewlyPlayedCard(new Card(CardType.MOVE, 2L));

        Player targetPlayer = getPlayersFromSimulateMove().get(0);

        ActionCommand command = new ActionCommand(CardType.MOVE,game,
                mockedPlayerRepo.findOne(game.getCurrentPlayerId()),targetPlayer);

        mockedGameEngine = new GameEngine();
        Player result = new Player();

        try {
            List<Positionable> positionables = mockedGameEngine.executeAction(command);
            result = (Player) positionables.get(0);

        }
        catch (InvocationTargetException e) {
        }

        List<Player> plist = new ArrayList<>();
        plist.add(targetPlayer);
        TurnDTO dto = new TurnDTO(CardType.MOVE,plist);

        actionService.executeDTO(1L,dto);

        Assert.assertTrue("The player 2 moved to car 1", mockedPlayerRepo.findOne(2L).getCar() == result.getCar());

    }

    @Test
    public void executeActionUpdatesLoot() {

        game.setRoundId(2);
        game.setCurrentPlayerId(1L);
        round2.addNewlyPlayedCard(new Card(CardType.ROBBERY, 1L));

        ActionCommand command = new ActionCommand(CardType.ROBBERY,game,
                mockedPlayerRepo.findOne(game.getCurrentPlayerId()),null);
        command.setTargetLoot(mockedLootRepo.findOne(1L));

        mockedGameEngine = new GameEngine();
        Loot resLoot = new Loot(LootType.JEWEL,1L,1);

        try {
            List<Positionable> positionables = mockedGameEngine.executeAction(command);
            resLoot = (Loot) positionables.get(0);
        }
        catch (InvocationTargetException e) {
        }

        TurnDTO dto = new TurnDTO();
        dto.setType(CardType.ROBBERY);
        dto.setLootID(1L);

        actionService.executeDTO(1L,dto);

        Assert.assertTrue("The player 1 has picked up the loot",
                mockedPlayerRepo.findOne(1L).getLoots().get(0).getValue() == resLoot.getValue());
        Assert.assertTrue("Loot has now OwnerId 1",
                resLoot.getOwnerId().equals(mockedLootRepo.findOne(1L).getOwnerId()));

    }

    private List<Player> getPlayersFromSimulateMove() {

        ActionCommand command = new ActionCommand(CardType.MOVE,game,
                mockedPlayerRepo.findOne(2L),null);

        List<Player> res = new ArrayList<>();
        mockedGameEngine = new GameEngine();

        try {
            List<Positionable> positionables = mockedGameEngine.simulateAction(command);
            res.add((Player) positionables.get(0));
        }
        catch (InvocationTargetException e) {
        }

        return res;

    }
}

