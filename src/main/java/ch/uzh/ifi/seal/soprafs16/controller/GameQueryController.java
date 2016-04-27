package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Move;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.service.GameService;
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

    private static final Logger logger = LoggerFactory.getLogger(GameQueryController.class);

    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private GameService gameService;

    private static final String CONTEXT = "/games";

    /**
     * Context: /games
     */
    @RequestMapping(value = CONTEXT, method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public List<Game> listGames(@RequestParam(value = "filter", required = false) String filter) {
        logger.debug("listGames " + (filter == null ? "" : filter));
        return gameService.listGames(filter);
    }

    /**
     * Context: /games/{game-id}
     */
    @RequestMapping(value = CONTEXT + "/{gameId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Game getGame(@PathVariable Long gameId) {
        //TODO: put logic of methods in GameServices
        logger.debug("getGame: " + gameId);

        Game game = gameRepo.findOne(gameId);

        return game;
    }


    /**
     * Context: /games/{game-id}/player/{playerId}
     */
    @RequestMapping(value = CONTEXT + "/{gameId}/player/{playerId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Player getPlayer(@PathVariable Long gameId, @PathVariable Long playerId) {
        //TODO: put logic of methods in GameServices
        logger.debug("getPlayer: " + gameId);

        Game game = gameRepo.findOne(gameId);

        for (Player player : game.getPlayers()) {
            if (player.getId().equals(playerId)) {
                return player;
            }
        }

        return new Player();
    }
}