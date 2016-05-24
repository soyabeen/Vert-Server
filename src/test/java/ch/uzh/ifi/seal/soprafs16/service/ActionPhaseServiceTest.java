package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.*;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
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
import java.util.Arrays;
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
    private Round round3;
    private Round round4;
    private Round round5;

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
        player1.setCharacter(Character.BELLE);

        Player player2 = new Player();
        player2.setId(2L);
        player2.setUsername("P2");
        player2.setCar(2);
        player2.setLevel(Positionable.Level.BOTTOM);
        player2.setCharacter(Character.DJANGO);

        game.addPlayer(player1);
        game.addPlayer(player2);

        round1 = new Round(game.getId(), game.getRoundId(), null, null, "");
        round2 = new Round(game.getId(), 2, null, null, "");


        List<Turn> turns4 = Arrays.asList(
                Turn.REVERSE,
                Turn.HIDDEN,
                Turn.NORMAL,
                Turn.DOUBLE_TURNS);

        List<Turn> turns5 = Arrays.asList(
                Turn.REVERSE,
                Turn.HIDDEN,
                Turn.NORMAL,
                Turn.NORMAL);

        round3 = new Round(game.getId(), 3, turns4, null, "");
        round4 = new Round(game.getId(), 4, turns4, null, "");
        round5 = new Round(game.getId(), 5, turns5, null, "");

        List<Round> rounds = new ArrayList<>();
        rounds.add(round1);
        rounds.add(round2);
        rounds.add(round3);
        rounds.add(round4);
        rounds.add(round5);

        Loot loot = new Loot(LootType.JEWEL, 1L, 500, 1, Positionable.Level.BOTTOM);
        loot.setId(1L);
        loot.setOwnerId(2L);
        List<Loot> loots = new ArrayList<>();
        loots.add(loot);

        Loot loot2 = new Loot(LootType.JEWEL, 1L, 500, 1, Positionable.Level.BOTTOM);
        loot2.setId(2L);
        loots.add(loot2);

        Loot loot3 = new Loot(LootType.JEWEL, 1L, 500, 1, Positionable.Level.BOTTOM);
        loot3.setId(3L);
        loots.add(loot3);
        game.setLoots(loots);

        game.setLoots(loots);

        when(mockedGameRepo.findOne(1L)).thenReturn(game);
        when(mockedRoundRepo.findByGameIdAndNthRound(game.getId(), 1)).thenReturn(round1);
        when(mockedRoundRepo.findByGameIdAndNthRound(game.getId(), 2)).thenReturn(round2);
        when(mockedRoundRepo.findByGameIdAndNthRound(game.getId(), 3)).thenReturn(round3);
        when(mockedRoundRepo.findByGameIdAndNthRound(game.getId(), 4)).thenReturn(round4);
        when(mockedRoundRepo.findByGameIdAndNthRound(game.getId(), 5)).thenReturn(round5);
        when(mockedRoundRepo.findByGameId(game.getId())).thenReturn(rounds);
        when(mockedPlayerRepo.findOne(1L)).thenReturn(player1);
        when(mockedPlayerRepo.findOne(2L)).thenReturn(player2);
        when(mockedLootRepo.findOne(1L)).thenReturn(loot);
        when(mockedLootRepo.findOne(2L)).thenReturn(loot2);
        when(mockedLootRepo.findOne(3L)).thenReturn(loot3);
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
        round2.addNewlyPlayedCard(new Card(CardType.MOVE, 1L));

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
        round2.addNewlyPlayedCard(new Card(CardType.MOVE, 2L));

        ActionCommand command = new ActionCommand(CardType.ROBBERY,game,
                mockedPlayerRepo.findOne(game.getCurrentPlayerId()),null);
        command.setTargetLoot(mockedLootRepo.findOne(2L));

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
        dto.setLootId(3L);

        actionService.executeDTO(1L,dto);

        Assert.assertTrue("The player 1 has picked up the loot",
                mockedPlayerRepo.findOne(1L).getLoots().get(0).getValue() == resLoot.getValue());
        Assert.assertTrue("Loot has now OwnerId 1",
                resLoot.getOwnerId().equals(mockedLootRepo.findOne(3L).getOwnerId()));

    }

    @Test
    public void executeWithMoreCards() {

        game.setRoundId(2);
        game.setCurrentPlayerId(2L);
        round2.addNewlyPlayedCard(new Card(CardType.MOVE, 2L));
        round2.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));
        round2.addNewlyPlayedCard(new Card(CardType.DRAW, 2L));
        round2.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));

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
    public void testEndOfActionphase() {

        game.setRoundId(4);
        game.setCurrentPlayerId(1L);
        round4.addNewlyPlayedCard(new Card(CardType.MOVE, 1L));
        round4.addNewlyPlayedCard(new Card(CardType.MOVE, 2L));
        round4.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));
        round4.addNewlyPlayedCard(new Card(CardType.DRAW, 2L));
        round4.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));
        round4.addNewlyPlayedCard(new Card(CardType.DRAW, 2L));
        round4.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));
        round4.addNewlyPlayedCard(new Card(CardType.DRAW, 2L));
        round4.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));
        round4.addNewlyPlayedCard(new Card(CardType.DRAW, 2L));
        round4.setEnd(RoundEndEvent.ANGRY_MARSHAL);
        game.setTurnId(5);
        game.setStatus(GameStatus.ACTIONPHASE);

        Player target1 = getPlayersFromSimulateMove().get(0);

        List<Player> plist = new ArrayList<>();
        plist.add(target1);
        TurnDTO dto1 = new TurnDTO(CardType.MOVE,plist);
        actionService.executeDTO(1L,dto1);

        Player target2 = getPlayersFromSimulateMove().get(0);
        plist.remove(0);
        plist.add(target2);
        TurnDTO dto2 = new TurnDTO(CardType.MOVE,plist);
        actionService.executeDTO(1L,dto2);

        TurnDTO emptyDTO = new TurnDTO();
        emptyDTO.setType(CardType.DRAW);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);

        Assert.assertTrue("Status changed back to planningphase after actionphase", game.getStatus().equals(GameStatus.PLANNINGPHASE));
        Assert.assertTrue("TurnId was reset to 1 after actionphase", game.getTurnId() == 1);
        Assert.assertTrue("RoundId incremented after actionphase", game.getRoundId() == 5);

    }

    @Test
    public void testEndOfActionphaseAtEndOfGame() {

        game.setRoundId(5);
        game.setCurrentPlayerId(1L);
        round5.addNewlyPlayedCard(new Card(CardType.MOVE, 1L));
        round5.addNewlyPlayedCard(new Card(CardType.MOVE, 2L));
        round5.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));
        round5.addNewlyPlayedCard(new Card(CardType.DRAW, 2L));
        round5.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));
        round5.addNewlyPlayedCard(new Card(CardType.DRAW, 2L));
        round5.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));
        round5.addNewlyPlayedCard(new Card(CardType.DRAW, 2L));
        round5.setEnd(RoundEndEvent.ANGRY_MARSHAL);
        game.setTurnId(5);
        game.setStatus(GameStatus.ACTIONPHASE);

        Player target1 = getPlayersFromSimulateMove().get(0);

        List<Player> plist = new ArrayList<>();
        plist.add(target1);
        TurnDTO dto1 = new TurnDTO(CardType.MOVE,plist);
        actionService.executeDTO(1L,dto1);

        Player target2 = getPlayersFromSimulateMove().get(0);
        plist.remove(0);
        plist.add(target2);
        TurnDTO dto2 = new TurnDTO(CardType.MOVE,plist);
        actionService.executeDTO(1L,dto2);

        TurnDTO emptyDTO = new TurnDTO();
        emptyDTO.setType(CardType.DRAW);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);
        actionService.executeDTO(1L,emptyDTO);

        Assert.assertTrue("Status changed to finished after actionphase at round 5", game.getStatus().equals(GameStatus.FINISHED));
    }

    @Test
    public void testHasNoTarget() {

        game.setRoundId(3);
        game.setCurrentPlayerId(1L);
        round3.addNewlyPlayedCard(new Card(CardType.FIRE, 1L));
        round3.addNewlyPlayedCard(new Card(CardType.PUNCH, 2L));
        round3.addNewlyPlayedCard(new Card(CardType.DRAW, 1L));
        round3.addNewlyPlayedCard(new Card(CardType.DRAW, 2L));
        game.setTurnId(3);
        game.setStatus(GameStatus.ACTIONPHASE);


        TurnDTO dto1 = new TurnDTO();
        dto1.setType(CardType.FIRE);
        actionService.executeDTO(1L,dto1);

        Assert.assertTrue("Fire with no targets does nothing", game.getCurrentPlayerId().equals(2L));

        TurnDTO dto2 = new TurnDTO();
        dto2.setType(CardType.PUNCH);
        actionService.executeDTO(1L,dto2);

        Assert.assertTrue("Punch with no targets does nothing", game.getCurrentPlayerId().equals(1L));

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

