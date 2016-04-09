package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Loot;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.utils.GameConfigurator;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * Created by soyabeen on 31.03.16.
 */
@Service("gameService")
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private LootService lootService;

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private PlayerRepository playerRepo;

    private GameConfigurator gameConf;

    public GameService() {
        gameConf = new GameConfigurator();
    }


    private Game createGame(String gameName, Player owner, int players) {
        Game gameShell = new Game();
        gameShell.setStatus(GameStatus.PENDING);
        gameShell.setName(gameName);
        gameShell.setOwner(owner.getUsername());
        gameShell.addPlayer(owner);
        logger.debug("game shell " + gameShell.toString());

        Game pendingGame = gameRepo.save(gameShell);
        logger.debug("game pending " + pendingGame.toString());
        Game game = gameConf.configureGameForNrOfPlayers(pendingGame, players);
        game.setLoots(lootService.saveLootsOfAGame(game.getLoots()));
        logger.debug("game with loots " + pendingGame.toString());
        return gameRepo.save(game);
    }

    public Game createGame(Game game, String userToken, int nrOfPlayers) {
        logger.debug("create game for name: {}, owner: {}, players: {}", game.getName(), userToken, nrOfPlayers);

        InputArgValidator.checkNotEmpty(game.getName(), "gamename");
        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepo, "token");
        InputArgValidator.checkNotEmpty(tokenOwner.getUsername(), "owner");

       // game name available?
        if (gameRepo.findByName(game.getName()) != null) {
            throw new InvalidInputException("Invalid arg : Name of game is already used.");
        }

        int players = (nrOfPlayers < GameConfigurator.MIN_PLAYERS)
                ? GameConfigurator.MAX_PLAYERS : nrOfPlayers;
        return createGame(game.getName(), tokenOwner, players);
    }

    public void startGame(Long gameId, String userToken) {

        Player tokenOwner = InputArgValidator.checkTokenHasValidPlayer(userToken, playerRepo, "token");
        Game game = (Game) InputArgValidator.checkAvailabeId(gameId, gameRepo, "gameid");

        // Game must be in pending state
        if (!GameStatus.PENDING.equals(game.getStatus())) {
            throw new IllegalStateException("Only games in " + GameStatus.PENDING
                    + " can be startet. This game is " + game.getStatus());
        }

        // Belongs the game to the user?
        if (!tokenOwner.getUsername().equals(game.getOwner())) {
            throw new IllegalStateException("User is not allowed to start the game. User must be game owner.");
        }

        // Enough players?
        List<Player> players = playerService.listPlayersForGame(game.getId());
        if (players.size() < GameConfigurator.MIN_PLAYERS) {
            throw new IllegalStateException("Not enough players to start the game. Need at least "
                    + GameConfigurator.MIN_PLAYERS + " players.");
        }

        // TODO: start game;
    }
}
