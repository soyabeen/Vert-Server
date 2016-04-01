package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import ch.uzh.ifi.seal.soprafs16.utils.GameConfiguration;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private GameRepository gameRepo;

    @Autowired
    private UserRepository userRepo;


    private Game createGame(String gameName, User owner, int players) {
        Game game = new Game();
        game.setName(gameName);
        game.setOwner(owner.getUsername());
        game.setStatus(GameStatus.PENDING);
        game.setNumberOfPlayers(players);
        game.addUser(owner);
        return gameRepo.save(game);
    }

    public Game createGame(String gameName, String usernameOfOwner, int nrOfPlayers) {
        logger.debug("create game for name: {}, owner: {}, players: {}", gameName, usernameOfOwner, nrOfPlayers);

        InputArgValidator.checkNotEmpty(gameName, "gamename");
        InputArgValidator.checkNotEmpty(usernameOfOwner, "owner");

        // game name available?
        User owner = gameRepo.findByName(gameName);
        if (owner != null) {
            throw new InvalidInputException("Invalid arg : Name of game is already used.");
        }

        int players = (nrOfPlayers < GameConfiguration.MIN_PLAYERS)
                ? GameConfiguration.MAX_PLAYERS : nrOfPlayers;
        return createGame(gameName, owner, players);
    }

    private void createNewGameBoardForGame(Game game) {
        // nr of cars
        // generate loots
        // add init loot and card deck to player
        // set next player
        // set game state
    }

    public void startGame(Long gameId, String userToken) {

        User tokenOwner = InputArgValidator.checkTokenHasValidUser(userToken, userRepo, "token");
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
        if (players.size() < GameConfiguration.MIN_PLAYERS) {
            throw new IllegalStateException("Not enough players to start the game. Need at least "
                    + GameConfiguration.MIN_PLAYERS + " players.");
        }

        // TODO: start game;
    }
}
