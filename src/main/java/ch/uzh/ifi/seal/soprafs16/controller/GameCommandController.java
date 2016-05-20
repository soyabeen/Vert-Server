package ch.uzh.ifi.seal.soprafs16.controller;

import ch.uzh.ifi.seal.soprafs16.model.Game;
import ch.uzh.ifi.seal.soprafs16.model.Player;
import ch.uzh.ifi.seal.soprafs16.model.repositories.GameRepository;
import ch.uzh.ifi.seal.soprafs16.model.repositories.PlayerRepository;
import ch.uzh.ifi.seal.soprafs16.service.GameService;
import ch.uzh.ifi.seal.soprafs16.utils.FastLaneRoundConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class GameCommandController
        extends GenericController {

    private static final Logger logger = LoggerFactory.getLogger(GameCommandController.class);

    private static final String CONTEXT = "/games";

    @Autowired
    private GameService gameService;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private GameRepository gameRepo;

    @RequestMapping(
            value = CONTEXT,
            method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Game createGame(@RequestBody Game game, @RequestParam("token") String userToken) {
        logger.debug("POST:{} - createGame - token: {}, {}", CONTEXT, userToken, game.toString() );

        return gameService.createGame(game, userToken, game.getNumberOfPlayers());
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.debug("POST:{} - startGame - token: {}, gameid: {}",  CONTEXT + "/{gameId}/start", userToken, gameId );

        gameService.startGame(gameId, userToken);
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/fasttrack", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void startFastlaneGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.debug("POST:{} - startFastlaneGame - token: {}, gameid: {}", CONTEXT + "/{gameId}/fastlane", userToken, gameId);

        gameService.startGame(gameId, userToken, new FastLaneRoundConfigurator());
    }


    @RequestMapping(value = CONTEXT + "/{gameId}/stop", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void stopGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.debug("stopGame: " + gameId);

        Game game = gameRepo.findOne(gameId);
        Player owner = playerRepo.findByToken(userToken);

        if (owner != null && game != null && game.getOwner().equals(owner.getUsername())) {
            gameRepo.delete(game);
        }
    }

    @RequestMapping(value = CONTEXT + "/{gameId}/exit", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void exitGame(@PathVariable Long gameId, @RequestParam("token") String userToken) {
        logger.debug("exitGame: " + gameId);

        gameService.exitGame(gameId, userToken);
    }

}