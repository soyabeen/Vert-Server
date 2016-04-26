package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.Character;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
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

    private static final int NR_OF_DEMO_PLAYERS = 2;

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerService playerService;

    private List<Player> generateRandomPlayers(int nrOfPlayers) {
        List<Player> generated = new ArrayList<>();
        for (int i = 0; i < nrOfPlayers; i++) {
            Player p = new Player("DemoPlayer-" + i + "-" + UUID.randomUUID().toString());
            generated.add(playerService.createPlayer(p));
        }
        return generated;
    }

    public Game initDemoGame() {
        List<Player> players = generateRandomPlayers(NR_OF_DEMO_PLAYERS);

        Game shell = new Game();
        shell.setName("Demo-" + UUID.randomUUID().toString());
        Game demo = gameService.createGame(shell, players.get(0).getToken(), NR_OF_DEMO_PLAYERS);

        for (Player p : players) {
            demo.addPlayer(playerService.assignPlayer(demo.getId(), p, Character.CHEYENNE));
        }

      gameService.startGame();
        return demo;
    }

}
