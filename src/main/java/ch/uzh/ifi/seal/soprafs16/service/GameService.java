package ch.uzh.ifi.seal.soprafs16.service;

import ch.uzh.ifi.seal.soprafs16.constant.GameStatus;
import ch.uzh.ifi.seal.soprafs16.exception.InvalidInputException;
import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.utils.InputArgValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by soyabeen on 31.03.16.
 */
@Service("gameService")
public class GameService {

    private static final Logger logger  = LoggerFactory.getLogger(GameService.class);

    private static final int DEFAULT_PLAYER_NR = 4;

    @Autowired
    private GameRepository gameRepo;

    private Game createGame(String gameName, User owner, int players) {
        Game  game = new Game();
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
        if(owner != null) {
            throw new InvalidInputException("Invalid arg : Name of game is already used.");
        }

        int players = (nrOfPlayers<2) ? DEFAULT_PLAYER_NR : nrOfPlayers;
        return createGame(gameName, owner, players);
    }
}
