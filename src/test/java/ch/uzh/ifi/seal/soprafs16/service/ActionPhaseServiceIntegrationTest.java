package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.Application;
import ch.uzh.ifi.seal.soprafs16.constant.CardType;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.constant.LootType;
import ch.uzh.ifi.seal.soprafs16.dto.TurnDTO;
import ch.uzh.ifi.seal.soprafs16.helper.PositionedPlayer;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.utils.HostageEndEventRoundConfigurator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soyabeen on 25.05.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class ActionPhaseServiceIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(ActionPhaseServiceIntegrationTest.class);

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ActionPhaseService actionService;

    @Autowired
    private RoundService roundService;

    private void playCard(Player player, Game game, int firstRound, CardType type) {
        TurnDTO floorChange = new TurnDTO();
        floorChange.setType(type);
        Move playFloorChange = roundService.getMoveFromDTO(game.getId(), player.getToken(), floorChange);
        roundService.makeAMove(game.getId(), firstRound, playFloorChange);
    }

    private void execDraw(Game game) {
        TurnDTO emptyDTO = new TurnDTO();
        emptyDTO.setType(CardType.DRAW);
        actionService.executeDTO(game.getId(), emptyDTO);
    }

    private void execFloorchange(Game game, Player targetPosition) {
        List<Player> pos = new ArrayList<>();
        pos.add(targetPosition);
        TurnDTO dto = new TurnDTO(CardType.FLOORCHANGE, pos);
        actionService.executeDTO(game.getId(), dto);
    }

    private void execMove(Game game, Player targetPosition) {
        List<Player> pos = new ArrayList<>();
        pos.add(targetPosition);
        TurnDTO dto = new TurnDTO(CardType.MOVE, pos);
        actionService.executeDTO(game.getId(), dto);
    }

    @Test
    public void testEndOfActionphaseWithHostageEvent() {

        Player pShell1 = new Player("owner-hostage-event");
        Player owner = playerService.createPlayer(pShell1);

        Player pShell2 = new Player("otherPlayer-hostage-event");
        Player other = playerService.createPlayer(pShell2);

        Game shell = new Game();
        shell.setName("ActionPhaseServiceIntegrationTest");
        Game game = gameService.createGame(shell, owner.getToken(), -1);

        playerService.assignPlayer(game.getId(), other, Character.BELLE);
        playerService.initializeCharacter(game.getId(), owner, Character.DJANGO);

        // start fast track game
        gameService.startGame(game.getId(), owner.getToken(), new HostageEndEventRoundConfigurator());

        Game startedGame = gameService.loadGameFromRepo(game.getId());
        logger.debug("Started {}", startedGame);
        Assert.assertEquals("Game switched to planning phase.", GameStatus.PLANNINGPHASE, startedGame.getStatus());

        //
        // planning phase
        //
        final int firstRound = 1;

        // draw cards to make sure floorchange and move is on hand.
        playCard(owner, startedGame, firstRound, CardType.DRAW);
        playCard(other, startedGame, firstRound, CardType.DRAW);

        playCard(owner, startedGame, firstRound, CardType.FLOORCHANGE);
        playCard(other, startedGame, firstRound, CardType.FLOORCHANGE);

        Round current2 = roundService.getCurrentRoundInformation(startedGame.getId());
        logger.debug("Current turn2 {}", current2);
        Assert.assertEquals("Current round has 4 played cards.", 4, current2.getCardStack().size());

        playCard(owner, startedGame, firstRound, CardType.MOVE);
        playCard(other, startedGame, firstRound, CardType.MOVE);

        Round current3 = roundService.getCurrentRoundInformation(startedGame.getId());
        logger.debug("Current turn3 {}", current3);
        Assert.assertEquals("Current round has 6 played cards.", 6, current3.getCardStack().size());


        playCard(owner, startedGame, firstRound, CardType.DRAW);
        playCard(other, startedGame, firstRound, CardType.DRAW);

        Round current4 = roundService.getCurrentRoundInformation(startedGame.getId());
        logger.debug("Current turn4 {}", current4);
        Assert.assertEquals("Current round has 8 played cards.", 8, current4.getCardStack().size());

        //
        // action phase
        //
        Game actionPhaseGame = gameService.loadGameFromRepo(game.getId());
        logger.debug("In action phase {}", actionPhaseGame);
        Assert.assertEquals("Game switched to action phase.", GameStatus.ACTIONPHASE, actionPhaseGame.getStatus());

        execDraw(actionPhaseGame);
        execDraw(actionPhaseGame);

        execFloorchange(actionPhaseGame, PositionedPlayer.builder().onUpperLevelAt(2).build());
        execFloorchange(actionPhaseGame, PositionedPlayer.builder().onUpperLevelAt(1).build());

        execMove(actionPhaseGame, PositionedPlayer.builder().onUpperLevelAt(0).build());
        execMove(actionPhaseGame, PositionedPlayer.builder().onUpperLevelAt(0).build());

        execDraw(actionPhaseGame);
        execDraw(actionPhaseGame);

        //
        // check hostage endevent
        //
        Game finishedGame = gameService.loadGameFromRepo(game.getId());
        logger.debug("Finished {}", finishedGame);
        Assert.assertEquals("Game is finised.", GameStatus.FINISHED, finishedGame.getStatus());
        Assert.assertEquals("Same amount of loots on train", startedGame.getLoots().size(), finishedGame.getLoots().size());

        Player finishedOwner = null;
        Player finishedOther = null;
        for (Player p : finishedGame.getPlayers()) {
            if (owner.getId() == p.getId()) {
                finishedOwner = p;
            } else if (other.getId() == p.getId()) {
                finishedOther = p;
            } else {
                Assert.fail("Found unexpectes player " + p.toString());
            }
        }
        Assert.assertEquals("Owner has 2 loots.", 2, finishedOwner.getLoots().size());
        Assert.assertTrue("Owner has only purses.", containsOnlyPurses(finishedOwner.getLoots()));
        Assert.assertEquals("Other has 2 loots.", 2, finishedOther.getLoots().size());
        Assert.assertTrue("Other has only purses.", containsOnlyPurses(finishedOther.getLoots()));

    }

    private boolean containsOnlyPurses(List<Loot> loots) {
        for (Loot l : loots) {
            if (LootType.JEWEL == l.getType() || LootType.STRONGBOX == l.getType()) {
                return false;
            }
        }
        return true;
    }

}
