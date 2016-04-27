package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.utils.DemoRoundConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Can setup a real game with demo players and a special simple round configuration.
 * <p>
 * Created by soyabeen on 26.04.16.
 */
@Service("demoModeService")
public class DemoModeService {

    private static final Logger logger = LoggerFactory.getLogger(DemoModeService.class);

    private static final int NR_OF_DEMO_PLAYERS = 2;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    private Player createRandomPlayer(String username) {
        Player p = new Player(username);
        return playerService.createPlayer(p);
    }

    public Game initDemoGame() {
        Player owner = createRandomPlayer("DemoPlayer-1-" + UUID.randomUUID().toString());
        Player secondPlayer = createRandomPlayer("DemoPlayer-2-" + UUID.randomUUID().toString());

        Game shell = new Game();
        shell.setName("Demo-" + UUID.randomUUID().toString());
        Game demo = gameService.createGame(shell, owner.getToken(), NR_OF_DEMO_PLAYERS);

        owner = playerService.initializeCharacter(demo.getId(), owner, Character.TUCO);

        playerService.assignPlayer(demo.getId(), secondPlayer, Character.CHEYENNE);
        gameService.startGame(demo.getId(), owner.getToken(), new DemoRoundConfigurator());
        return gameService.loadGameFromRepo(demo.getId());
    }

}
