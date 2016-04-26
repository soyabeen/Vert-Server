package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.*;
import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.*;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.RoundRepository;
import ch.uzh.ifi.seal.soprafs16.utils.DemoRoundConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

/**
 * Created by soyabeen on 26.04.16.
 */
public class DemoModeServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(DemoModeServiceTest.class);

    @InjectMocks
    private DemoModeService demoModeService;

    @Mock
    private GameService gameService;

    @Mock
    private PlayerService playerService;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        int nrOfPlayers = 2;

        Player mPlayer = new Player("Demo-");
        mPlayer.setToken("token");
        mPlayer.setCharacter(Character.CHEYENNE);

        Game mGame = new Game();
        mGame.setId(1L);
        mGame.setName("Demo-");
        mGame.setNumberOfPlayers(nrOfPlayers);

        when(playerService.createPlayer((Player) any())).thenReturn(mPlayer);
        when(playerService.assignPlayer(anyLong(), (Player) any(), (Character) any())).thenReturn(mPlayer);
        when(gameService.createGame((Game) any(), anyString(), anyInt())).thenReturn(mGame);

    }


    @Test
    public void generateDemoGame() {

        logger.debug("Game:" + demoModeService.initDemoGame().toString());
        Game mGame = new Game();
        mGame.setId(1L);
        mGame.setName("Demo-");
        mGame.setNumberOfPlayers(2);
        logger.debug(new DemoRoundConfigurator().generateRoundsForGame(mGame).toString());
    }
}