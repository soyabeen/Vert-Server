package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.User;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.MoveRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class GameQueryController
        extends GenericController {

    private static final Logger logger  = LoggerFactory.getLogger(GameQueryController.class);

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private GameRepository gameRepo;
    @Autowired
    private MoveRepository moveRepo;


    private static final String CONTEXT = "/games";

    /*
     * Context: /game
     */
    @RequestMapping(value = CONTEXT, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Game> listGames() {
        logger.debug("listGames");
        List<Game> result = new ArrayList<>();
        gameRepo.findAll().forEach(result::add);
        return result;
    }

    /*
     * Context: /game/{game-id}
     */
    @RequestMapping(value = CONTEXT + "/{gameId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Game getGame(@PathVariable Long gameId) {
        logger.debug("getGame: " + gameId);

        Game game = gameRepo.findOne(gameId);

        return game;
    }

    /*
     * Context: /game/{game-id}/move
     */
    @RequestMapping(value = CONTEXT + "/{gameId}/move", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Move> listMoves(@PathVariable Long gameId) {
        logger.debug("listMoves");

        Game game = gameRepo.findOne(gameId);
        if (game != null) {
            return game.getMoves();
        }

        return null;
    }

    /*
     * Context: /game/{game-id}/move/{moveId}
     */
    @RequestMapping(value = CONTEXT + "/{gameId}/move/{moveId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Move getMove(@PathVariable Long gameId, @PathVariable Integer moveId) {
        logger.debug("getMove: " + gameId);

        Game game = gameRepo.findOne(gameId);
        if (game != null) {
            return game.getMoves().get(moveId);
        }

        return null;
    }

    /*
     * Context: /game/{game-id}/player/{playerId}
     */
    @RequestMapping(value = CONTEXT + "/{gameId}/player/{playerId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public User getPlayer(@PathVariable Long gameId, @PathVariable Integer playerId) {
        logger.debug("getPlayer: " + gameId);

        Game game = gameRepo.findOne(gameId);

        return game.getUsers().get(playerId);
    }
}